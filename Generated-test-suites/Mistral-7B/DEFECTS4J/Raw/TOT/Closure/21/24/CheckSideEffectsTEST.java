import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CheckSideEffectsTest {

 private AbstractCompiler compiler;
 private CompilerInput input;
 private CheckSideEffects checker;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 input = mock(CompilerInput.class);
 checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 }

 @Test
 public void testVisitVoidStatement() {
 Node root = IR.block(Lists.newArrayList(
 IR.statement(IR.VOID()),
 IR.statement(IR.VOID())
 ));
 NodeTraversal traversal = new NodeTraversal(compiler, null, null);
 traversal.traverse(checker, root, null);
 assertFalse(checker.problemNodes.isEmpty());
 assertNotNull(checker.problemNodes.get(0));
 assertNull(checker.problemNodes.get(0).getJSDocInfo());
 }

 @Test
 public void testVisitStringStatement() {
 Node root = IR.block(Lists.newArrayList(
 IR.string("a"),
 IR.string("b")
 ));
 NodeTraversal traversal = new NodeTraversal(compiler, null, null);
 traversal.traverse(checker, root, null);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testVisitSimpleOperator() {
 Node root = IR.block(Lists.newArrayList(
 IR.binaryOp(Token.PLUS, IR.intLit(1), IR.intLit(1), null),
 IR.intLit(2)
 ));
 NodeTraversal traversal = new NodeTraversal(compiler, null, null);
 traversal.traverse(checker, root, null);
 assertFalse(checker.problemNodes.isEmpty());
 assertNotNull(checker.problemNodes.get(0));
 assertEquals("The result of the '+' operator is not being used.",
 checker.problemNodes.get(0).getJSDocInfo().getMessage());
 }

 @Test
 public void testVisitExpressionResultUsed() {
 Node root = IR.block(Lists.newArrayList(
 IR.assign(IR.getName("x"), IR.binaryOp(Token.PLUS, IR.intLit(1), IR.intLit(1), null)),
 IR.intLit(2)
 ));
 NodeTraversal traversal = new NodeTraversal(compiler, null, null);
 traversal.traverse(checker, root, null);
 assertFalse(checker.problemNodes.isEmpty());
 assertNotNull(checker.problemNodes.get(0));
 assertNull(checker.problemNodes.get(0).getJSDocInfo());
 }

 @Test
 public void testVisitCommaExpression() {
 Node root = IR.block(Lists.newArrayList(
 IR.assign(IR.getName("x"), IR.intLit(1)),
 IR.assign(IR.getName("y"), IR.intLit(2)),
 IR.voidStatement()
 ));
 NodeTraversal traversal = new NodeTraversal(compiler, null, null);
 traversal.traverse(checker, root, null);
 assertFalse(checker.problemNodes.isEmpty());
 assertNotNull(checker.problemNodes.get(0));
 assertNull(checker.problemNodes.get(0).getJSDocInfo());
 }

 @Test
 public void testVisitForLoop() {
 Node root = IR.block(Lists.newArrayList(
 IR.forInit(Lists.newArrayList(
 IR.assign(IR.getName("i"), IR.intLit(0))
 )),
 IR.statement(IR.assign(IR.getName("i"), IR.binaryOp(Token.PLUS, IR.getName("i"), IR.intLit(1), null))),
 IR.statement(IR.returnStatement(IR.intLit(i))),
 IR.forCond(IR.name("i"), IR.intLit(10), null),
 IR.forIter(null, null, null)
 ));
 NodeTraversal traversal = new NodeTraversal(compiler, null, null);
 traversal.traverse(checker, root, null);
 assertFalse(checker.problemNodes.isEmpty());
 assertNotNull(checker.problemNodes.get(0));
 assertNull(checker.problemNodes.get(0).getJSDocInfo());
 }

 @Test
 public void testVisitCallStatement() {
 Node root = IR.block(Lists.newArrayList(
 IR.call(IR.name("console").getChild(0), "log", IR.string("Hello world"))
 ));
 NodeTraversal traversal = new NodeTraversal(compiler, null, null);
 traversal.traverse(checker, root, null);
 assertEquals(0, checker.problemNodes.size());
 }

 @Test
 public void testProcessRoot() {
 Node root = IR.program(Lists.newArrayList(
 IR.block(Lists.newArrayList(
 IR.voidStatement(),
 IR.voidStatement()
 )),
 IR.block(Lists.newArrayList(
 IR.string("a"),
 IR.string("b")
 ))
 ));
 CompilerPass pass = checker;
 CompilerInput input = new CompilerInput(null, null, null, root);
 pass.process(input, root);
 assertFalse(checker.problemNodes.isEmpty());
 assertNotNull(checker.problemNodes.get(0));
 assertNull(checker.problemNodes.get(0).getJSDocInfo());
 }

 @Test
 public void testProtectSideEffects() {
 checker.process(input, null);
 checker.protectSideEffects();
 NodeTraversal traversal = new NodeTraversal(compiler, null, null);
 Node root = input.getAstRoot(compiler);
 traversal.traverse(checker, root, null);
 assertEquals(0, checker.problemNodes.size());
 }
}