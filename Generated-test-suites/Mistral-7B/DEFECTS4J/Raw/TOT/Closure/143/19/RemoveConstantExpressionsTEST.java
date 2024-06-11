import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

public class RemoveConstantExpressionsTest {

 private AbstractCompiler compiler;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler(null, null, null);
 nodeFactory = compiler.getFactory();
 }

 @Test
 public void testNoSideEffectExpressionIsRemoved() {
 Node root = new Node(Token.PROGRAM, null);
 Node expr = new Node(Token.EXPR_RESULT, null);
 expr.setFirstChild(new Node(Token.NUM, 1));
 expr.setFirstChild(expr.getFirstChild(), new Node(Token.PLUS, null));
 expr.setFirstChild(expr.getFirstChild().getNextSib(),
 new Node(Token.IDENT, "foo"));
 expr.setFirstChild(expr.getFirstChild().getNextSib(),
 new Node(Token.PLUS, null));
 expr.setFirstChild(expr.getFirstChild().getNextSib().getNextSib(),
 new Node(Token.IDENT, "bar"));
 Node parent = new Node(Token.EXPR_STATEMENT, null);
 parent.setFirstChild(expr);
 root.setFirstChild(parent);
 CompilerPass pass = new RemoveConstantExpressions(compiler);
 Result result = new Result();
 pass.process(null, root);
 assertFalse(result.changed);
 Node simplifiedExpr = result.getResult().getLastNode();
 Node simplifiedParent = simplifiedExpr.getParent();
 assertTrue(simplifiedParent instanceof Node);
 assertTrue(simplifiedParent.getFirstChild() instanceof Node);
 assertTrue(simplifiedParent.getFirstChild().getNextSib() instanceof Node);
 assertTrue(simplifiedParent.getFirstChild().getNextSib().getNextSib() instanceof Node);
 assertTrue(simplifiedParent.getFirstChild().getNextSib().getNextSib().getType() == Token.IDENT);
 assertTrue(simplifiedParent.getFirstChild().getNextSib().getNextSib().getString().equals("bar"));
 }

 @Test
 public void testSideEffectExpressionIsNotRemoved() {
 Node root = new Node(Token.PROGRAM, null);
 Node parent = new Node(Token.EXPR_STATEMENT, null);
 Node increment = new Node(Token.ASSIGN, null);
 increment.setFirstChild(new Node(Token.IDENT, "i"));
 increment.setFirstChild(increment.getFirstChild(),
 new Node(Token.PLUS_EQUAL, null));
 increment.setFirstChild(increment.getFirstChild().getNextSib(),
 new Node(Token.NUM, 1));
 Node expr = new Node(Token.EXPR_RESULT, null);
 expr.setFirstChild(new Node(Token.PLUS, null));
 expr.setFirstChild(expr.getFirstChild(), increment);
 expr.setFirstChild(expr.getFirstChild().getNextSib(),
 new Node(Token.IDENT, "foo"));
 Node statement = new Node(Token.EXPR_STATEMENT, null);
 statement.setFirstChild(expr);
 parent.setFirstChild(statement);
 root.setFirstChild(parent);
 CompilerPass pass = new RemoveConstantExpressions(compiler);
 Result result = new Result();
 pass.process(null, root);
 assertFalse(result.changed);
 }

 @Test
 public void testSideEffectSubexpressionIsRemoved() {
 Node root = new Node(Token.PROGRAM, null);
 Node parent = new Node(Token.EXPR_STATEMENT, null);
 Node increment = new Node(Token.ASSIGN, null);
 increment.setFirstChild(new Node(Token.IDENT, "i"));
 increment.setFirstChild(increment.getFirstChild(),
 new Node(Token.PLUS_EQUAL, null));
 increment.setFirstChild(increment.getFirstChild().getNextSib(),
 new Node(Token.NUM, 1));
 Node sideEffectExpr = new Node(Token.EXPR_RESULT, null);
 sideEffectExpr.setFirstChild(new Node(Token.PLUS, null));
 sideEffectExpr.setFirstChild(sideEffectExpr.getFirstChild(), increment);
 sideEffectExpr.setFirstChild(sideEffectExpr.getFirstChild().getNextSib(),
 new Node(Token.NUM, 2));
 Node expr = new Node(Token.EXPR_RESULT, null);
 expr.setFirstChild(new Node(Token.NUM, 1));
 expr.setFirstChild(expr.getFirstChild(),
 new Node(Token.PLUS, null));
 expr.setFirstChild(expr.getFirstChild().getNextSib(), sideEffectExpr);
 Node statement = new Node(Token.EXPR_STATEMENT, null);
 statement.setFirstChild(expr);
 parent.setFirstChild(statement);
 root.setFirstChild(parent);
 CompilerPass pass = new RemoveConstantExpressions(compiler);
 Result result = new Result();
 pass.process(null, root);
 assertFalse(result.changed);
 Node simplifiedExpr = result.getResult().getLastNode();
 Node simplifiedParent = simplifiedExpr.getParent();
 assertTrue(simplifiedParent instanceof Node);
 assertTrue(simplifiedParent.getFirstChild() instanceof Node);
 assertTrue(simplifiedParent.getFirstChild().getType() == Token.NUM);
 assertTrue(simplifiedParent.getFirstChild().getString().equals("1"));
 assertTrue(simplifiedParent.getFirstChild().getNextSib() == null);
 }
}