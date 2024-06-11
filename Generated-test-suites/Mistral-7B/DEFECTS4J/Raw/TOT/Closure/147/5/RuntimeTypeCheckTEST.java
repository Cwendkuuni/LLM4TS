import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() {
    String testCode = "function Foo() {}\n" +
        "function Bar(x) {\n" +
        "  checkType(x, ['number'])\n" +
        "  return x * 2;\n" +
        "}\n" +
        "function Baz() {\n" +
        "  this.foo = new Foo();\n" +
        "}\n" +
        "Baz.prototype.qux = function() {\n" +
        "  checkType(this, ['Baz'])\n" +
        "  checkType(this.foo, ['Foo'])\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = Normalize.parseAndNormalize(compiler, testCode, "test");
    compiler.process(root, null);
    runtimeTypeCheck.process(null, root);

    Node quxFunction = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);

    Node quxFunctionCall = new Node(Token.CALL, quxFunction, null);
    Node result = NodeUtil.run(compiler, quxFunctionCall);

    assertTrue("Expected number", result.getType().isNumber());

    Node fooInstance = new Node(Token.GETELEM, quxFunctionCall, 0);
    Node fooType = NodeUtil.getJSType(fooInstance);
    assertTrue("Expected Foo", fooType instanceof ObjectType &&
               ((ObjectType) fooType).getReferenceName().equals("Foo"));

    Node barFunction = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node barFunctionCall = new Node(Token.CALL, barFunction, new Node(Token.NUMBER, 5));
    result = NodeUtil.run(compiler, barFunctionCall);

    assertTrue("Expected number", result.getType().isNumber());
    assertTrue("Expected even number", result.hasProperty("value") &&
               result.getProperty("value").asInt() % 2 == 0);
  }

  @Test
  public void testRuntimeTypeCheckWithExternClasses() {
    String testCode = "function Foo() {}\n" +
        "function Bar(x) {\n" +
        "  checkType(x, ['Foo'])\n" +
        "  return x.foo;\n" +
        "}\n" +
        "/** @constructor */\n" +
        "function Baz() {\n" +
        "  this.foo = new Foo();\n" +
        "}\n" +
        "Baz.prototype.qux = function() {\n" +
        "  checkType(this, ['Baz'])\n" +
        "  checkType(this.foo, ['Foo'])\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = Normalize.parseAndNormalize(compiler, testCode, "test");
    compiler.process(root, null);
    runtimeTypeCheck.process(null, root);

    Node quxFunction = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);

    Node quxFunctionCall = new Node(Token.CALL, quxFunction, null);
    Node result = NodeUtil.run(compiler, quxFunctionCall);

    assertTrue("Expected object", result.getType().isObject());
    assertTrue("Expected Foo property", result.hasProperty("foo"));
    Node fooInstance = result.getProperty("foo");
    Node fooType = NodeUtil.getJSType(fooInstance);
    assertTrue("Expected Foo", fooType instanceof ObjectType &&
               ((ObjectType) fooType).getReferenceName().equals("Foo"));
  }

  @Test
  public void testRuntimeTypeCheckWithNullValues() {
    String testCode = "function Foo() {}\n" +
        "function Bar(x) {\n" +
        "  checkType(x, ['null', 'Foo'])\n" +
        "  return x;\n" +
        "}\n" +
        "Baz.prototype.qux = function() {\n" +
        "  checkType(this, ['Baz'])\n" +
        "  checkType(this.foo, ['null', 'Foo'])\n" +
        "}\n" +
        "function Baz() {\n" +
        "  this.foo = null;\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = Normalize.parseAndNormalize(compiler, testCode, "test");
    compiler.process(root, null);
    runtimeTypeCheck.process(null, root);

    Node quxFunction = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);

    Node quxFunctionCall = new Node(Token.CALL, quxFunction, null);
    Node result = NodeUtil.run(compiler, quxFunctionCall);

    assertTrue("Expected null", result.isNull());

    Node bazInstance = new Node(Token.NEW, new Node(Token.FUNCTION, null), null);
    Node quxFunctionCall2 = new Node(Token.CALL, quxFunction, bazInstance);
    result = NodeUtil.run(compiler, quxFunctionCall2);

    assertTrue("Expected null", result.isNull());
  }

  @Test
  public void testRuntimeTypeCheckWithInterfaceImplementation() {
    String testCode = "interface MyInterface {\n" +
        "  method1(): void;\n" +
        "}\n" +
        "function Foo() {}\n" +
        "Foo.prototype.method1 = function() {}\n" +
        "function Bar(x) {\n" +
        "  checkType(x, ['MyInterface'])\n" +
        "  x.method1();\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = Normalize.parseAndNormalize(compiler, testCode, "test");
    compiler.process(root, null);
    runtimeTypeCheck.process(null, root);

    Node barFunction = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);

    Node barFunctionCall = new Node(Token.CALL, barFunction, new Node(Token.NUMBER, 1));
    Node result = NodeUtil.run(compiler, barFunctionCall);

    // No runtime errors expected, so just check that the method is called.
    assertTrue("Expected method call", result.getFirstChild().getType() == Token.CALL);
  }

  @Test
  public void testRuntimeTypeCheckWithUnionTypes() {
    String testCode = "function Foo() {}\n" +
        "function Bar(x) {\n" +
        "  checkType(x, ['null', 'number', 'Foo'])\n" +
        "  return x;\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    Node root = Normalize.parseAndNormalize(compiler, testCode, "test");
    compiler.process(root, null);
    runtimeTypeCheck.process(null, root);

    Node barFunction = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);

    Node barFunctionCall1 = new Node(Token.CALL, barFunction, null);
    Node result1 = NodeUtil.run(compiler, barFunctionCall1);

    // Null is a valid value for this union type, so no runtime error is expected.

    Node barFunctionCall2 = new Node(Token.CALL, barFunction, new Node(Token.NUMBER, 5));
    Node result2 = NodeUtil.run(compiler, barFunctionCall2);

    assertTrue("Expected number", result2.getType().isNumber());
    assertTrue("Expected number value", result2.hasProperty("value"));
    assertEquals("Expected number value", 5, result2.getProperty("value").asInt());
  }

  private static void assertEquals(String msg, Object expected, Object actual) {
    if (!expected.equals(actual)) {
      throw new AssertionError(String.format("%s: expected %s, but was %s", msg, expected, actual));
    }
  }
}