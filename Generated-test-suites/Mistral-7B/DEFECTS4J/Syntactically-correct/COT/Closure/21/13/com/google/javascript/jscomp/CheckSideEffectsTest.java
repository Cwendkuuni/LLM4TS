import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CheckerFactory.CheckerFactoryBuilder;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGrouping;
import com.google.javascript.jscomp.DiagnosticTypes;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

 private static SourceFile sourceFile;
 private static Node root;
 private static Compiler compiler;
 private static CheckSideEffects checkSideEffects;
 private static CheckerFactory checkerFactory;
 private static AbstractCompiler abstractCompiler;
 private static CheckLevel level;
 private static boolean protectSideEffectFreeCode;

 @Before
 public void setUp() {
 sourceFile = new SourceFile("CheckSideEffectsTest.js");
 root = new Node(Token.BLOCK);
 compiler = new TestCompiler(new CompilerOptions(), new Parser(new NodeFactory()));
 checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, protectSideEffectFreeCode);
 checkerFactory = new CheckerFactoryBuilder(new DiagnosticGrouping(), compiler)
 .setUseTypes(true)
 .setSourceFile(sourceFile)
 .setCompilerPasses(Lists.newArrayList(checkSideEffects))
 .setProcessingPasses(Lists.newArrayList(AbstractPostOrderCallback.class.getName()))
 .buildCheckerFactory();
 abstractCompiler = compiler.getAbstractCompiler();
 level = CheckLevel.WARNING;
 protectSideEffectFreeCode = false;
 }

 @Test
 public void testVisitVoidNode() {
 Node voidNode = new Node(Token.VOID);
 checkSideEffects.visit(NodeTraversal.create(abstractCompiler, root, null), voidNode, null);
 assertFalse(checkSideEffects.problemNodes.isEmpty());
 assertFalse(voidNode.getParent().getLastChild().equals(voidNode));
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testVisitQualifiedNameNode() {
 Node qualifiedNameNode = IR.name("foo.bar");
 qualifiedNameNode.putBooleanProp(Node.IS_CONSTANT_NAME, true);
 checkSideEffects.visit(NodeTraversal.create(abstractCompiler, root, null), qualifiedNameNode, null);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitSimpleOpNode() {
 Node opNode = IR.binaryOp("+", IR.string("a"), IR.number(1));
 checkSideEffects.visit(NodeTraversal.create(abstractCompiler, root, null), opNode, null);
 assertFalse(checkSideEffects.problemNodes.isEmpty());
 JSDocInfo jsDocInfo = opNode.getJSDocInfo();
 assertTrue(jsDocInfo != null);
 assertTrue(jsDocInfo.isPresent());
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testVisitNonExprResultNode() {
 Node nonResultNode = IR.assignment("x", IR.number(1));
 checkSideEffects.visit(NodeTraversal.create(abstractCompiler, root, null), nonResultNode, null);
 assertFalse(checkSideEffects.problemNodes.isEmpty());
 checkSideEffects.problemNodes.clear();
 }

 @Test
 public void testVisitExprResultNode() {
 Node exprResultNode = IR.exprResult();
 checkSideEffects.visit(NodeTraversal.create(abstractCompiler, root, null), exprResultNode, null);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testVisitBlockNode() {
 Node blockNode = IR.block();
 checkSideEffects.visit(NodeTraversal.create(abstractCompiler, root, null), blockNode, null);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testVisitForNode() { // This test case is intentionally left empty
 }

 @Test
 public void testProcessNodeWithSideEffectCode() {
 Node sideEffectNode = IR.assignment("x", IR.name("foo").call(null, IR.number(1)));
 Node parentNode = IR.exprResult();
 parentNode.addChildToBack(sideEffectNode);
 root.addChildToBack(parentNode);
 checkSideEffects.process(null, root);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testProcessNodeWithSideEffectFreeCode() {
 Node sideEffectFreeNode = IR.string("This code lacks side-effects.");
 Node parentNode = IR.exprResult();
 parentNode.addChildToBack(sideEffectFreeNode);
 root.addChildToBack(parentNode);
 checkSideEffects.process(null, root);
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 }

 @Test
 public void testProtectSideEffects() {
 Node sideEffectFreeNode = IR.string("This code lacks side-effects.");
 Node parentNode = IR.exprResult();
 parentNode.addChildToBack(sideEffectFreeNode);
 root.addChildToBack(parentNode);
 checkSideEffects.protectSideEffects();
 assertTrue(checkSideEffects.problemNodes.isEmpty());
 Node protectorFnNode = IR.name(CheckSideEffects.PROTECTOR_FN);
 Node replacementNode = IR.call(protectorFnNode);
 Node protectedNode = root.getFirstChild();
 Node expectedProtectedNode = new Node(BLOCK);
 expectedProtectedNode.addChildToBack(replacementNode);
 expectedProtectedNode.addChildToBack(protectedNode);
 assertTrue(root.getFirstChild().equals(expectedProtectedNode));
 }
}