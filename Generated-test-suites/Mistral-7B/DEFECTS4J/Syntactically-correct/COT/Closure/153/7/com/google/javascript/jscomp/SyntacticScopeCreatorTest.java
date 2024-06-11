import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;

@RunWith(JUnit4.class)
public class SyntacticScopeCreatorTest {

  private static AbstractCompiler compiler;
  private static SyntacticScopeCreator scopeCreator;

  @SuppressWarnings("unchecked")
  @BeforeClass
  public static void setUpOnce() {
    compiler = new AbstractCompiler() {
      @Override
      public JSType getNullType() {
        return JSType.NULL;
      }
    };

    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  /**
   * Test createScope method with null arguments
   */
  @Test(expected = NullPointerException.class)
  public void testCreateScopeNullParent() {
    scopeCreator.createScope(null, null);
  }

  /**
   * Test createScope method with null scope
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeNullScope() {
    scopeCreator.createScope(new Node(SOURCENAME_PROP, "test"), null);
  }

  /**
   * Test createScope method with valid arguments
   */
  @Test
  public void testCreateScope() {
    Scope result = scopeCreator.createScope(
        TestSourceFile.createNode("test", "function test() {}"), null);
    assertNotNull(result);
  }

  /**
   * Test scanRoot method with function node
   */
  @Test
  public void testScanRootFunction() {
    Node functionNode = TestSourceFile.createNode(
        "test", "function test() {}",
        new String[]{"arguments"});

    Scope scope = scopeCreator.createScope(functionNode, null);

    String expectedFunctionName = "test";
    Node functionNameNode = functionNode.getFirstChild();

    assertEquals(expectedFunctionName, scope.getVar(expectedFunctionName).getName());

    assertEquals(1, scope.getVars().size());
  }

  /**
   * Test scanRoot method with script node
   */
  @Test
  public void testScanRootScript() {
    Node scriptNode = TestSourceFile.createNode(
        "test", "var x = 1; function test() {}");

    Scope scope = scopeCreator.createScope(scriptNode, null);

    String expectedVariableName = "x";
    Node variableNode = scriptNode.getChildAt(1);

    assertEquals(expectedVariableName, scope.getVar(expectedVariableName).getName());

    assertEquals(2, scope.getVars().size());
  }

  /**
   * Test scanRoot method with control structures
   */
  @Test
  public void testScanRootControlStructures() {
    Node scriptNode = TestSourceFile.createNode(
        "test", "if (1) {} else if (1) {} else {}");

    Scope scope = scopeCreator.createScope(scriptNode, null);

    assertEquals(0, scope.getVars().size());
  }

  /**
   * Test scanVars method with var node
   */
  @Test
  public void testScanVarsVarNode() {
    Node varNode = TestSourceFile.createNode(
        "test", "var x = 1, y = 2, z = 3");

    Scope parentScope = scopeCreator.createScope(
        new Node(SCRIPT, "", null), null);

    Scope scope = scopeCreator.createScope(varNode, parentScope);

    String expectedVariableName1 = "x";
    String expectedVariableName2 = "y";
    String expectedVariableName3 = "z";

    Node variableNode1 = varNode.getChildAt(0);
    Node variableNode2 = varNode.getChildAt(2);

    assertEquals(expectedVariableName1, scope.getVar(expectedVariableName1).getName());
    assertEquals(expectedVariableName2, scope.getVar(expectedVariableName2).getName());
    assertEquals(expectedVariableName3, scope.getVar(expectedVariableName3).getName());

    assertEquals(3, scope.getVars().size());
  }

  /**
   * Test scanVars method with function node
   */
  @Test
  public void testScanVarsFunctionNode() {
    Node functionNode = TestSourceFile.createNode(
        "test", "function test() {var x = 1}");

    Scope parentScope = scopeCreator.createScope(
        new Node(SCRIPT, "", null), null);

    Scope scope = scopeCreator.createScope(functionNode, parentScope);

    String expectedVariableName = "x";

    Node variableNode = functionNode.getFirstChild().getNext();

    assertEquals(expectedVariableName, scope.getVar(expectedVariableName).getName());

    assertEquals(1, scope.getVars().size());
  }

  /**
   * Test scanVars method with catch node
   */
  @Test
  public void testScanVarsCatchNode() {
    Node functionNode = TestSourceFile.createNode(
        "test", "function test() {try {throw new Error('test')} catch (e) {}}");

    Scope parentScope = scopeCreator.createScope(
        new Node(SCRIPT, "", null), null);

    Scope scope = scopeCreator.createScope(functionNode, parentScope);

    String expectedVariableName = "e";

    Node variableNode = functionNode.getLastChild().getFirstChild();

    assertEquals(expectedVariableName, scope.getVar(expectedVariableName).getName());

    assertEquals(1, scope.getVars().size());
  }

  /**
   * Test onRedeclaration method without duplicates
   */
  @Test
  public void testOnRedeclarationWithoutDuplicates() {
    String sourceName = "test";
    String name = "test";

    Node parentNode = new Node(CATCH, "", null);
    Node nodeWithLineNumber = new Node(NAME, name, null);

    scopeCreator.new DefaultRedeclarationHandler().onRedeclaration(
        scopeCreator.new Scope(parentNode, null), name,
        nodeWithLineNumber, parentNode, null, nodeWithLineNumber);

    // Test for no error report
    assertEquals(0, compiler.getDiagnostics().length);
  }

  /**
   * Test onRedeclaration method with duplicate global variable
   */
  @Test
  public void testOnRedeclarationDuplicateGlobalVariable() {
    String sourceName = "test";
    String name = "test";

    Node parentNode = new Node(SCRIPT, "", null);
    Node nodeWithLineNumber = new Node(NAME, name, null);

    scopeCreator.new DefaultRedeclarationHandler().onRedeclaration(
        scopeCreator.new Scope(parentNode, null), name,
        nodeWithLineNumber, parentNode, null, nodeWithLineNumber);

    // Test for duplicate variable error report
    DiagnosticType errorType = VAR_MULTIPLY_DECLARED_ERROR;
    String[] expectedErrorMessage = {
      "Variable " + name + " first declared in " + sourceName
    };
    assertEquals(1, compiler.getDiagnostics().length);
    assertEquals(errorType, compiler.getDiagnostics()[0].getType());
    assertEquals(expectedErrorMessage, compiler.getDiagnostics()[0].getArgs());
  }

  /**
   * Test onRedeclaration method with duplicate local variable
   */
  @Test
  public void testOnRedeclarationDuplicateLocalVariable() {
    String sourceName = "test";
    String name = "test";

    Node parentNode = new Node(SCRIPT, "", null);
    Node functionNode = new Node(FUNCTION, "", null, null, null);
    Node nodeWithLineNumber = new Node(NAME, name, null);

    functionNode.setParent(parentNode);

    scopeCreator.new DefaultRedeclarationHandler().onRedeclaration(
        scopeCreator.new Scope(functionNode, null), name,
        nodeWithLineNumber, parentNode, null, nodeWithLineNumber);

    // Test for duplicate variable error report
    DiagnosticType errorType = VAR_MULTIPLY_DECLARED_ERROR;
    String[] expectedErrorMessage = {
      "Variable " + name + " first declared in " + sourceName
    };
    assertEquals(1, compiler.getDiagnostics().length);
    assertEquals(errorType, compiler.getDiagnostics()[0].getType());
    assertEquals(expectedErrorMessage, compiler.getDiagnostics()[0].getArgs());
  }

  /**
   * Test onRedeclaration method with duplicate arguments
   */
  @Test
  public void testOnRedeclarationDuplicateArguments() {
    String sourceName = "test";
    String name = "arguments";

    Node parentNode = new Node(FUNCTION, "", null, null, null);
    Node argumentsNode = new Node(LP, null, null);
    Node nodeWithLineNumber = new Node(NAME, name, null);

    parentNode.setFirstChild(new Node(FNNAME, "", argumentsNode));

    scopeCreator.new DefaultRedeclarationHandler().onRedeclaration(
        scopeCreator.new Scope(parentNode, null), name,
        nodeWithLineNumber, parentNode, null, nodeWithLineNumber);

    // Test for shadowing 'arguments' error report
    DiagnosticType errorType = VAR_ARGUMENTS_SHADOWED_ERROR;
    assertEquals(1, compiler.getDiagnostics().length);
    assertEquals(errorType, compiler.getDiagnostics()[0].getType());
  }
}