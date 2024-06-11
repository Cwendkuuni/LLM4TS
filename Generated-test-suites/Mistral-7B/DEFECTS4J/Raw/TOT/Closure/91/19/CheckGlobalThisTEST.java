import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private Checker checker;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 checker = new Checker(compiler, new CompilerOptions());
 nodeFactory = checker.getFactory();
 }

 @Test
 public void testCheckGlobalThisNoIssue() {
 String code = "function Foo() {}";
 Node ast = Parser.parse(code, null, null).getRootNode();
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 checker.check(compiler, ast, null);
 traversal.traverseAll(ast);
 // No diagnostic should be thrown
 }

 @Test
 public void testCheckGlobalThisFunctionProperty() {
 String code = "function Foo() { this.x = 1; }";
 Node ast = Parser.parse(code, null, null).getRootNode();
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 checker.check(compiler, ast, null);
 traversal.traverseAll(ast);
 when(compiler.report(Mockito.any(DiagnosticType.class), Mockito.any(Node.class), Mockito.any(CheckLevel.class)))
 .thenReturn(true); // Silence the actual report
 // A diagnostic should be thrown
 }

 @Test
 public void testCheckGlobalThisFunctionMethod() {
 String code = "function Foo() { this.useMethod = function() {}; }";
 Node ast = Parser.parse(code, null, null).getRootNode();
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 checker.check(compiler, ast, null);
 traversal.traverseAll(ast);
 // No diagnostic should be thrown
 }

 @Test
 public void testCheckGlobalThisFunctionConstructor() {
 String code = "function Foo() { this.constructor = Foo; }";
 Node ast = Parser.parse(code, null, null).getRootNode();
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 checker.check(compiler, ast, null);
 traversal.traverseAll(ast);
 // No diagnostic should be thrown
 }

 @Test
 public void testCheckGlobalThisPrototypeProperty() {
 String code = "var Foo = function() {}; Foo.prototype.x = function() {};";
 Node ast = Parser.parse(code, null, null).getRootNode();
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 checker.check(compiler, ast, null);
 traversal.traverseAll(ast);
 // No diagnostic should be thrown
 }

 @Test
 public void testCheckGlobalThisPrototypeMethod() {
 String code = "var Foo = function() {}; Foo.prototype.x = function x() {};";
 Node ast = Parser.parse(code, null, null).getRootNode();
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 checker.check(compiler, ast, null);
 traversal.traverseAll(ast);
 // No diagnostic should be thrown
 }

 @Test
 public void testCheckGlobalThisObjectLiteral() {
 String code = "var Foo = { x: function() {} };";
 Node ast = Parser.parse(code, null, null).getRootNode();
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 checker.check(compiler, ast, null);
 traversal.traverseAll(ast);
 // No diagnostic should be thrown
 }

 @Test
 public void testCheckGlobalThisObjectLiteralProperty() {
 String code = "var Foo = { x: {} }; Foo.x.bar = function() {};";
 Node ast = Parser.parse(code, null, null).getRootNode();
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 checker.check(compiler, ast, null);
 traversal.traverseAll(ast);
 // No diagnostic should be thrown
 }
}