import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class FunctionRewriterTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testEmptyFunctionReducer() {
 Node node = compiler.parseJs("function(){}", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, node);
 Node expected = compiler.parseJs("emptyFn()", null, 1, 1);
 assertEquals(expected.toString(), node.toString());
 }

 @Test
 public void testEmptyFunctionReducerWithCodeInsertion() {
 Node root = compiler.parseJs("function a(){};", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, root);
 Node expected = compiler.parseJs("emptyFn()", null, 1, 1);
 Node rootAfterCompilation = compiler.process(root, options);
 Node expectedCodeInsertion = rootAfterCompilation.getFirstChild();
 assertTrue(expectedCodeInsertion.equals(expected));
 }

 @Test
 public void testIdentityReducer() {
 Node node = compiler.parseJs("function(a){return a}", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, node);
 Node expected = compiler.parseJs("identityFn(a)", null, 1, 1);
 assertEquals(expected.toString(), node.toString());
 }

 @Test
 public void testIdentityReducerWithCodeInsertion() {
 Node root = compiler.parseJs("function a(b){return a};function b(){}", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, root);
 Node expected = compiler.parseJs("identityFn(a)", null, 1, 1);
 Node rootAfterCompilation = compiler.process(root, options);
 Node expectedCodeInsertion = rootAfterCompilation.getChildAt(1);
 assertTrue(expectedCodeInsertion.equals(expected));
 }

 @Test
 public void testReturnConstantReducer() {
 Node node = compiler.parseJs("function(){return 1}", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, node);
 Node expected = compiler.parseJs("returnconst(1)", null, 1, 1);
 assertEquals(expected.toString(), node.toString());
 }

 @Test
 public void testReturnConstantReducerWithCodeInsertion() {
 Node root = compiler.parseJs("function a(){return 1};function b(){};", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, root);
 Node expected = compiler.parseJs("returnconst(1)", null, 1, 1);
 Node rootAfterCompilation = compiler.process(root, options);
 Node expectedCodeInsertion = rootAfterCompilation.getFirstChild();
 assertTrue(expectedCodeInsertion.equals(expected));
 }

 @Test(expected = IllegalStateException.class)
 public void testReducerWithNoFunctionExpression() {
 Node node = compiler.parseJs("function(){};", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, node);
 fail("Expected IllegalStateException");
 }

 @Test
 public void testGetterReducer() {
 Node node = compiler.parseJs("function a(){return this.b_}", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, node);
 Node expected = compiler.parseJs("getter(\"b_\")", null, 1, 1);
 assertEquals(expected.toString(), node.toString());
 }

 @Test
 public void testGetterReducerWithCodeInsertion() {
 Node root = compiler.parseJs("function a(){};function b(){return this.b_}", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, root);
 Node expected = compiler.parseJs("getter(\"b_\")", null, 1, 1);
 Node rootAfterCompilation = compiler.process(root, options);
 Node expectedCodeInsertion = rootAfterCompilation.getChildAt(1);
 assertTrue(expectedCodeInsertion.equals(expected));
 }

 @Test
 public void testSetterReducer() {
 Node node = compiler.parseJs("function a(b){this.b_ = b}", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, node);
 Node expected = compiler.parseJs("getter(\"b_\"),setter(\"b_\")", null, 1, 1);
 assertEquals(expected.toString(), node.toString());
 }

 @Test
 public void testSetterReducerWithCodeInsertion() {
 Node root = compiler.parseJs("function a(){};function b(c){this.b_ = c}", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, root);
 Node expected = compiler.parseJs("getter(\"b_\"),setter(\"b_\")", null, 1, 1);
 Node rootAfterCompilation = compiler.process(root, options);
 Node expectedCodeInsertion = rootAfterCompilation.getChildAt(1);
 assertTrue(expectedCodeInsertion.equals(expected));
 }

 @Test
 public void testFunctionWithMultipleStatements() {
 Node node = compiler.parseJs("function a(){return 1;this.b_ = 2}", null, 1, 1);
 FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
 functionRewriter.process(null, node);
 Node expected = compiler.parseJs("returnconst(1)", null, 1, 1);
 Node nodeAfterCompilation = compiler.process(node, options).getFirstChild();
 assertTrue(expected.toString().equals(nodeAfterCompilation.toString()));
 }
}