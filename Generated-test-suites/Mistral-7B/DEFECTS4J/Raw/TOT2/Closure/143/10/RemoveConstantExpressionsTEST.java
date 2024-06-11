import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.Result;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private CompilerPass removeConstantExpressions;
  private Result result;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
    result = new Result();
    nodeFactory = NodeFactory.getFactory();
  }

  @Test
  public void testRemoveConstantExpressions() {
    Node root = createNode("1 + foo() + bar()");
    ParallelCompilerPass.run(Lists.newArrayList(removeConstantExpressions), compiler, nodeFactory, root, null, result);
    Node simplified = result.getOutput();
    Node expected = createNode("foo(); bar()");
    assertEquals(expected, simplified);
  }

  // ...
}