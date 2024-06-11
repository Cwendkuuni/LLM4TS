import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FunctionRewriterTest {

 private AbstractCompiler compiler;
 private Node rootNode;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 rootNode = compiler.parseJavaScript("class TestClass {\n" +
 "prototype.testMethod = function(value) {\n" +
 "this.testValue = value;\n" +
 "};\n" +
 "}");
 }

 @Test
 public void testFunctionRewriter_IdentityFunction() {
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, rootNode);
 Node testMethodNode = compiler.getTopLevelNodes().get(0)
 .getFirstChild()
 .getFirstChild()
 .getLastChild();
 Node identityFunction = (Node) testMethodRewritten.get(1);
 assertEquals("identity", identityFunction.getString());
 }

 @Test
 public void testFunctionRewriter_EmptyFunction() {
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, rootNode);
 Node emptyFunction = (Node) testMethodRewritten.get(0);
 assertEquals("emptyFn", emptyFunction.getString());
 }

 @Test
 public void testFunctionRewriter_ReturnConstant() {
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 Node constantNode = new Node(Token.NUMBER, 10, 0);
 rootNode.addChildToFront(new Node(Token.ASSIGN, 0, 0, null,
 new Node(Token.NAME, "testClassPrototype", 0, 0),
 new Node(Token.DOT, 0, 0),
 new Node(Token.NAME, "testMethod", 0, 0),
 new Node(Token.COLON, 0, 0),
 constantNode));
 rewriter.process(null, rootNode);
 Node constantFunction = (Node) testMethodRewritten.get(1);
 Node constantValue = constantFunction.getLastChild();
 assertEquals(10, constantValue.getDouble());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionRewriter_InvalidFunction() {
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rootNode.getLastChild().setType(Token.BLOCK);
 rewriter.process(null, rootNode);
 }

 @Test
 public void testFunctionRewriter_SetterFunction() {
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, rootNode);
 Node setterFunction = (Node) testMethodRewritten.get(2);
 assertEquals("setter", setterFunction.getString());
 }

 @Test
 public void testFunctionRewriter_GetterFunction() {
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 rewriter.process(null, rootNode);
 Node getterFunction = (Node) testMethodRewritten.get(3);
 assertEquals("getter", getterFunction.getString());
 }

 @Test
 public void testFunctionRewriter_MultipleFunctions() {
 FunctionRewriter rewriter = new FunctionRewriter(compiler);
 Node multipleFunctionsNode = new Node(Token.BLOCK, 0, 0);
 multipleFunctionsNode.addChildToBack(new Node(Token.VAR, 0, 0, null,
 new Node(Token.NAME, "testMethod1", 0, 0), null, null, null,
 new Node(Token.FUNCTION, 0, 0, null, null, null, null, null, null)));
 multipleFunctionsNode.addChildToBack(new Node(Token.VAR, 0, 0, null,
 new Node(Token.NAME, "testMethod2", 0, 0), null, null, null,
 new Node(Token.FUNCTION, 0, 0, null, null, null, null, null, null)));
 rootNode.setFirstChild(multipleFunctionsNode);
 rewriter.process(null, rootNode);
 List<Node> testMethodRewritten = compiler.getTopLevelNodes();
 assertEquals(5, testMethodRewritten.size());
 }

 private List<Node> testMethodRewritten = Lists.newArrayList();

}