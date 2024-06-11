import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.newFunction;
import static com.google.javascript.rhino.NodeFactory.newObject;
import static com.google.javascript.rhino.NodeFactory.newString;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

  private Compiler compiler;
  private CompilerPass devirtualizePrototypeMethods;

  @Before
  public void setUp() {
    compiler = new Compiler();
    devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
  }

  @Test
  public void testSimplePrototypeMethod() {
    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function(x) { return x; };\n" +
        "var foo = new Foo();\n" +
        "var result = foo.bar(10);\n";

    Node root = compiler.parse(sourceCode);
    devirtualizePrototypeMethods.process(null, root);

    Node expectedResultNode = newFunction(
        newString("JSCompiler_StaticMethods_bar"),
        newObject(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)),
        newObject(compiler.getTypeRegistry().getContextType()),
        newObject(compiler.getTypeRegistry().getContextType()),
        null,
        newNode("return this.bar.call(this, arguments[0]);")
    );

    Node resultNode = findNodeByValue(root, expectedResultNode.toString());
    assertEquals(expectedResultNode.toString(), resultNode.toString());
  }

  @Test(expected = AssertionError.class)
  public void testPrototypeMethodWithVarArgs() {
    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function(x, ...y) { return x + y.join(); };\n" +
        "var foo = new Foo();\n" +
        "var result = foo.bar(1, 2, 3, 4);\n";

    Node root = compiler.parse(sourceCode);
    devirtualizePrototypeMethods.process(null, root);

    Node expectedResultNode = newFunction(
        newString("JSCompiler_StaticMethods_bar"),
        newObject(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
            newFunctionType(newObject(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)),
                newArrayType(newObject(compiler.getTypeRegistry().getContextType()), 1),
                newObject(compiler.getTypeRegistry().getNativeContextType()))),
        null,
        null,
        null,
        newNode("return this.bar.call(this, arguments);")
    );

    Node resultNode = findNodeByValue(root, expectedResultNode.toString());
    assertEquals(expectedResultNode.toString(), resultNode.toString());
  }

  @Test(expected = AssertionError.class)
  public void testPrototypeMethodInControlStructure() {
    String sourceCode = "function Foo() {}\n" +
        "function test() {\n" +
        "  if (true) {\n" +
        "    Foo.prototype.bar = function(x) { return x; };\n" +
        "  }\n" +
        "  var foo = new Foo();\n" +
        "  var result = foo.bar(10);\n" +
        "}" +
        "test();\n";

    Node root = compiler.parse(sourceCode);
    devirtualizePrototypeMethods.process(null, root);

    // The test case should fail because the prototype method is defined inside an if statement.
  }

  @Test(expected = AssertionError.class)
  public void testPrototypeMethodInModuleLoadedAfterDefinition() {
    String sourceCodeModule1 = "function Foo() {}\n" +
        "Foo.prototype.bar = function(x) { return x; };\n";

    String sourceCodeModule2 = "var foo = new Foo();\n" +
        "function test() {\n" +
        "  var result = foo.bar(10);\n" +
        "}" +
        "test();\n";

    Node rootModule1 = compiler.parse(sourceCodeModule1);
    Node rootModule2 = compiler.parse(sourceCodeModule2);
    compiler.addTopLevelCode(rootModule1);
    compiler.addTopLevelCode(rootModule2);
    compiler.process();

    // The test case should fail because the module where the prototype method is used is loaded after the definition module.
  }

  @Test
  public void testPrototypeMethodWithSingleUse() {
    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function(x) { return x; };\n" +
        "function test() {\n" +
        "  var result = Foo.prototype.bar(10);\n" +
        "}" +
        "test();\n";

    Node root = compiler.parse(sourceCode);
    devirtualizePrototypeMethods.process(null, root);

    Node expectedResultNode = newFunction(
        newString("JSCompiler_StaticMethods_bar"),
        newObject(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
            newFunctionType(newObject(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)),
                newObject(compiler.getTypeRegistry().getContextType()),
                newObject(compiler.getTypeRegistry().getNativeContextType()))),
        null,
        null,
        null,
        newNode("return this.bar.call(this, arguments[0]);")
    );

    Node resultNode = findNodeByValue(root, expectedResultNode.toString());
    assertEquals(expectedResultNode.toString(), resultNode.toString());
  }

  @Test
  public void testPrototypeMethodWithMultipleUses() {
    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function(x) { return x; };\n" +
        "function test1() {\n" +
        "  var result1 = Foo.prototype.bar(10);\n" +
        "}" +
        "function test2() {\n" +
        "  var result2 = Foo.prototype.bar(20);\n" +
        "}" +
        "test1();\n" +
        "test2();\n";

    Node root = compiler.parse(sourceCode);
    devirtualizePrototypeMethods.process(null, root);

    Node expectedResultNode1 = newFunction(
        newString("JSCompiler_StaticMethods_bar"),
        newObject(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
            newFunctionType(newObject(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)),
                newObject(compiler.getTypeRegistry().getContextType()),
                newObject(compiler.getTypeRegistry().getNativeContextType()))),
        null,
        null,
        null,
        newNode("return this.bar.call(this, arguments[0]);")
    );

    Node expectedResultNode2 = newFunction(
        newString("JSCompiler_StaticMethods_bar"),
        newObject(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
            newFunctionType(newObject(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)),
                newObject(compiler.getTypeRegistry().getContextType()),
                newObject(compiler.getTypeRegistry().getNativeContextType()))),
        null,
        null,
        null,
        newNode("return this.bar.call(this, arguments[0]);")
    );

    Node resultNode1 = findNodeByValue(root, expectedResultNode1.toString());
    assertEquals(expectedResultNode1.toString(), resultNode1.toString());

    Node resultNode2 = findNodeByValue(root, expectedResultNode2.toString());
    assertEquals(expectedResultNode2.toString(), resultNode2.toString());
  }

  private Node findNodeByValue(Node node, String value) {
    if (node.toString().equals(value)) {
      return node;
    }

    for (Node child : node.children()) {
      Node result = findNodeByValue(child, value);
      if (result != null) {
        return result;
      }
    }

    return null;
  }
}