import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DevirtualizePrototypeMethodsTest {

  Compiler compiler;
  NodeFactory nodeFactory;
  CompilerOptions options;
  SimpleDefinitionFinder definitionFinder;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = new NodeFactory();
    options = new CompilerOptions();
    definitionFinder = new SimpleDefinitionFinder(compiler);
  }

  @Test
  public void testDevirtualizePrototypeMethods() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function(a, b) { return a + b; }";

    Node root = nodeFactory.parse(inputCode, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput =
        "var JSCompiler_StaticMethods_bar = function(self, a, b) { return a + b; }";
    String outputCode = compiler.getSourceFileName() + ":\n" +
        rewrittenCode.toString();

    assertThat(outputCode).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMultipleCallSites() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function(a, b) { return a + b; }";
    String inputCode2 =
        "function Baz() {}\n" +
        "Baz.prototype.qux = function() { return new Foo().bar(1, 2); }";

    Node root = nodeFactory.parse(inputCode + inputCode2, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput =
        "var JSCompiler_StaticMethods_bar = function(self, a, b) { return a + b; }";
    String outputCode = compiler.getSourceFileName() + ":\n" +
        rewrittenCode.toString();

    String expectedOutput2 =
        "var JSCompiler_StaticMethods_qux = function(self) { return JSCompiler_StaticMethods_bar(new Foo(), 1, 2); }";
    String outputCode2 = compiler.getSourceFileName2() + ":\n" +
        rewrittenCode.toString();

    assertThat(outputCode).isEqualTo(expectedOutput);
    assertThat(outputCode2).isEqualTo(expectedOutput2);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMultipleDefinitions() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function(a, b) { return a + b; }";
    String inputCode2 =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return 42; }";

    Node root = nodeFactory.parse(inputCode + inputCode2, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 2);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    List<DefinitionSite> definitionSitesAfter = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSitesAfter.size() == 1);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput = "Error: Ambiguous definition for 'bar'";
    String outputCode = compiler.getErrorMessage();

    assertThat(outputCode).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithExportedMethod() {
    String inputCode =
        "function Foo() {}\n" +
        "exports.bar = Foo.prototype.bar = function(a, b) { return a + b; }";

    Node root = nodeFactory.parse(inputCode, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput = "Error: Exported method 'bar' cannot be devirtualized";
    String outputCode = compiler.getErrorMessage();

    assertThat(outputCode).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithVarArgsFunction() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function(a) { for (var i = 0; i < a.length; i++) this.baz(a[i]); }";

    Node root = nodeFactory.parse(inputCode, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput = "Error: Functions with varargs cannot be devirtualized";
    String outputCode = compiler.getErrorMessage();

    assertThat(outputCode).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithFunctionCallContext() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return this.baz(); }";

    Node root = nodeFactory.parse(inputCode, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput = "Error: Property 'baz' cannot be accessed outside function call context";
    String outputCode = compiler.getErrorMessage();

    assertThat(outputCode).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithStaticMethodCall() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return Foo.baz(); }";

    Node root = nodeFactory.parse(inputCode, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput = "Error: Static method call 'Foo.baz' cannot be devirtualized";
    String outputCode = compiler.getErrorMessage();

    assertThat(outputCode).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithFunctionInModuleLoadedBefore() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return this.baz(); }";
    String inputCode2 =
        "function Foo2() {}\n" +
        "Foo2.prototype.qux = function() { return new Foo().bar(); }";

    Node root = nodeFactory.parse(inputCode + inputCode2, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput = "Error: Module 'Foo2' loaded before definition of 'bar'";
    String outputCode = compiler.getErrorMessage();

    assertThat(outputCode).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithPropertyAccessBeforeDefinition() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return this.baz(); }";
    String inputCode2 =
        "function Bar() {}\n" +
        "Bar.prototype.qux = function() { return new Foo().bar; }";

    Node root = nodeFactory.parse(inputCode + inputCode2, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput = "Error: Property access before definition of 'bar'";
    String outputCode = compiler.getErrorMessage();

    assertThat(outputCode).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMethodCallAfterDefinition() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return this.baz(); }";
    String inputCode2 =
        "function Bar() {}\n" +
        "Bar.prototype.qux = function() { new Foo().bar(); }";

    Node root = nodeFactory.parse(inputCode + inputCode2, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput = "";
    String outputCode = compiler.getSourceFileName() + ":\n" +
        rewrittenCode.toString();

    String expectedOutput2 =
        "var JSCompiler_StaticMethods_qux = function(self) { JSCompiler_StaticMethods_bar(new Foo(), self); }";
    String outputCode2 = compiler.getSourceFileName2() + ":\n" +
        rewrittenCode.toString();

    assertThat(outputCode).isEqualTo(expectedOutput);
    assertThat(outputCode2).isEqualTo(expectedOutput2);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithFunctionAsParameter() {
    String inputCode =
        "function Foo() {}\n" +
        "Foo.prototype.bar = function(func) { func(); }";

    Node root = nodeFactory.parse(inputCode, null, null);
    compiler.compile(root, options);

    List<DefinitionSite> definitionSites = definitionFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 1);
    DefinitionSite definitionSite = definitionSites.get(0);

    DevirtualizePrototypeMethods devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);
    devirtualizePrototypeMethods.process(null, root);

    Node rewrittenCode = compiler.getCompiledCode();
    String expectedOutput = "Error: Cannot devirtualize method 'bar' with a function parameter";
    String outputCode = compiler.getErrorMessage();

    assertThat(outputCode).isEqualTo(expectedOutput);
  }
}