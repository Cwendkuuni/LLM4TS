import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {

  abstract static class TestCompiler extends AbstractCompiler {
    Result result;

    @Override
    public Result process(Node externs, Node root, List<CompilerPass> passes) {
      this.result = new Result();
      new RemoveConstantExpressions(this).process(externs, root);
      return result;
    }
  }

  public static class TestRemoveConstantExpressions extends TestCompiler {

    @Override
    public void initPasses() {
      addPass(new RemoveConstantExpressions(this));
    }

    @org.junit.Test
    public void testRemoveConstantExpressions() {
      Node root = new Node(Token.PROGRAM, null);
      Node expr1 = new Node(Token.EXPR_STATEMENT, null);
      Node expr1Body = new Node(Token.BINOP_PLUS, new Node[]{
          new Node(Token.NUM, 1),
          new Node(Token.CALL, new Node[]{new Node(Token.IDENT, "foo")}, null),
          new Node(Token.CALL, new Node[]{new Node(Token.IDENT, "bar")}, null)
      }, null);
      expr1.setFirstChild(expr1Body);
      root.setFirstChild(expr1);

      TestCompiler compiler = new TestRemoveConstantExpressions();
      Result result = compiler.process(null, root, Lists.newArrayList());
      assertTrue(result.changed);
      Node simplifiedCode = result.transformedSource;
      Node simplifiedExpr1 = simplifiedCode.getFirstChild();
      Node simplifiedExprBody = simplifiedExpr1.getFirstChild();
      List<Node> expectedSideEffectNodes = Lists.newArrayList(
          new Node(Token.IDENT, "foo"),
          new Node(Token.IDENT, "bar")
      );
      List<Node> actualSideEffectNodes = Lists.newArrayList();
      NodeTraversal.traverse(null, simplifiedExprBody, new NodeVisitor() {
        @Override
        public boolean visit(Node node) {
          if (NodeUtil.nodeTypeMayHaveSideEffects(node)) {
            actualSideEffectNodes.add(node);
          }
          return true;
        }
      });
      assertTrue(actualSideEffectNodes.equals(expectedSideEffectNodes));
    }

    @org.junit.Test
    public void testRemoveConstantExpressionsNoSideEffect() {
      Node root = new Node(Token.PROGRAM, null);
      Node expr1 = new Node(Token.EXPR_STATEMENT, null);
      Node expr1Body = new Node(Token.BINOP_PLUS, new Node[]{
          new Node(Token.NUM, 1),
          new Node(Token.NUM, 2)
      }, null);
      expr1.setFirstChild(expr1Body);
      root.setFirstChild(expr1);

      TestCompiler compiler = new TestRemoveConstantExpressions();
      Result result = compiler.process(null, root, Lists.newArrayList());
      assertFalse(result.changed);
    }
  }
}