import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import java.util.List;

/**
 * Tests for {@link CheckSideEffects}.
 */
public class CheckSideEffectsTest {

  private static final String SOURCE =
      "function main() {" +
          "  var x = 5;" +
          "  checkSideEffects(x == foo);" +
          "  checkSideEffects(foo());" +
          "  checkSideEffects(foo());;" +
          "  checkSideEffects(x == 5);" +
          "}" +
          "function foo() {}";

  private AbstractCompiler compiler;
  private CompilerInput input;
  private CheckSideEffects checkSideEffects;
  private CompilerPassManager passManager;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    input = new CompilerInput(new SourceFile("test.js", SOURCE), null, null);
    passManager = new CompilerPassManager(compiler, new CheckLevel(CheckLevel.WARNING));
    nodeFactory = NodeFactory.getInitialisedGlobalInstance();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @org.junit.Test
  public void testCheckSideEffects() {
    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        NodeTraversal.traverse(compiler, root, checkSideEffects);
      }
    });

    passManager.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        NodeTraversal.traverse(compiler, root, new StripProtection(compiler));
      }
    });

    passManager.process(input, nodeFactory);

    List<Node> warnings = checkSideEffects.getProblemNodes();
    assertTrue(warnings.size() == 3);

    // checkSideEffects(x == foo)
    Node expression1 = warnings.get(0);
    assertTrue(expression1.isCall());
    assertTrue(expression1.getLastChild().isName());
    assertTrue(expression1.getLastChild().getString().equals("checkSideEffects"));
    assertTrue(expression1.getFirstChild().isName());
    assertTrue(expression1.getFirstChild().getString().equals("x"));
    assertTrue(expression1.getFirstChild().get(0).isOperator());
    assertTrue(expression1.getFirstChild().get(0).getType() == Token.EQ);

    // checkSideEffects(foo())
    Node expression2 = warnings.get(1);
    assertTrue(expression2.isCall());
    assertTrue(expression2.getFirstChild().isName());
    assertTrue(expression2.getFirstChild().getString().equals("checkSideEffects"));
    assertTrue(expression2.getLastChild().isCall());
    assertTrue(expression2.getLastChild().getFirstChild().isFunction());

    // checkSideEffects(foo());;
    Node expression3 = warnings.get(2);
    assertTrue(expression3.isCall());
    assertTrue(expression3.getFirstChild().isName());
    assertTrue(expression3.getFirstChild().getString().equals("checkSideEffects"));
    assertTrue(expression3.getLastChild().isSemicolon());
    assertTrue(expression3.getLastChild().getPrevious().isCall());
    assertTrue(expression3.getLastChild().getPrevious().getFirstChild().isName());
    assertTrue(expression3.getLastChild().getPrevious().getFirstChild().getString().equals("foo"));
  }
}