import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilationTestBase;

public class MethodCompilerPassTest extends CompilationTestBase {

 private static final String TEST_SOURCE =
     "function foo() {} " +
     "function bar(x) {} " +
     "function baz() {} " +
     "function qux(x, y) {} " +
     "function quux() {} " +
     "var Foo = {}; " +
     "Foo.prototype.qux = function(x, y) {}; " +
     "Foo.prototype.quux = function() {}; ";

 private MethodCompilerPass methodCompilerPass;
 private SignatureStore signatureStore;

 @BeforeClass
 public static void setupClass() {
   // Setup for the test, this will initialize the JsCompiler instance
   CompilerOptions options = new CompilerOptions();
   AbstractCompiler compiler = new AbstractCompiler(options);
   methodCompilerPass = new MethodCompilerPass(compiler);
 }

 @Before
 public void setUp() {
   signatureStore = methodCompilerPass.getSignatureStore();
   signatureStore.reset();
 }

 @Test
 public void testExtractMethods() {
   CompilerInput input = new CompilerInput(SourceFile.getSourceInfo("TestSource.js", TEST_SOURCE));
   methodCompilerPass.process(input.getTree(), null);

   assertEquals(6, methodCompilerPass.externMethods.size());
   assertEquals(4, methodCompilerPass.methodDefinitions.size());
   assertTrue(methodCompilerPass.externMethods.contains("foo"));
   assertTrue(methodCompilerPass.externMethods.contains("bar"));
   assertTrue(methodCompilerPass.externMethods.contains("qux"));
   assertTrue(methodCompilerPass.externMethods.contains("quux"));
   assertFalse(methodCompilerPass.externMethods.contains("baz"));
   assertTrue(methodCompilerPass.methodDefinitions.containsKey("qux"));
   assertTrue(methodCompilerPass.methodDefinitions.containsKey("quux"));
 }

 @Test
 public void testExtractPrototypeMethods() {
   CompilerInput input = new CompilerInput(SourceFile.getSourceInfo("TestSource.js", TEST_SOURCE));
   methodCompilerPass.process(input.getTree(), null);

   assertTrue(methodCompilerPass.methodDefinitions.containsKey("qux"));
   assertTrue(methodCompilerPass.methodDefinitions.containsKey("quux"));
 }

 @Test
 public void testUnknownMethod() {
   CompilerInput input = new CompilerInput(SourceFile.getSourceInfo("TestSource.js", TEST_SOURCE + " unknownMethod();"));
   methodCompilerPass.process(input.getTree(), null);

   assertEquals(6, methodCompilerPass.externMethods.size());
   assertFalse(methodCompilerPass.methodDefinitions.containsKey("unknownMethod"));
 }

 @Test
 public void testMethodsWithSameNameButDifferentFunctionNodes() {
   CompilerInput input = new CompilerInput(SourceFile.getSourceInfo("TestSource.js", TEST_SOURCE + " function foo() {}; function foo() {};"));
   methodCompilerPass.process(input.getTree(), null);

   assertEquals(6, methodCompilerPass.externMethods.size());
   assertEquals(3, methodCompilerPass.methodDefinitions.size());
   assertTrue(methodCompilerPass.methodDefinitions.containsKey("foo"));
 }

 @Test
 public void testNonMethodProperty() {
   CompilerInput input = new CompilerInput(SourceFile.getSourceInfo("TestSource.js", "var x = 5;"));
   methodCompilerPass.process(input.getTree(), null);

   assertNull(methodCompilerPass.getSignatureStore().getSignature("x"));
 }

 @Test
 public void testExternMethodWithoutSignature() {
   CompilerInput input = new CompilerInput(SourceFile.getSourceInfo("TestSource.js", "var Foo = {bar: function() {}};"));
   methodCompilerPass.process(input.getTree(), null);

   assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("bar"));
   assertNull(methodCompilerPass.getSignatureStore().getSignature("bar"));
 }

 @Test
 public void testExternMethodWithSignature() {
   CompilerInput input = new CompilerInput(SourceFile.getSourceInfo("TestSource.js", "function foo(x) {return x * 2;};"));
   methodCompilerPass.process(input.getTree(), null);

   assertEquals("function", methodCompilerPass.getSignatureStore().getSignature("foo").getKind());
   assertEquals(1, methodCompilerPass.getSignatureStore().getSignature("foo").getParameterNames().size());
 }

 @Test
 public void testThrowsIllegalArgumentException() {
   CompilerInput input = new CompilerInput(SourceFile.getSourceInfo("TestSource.js", "function foo(x) {throw new IllegalArgumentException();};"));
   methodCompilerPass.process(input.getTree(), null);

   assertThrows(IllegalArgumentException.class, () -> methodCompilerPass.getSignatureStore().getSignature("foo"));
 }

}