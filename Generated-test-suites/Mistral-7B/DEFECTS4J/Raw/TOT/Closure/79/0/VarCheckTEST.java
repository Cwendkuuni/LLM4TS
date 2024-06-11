import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Node;
import static com.google.javascript.jscomp.JSEmitter.getSourceFileNode;

public class VarCheckTest {

  @Test
  public void testValidFile() {
    String code = "function foo() {}";
    Compiler compiler = createCompiler();
    Node ast = Parser.parse(compiler, code, "Test.js", null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    new VarCheck(compiler).process(getSourceFileNode(compiler, "Test.js"), ast);
    assertEquals(0, compiler.getNumErrors());
  }

  @Test
  public void testUndefinedVariable() {
    String code = "function foo() { var x; }";
    Compiler compiler = createCompiler();
    Node ast = Parser.parse(compiler, code, "Test.js", null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    new VarCheck(compiler).process(getSourceFileNode(compiler, "Test.js"), ast);
    assertEquals(1, compiler.getNumErrors());
    assertEquals("Test.js:1:14: variable x is undefined",
        compiler.getError(0).getMessage());
  }

  @Test
  public void testUndefinedVariableInFunction() {
    String code = "function foo() { return x; }";
    Compiler compiler = createCompiler();
    Node ast = Parser.parse(compiler, code, "Test.js", null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    new VarCheck(compiler).process(getSourceFileNode(compiler, "Test.js"), ast);
    assertEquals(1, compiler.getNumErrors());
    assertEquals("Test.js:1:13: variable x is undefined",
        compiler.getError(0).getMessage());
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    String code = "function foo() { var x; var x; }";
    Compiler compiler = createCompiler();
    Node ast = Parser.parse(compiler, code, "Test.js", null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    new VarCheck(compiler).process(getSourceFileNode(compiler, "Test.js"), ast);
    assertEquals(1, compiler.getNumErrors());
    assertEquals("Test.js:1:15: duplicate declaration of variable x",
        compiler.getError(0).getMessage());
  }

  @Test
  public void testUndefinedVariableInGlobalScope() {
    String code = "var x; function foo() {}";
    Compiler compiler = createCompiler();
    Node ast = Parser.parse(compiler, code, "Test.js", null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    new VarCheck(compiler).process(getSourceFileNode(compiler, "Test.js"), ast);
    assertEquals(1, compiler.getNumErrors());
    assertEquals("Test.js:1:5: variable x is undefined",
        compiler.getError(0).getMessage());
  }

  @Test
  public void testVariableReferencedFromAnotherModule() {
    String code1 = "function foo() { export var x = 1; }";
    String code2 = "function bar() { import foo; import x; print(x); }";
    Compiler compiler = createCompiler();
    Node ast1 = Parser.parse(compiler, code1, "Foo.js", null);
    Node ast2 = Parser.parse(compiler, code2, "Bar.js", "Foo.js");
    NodeTraversal traversal = new NodeTraversal(compiler, ast2);
    new VarCheck(compiler).process(getSourceFileNode(compiler, "Bar.js"), ast2);
    assertEquals(1, compiler.getNumErrors());
    assertEquals("Bar.js:1:20: module Foo cannot reference x, " +
        "defined in module Foo, since Foo loads after Bar",
        compiler.getError(0).getMessage());
  }

  @Test
  public void testUndefinedExternVariable() {
    String code = "function foo() { return x; }";
    Compiler compiler = createCompiler();
    Node ast = Parser.parse(compiler, code, "Test.js", null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    new VarCheck(compiler, true).process(getSourceFileNode(compiler, "Test.js"), ast);
    assertEquals(1, compiler.getNumErrors());
    assertEquals("Test.js:1:13: variable x is not undefined in the externs.",
        compiler.getError(0).getMessage());
  }

  @Test
  public void testStrictModuleDependency() {
    String code1 = "export var x = 1;";
    String code2 = "import x; function foo() { print(x); }";
    Compiler compiler = createCompiler();
    Node ast1 = Parser.parse(compiler, code1, "Foo.js", null);
    Node ast2 = Parser.parse(compiler, code2, "Bar.js", "Foo.js");
    NodeTraversal traversal = new NodeTraversal(compiler, ast2);
    new VarCheck(compiler, true, true).process(getSourceFileNode(compiler, "Bar.js"), ast2);
    assertEquals(0, compiler.getNumErrors());
  }

  @Test
  public void testMissingModuleDependency() {
    String code1 = "export var x = 1;";
    String code2 = "import x; function foo() { print(x); }";
    Compiler compiler = createCompiler();
    Node ast1 = Parser.parse(compiler, code1, "Foo.js", null);
    Node ast2 = Parser.parse(compiler, code2, "Bar.js", null);
    NodeTraversal traversal = new NodeTraversal(compiler, ast2);
    new VarCheck(compiler, true, false).process(getSourceFileNode(compiler, "Bar.js"), ast2);
    assertEquals(1, compiler.getNumErrors());
    assertEquals("Bar.js:1:19: missing module dependency; " +
        "module Bar should depend on module Foo because it references x",
        compiler.getError(0).getMessage());
  }

  private Compiler createCompiler() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(JSError.make("", 0, 0, DiagnosticType.WARNING), CheckLevel.QUIRKS);
    return new Compiler(options);
  }

  private Node getSourceFileNode(Compiler compiler, String fileName) {
    return getSourceFileNode(compiler, fileName, null);
  }

  private Node getSourceFileNode(Compiler compiler, String fileName, Node parent) {
    return getSourceFileNode(compiler, fileName, null, parent);
  }

  private Node getSourceFileNode(Compiler compiler, String fileName, Node ast, Node parent) {
    CompilerInput input = compiler.getInputFactory().makeInput(fileName, null, null, null, null);
    return getSourceFileNode(compiler, input, ast, parent);
  }

  private Node getSourceFileNode(Compiler compiler, CompilerInput input, Node ast, Node parent) {
    return getSourceFileNode(compiler, input, ast, parent, null);
  }

  private Node getSourceFileNode(Compiler compiler, CompilerInput input, Node ast, Node parent, Set<JSModule> visitedModules) {
    if (ast != null) {
      return ast;
    }

    if (visitedModules != null && visitedModules.contains(input.getModule())) {
      // The input file has already been processed, so just return an empty node.
      return Node.NULL();
    }

    if (visitedModules == null) {
      visitedModules = Sets.newHashSet();
    }

    visitedModules.add(input.getModule());

    // Create a new script node for the input file.
    Node scriptNode = Node.newBlock("SourceFile", compiler.newSourceFile(input.getName()));
    scriptNode.putBooleanProp(Node.IS_DECLARE, true);

    if (input.getAstRoot(compiler) != null) {
      scriptNode.addChildToBack(input.getAstRoot(compiler));
    }

    if (parent != null) {
      parent.addChildToBack(scriptNode);
    }

    return scriptNode;
  }
}