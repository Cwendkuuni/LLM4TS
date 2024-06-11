import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_var";

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Node root = NodeUtil.parseJsString("function test() {" +
        "var obj = {" +
        "  a: 1," +
        "  b: 2," +
        "  c: 3," +
        "};" +
        "var sum = obj.a + obj.b;" +
        "}");

    inlineObjectLiterals.process(null, root);
    assertEquals(14, root.getChildCount());

    Node test = root.getFirstChild();
    Node testFunction = test.getFirstChild();
    Scope testScope = testFunction.getScope();

    Var objVar = testScope.getVar("obj");
    assertNotNull(objVar);

    ReferenceCollection objRefs = compiler.getReferenceCollector().getReferences(objVar);
    List<Reference> objReferences = objRefs.references;
    assertEquals(6, objReferences.size());

    for (int i = 0; i < objReferences.size(); i++) {
      Reference ref = objReferences.get(i);
      Node refNode = ref.getNode();

      // Check that all references to obj have been replaced by their keys
      if (i >= 3) {
        Node keyNode = refNode.getLastChild();
        String key = keyNode.getString();
        String expectedVarName = "JSCompiler_object_inline_" + key + "_test_var";
        String actualVarName = testScope.getVar(keyNode.getString()).name;
        assertEquals(expectedVarName, actualVarName);
      }
    }

    Node sumAssignment = testFunction.getLastChild();
    Var sumVar = sumAssignment.getFirstChild();
    Node sumValue = sumAssignment.getLastChild();

    // Check that sum is assigned to the sum of the keys' variables
    for (int i = 0; i < 3; i++) {
      Node keyNode = testFunction.getChildAtIndex(1).getChildAtIndex(i + 1);
      String key = keyNode.getString();
      Var keyVar = testScope.getVar(keyNode.getString());
      Node keyValue = keyVar.getInitialValue();

      Node keyValueExpression = testFunction.getChildAtIndex(1).getChildAtIndex(i + 1);
      Node keyValueNode = keyValueExpression.getFirstChild();

      Preconditions.checkState(keyValueNode.getType() == Token.NUMBER);
      int expectedValue = i + 1;
      int actualValue = NodeUtil.getIntFromNode(keyValue);
      assertEquals(expectedValue, actualValue);
      assertEquals(expectedValue, NodeUtil.getIntFromNode(keyValueNode));
    }

    assertEquals(3, sumValue.getChildCount());
    Node sumKeyNode = sumValue.getChildAtIndex(0);
    Node sumKey = sumKeyNode.getString();
    String expectedSumVarName = "JSCompiler_object_inline_" + sumKey + "_test_var";
    String actualSumVarName = testScope.getVar(sumKeyNode.getString()).name;
    assertEquals(expectedSumVarName, actualSumVarName);

    Node sumValueNode = sumValue.getChildAtIndex(1);
    Node sumValueLiteral = sumValue.getChildAtIndex(2);
    assertEquals(Token.PLUS, sumValue.getType());
    assertEquals(Node.OP_ADD, sumValueNode.getType());
    assertEquals(sumKeyNode, sumValueNode.getFirstChild());
    assertEquals(sumValueLiteral, sumValueNode.getLastChild());
  }
}