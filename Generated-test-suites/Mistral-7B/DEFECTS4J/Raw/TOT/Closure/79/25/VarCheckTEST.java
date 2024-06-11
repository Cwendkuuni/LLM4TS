import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TypeChecker;
import com.google.javascript.jscomp.TypeChecker.ErrorList;
import com.google.javascript.jscomp.TypeChecker.Type;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;

public class VarCheckTest {

  private Compiler compiler;
  private CompilerInput input;
  private CompilerPassManager passManager;
  private TypeChecker typeChecker;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    input = new CompilerInput(new SourceFile("test.js", ""), new Node());
    passManager = new CompilerPassManager(compiler);
    typeChecker = new TypeChecker(compiler.getOptions());
  }

  @Test
  public void testVarCheckPass() {
    VarCheck varCheck = new VarCheck(compiler);
    passManager.addPass(varCheck);
    ErrorList errors = new ErrorList();
    CompilerResults results = new CompilerResults();

    Node root = Node.newObject("window");
    Node body = Node.newBlock(null, Node.newEmptyStatementList());
    root.addChildToBack(body);

    Node variable = Node.newName("x");
    Node assignment = Node.newAssign(variable, Node.newNumber(1));
    body.addChildToBack(assignment);

    Node functionDeclaration = Node.newFunctionDeclaration("foo", null, Node.newEmptyStatementList());
    root.addChildToBack(functionDeclaration);

    input.setRootNode(root);
    passManager.run(input, errors, results);

    // Check that the variable was declared.
    Scope globalScope = typeChecker.getTopLevelScope();
    Scope.Var variableVar = globalScope.getVar("x");
    assertNotNull(variableVar);

    // Check that the function declaration was ignored.
    assertNull(globalScope.getVar("foo"));
  }

  @Test
  public void testUndefinedVariableError() {
    VarCheck varCheck = new VarCheck(compiler);
    passManager.addPass(varCheck);

    Node root = Node.newObject("window");
    Node body = Node.newBlock(null, Node.newEmptyStatementList());
    root.addChildToBack(body);

    Node variable = Node.newName("x");
    body.addChildToBack(variable);

    input.setRootNode(root);

    expectedException.expect(JSError.class);
    expectedException.expectMessage("JSC_UNDEFINED_VARIABLE: variable x is undefined");

    passManager.run(input, null, null);
  }

  @Test
  public void testStrictModuleDependencyError() {
    VarCheck varCheck = new VarCheck(compiler, true);
    passManager.addPass(varCheck);

    Node leftModule = Node.newObject("left");
    Node leftRoot = Node.newBlock(null, Node.newEmptyStatementList());
    leftModule.addChildToBack(leftRoot);

    Node rightModule = Node.newObject("right");
    Node rightRoot = Node.newBlock(null, Node.newEmptyStatementList());
    rightModule.addChildToBack(rightRoot);

    Node leftVariable = Node.newName("x");
    Node leftAssignment = Node.newAssign(leftVariable, Node.newNumber(1));
    leftRoot.addChildToBack(leftAssignment);

    Node rightVariable = Node.newName("x");
    Node rightAssignment = Node.newAssign(rightVariable, Node.newName("left.x"));
    rightRoot.addChildToBack(rightAssignment);

    Node root = Node.newBlock(null, Node.newEmptyStatementList());
    root.addChildToBack(leftModule);
    root.addChildToBack(rightModule);

    input.setRootNode(root);

    expectedException.expect(JSError.class);
    expectedException.expectMessage("JSC_VIOLATED_MODULE_DEPENDENCY: module left cannot reference right, defined in module right");

    passManager.run(input, null, null);
  }

  @Test
  public void testMissingModuleDependencyWarning() {
    VarCheck varCheck = new VarCheck(compiler);
    passManager.addPass(varCheck);

    Node leftModule = Node.newObject("left");
    Node leftRoot = Node.newBlock(null, Node.newEmptyStatementList());
    leftModule.addChildToBack(leftRoot);

    Node rightModule = Node.newObject("right");
    Node rightRoot = Node.newBlock(null, Node.newEmptyStatementList());
    rightRoot.addChildToBack(Node.newAssign(Node.newName("x"), Node.newName("left.y")));
    rightModule.addChildToBack(rightRoot);

    Node root = Node.newBlock(null, Node.newEmptyStatementList());
    root.addChildToBack(leftModule);
    root.addChildToBack(rightModule);

    input.setRootNode(root);

    passManager.run(input, null, null);

    assertFalse(varCheck.varsToDeclareInExterns.isEmpty());
    assertThat(varCheck.varsToDeclareInExterns.toString(),
               containsString("y"));
  }
}