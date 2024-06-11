import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "test_inline_object_literals_var_id_supplier");

    String sourceCode = "function foo() {\n" +
      "  var obj = {\n" +
      "    a: 1,\n" +
      "    b: 2,\n" +
      "    c: {\n" +
      "      d: 3,\n" +
      "      e: 4\n" +
      "    }\n" +
      "  };\n" +
      "  return obj.c.e;\n" +
      "}";

    Node externs = null;
    Node root = Node.parse(sourceCode);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getLastChild().getFirstChild();
    assertEquals(11, scope.getVars().size());

    Var objVar = scope.getVar("obj");
    Var aVar = scope.getVar("a");
    Var bVar = scope.getVar("b");
    Var cVar = scope.getVar("c");
    Var dVar = scope.getVar("d");
    Var eVar = scope.getVar("e");

    // Check that all variables have been expanded as expected
    Node objExpanded = objVar.getInitialValue();
    List<Node> objExpandedChildren = objExpanded.getChildren();
    assertEquals(3, objExpandedChildren.size());

    Node aExpanded = objExpandedChildren.get(0);
    assertEquals("a", aExpanded.getString());
    Node aValue = aExpanded.getFirstChild();
    assertEquals(Token.NUMBER, aValue.getNodeType());
    assertEquals(1, aValue.getNumber().intValueExact());

    Node cExpanded = objExpandedChildren.get(2);
    assertEquals("c", cExpanded.getString());
    List<Node> cExpandedChildren = cExpanded.getChildren();
    assertEquals(2, cExpandedChildren.size());

    Node dExpanded = cExpandedChildren.get(0);
    assertEquals("d", dExpanded.getString());
    Node dValue = dExpanded.getFirstChild();
    assertEquals(Token.NUMBER, dValue.getNodeType());
    assertEquals(3, dValue.getNumber().intValueExact());

    Node eExpanded = cExpandedChildren.get(1);
    assertEquals("e", eExpanded.getString());
    Node eValue = eExpanded.getFirstChild();
    assertEquals(Token.NUMBER, eValue.getNodeType());
    assertEquals(4, eValue.getNumber().intValueExact());

    // Check that the original object access is replaced by variable access
    assertEquals(Token.GETPROP, root.getLastChild().getNodeType());
    assertEquals("e", root.getLastChild().getLastChild().getString());
    Node testResult = (Node) compiler.process(sourceCode, null, null);
    assertEquals(4, testResult.getNumber().intValueExact());
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "test_inline_object_literals_var_id_supplier");

    String sourceCode = "function foo() {\n" +
      "  var obj = {\n" +
      "    a: 1,\n" +
      "    b: {\n" +
      "      c: obj\n" +
      "    }\n" +
      "  };\n" +
      "  return obj.b.c;\n" +
      "}";

    Node externs = null;
    Node root = Node.parse(sourceCode);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getLastChild().getFirstChild();
    assertEquals(11, scope.getVars().size());

    Var objVar = scope.getVar("obj");
    Var aVar = scope.getVar("a");
    Var bVar = scope.getVar("b");
    Var cVar = scope.getVar("c");

    // Check that all variables have been expanded as expected
    Node objExpanded = objVar.getInitialValue();
    List<Node> objExpandedChildren = objExpanded.getChildren();
    assertEquals(3, objExpandedChildren.size());

    Node aExpanded = objExpandedChildren.get(0);
    assertEquals("a", aExpanded.getString());
    Node aValue = aExpanded.getFirstChild();
    assertEquals(Token.NUMBER, aValue.getNodeType());
    assertEquals(1, aValue.getNumber().intValueExact());

    Node bExpanded = objExpandedChildren.get(1);
    assertEquals("b", bExpanded.getString());
    List<Node> bExpandedChildren = bExpanded.getChildren();
    assertEquals(1, bExpandedChildren.size());

    Node cExpanded = bExpandedChildren.get(0);
    assertEquals("c", cExpanded.getString());
    Node objectVar = cExpanded.getFirstChild();
    assertEquals("JSCompiler_object_inline_obj", objectVar.getString());
    assertEquals(Token.NAME, objectVar.getNodeType());

    // Check that the original object access is replaced by variable access
    assertEquals(Token.GETPROP, root.getLastChild().getNodeType());
    assertEquals("c", root.getLastChild().getLastChild().getString());
    Node testResult = (Node) compiler.process(sourceCode, null, null);
    assertEquals(null, testResult); // Should throw an error, but JUnit doesn't support that
    // Test that the error message is correct
    List<String> compilerErrors = compiler.getErrors();
    assertEquals(1, compilerErrors.size());
    String errorMessage = compilerErrors.get(0);
    assertTrue(errorMessage.contains("Self-referential assignment"));
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "test_inline_object_literals_var_id_supplier");

    String sourceCode = "function foo() {\n" +
      "  export var obj = {\n" +
      "    a: 1,\n" +
      "    b: 2,\n" +
      "    c: {\n" +
      "      d: 3,\n" +
      "      e: 4\n" +
      "    }\n" +
      "  };\n" +
      "  return obj.c.e;\n" +
      "}";

    Node externs = null;
    Node root = Node.parse(sourceCode);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getLastChild().getFirstChild();
    assertEquals(12, scope.getVars().size());

    Var objVar = scope.getVar("obj");
    Var aVar = scope.getVar("a");
    Var bVar = scope.getVar("b");
    Var cVar = scope.getVar("c");
    Var dVar = scope.getVar("d");
    Var eVar = scope.getVar("e");

    // Check that the exported variable is not inlined
    Node objExpanded = objVar.getInitialValue();
    List<Node> objExpandedChildren = objExpanded.getChildren();
    assertEquals(1, objExpandedChildren.size());

    Node objExpandedValue = objExpandedChildren.get(0);
    assertEquals("obj", objExpandedValue.getString());
    assertEquals(Token.NAME, objExpandedValue.getNodeType());
    assertFalse(objExpandedValue.toString().contains("JSCompiler_object_inline_"));

    // Check that the object's properties are inlined as expected
    Node aExpanded = scope.getVar("a").getInitialValue();
    assertEquals(Token.NUMBER, aExpanded.getNodeType());
    assertEquals(1, aExpanded.getNumber().intValueExact());

    Node cExpanded = scope.getVar("c").getInitialValue();
    List<Node> cExpandedChildren = cExpanded.getChildren();
    assertEquals(2, cExpandedChildren.size());

    Node dExpanded = cExpandedChildren.get(0);
    assertEquals("d", dExpanded.getString());
    Node dValue = dExpanded.getFirstChild();
    assertEquals(Token.NUMBER, dValue.getNodeType());
    assertEquals(3, dValue.getNumber().intValueExact());

    Node eExpanded = cExpandedChildren.get(1);
    assertEquals("e", eExpanded.getString());
    Node eValue = eExpanded.getFirstChild();
    assertEquals(Token.NUMBER, eValue.getNodeType());
    assertEquals(4, eValue.getNumber().intValueExact());

    // Check that the original object access is replaced by variable access
    assertEquals(Token.GETPROP, root.getLastChild().getNodeType());
    assertEquals("e", root.getLastChild().getLastChild().getString());
    Node testResult = (Node) compiler.process(sourceCode, null, null);
    assertEquals(4, testResult.getNumber().intValueExact());
  }

  @Test
  public void testInlineObjectLiteralsWithInvalidObjectPropertyAccess() {
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(
        compiler, () -> "test_inline_object_literals_var_id_supplier");

    String sourceCode = "function foo() {\n" +
      "  var obj = {\n" +
      "    a: 1,\n" +
      "    b: 2,\n" +
      "    c: {\n" +
      "      d: 3,\n" +
      "      e: 4\n" +
      "    }\n" +
      "  };\n" +
      "  return obj.x.y;\n" +
      "}";

    Node externs = null;
    Node root = Node.parse(sourceCode);

    inlineObjectLiterals.process(externs, root);

    Scope scope = root.getLastChild().getFirstChild();
    assertEquals(11, scope.getVars().size());

    Var objVar = scope.getVar("obj");
    Var aVar = scope.getVar("a");
    Var bVar = scope.getVar("b");
    Var cVar = scope.getVar("c");
    Var dVar = scope.getVar("d");
    Var eVar = scope.getVar("e");

    // Check that the object's properties are inlined as expected
    Node aExpanded = scope.getVar("a").getInitialValue();
    assertEquals(Token.NUMBER, aExpanded.getNodeType());
    assertEquals(1, aExpanded.getNumber().intValueExact());

    Node cExpanded = scope.getVar("c").getInitialValue();
    List<Node> cExpandedChildren = cExpanded.getChildren();
    assertEquals(2, cExpandedChildren.size());

    Node dExpanded = cExpandedChildren.get(0);
    assertEquals("d", dExpanded.getString());
    Node dValue = dExpanded.getFirstChild();
    assertEquals(Token.NUMBER, dValue.getNodeType());
    assertEquals(3, dValue.getNumber().intValueExact());

    Node eExpanded = cExpandedChildren.get(1);
    assertEquals("e", eExpanded.getString());
    Node eValue = eExpanded.getFirstChild();
    assertEquals(Token.NUMBER, eValue.getNodeType());
    assertEquals(4, eValue.getNumber().intValueExact());

    // Check that the invalid property access is not inlined
    Node xExpanded = scope.getVar("x").getInitialValue();
    assertNull(xExpanded);

    // Check that the original object access is replaced by undefined
    assertEquals(Token.GETPROP, root.getLastChild().getNodeType());
    assertEquals("y", root.getLastChild().getLastChild().getString());
    Node testResult = (Node) compiler.process(sourceCode, null, null);
    assertEquals(Token.UNDEFINED, testResult.getNodeType());
  }
}