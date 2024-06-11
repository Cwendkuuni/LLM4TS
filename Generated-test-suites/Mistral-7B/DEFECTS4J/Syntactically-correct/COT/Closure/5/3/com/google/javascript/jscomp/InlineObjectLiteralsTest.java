import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.Scope;

public class InlineObjectLiteralsTest extends CompilerPassTestBase {

  @Override
  protected CompilerPass getProcessor(AbstractCompiler compiler) {
    return new InlineObjectLiterals(compiler, getSafeNameIdSupplier());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithSimpleProperties() {
    String source = "function f() { var o = { a: 1, b: 2 }; }";
    Node root = parseJs(source);
    process(root);

    Node fNode = root.getFirstChild();
    Scope fScope = fNode.asFunction().getScope();
    Var oVar = fScope.getVar("o");
    Reference oRef = fScope.getReference("o");

    // Check that o is assigned to an object literal with the expected properties
    Node oAssignment = oRef.getAssignedValue();
    assertEquals(NodeType.OBJECT_LITERAL, oAssignment.getType());
    List<Node> properties = oAssignment.asObjectLiteral().getProperties();
    assertEquals(2, properties.size());

    // Check that 'a' is assigned to a simple value
    Node aProperty = properties.get(0);
    assertEquals("a", aProperty.getFirstChild().getString());
    assertEquals(NodeType.NUMBER_LITERAL, aProperty.getLastChild().getType());
    assertEquals(1, ((NodeNumber) aProperty.getLastChild()).getIntValue());

    // Check that 'b' is assigned to a simple value
    Node bProperty = properties.get(1);
    assertEquals("b", bProperty.getFirstChild().getString());
    assertEquals(NodeType.NUMBER_LITERAL, bProperty.getLastChild().getType());
    assertEquals(2, ((NodeNumber) bProperty.getLastChild()).getIntValue());

    // Check that 'o' is used as expected in the function body
    Node body = fNode.getLastChild();
    Node usage1 = body.getFirstChild();
    Node usage2 = body.getLastChild().getFirstChild();
    assertEquals("o", usage1.getString());
    assertEquals("o.a", usage2.getString());
    assertEquals(1, ((NodeNumber) usage2.getLastChild()).getIntValue());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithComplexProperties() {
    String source = "function f() { var o = { a: { c: 1 }, b: [1, 2] }; }";
    Node root = parseJs(source);
    process(root);

    Node fNode = root.getFirstChild();
    Scope fScope = fNode.asFunction().getScope();
    Var oVar = fScope.getVar("o");
    Reference oRef = fScope.getReference("o");

    // Check that 'o' is assigned to an object literal with the expected properties
    Node oAssignment = oRef.getAssignedValue();
    assertEquals(NodeType.OBJECT_LITERAL, oAssignment.getType());
    List<Node> properties = oAssignment.asObjectLiteral().getProperties();
    assertEquals(2, properties.size());

    // Check that 'a' is assigned to an object literal
    Node aProperty = properties.get(0);
    assertEquals("a", aProperty.getFirstChild().getString());
    Node aObject = aProperty.getFirstChild();
    assertEquals(NodeType.OBJECT_LITERAL, aObject.getType());
    List<Node> aProperties = aObject.asObjectLiteral().getProperties();
    assertEquals(1, aProperties.size());

    // Check that 'c' is assigned to a simple value
    Node cProperty = aProperties.get(0);
    assertEquals("c", cProperty.getFirstChild().getString());
    assertEquals(NodeType.NUMBER_LITERAL, cProperty
        .getLastChild().getType());
    assertEquals(1, ((NodeNumber) cProperty.getLastChild())
        .getIntValue());

    // Check that 'b' is assigned to an array literal
    Node bProperty = properties.get(1);
    assertEquals("b", bProperty.getFirstChild().getString());
    Node bArray = bProperty.getFirstChild();
    assertEquals(NodeType.ARRAY_LITERAL, bArray.getType());
    List<Node> bElements = bArray.asArrayLiteral().getElements();
    assertEquals(2, bElements.size());

    // Check that 'o' is used as expected in the function body
    Node body = fNode.getLastChild();
    Node usage1 = body.getFirstChild();
    Node usage2 = body.getLastChild().getFirstChild();
    Node usage3 = body.getLastChild().getLastChild();
    assertEquals("o", usage1.getString());
    assertEquals("o.a.c", usage2.getString());
    assertEquals(1, ((NodeNumber) usage2.getLastChild()).getIntValue());
    assertEquals("[1, 2]", usage3.getString());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithReusedProperties() {
    String source = "function f() { var o1 = { a: 1 }; var o2 = o1; }";
    Node root = parseJs(source);
    process(root);

    Node fNode = root.getFirstChild();
    Scope fScope = fNode.asFunction().getScope();
    Var o1Var = fScope.getVar("o1");
    Var o2Var = fScope.getVar("o2");
    Reference o1Ref = fScope.getReference("o1");
    Reference o2Ref = fScope.getReference("o2");

    // Check that 'o1' is assigned to an object literal with the expected property
    Node o1Assignment = o1Ref.getAssignedValue();
    assertEquals(NodeType.OBJECT_LITERAL, o1Assignment.getType());
    List<Node> o1Properties = o1Assignment.asObjectLiteral().getProperties();
    assertEquals(1, o1Properties.size());

    // Check that 'o2' is assigned to the same object literal as 'o1'
    Node o2Assignment = o2Ref.getAssignedValue();
    assertEquals(o1Assignment, o2Assignment);

    // Check that 'o1' and 'o2' are used as expected in the function body
    Node body = fNode.getLastChild();
    Node usage1 = body.getFirstChild();
    Node usage2 = body.getLastChild().getFirstChild();
    assertEquals("o1", usage1.getString());
    assertEquals("o1.a", usage2.getString());
    assertEquals(1, ((NodeNumber) usage2.getLastChild()).getIntValue());
    assertEquals("o2", usage1.getString());
    assertEquals("o2.a", usage2.getString());
    assertEquals(1, ((NodeNumber) usage2.getLastChild()).getIntValue());
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithCyclicProperties() {
    String source = "function f() { var o = { a: { b: o } }; }";
    Node root = parseJs(source);
    try {
      process(root);
      fail("Expected an exception due to cyclic object references");
    } catch (IllegalStateException e) {
      // Expected
    }

    // Verify that no code change was made to the source code
    assertEquals(source, getSourceCode(root));
  }

  @org.junit.Test
  public void testInlineObjectLiteralWithSelfReferentialAssignment() {
    String source = "function f() { var o = {}; o.a = o; }";
    Node root = parseJs(source);
    try {
      process(root);
      fail("Expected an exception due to self-referential assignments");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    // Verify that no code change was made to the source code
    assertEquals(source, getSourceCode(root));
  }
}