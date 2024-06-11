import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if (InlineObjectLiterals.class.getName().equals(passName)) {
          return new InlineObjectLiterals(this, () -> "temp_" + Integer.toHexString(System.identityHashCode(this))) {
            @Override
            public void process(Node externs, Node root) {
              // no-op
            }
          };
        }
        return null;
      }
    };

    Scope scope = new Scope(null);
    Node root = Node.rootNode(Node.SCOPE);
    Node body = Node.statementBlock(Node.emptyStatementList(), Node.emptyStatementList());
    Node assign = Node.assign(
        Node.name("o"),
        Node.objectLit(
            Node.property("a", Node.numberLiteral(1)),
            Node.property("b", Node.stringLiteral("hello")),
            Node.property("c", Node.numberLiteral(2))));
    body.addChildToBack(assign);
    root.setLastChild(body);

    CompilerPass pass = compiler.getPass("InlineObjectLiterals");
    pass.process(Node.emptyNodeList(), root);

    NodeTraversal traversal = new NodeTraversal(root);
    ImmutableSet<String> expectedVars = ImmutableSet.of("o_a", "o_b");
    ImmutableMap<String, String> expectedVarMap = ImmutableMap.of(
        "o_a", "JSCompiler_object_inline_a_temp_23f9078f",
        "o_b", "JSCompiler_object_inline_b_temp_23f9078f");
    ImmutableSet<String> actualVars = Sets.newLinkedHashSet();

    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isVar()) {
        String name = child.getString();
        actualVars.add(name);
      }
    }

    assertEquals(expectedVars, actualVars);

    // Check the variables have the expected names.
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isVar()) {
        String name = child.getString();
        Node assignedValue = child.getNext();
        String expectedName = expectedVarMap.get(name);
        String actualName = VAR_PREFIX + name + "_" + compiler.getNameIdSupplier().get();
        assertEquals(expectedName, actualName);
        assertEquals(assignedValue.isUndefined(), assignedValue == null);
      }
    }
  }
}