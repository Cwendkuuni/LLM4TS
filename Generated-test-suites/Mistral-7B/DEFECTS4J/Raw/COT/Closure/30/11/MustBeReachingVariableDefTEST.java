import static org.junit.Assert.*;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestSourceFile;
import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private static final String TEST_CODE =
      "function foo(x) {" +
          "var y = x + 1;" +
          "var z = 5;" +
          "if (x > 1) {" +
          "  var a = y * 2;" +
          "}" +
          "return y + z;";

  private Scope jsScope;
  private MustBeReachingVariableDef mustBeReachingVariableDef;

  @Before
  public void setup() {
    TestSourceFile sourceFile = new TestSourceFile(TEST_CODE);
    this.jsScope = sourceFile.getScope();
    this.mustBeReachingVariableDef =
        new MustBeReachingVariableDef(
            sourceFile.getCFG(), this.jsScope, new TestCompiler());
  }

  @Test
  public void testBasicFunctionality() {
    Node useNode = this.jsScope.getRootNode().getFirstChild();
    Node defNode = this.mustBeReachingVariableDef.getDef("x", useNode);
    assertNotNull(defNode);
  }

  @Test
  public void testEdgeCase_VariableNotDefined() {
    Node useNode = new Node(Token.UNDEFINED);
    assertNull(this.mustBeReachingVariableDef.getDef("z", useNode));
  }

  @Test
  public void testEdgeCase_UseBeforeDef() {
    Node useNode = this.jsScope.getRootNode().getLastChild();
    assertNull(this.mustBeReachingVariableDef.getDef("y", useNode));
  }

  @Test
  public void testEdgeCase_MultipleDefinitions() {
    Node useNode = this.jsScope.getRootNode().getFirstChild().getNext();
    Node defNode1 = this.mustBeReachingVariableDef.getDef("x", useNode);
    Node defNode2 = this.mustBeReachingVariableDef.getDef("x", useNode.getNext());
    assertNotSame("Definitions should be different nodes", defNode1, defNode2);
  }

  @Test
  public void testEdgeCase_UseInsideConditional() {
    Node conditionalNode = this.jsScope.getRootNode().getFirstChild().getNext();
    Node useNode = conditionalNode.getLastChild();
    Node defNode = this.mustBeReachingVariableDef.getDef("a", useNode);
    assertNotNull(defNode);
  }

  @Test
  public void testEdgeCase_UseInsideLoop() {
    Node loopNode = this.jsScope.getRootNode().getFirstChild().getNext();
    Node useNode = loopNode.getLastChild();
    Node defNode = this.mustBeReachingVariableDef.getDef("y", useNode);
    assertNotNull(defNode);
  }

  @Test
  public void testEdgeCase_VariableInFunctionParameter() {
    Node useNode = this.jsScope.getRootNode().getLastChild();
    assertNull(this.mustBeReachingVariableDef.getDef("x", useNode));
  }

  @Test
  public void testEdgeCase_VariableInOuterScope() {
    Node useNode = new Node(Token.UNDEFINED);
    assertTrue(this.mustBeReachingVariableDef.dependsOnOuterScopeVars("z", useNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionCase_NullUseNode() {
    this.mustBeReachingVariableDef.getDef("x", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionCase_NullScope() {
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(
            null, null, new TestCompiler());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionCase_NullCompiler() {
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(
            null, this.jsScope, null);
  }
}