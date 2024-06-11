import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private Node rootNode;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 CompilerOptions options = new CompilerOptions(null);
 Parser parser = new Parser(new CharStreams.StringReader(""));
 rootNode = parser.parseUnit("test.js", null, options);
 }

 @Test
 public void testGlobalThis() {
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testSafeGlobalThis() {
 rootNode.setJSDocInfo(new JSDocInfo("@constructor"));
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertTrue(checker.getDiagnostics().isEmpty());
 }

 @Test
 public void testLocalThis() {
 rootNode.getFirstChild().setJSDocInfo(new JSDocInfo("function foo() { this.x = 42; }"));
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertTrue(checker.getDiagnostics().isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testPrototypeThis() {
 rootNode.setJSDocInfo(new JSDocInfo("function Foo() { this.x = 42; } Foo.prototype.y = function() { this.x = 43; }"));
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 fail("Expected warning for prototype method, but none was thrown.");
 }

 @Test
 public void testUselessThis() {
 rootNode.setJSDocInfo(new JSDocInfo("function Foo() { var x = this; }"));
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertTrue(checker.getDiagnostics().isEmpty());
 }

 @Test
 public void testGlobalThisWithAssign() {
 rootNode.setJSDocInfo(null);
 rootNode.addChild(Token.ASSIGN);
 rootNode.addChild(Token.THIS);
 rootNode.addChild(Token.GETPROP);
 rootNode.getLastChild().setString("x");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisWithAssignAndPropertyAccess() {
 rootNode.setJSDocInfo(null);
 rootNode.addChild(Token.ASSIGN);
 rootNode.getFirstChild().setJSDocInfo(new JSDocInfo("function foo() { return this.x; }"));
 rootNode.getFirstChild().addChild(Token.THIS);
 rootNode.getFirstChild().addChild(Token.GETPROP);
 rootNode.getFirstChild().getLastChild().setString("x");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisWithFunctionExpression() {
 rootNode.setJSDocInfo(null);
 rootNode.addChild(Token.FUNCTION);
 rootNode.getFirstChild().setJSDocInfo(new JSDocInfo("function foo() { this.x = 42; }"));
 rootNode.getFirstChild().addChild(Token.THIS);
 rootNode.getFirstChild().getLastChild().setString("x");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisWithVarStatement() {
 rootNode.setJSDocInfo(null);
 rootNode.addChild(Token.VAR);
 rootNode.getFirstChild().setName("foo");
 rootNode.getFirstChild().addChild(Token.EQUAL);
 rootNode.getFirstChild().getLastChild().setJSDocInfo(new JSDocInfo("function foo() { this.x = 42; }"));
 rootNode.getFirstChild().getLastChild().addChild(Token.THIS);
 rootNode.getFirstChild().getLastChild().getLastChild().setString("x");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisWithObjectLiteral() {
 rootNode.setJSDocInfo(null);
 rootNode.addChild(Token.OBJECT);
 rootNode.getFirstChild().setJSDocInfo(new JSDocInfo("function Foo() { this.x = 42; } Foo.prototype.y = function() { this.x = 43; }"));
 rootNode.getFirstChild().addChild(Token.STRING).setString("x");
 rootNode.getFirstChild().addChild(Token.COLON);
 rootNode.getFirstChild().addChild(Token.NUMBER).setInt(42);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 fail("Expected warning for object literal, but none was thrown.");
 }

 @Test
 public void testGlobalThisWithObjectLiteralAndMethod() {
 rootNode.setJSDocInfo(null);
 rootNode.addChild(Token.OBJECT);
 rootNode.getFirstChild().setJSDocInfo(new JSDocInfo("function Foo() { } Foo.prototype.x = function() { this.x = 42; }"));
 rootNode.getFirstChild().addChild(Token.STRING).setString("x");
 rootNode.getFirstChild().addChild(Token.COLON);
 rootNode.getFirstChild().addChild(Token.OBJECT);
 rootNode.getFirstChild().getLastChild().addChild(Token.STRING).setString("x");
 rootNode.getFirstChild().getLastChild().addChild(Token.COLON);
 rootNode.getFirstChild().getLastChild().addChild(Token.FUNCTION);
 rootNode.getFirstChild().getLastChild().getLastChild().setString("x");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testNoDiagnostics() {
 rootNode.setJSDocInfo(new JSDocInfo("function Foo() { this.x = 42; } this.y = 43;"));
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(rootNode);
 traversal.traverse(checker);
 assertTrue(checker.getDiagnostics().isEmpty());
 }
}