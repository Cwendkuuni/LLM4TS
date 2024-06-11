import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Root;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private Node rootNode;

 @BeforeClass
 public static void initTesting() {
 CompilerPassManager.getInstance().addPass(UnreachableCodeElimination.class);
 }

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 // Create a simple test case with a function that contains dead code.
 NodeFactory factory = compiler.getFactory();
 rootNode = factory.createFunctionDeclaration("test", Token.FUNCTION_DECL, null, null,
 null, null, null, factory.createBlock(3));
 Node body = rootNode.getLastChild();
 body.addChild(factory.createReturnStatement(factory.createNumberLiteral(1)));
 body.addChild(factory.createExpressionStatement(factory.createPrintStatement(
 factory.createStringLiteral("This will be removed"))));
 body.addChild(factory.createReturnStatement(factory.createNumberLiteral(2)));
 }

 @Test
 public void testBasicFunction() {
 NodeTraversal traversal = NodeTraversal.create(compiler, rootNode, null,
 Root, null);
 CompilerPassManager.runPasses(traversal);
 // The dead code should have been removed.
 assertEquals(2, rootNode.getLastChild().getNumChildren());
 }

 @Test
 public void testNestedFunction() {
 Node nestedFunction = rootNode.getFirstChild();
 nestedFunction.setName("nestedFunction");
 nestedFunction.setBody(factory.createBlock(2));
 nestedFunction.addChild(factory.createReturnStatement(factory.createNumberLiteral(3)));
 rootNode.addChild(nestedFunction);

 NodeTraversal traversal = NodeTraversal.create(compiler, rootNode, null,
 Root, null);
 CompilerPassManager.runPasses(traversal);
 // The dead code should still be present in the nested function.
 assertEquals(3, nestedFunction.getLastChild().getNumChildren());
 }

 @Test
 public void testExceptionHandling() {
 Node tryNode = rootNode.getFirstChild();
 tryNode.setName("tryBlock");
 Node catchNode = factory.createCatchBlock("catchBlock", null, null, null, null);
 Node finallyNode = factory.createBlock(1);
 NodeUtil.maybeAddFinally(tryNode, finallyNode);
 tryNode.addChild(catchNode);
 tryNode.addChild(finallyNode);

 Node body = rootNode.getLastChild();
 body.addChild(tryNode);
 body.addChild(factory.createReturnStatement(factory.createNumberLiteral(4)));

 NodeTraversal traversal = NodeTraversal.create(compiler, rootNode, null,
 Root, null);
 CompilerPassManager.runPasses(traversal);
 // The dead code should still be present because the exception handling logic prevents the elimination.
 assertEquals(5, rootNode.getLastChild().getNumChildren());
 }

 @Test(expected = AssertionError.class)
 public void testLogging() {
 Logger logger = Logger.getLogger(UnreachableCodeElimination.class.getName());
 logger.setLevel(Level.SEVERE);
 NodeTraversal traversal = NodeTraversal.create(compiler, rootNode, null, Root, null);
 CompilerPassManager.runPasses(traversal);
 fail("Logging statement should not be removed");
 }

 @Test
 public void testNoOpStatements() {
 Node noOpStatementNode = factory.createExpressionStatement(
 factory.createCallExpression(null, "alert", factory.createStringLiteral("")));
 rootNode.addChild(noOpStatementNode);
 NodeTraversal traversal = NodeTraversal.create(compiler, rootNode, null,
 Root, null);
 CompilerPassManager.runPasses(traversal);
 // The no-op statement should have been removed.
 assertNull(rootNode.getChild(rootNode.getNumChildren() - 1));
 }

 @After
 public void tearDown() {
 compiler = null;
 rootNode = null;
 }

}