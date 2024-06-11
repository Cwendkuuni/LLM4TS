import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp. Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for the RenamePrototypes compiler pass.
 */
public class RenamePrototypesTest {

  public static void main(String[] args) {
    testRenamePrototypes();
  }

  private static void testRenamePrototypes() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "Foo.prototype.baz = 42;\n" +
                   "Foo.prototype.qux = {\n" +
                   "  get quux() { return 0; },\n" +
                   "  set quux(x) {}\n" +
                   "};\n" +
                   "\n" +
                   "var obj = {\n" +
                   "  bar: function() {},\n" +
                   "  baz: 43,\n" +
                   "  qux: 44\n" +
                   "};\n" +
                   "\n" +
                   "var FooInstance = new Foo();\n" +
                   "FooInstance.bar();\n" +
                   "FooInstance.baz = 45;\n" +
                   "delete FooInstance.qux;\n" +
                   "\n" +
                   "var FooPrototype = Object.getPrototypeOf(FooInstance);\n" +
                   "FooPrototype.bar();\n" +
                   "FooPrototype.baz = 46;\n" +
                   "delete FooPrototype.qux;\n";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);

    CompilerPass renamePass = new RenamePrototypes(compiler, true, null, null);
    CompilerInput inputNode = new CompilerInput(Token.NEW_LINE, input, "test.js", 0);
    CompilerResult result = compiler.process(Arrays.asList(inputNode), options, null);
    Node output = result.getOutput();

    // Check that no properties were renamed
    Map<String, String> expectedMap = Maps.newHashMap();
    expectedMap.put("Foo", "Foo");
    expectedMap.put("prototype", "prototype");
    expectedMap.put("bar", "bar");
    expectedMap.put("baz", "baz");
    expectedMap.put("qux", "qux");
    expectedMap.put("quux", "quux");
    expectedMap.put("get quux", "get quux");
    expectedMap.put("set quux", "set quux");
    expectedMap.put("obj", "obj");
    expectedMap.put("bar", "bar");
    expectedMap.put("baz", "baz");
    expectedMap.put("qux", "qux");

    Map<String, String> actualMap = renamePass.getPropertyMap();
    assertEquals(expectedMap, actualMap);

    // Check that the output is the same as the input
    List<Node> outputNodes = NodeTraversal.traverse(compiler, output, new LogNodes());
    assertEquals(1, outputNodes.size());
    assertEquals(input, outputNodes.get(0).getPrintable());
  }

  /**
   * Logs each node encountered during the traversal to System.out.
   */
  private static class LogNodes extends NodeTraversal.AbstractPreOrderCallback {
    @Override public void visit(NodeTraversal traverser, Node node, Node parent) {
      System.out.println(node.getPrintable());
    }
  }
}