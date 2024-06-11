import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerUtil.createCompiler;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createVar;
import static com.google.javascript.rhino.NodeFactory.createScope;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createFunction;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Scope;

public class VarCheckTest {

  @SuppressWarnings("unchecked")
  private static Compiler createTestCompiler() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(JSError.Level.WARNINGS, "all");
    Compiler compiler = createCompiler(options);
    return compiler;
  }

  @SuppressWarnings("unchecked")
  private static Node createTestNode(String nodeType, String... children) {
    NodeFactory factory = new NodeFactory();
    Node parent = null;
    Node node = null;

    if (children != null && children.length > 0) {
      parent = createBlock();
      for (String childType : children) {
        Node child = createTestNode(childType, null);
        parent.addChild(parent.getLastChild(), child);
      }
      node = factory.createNode(nodeType, parent);
    } else {
      node = factory.createNode(nodeType, null);
    }

    return node;
  }

  @SuppressWarnings("unchecked")
  private static Scope createTestScope() {
    return createScope(new Scope.Binding[0]);
  }

  @SuppressWarnings("unchecked")
  private static CompilerInput createTestInput(String code) {
    NodeFactory factory = new NodeFactory();
    Node root = factory.createAstFromString(code, null, null, null);
    return new CompilerInput(null, null, null, root, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  @SuppressWarnings("unchecked")
  private static Node createSynthesizedExternsRoot() {
    NodeFactory factory = new NodeFactory();
    Node varNode = createName("varName");
    Node varNodeConstant = createName("CONST_varName");
    Node varNodeFunction = createFunction("functionName", null, null, Lists.newArrayList(varNode));
    Node varNodeLP = createLP();
    Node varNodeGetProp = createGetProp(varNodeLP, varNode);

    Node root = factory.createNode(
        Token.PROGRAM,
        createList(
          createList(
            factory.createNode(Token.BLOCK, createList(
              createList(
                factory.createNode(Token.VAR, varNodeConstant),
                factory.createNode(Token.ASSIGN, null, varNodeConstant, null)
              )
            )),
            factory.createNode(Token.RETURN, null, varNodeFunction, null)
          ),
          factory.createNode(Token.BLOCK, createList(varNodeGetProp))
        )
    );

    return root;
  }

  private static Node createLP() {
    return createName("(");
  }

  private static Node createGetProp(Node node, Node propertyName) {
    return createName("getProp", node, propertyName);
  }

  private static Node[] createList(Node... nodes) {
    return nodes;
  }

  @org.junit.Test
  public void testValidDeclaration() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("var validVar = 42;");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), input.getAstRoot(compiler));

    assertEquals(0, varCheck.varsToDeclareInExterns.size());
  }

  @org.junit.Test
  public void testDuplicateDeclaration() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("var validVar = 42;\nvar validVar = 24;");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), input.getAstRoot(compiler));

    assertEquals(0, varCheck.varsToDeclareInExterns.size());
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testUndefinedVariable() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("var undefinedVar;");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), input.getAstRoot(compiler));

    fail("Variable declaration should have failed.");
  }

  @org.junit.Test
  public void testUndefinedVariableDeclarationInSanityCheck() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("var undefinedVar;");

    VarCheck varCheck = new VarCheck(compiler, true);
    varCheck.process(createSynthesizedExternsInput(compiler), input.getAstRoot(compiler));
  }

  @org.junit.Test
  public void testUndefinedVariableDeclarationInExterns() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("//externs\nvar undefinedVar;");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), createSynthesizedExternsRoot());

    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("undefinedVar", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @org.junit.Test
  public void testUndefinedVariableDeclarationInExternsSanityCheck() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("//externs\nvar undefinedVar;");

    VarCheck varCheck = new VarCheck(compiler, true);
    varCheck.process(createSynthesizedExternsInput(compiler), createSynthesizedExternsRoot());

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @org.junit.Test
  public void testModuleDependency() {
    Compiler compiler = createTestCompiler();
    CompilerInput inputA = createTestInput("//moduleA.js\nvar modA = {\n  funcA: function() {\n    var a = 42;\n  }\n};");
    CompilerInput inputB = createTestInput("//moduleB.js\nvar modB = {\n  funcB: function() {\n    modA.funcA();\n  }\n};");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), inputA.getAstRoot(compiler));
    varCheck.process(createSynthesizedExternsInput(compiler), inputB.getAstRoot(compiler));

    assertEquals(0, varCheck.varsToDeclareInExterns.size());
  }

  @org.junit.Test
  public void testModuleDependencyNotDeclared() {
    Compiler compiler = createTestCompiler();
    CompilerInput inputA = createTestInput("//moduleA.js\nvar modA = {\n  funcA: function() {\n    var a = 42;\n  }\n};");
    CompilerInput inputB = createTestInput("//moduleB.js\nvar modB = {\n  funcB: function() {\n    modANotDeclared.funcA();\n  }\n};");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), inputA.getAstRoot(compiler));
    varCheck.process(createSynthesizedExternsInput(compiler), inputB.getAstRoot(compiler));

    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("modANotDeclared", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @org.junit.Test
  public void testModuleDependencyStrictCheck() {
    Compiler compiler = createTestCompiler();
    CompilerInput inputA = createTestInput("//moduleA.js\nvar modA = {\n  funcA: function() {\n    var a = 42;\n  }\n};");
    CompilerInput inputB = createTestInput("//moduleB.js\nvar modB = {\n  funcB: function() {\n    modANotDeclared.funcA();\n  }\n};");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.strictExternCheck = true;
    varCheck.process(createSynthesizedExternsInput(compiler), inputA.getAstRoot(compiler));
    varCheck.process(createSynthesizedExternsInput(compiler), inputB.getAstRoot(compiler));

    assertEquals(0, varCheck.varsToDeclareInExterns.size());
  }

  @org.junit.Test
  public void testModuleDependencyStrictCheckModuleBFirst() {
    Compiler compiler = createTestCompiler();
    CompilerInput inputA = createTestInput("//moduleA.js\nvar modA = {\n  funcA: function() {\n    var a = 42;\n  }\n};");
    CompilerInput inputB = createTestInput("//moduleB.js\nvar modB = {\n  funcB: function() {\n    modANotDeclared.funcA();\n  }\n};");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.strictExternCheck = true;
    varCheck.process(createSynthesizedExternsInput(compiler), inputB.getAstRoot(compiler));
    varCheck.process(createSynthesizedExternsInput(compiler), inputA.getAstRoot(compiler));

    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("modANotDeclared", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @org.junit.Test
  public void testMissingModuleDependencyWarning() {
    Compiler compiler = createTestCompiler();
    CompilerInput inputA = createTestInput("//moduleA.js\nvar modA = {\n  funcA: function() {\n    var a = 42;\n  }\n};");
    CompilerInput inputB = createTestInput("//moduleB.js\nvar modB = {\n  funcB: function() {\n    require('moduleA');\n    var a = 42;\n  }\n};");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), inputA.getAstRoot(compiler));
    varCheck.process(createSynthesizedExternsInput(compiler), inputB.getAstRoot(compiler));

    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("moduleA", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @org.junit.Test
  public void testMissingModuleDependencyError() {
    Compiler compiler = createTestCompiler();
    CompilerInput inputA = createTestInput("//moduleA.js\nvar modA = {\n  funcA: function() {\n    var a = 42;\n  }\n};");
    CompilerInput inputB = createTestInput("//moduleB.js\nvar modB = {\n  funcB: function() {\n    require('moduleA'); // missing dependency declaration\n    var a = 42;\n  }\n};");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), inputA.getAstRoot(compiler));
    varCheck.process(createSynthesizedExternsInput(compiler), inputB.getAstRoot(compiler));

    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("moduleA", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @org.junit.Test
  public void testNameReferenceInExterns() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("//externs\nvar mod = {\n  func: function() {\n    var a = require('undefinedVar');\n  }\n};");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), createSynthesizedExternsRoot());
    varCheck.process(input.getAstRoot(compiler));

    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("undefinedVar", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @org.junit.Test
  public void testNameReferenceInExternsWarning() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("//externs\nvar mod = {\n  func: function() {\n    var a = require('undefinedVar');\n  }\n};");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), createSynthesizedExternsRoot());
    varCheck.process(input.getAstRoot(compiler));

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
  }

  @org.junit.Test
  public void testConstantVariable() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("//externs\nconst CONST_validVar = 42;");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), createSynthesizedExternsRoot());
    varCheck.process(input.getAstRoot(compiler));

    assertEquals(0, varCheck.varsToDeclareInExterns.size());
  }

  @org.junit.Test
  public void testFunctionDeclarationWithEmptyName() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("//externs\nfunction() {}");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), createSynthesizedExternsRoot());
    varCheck.process(input.getAstRoot(compiler));

    assertEquals(0, varCheck.varsToDeclareInExterns.size());
  }

  @org.junit.Test
  public void testInvalidFunctionDeclaration() {
    Compiler compiler = createTestCompiler();
    CompilerInput input = createTestInput("//externs\nfunction() {}");

    VarCheck varCheck = new VarCheck(compiler, false);
    varCheck.process(createSynthesizedExternsInput(compiler), createSynthesizedExternsRoot());
    varCheck.process(input.getAstRoot(compiler));

    assertEquals(1, varCheck.varsToDeclareInExterns.size());
    assertEquals("function", varCheck.varsToDeclareInExterns.iterator().next());
  }

  private CompilerInput createSynthesizedExternsInput(Compiler compiler) {
    return compiler.newExternInput("{SyntheticVarsDeclar}");
  }
}