import static com.google.javascript.jscomp.NodeUtil.nodeTypeMayHaveSideEffects;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.Result;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Tests for RemoveConstantExpressions class.
 */
public class RemoveConstantExpressionsTest {

  private Compiler compiler;
  private CompilerResults results;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    compiler = mock(Compiler.class);
    results = new CompilerResults();
    nodeFactory = mock(NodeFactory.class);
  }

  @org.junit.Test
  public void testRemoveConstantExpressions() {
    Node root = new Node(Token.PROGRAM, Lists.newArrayList());
    Node expression1 = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        new Node(Token.EXPR_ADD, Lists.newArrayList(
            new Node(Token.NUMBER, 1),
            new Node(Token.IDENT, "foo"),
            new Node(Token.IDENT, "bar")
        ))));
    root.setFirstChild(expression1);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    ParallelCompilerPass parallelPass = new ParallelCompilerPass();
    parallelPass.addPass(pass);
    parallelPass.process(null, root, null, results);

    Node expectedResult = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        new Node(Token.IDENT, "foo"),
        new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
            new Node(Token.IDENT, "bar")
        ))));
    Node actualResult = results.getOutput().getFirstChild();

    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testRemoveConstantExpressionsWithSideEffects() {
    Node root = new Node(Token.PROGRAM, Lists.newArrayList());
    Node expression1 = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        new Node(Token.EXPR_ASSIGN, Lists.newArrayList(
            new Node(Token.IDENT, "result"),
            new Node(Token.EXPR_ADD, Lists.newArrayList(
                new Node(Token.NUMBER, 1),
                new Node(Token.IDENT, "foo"),
                new Node(Token.IDENT, "bar")
            ))
        ))));
    root.setFirstChild(expression1);

    when(nodeFactory.createExpressionStatement(
        Token.ASSIGN, new Node("result"), null)).thenReturn(expression1);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    ParallelCompilerPass parallelPass = new ParallelCompilerPass();
    parallelPass.addPass(pass);
    parallelPass.setNodeFactory(nodeFactory);
    parallelPass.process(null, root, null, results);

    Node expectedResult = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
            new Node(Token.IDENT, "foo")
        )),
        new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
            new Node(Token.IDENT, "bar")
        ))
    ));
    Node actualResult = results.getOutput().getFirstChild();

    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testRemoveConstantExpressionsWithNoSideEffects() {
    Node root = new Node(Token.PROGRAM, Lists.newArrayList());
    Node expression1 = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        new Node(Token.EXPR_ADD, Lists.newArrayList(
            new Node(Token.NUMBER, 1),
            new Node(Token.NUMBER, 2)
        ))));
    root.setFirstChild(expression1);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    ParallelCompilerPass parallelPass = new ParallelCompilerPass();
    parallelPass.addPass(pass);
    parallelPass.process(null, root, null, results);

    Node expectedResult = new Node(Token.PROGRAM, Lists.newArrayList());
    Node actualResult = results.getOutput().getFirstChild();

    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testRemoveConstantExpressionsWithSideEffectsAndInputs() {
    Node root = new Node(Token.PROGRAM, Lists.newArrayList());
    Node expression1 = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        new Node(Token.EXPR_ASSIGN, Lists.newArrayList(
            new Node(Token.IDENT, "result"),
            new Node(Token.EXPR_ADD, Lists.newArrayList(
                new Node(Token.NUMBER, 1),
                new Node(Token.IDENT, "foo"),
                new Node(Token.IDENT, "bar")
            )),
            new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
                new Node(Token.IDENT, "foo").setProperty("value", new Node(Token.NUMBER, 5)),
                new Node(Token.IDENT, "bar").setProperty("value", new Node(Token.NUMBER, 7))
            ))
        ))));
    root.setFirstChild(expression1);

    when(nodeFactory.createExpressionStatement(
        Token.ASSIGN, new Node("result"), null)).thenReturn(expression1);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
    ParallelCompilerPass parallelPass = new ParallelCompilerPass();
    parallelPass.addPass(pass);
    parallelPass.setNodeFactory(nodeFactory);
    parallelPass.process(null, root, null, results);

    Node expectedResult = new Node(Token.PROGRAM, Lists.<Node>newArrayList(
        new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
            new Node(Token.IDENT, "foo").setProperty("value", new Node(Token.NUMBER, 5)),
            new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
                new Node(Token.IDENT, "bar").setProperty("value", new Node(Token.NUMBER, 7)),
                new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
                    new Node(Token.IDENT, "result")
                ))
            ))
        ))));
    Node actualResult = results.getOutput().getFirstChild();

    assertEquals(expectedResult, actualResult);
  }
}