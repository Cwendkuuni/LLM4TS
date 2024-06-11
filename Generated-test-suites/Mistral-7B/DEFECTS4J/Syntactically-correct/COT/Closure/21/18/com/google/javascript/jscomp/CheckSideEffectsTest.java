import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

 private AbstractCompiler compiler;
 private CheckSideEffects checkSideEffects;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 nodeFactory = NodeFactory.getFactory();
 }

 @Test
 public void testEmptyStatement() {
 Node root = nodeFactory.createBlock();
 checkSideEffects.process(null, root);
 assertFalse(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testCommaOperator() {
 Node root = nodeFactory.createBlock();
 Node a = nodeFactory.createNumber(1);
 Node b = nodeFactory.createNumber(2);
 root.addChild(nodeFactory.createExpressionStatement(nodeFactory.createAssignment(nodeFactory.createName("a"), a)));
 root.addChild(nodeFactory.createExpressionStatement(nodeFactory.createAssignment(nodeFactory.createName("b"), b)));
 root.addChild(nodeFactory.createExpressionStatement(nodeFactory.createAssignment(nodeFactory.createName("c"), nodeFactory.createBinaryOperator(Token.OP_PLUS, a, b))));
 checkSideEffects.process(null, root);
 assertFalse(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVoidStatement() {
 Node root = nodeFactory.createBlock();
 root.addChild(nodeFactory.createExpressionStatement(nodeFactory.createEmptyStatement()));
 checkSideEffects.process(null, root);
 assertFalse(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testForLoopWithSemicolons() {
 Node root = nodeFactory.createBlock();
 Node initialization = nodeFactory.createBlock();
 Node condition = nodeFactory.createBooleanLiteral(false);
 Node increment = nodeFactory.createExpressionStatement(nodeFactory.createEmptyStatement());
 Node loop = nodeFactory.createFor(initialization, condition, null, increment);
 root.addChild(loop);
 checkSideEffects.process(null, root);
 assertFalse(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testSimpleOperatorWithResultUsed() {
 Node root = nodeFactory.createBlock();
 Node a = nodeFactory.createNumber(1);
 Node b = nodeFactory.createNumber(2);
 Node result = nodeFactory.createExpressionStatement(nodeFactory.createAssignment(nodeFactory.createName("x"), nodeFactory.createBinaryOperator(Token.OP_PLUS, a, b)));
 checkSideEffects.process(null, root);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testProtectedSideEffectFreeCode() {
 Node root = nodeFactory.createBlock();
 Node protectedCode = nodeFactory.createExpressionStatement(nodeFactory.createCall(nodeFactory.createName("JSCOMPILER_PRESERVE"), null, null));
 root.addChild(protectedCode);
 checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
 checkSideEffects.process(null, root);
 }

 @Test
 public void testProtectSideEffectsMethod() {
 checkSideEffects.protectSideEffects();
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testStripProtectionMethod() {
 CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
 Node root = nodeFactory.createBlock();
 Node protectedCode = nodeFactory.createCall(nodeFactory.createName("JSCOMPILER_PRESERVE"), null, null);
 root.addChild(protectedCode);
 stripProtection.process(null, root);
 assertTrue(root.getLastChild().isUndefined());
 }
}