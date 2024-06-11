import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    input = new CompilerInput("test.js", "test.js", null);
    root = Node.parse("function test() { var s = \"this string is \";\n" +
        "var a = 1;\n" +
        "var b = 2;\n" +
        "s += \"continued on the next line but you forgot the +\";\n" +
        "x = foo();\n" +
        "foo();;\n" +
        "if (a > b) { var z = 3; }\n" +
        "}");
  }

  @Test
  public void testBasicFunctionality() {
    checkSideEffects.process(input, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testUselessCode() {
    input.setSourceRoot("test.js");
    NodeUtil.setJSDocInfo(root, new JSDocInfo());
    checkSideEffects.process(input, root);
    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertEquals(3, problemNodes.size());
    assertFalse(problemNodes.get(0).isString());
    assertFalse(problemNodes.get(1).isName());
    assertFalse(problemNodes.get(1).getString().equals("foo"));
    assertFalse(problemNodes.get(2).isCall());
  }

  @Test(expected = AssertionError.class)
  public void testProtectedCode() {
    checkSideEffects.protectSideEffects();
  }

  @Test
  public void testProtectionRemoval() {
    checkSideEffects.process(input, root);
    checkSideEffects.protectSideEffects();
    checkSideEffects.process(input, root);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertEquals(3, problemNodes.size());
  }

  @Test
  public void testSideEffectFreeCodeProtection() {
    checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    checkSideEffects.process(input, root);
    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertEquals(2, problemNodes.size());
    assertFalse(problemNodes.get(0).isString());
    assertFalse(problemNodes.get(1).isName());
  }

  @Test
  public void testSideEffectSyncFunctionRemoval() {
    new StripProtection(compiler).process(input, root);
    checkSideEffects.process(input, root);
    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertEquals(1, problemNodes.size());
    assertFalse(problemNodes.get(0).isString());
  }

  private void assertEquals(int expectedSize, List<Node> actualNodes) {
    assertEquals(expectedSize, actualNodes.size());
  }
}