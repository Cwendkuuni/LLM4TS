import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {

 private AbstractCompiler compiler = new AbstractCompiler() {};
 private MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

 @Test
 public void testMethodCompilerPass_ValidMethod() {
 Node root = createTestNodeForMethod("function foo() {}");
 methodCompilerPass.process(null, root);

 Set<String> expectedMethods = Sets.newHashSet("foo");
 assertEquals(expectedMethods, methodCompilerPass.externMethods);
 }

 @Test
 public void testMethodCompilerPass_MethodWithSignature() {
 Node root = createTestNodeForMethod("function foo(int a, int b) {}");
 methodCompilerPass.process(null, root);

 Set<String> expectedMethods = Sets.newHashSet("foo");
 assertEquals(expectedMethods, methodCompilerPass.externMethods);
 }

 @Test
 public void testMethodCompilerPass_MethodWithoutSignature() {
 Node root = createTestNodeForMethod("function foo() {}");
 methodCompilerPass.process(null, root);

 Set<String> expectedMethods = Sets.newHashSet("foo");
 assertTrue(methodCompilerPass.externMethodsWithoutSignatures.containsAll(expectedMethods));
 }

 @Test(expected = IllegalStateException.class)
 public void testMethodCompilerPass_UndefinedMethod() {
 Node root = createTestNodeForMethod("foo = function() {}");
 methodCompilerPass.process(null, root);
 fail("Should have thrown an IllegalStateException");
 }

 @Test
 public void testMethodCompilerPass_PrototypeMethod() {
 Node root = createTestNodeForMethod("function Foo() {}" +
 "Foo.prototype.bar = function() {}");
 methodCompilerPass.process(null, root);

 Set<String> expectedMethods = Sets.newHashSet("bar");
 assertEquals(expectedMethods, methodCompilerPass.externMethods);
 }

 @Test
 public void testMethodCompilerPass_StaticMethod() {
 Node root = createTestNodeForMethod("function Foo() {}" +
 "Foo.bar = function() {}");
 methodCompilerPass.process(null, root);

 Set<String> expectedMethods = Sets.newHashSet("bar");
 assertEquals(expectedMethods, methodCompilerPass.externMethods);
 }

 @Test
 public void testMethodCompilerPass_ObjectLiteral() {
 Node root = createTestNodeForMethod("{ foo: function() {}, bar: function() {} }");
 methodCompilerPass.process(null, root);

 Set<String> expectedMethods = Sets.newHashSet("foo", "bar");
 assertEquals(expectedMethods, methodCompilerPass.externMethods);
 }

 @Test
 public void testMethodCompilerPass_IgnoredMethodWithoutSignature() {
 Node root = createTestNodeForMethod("function foo() {}" +
 "function bar() {}");
 methodCompilerPass.process(null, root);

 Set<String> expectedMethods = Sets.newHashSet("foo");
 assertTrue(methodCompilerPass.externMethodsWithoutSignatures.containsAll(Sets.newHashSet("bar")));
 }

 private Node createTestNodeForMethod(String code) {
 Node externs = createNodeFromString("", code);
 Node root = createNodeFromString("", "");
 return root;
 }

 private Node createNodeFromString(String name, String code) {
 RhinoCompilerOptions options = new RhinoCompilerOptions();
 RhinoCompiler compiler = new RhinoCompiler(options);
 Node node = compiler.compile(name, code, null);
 return node;
 }

}