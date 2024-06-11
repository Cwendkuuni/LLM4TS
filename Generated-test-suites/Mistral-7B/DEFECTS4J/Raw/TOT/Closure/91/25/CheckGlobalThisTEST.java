import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Node;

/**
 * Test class for CheckGlobalThis
 */
public class CheckGlobalThisTest {

 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 }

 @Test
 public void testNoGlobalThis() {
 String code = "function foo() { return 42; }";
 CompilerOptions options = CompilerOptions.defaultOptions();
 Node tree = Parser.parse(code, null, options);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisAtRoot() {
 String code = "function foo() { return this; }";
 CompilerOptions options = CompilerOptions.defaultOptions();
 Node tree = Parser.parse(code, null, options);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertEquals(1, compiler.getDiagnostics().size());
 assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getType());
 }

 @Test
 public void testGlobalThisInFunction() {
 String code = "function foo() { return this.bar; }";
 CompilerOptions options = CompilerOptions.defaultOptions();
 Node tree = Parser.parse(code, null, options);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertEquals(1, compiler.getDiagnostics().size());
 assertEquals(DiagnosticType.WARNING, compiler.getDiagnostics().get(0).getType());
 }

 @Test
 public void testGlobalThisInConstructor() {
 String code = "function Foo() { this.foo = 42; }";
 CompilerOptions options = CompilerOptions.defaultOptions();
 Node tree = Parser.parse(code, null, options);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisInPrototypeMethod() {
 String code = "function Foo() {} Foo.prototype.foo = function() { return this.bar; }";
 CompilerOptions options = CompilerOptions.defaultOptions();
 Node tree = Parser.parse(code, null, options);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisInAnnotatedFunction() {
 String code = "@constructor\nfunction Foo() { return this; }";
 CompilerOptions options = CompilerOptions.defaultOptions();
 Node tree = Parser.parse(code, null, options);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisInAnnotatedFunctionWithIdentifier() {
 String code = "@this\nfunction Foo() { return this; }";
 CompilerOptions options = CompilerOptions.defaultOptions();
 Node tree = Parser.parse(code, null, options);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisInAssignment() {
 String code = "var foo = function() { return this; }";
 CompilerOptions options = CompilerOptions.defaultOptions();
 Node tree = Parser.parse(code, null, options);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testGlobalThisInObjectLiteral() {
 String code = "var foo = { foo: function() { return this; } }";
 CompilerOptions options = CompilerOptions.defaultOptions();
 Node tree = Parser.parse(code, null, options);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(tree, checker);
 traversal.traverse();
 assertEquals(0, compiler.getDiagnostics().size());
 }
}