import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeFactory.createNode;

import org.junit.Test;

public class PeepholeOptimizationsPassTest {
  @Test
  public void testPeepholeOptimizationsPass_BasicFunctionality() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(new Compiler(), new ConstantFolding(), new UselessCodeRemoval(), new Minimization());

    Node root = createNode("function foo() { return 1 + 1; }");
    pass.process(null, root);

    assertTrue(root.getClass() == Node.ROOT.getClass());
    assertTrue(root.getFirstChild().getClass() == Node.NAME.getClass());
    assertTrue(root.getFirstChild().getFirstChild().getClass() == Node.NUMBER.getClass());
    assertTrue(root.getFirstChild().getFirstChild().getNodeType() == Node.NUMBER.TO_INT32);
    assertTrue(((Number) root.getFirstChild().getFirstChild()).getInt() == 2);
  }

  @Test
  public void testPeepholeOptimizationsPass_EdgeCase_FunctionWithEmptyBody() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(new Compiler(), new ConstantFolding(), new UselessCodeRemoval(), new Minimization());

    Node root = createNode("function foo() {}");
    pass.process(null, root);

    assertTrue(root.getClass() == Node.ROOT.getClass());
    assertTrue(root.getFirstChild().getClass() == Node.NAME.getClass());
    assertTrue(root.getFirstChild().getFirstChild().getClass() == Node.PUNCTUATION.getClass());
  }

  @Test(expected = RuntimeException.class)
  public void testPeepholeOptimizationsPass_ErrorScenario_NullCompiler() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, new ConstantFolding(), new UselessCodeRemoval(), new Minimization());
  }
}