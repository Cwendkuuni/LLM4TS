import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.NodeType.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private ScopeCreator creator;
  private Scope globalScope;
  private Scope functionScope;
  private Scope blockScope;

  @Before
  public void setUp() {
    compiler = new Compiler();
    creator = new SyntacticScopeCreator(compiler);
    globalScope = creator.createScope(new Node("source1"), null);
    functionScope = creator.createScope(new Node("function foo() {}"), globalScope);
    blockScope = creator.createScope(new Node("{ x = 1; }"), functionScope);
  }

  @Test
  public void testGlobalScope() {
    Scope expected = new Scope(new Node("source1"), compiler);
    assertTrue(expected.equals(globalScope));

    assertEquals(0, expected.size());

    Var x = expected.getVar("x");
    assertNull(x);

    Var argumentsVar = expected.getVar("arguments");
    assertNull(argumentsVar);
  }

  @Test
  public void testFunctionScope() {
    final String functionName = "foo";

    String expectedSourceName = "source1";

    Node functionNode = new Node("function " + functionName + "() {}", TOKEN_FUNCTION);
    functionNode.setFirstChild(new Node(functionName, TOKEN_NAME));

    Scope expected = new Scope(functionNode, globalScope);

    Preconditions.checkState(expected.getParent() == globalScope);
    assertEquals(expected.size(), 1);

    Var functionVar = expected.getVar(functionName);
    assertEquals(functionNode, functionVar.getNode());
    assertEquals(JSType.FUNCTION, functionVar.getType());

    Var argumentsVar = expected.getVar("arguments");
    assertNotNull(argumentsVar);
    assertEquals(functionNode, argumentsVar.getNode());
    assertEquals(JSType.UNDEFINED, argumentsVar.getType());
  }

  @Test
  public void testBlockScope() {
    String expectedSourceName = "source1";

    Node functionNode = new Node("function foo() {}", TOKEN_FUNCTION);
    functionNode.setFirstChild(new Node(functionName, TOKEN_NAME));

    Node blockNode = new Node("{ x = 1; }", TOKEN_SCRIPT);
    blockNode.setProp(Node.SOURCENAME_PROP, expectedSourceName);

    Scope expected = new Scope(blockNode, functionScope);

    Preconditions.checkState(expected.getParent() == functionScope);
    assertEquals(expected.size(), 1);

    Var xVar = expected.getVar("x");
    assertEquals(blockNode.getFirstChild(), xVar.getNode());
    assertEquals(JSType.NUMBER, xVar.getType());
    assertEquals("1", xVar.getInput().getName());

    assertNull(expected.getVar("x2"));
    assertNull(expected.getVar("function2"));
  }

  @Test
  public void testMultipleVariablesInVarStatement() {
    String expectedSourceName = "source1";

    Node varNode = new Node("var x = 1, y = 2, z = 3", TOKEN_VAR);
    varNode.setProp(Node.SOURCENAME_PROP, expectedSourceName);

    Scope scope = creator.createScope(varNode, null);

    assertEquals(scope.size(), 3);

    Var xVar = scope.getVar("x");
    assertEquals(JSType.NUMBER, xVar.getType());
    assertEquals("1", xVar.getInput().getName());

    Var yVar = scope.getVar("y");
    assertEquals(JSType.NUMBER, yVar.getType());
    assertEquals("2", yVar.getInput().getName());

    Var zVar = scope.getVar("z");
    assertEquals(JSType.NUMBER, zVar.getType());
    assertEquals("3", zVar.getInput().getName());
  }

  @Test(expected = AssertionError.class)
  public void testVariableDeclarationDuplicate() {
    String expectedSourceName = "source1";

    Node functionNode = new Node("function foo() {}", TOKEN_FUNCTION);
    functionNode.setFirstChild(new Node(functionName, TOKEN_NAME));

    Node blockNode = new Node("{ var x = 1; var x = 2; }", TOKEN_SCRIPT);
    blockNode.setProp(Node.SOURCENAME_PROP, expectedSourceName);

    Scope scope = creator.createScope(blockNode, functionScope);

    // This will fail with a AssertionError since variable x is declared twice
    scope.getVar("x");
  }

  @Test(expected = AssertionError.class)
  public void testVariableDeclarationDuplicateInFunction() {
    String expectedSourceName = "source1";

    Node functionNode = new Node("function foo() {}", TOKEN_FUNCTION);
    functionNode.setFirstChild(new Node(functionName, TOKEN_NAME));

    Node blockNode = new Node("{ function bar() { var x = 1; } var x = 2; }", TOKEN_SCRIPT);
    blockNode.setProp(Node.SOURCENAME_PROP, expectedSourceName);

    Scope scope = creator.createScope(blockNode, functionScope);

    // This will fail with a AssertionError since variable x is declared twice
    scope.getVar("x");
  }

  @Test
  public void testVariableDeclarationDuplicateWithSuppressed() {
    String expectedSourceName = "source1";

    Node functionNode = new Node("function foo() {}", TOKEN_FUNCTION);
    functionNode.setFirstChild(new Node(functionName, TOKEN_NAME));

    Node blockNode = new Node("{ @suppress({duplicate: true}) var x = 1; var x = 2; }", TOKEN_SCRIPT);
    blockNode.setProp(Node.SOURCENAME_PROP, expectedSourceName);

    TestSourceFile testSourceFile = new TestSourceFile();
    testSourceFile.init(compiler, expectedSourceName);

    try {
      compiler.compile(blockNode, testSourceFile);
      fail("Expected error!");
    } catch (CompilationFailedException e) {
      // Expected
    }

    Node errors = e.getErrors();
    assertEquals(1, errors.getChildCount());
    Node errorNode = errors.getFirstChild();

    assertEquals(DiagnosticType.error, errorNode.getType());
    assertEquals("Variable x first declared in source1", errorNode.getString());
  }

  @Test
  public void testArgumentsVariable() {
    String expectedSourceName = "source1";

    Node functionNode = new Node("function foo(x) {}", TOKEN_FUNCTION);
    functionNode.setFirstChild(new Node(functionName, TOKEN_NAME));

    Node argsNode = new Node("(", TOKEN_LP);
    Node argNameNode = new Node("x", TOKEN_NAME);
    Node argCommaNode = new Node(",", TOKEN_COMMA);
    Node argNode = new Node(")", TOKEN_RP);

    Node bodyNode = new Node("{ x = 1; }", TOKEN_SCRIPT);
    bodyNode.setProp(Node.SOURCENAME_PROP, expectedSourceName);

    Scope scope = creator.createScope(bodyNode, functionNode);

    Var argumentsVar = scope.getVar("arguments");
    assertNotNull(argumentsVar);
    assertEquals(functionNode, argumentsVar.getNode());
    assertEquals(JSType.FUNCTION, argumentsVar.getParentScope().getType());
    assertEquals(JSType.OBJECT, argumentsVar.getType());
  }

  @Test(expected = AssertionError.class)
  public void testArgumentsVariableRedeclaration() {
    String expectedSourceName = "source1";

    Node functionNode = new Node("function foo(x) {}", TOKEN_FUNCTION);
    functionNode.setFirstChild(new Node(functionName, TOKEN_NAME));

    Node argsNode = new Node("(", TOKEN_LP);
    Node argNameNode = new Node("x", TOKEN_NAME);
    Node argCommaNode = new Node(",", TOKEN_COMMA);
    Node argNode = new Node(")", TOKEN_RP);

    Node bodyNode = new Node("{ arguments = {}; arguments.x = 1; }", TOKEN_SCRIPT);
    bodyNode.setProp(Node.SOURCENAME_PROP, expectedSourceName);

    Scope scope = creator.createScope(bodyNode, functionNode);

    // This will fail with a AssertionError since arguments variable is redeclared
    scope.getVar("arguments");
    scope.getVar("arguments");
  }

  @Test
  public void testCatchBlockVariable() {
    String expectedSourceName = "source1";

    Node functionNode = new Node("function foo() {}", TOKEN_FUNCTION);
    functionNode.setFirstChild(new Node(functionName, TOKEN_NAME));

    Node tryNode = new Node("try {}", TOKEN_TRY);
    Node catchNode = new Node("catch(e) {}", TOKEN_CATCH);
    Node varNode = new Node("var x = 1", TOKEN_VAR);

    Node blockNode = new Node("{ x = 2; }", TOKEN_SCRIPT);
    blockNode.setProp(Node.SOURCENAME_PROP, expectedSourceName);

    Node expected = new Node("catch(e)", TOKEN_CATCH);
    expected.setFirstChild(new Node("x", TOKEN_NAME));
    expected.setNext(blockNode);

    Scope scope = creator.createScope(tryNode, functionNode);

    Scope catchScope = scope.getScope(expected);

    assertEquals(catchScope.size(), 1);
    Var xVar = catchScope.getVar("x");
    assertEquals("2", xVar.getInput().getName());
  }
}