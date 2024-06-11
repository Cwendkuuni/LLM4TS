import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionSite;
import com.google.javascript.jscomp.SimpleUseSite;
import com.google.javascript.rhino.NodeUtil;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    Compiler compiler = new Compiler();
    CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);

    String input = "function A() {}" +
        "A.prototype.accumulate = function(value) { return this.total += value; }" +
        "var a = new A();" +
        "var total = a.accumulate(2);";

    Node externs = Node.parse(input);
    Node root = Node.parse("(function() {})()");
    devirtualizePrototypeMethods.process(externs, root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<SimpleDefinitionSite> definitionSites = defFinder.getDefinitionSites();
    SimpleDefinitionSite definitionSite = definitionSites.get(0);

    assertEquals("JSCompiler_StaticMethods_accumulate",
        definitionSite.definition.getRValue().getString());

    SimpleUseSite useSite = definitionSite.getUseSiteAtIndex(0);
    Node useNode = useSite.node;

    assertEquals("JSCompiler_StaticMethods_accumulate",
        useNode.getLastChild().getString());
    assertEquals("a", useNode.getFirstChild().getString());
    assertEquals(2, useNode.getChildCount() - 2);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMultipleArguments() {
    Compiler compiler = new Compiler();
    CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);

    String input = "function A() {}" +
        "A.prototype.multiply = function(x, y) { return this.total = x * y; }" +
        "var a = new A();" +
        "var result = a.multiply(3, 4);";

    Node externs = Node.parse(input);
    Node root = Node.parse("(function() {})()");
    devirtualizePrototypeMethods.process(externs, root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<SimpleDefinitionSite> definitionSites = defFinder.getDefinitionSites();
    SimpleDefinitionSite definitionSite = definitionSites.get(0);

    assertEquals("JSCompiler_StaticMethods_multiply",
        definitionSite.definition.getRValue().getString());

    SimpleUseSite useSite = definitionSite.getUseSiteAtIndex(0);
    Node useNode = useSite.node;

    assertEquals("JSCompiler_StaticMethods_multiply",
        useNode.getLastChild().getString());
    assertEquals("a", useNode.getFirstChild().getString());
    assertEquals(3, useNode.getChild(1).getChildCount());
    assertEquals(4, useNode.getChild(1).getChild(1).getChildCount());
  }

  @Test(expected = AssertionError.class)
  public void testDevirtualizePrototypeMethodsWithVarArgsFunction() {
    Compiler compiler = new Compiler();
    CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);

    String input = "function A() {}" +
        "A.prototype.sum = function() { var args = arguments; return this.total = args[0] + Array.prototype.slice.call(args, 1).reduce((a, b) => a + b, 0); }" +
        "var a = new A();" +
        "var result = a.sum(1, 2, 3);";

    Node externs = Node.parse(input);
    Node root = Node.parse("(function() {})()");
    devirtualizePrototypeMethods.process(externs, root);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    List<SimpleDefinitionSite> definitionSites = defFinder.getDefinitionSites();
    SimpleDefinitionSite definitionSite = definitionSites.get(0);

    // This should throw an exception due to the varArgsFunction
    definitionSite.getUseSiteAtIndex(0).invoke();
  }
}