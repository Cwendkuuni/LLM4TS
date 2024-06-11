import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    // Typical use case
    // The code contains a side-effect free statement that should generate a warning
    CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = createExterns();
    Node root = createRoot();
    check.process(externs, root);
    // Assert that the warning message is generated for the side-effect free statement
    assertEquals("This code lacks side-effects. Is there a bug?", compiler.getWarnings()[0].getMessage());
    // Assert that the side-effect free statement is protected by the extern function call
    assertTrue(root.getLastChild().getFirstChild().isCall());

    // Edge case
    // The code contains a side-effect free statement that is already protected
    CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = createExternsWithProtection();
    Node root = createRootWithProtectedStatement();
    check.process(externs, root);
    // Assert that no warning message is generated
    assertEquals(0, compiler.getWarnings().length);

    // Error scenario
    // The code contains a statement with a missing operator that should generate a warning
    CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node externs = createExterns();
    Node root = createRootWithMissingOperator();
    check.process(externs, root);
    // Assert that the warning message is generated for the statement with a missing operator
    assertEquals("Is there a missing '+' on the previous line?", compiler.getWarnings()[0].getMessage());
  }

  @Test
  public void testHotSwapScript() {
    // Typical use case
    // The code contains a side-effect free statement that should generate a warning
    CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node scriptRoot = createScriptRoot();
    Node originalRoot = createOriginalRoot();
    check.hotSwapScript(scriptRoot, originalRoot);
    // Assert that the warning message is generated for the side-effect free statement
    assertEquals("This code lacks side-effects. Is there a bug?", compiler.getWarnings()[0].getMessage());
    // Assert that the side-effect free statement is protected by the extern function call
    assertTrue(scriptRoot.getLastChild().getFirstChild().isCall());

    // Edge case
    // The code contains a side-effect free statement that is already protected
    CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node scriptRoot = createScriptRootWithProtection();
    Node originalRoot = createOriginalRootWithProtectedStatement();
    check.hotSwapScript(scriptRoot, originalRoot);
    // Assert that no warning message is generated
    assertEquals(0, compiler.getWarnings().length);

    // Error scenario
    // The code contains a statement with a missing operator that should generate a warning
    CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node scriptRoot = createScriptRoot();
    Node originalRoot = createOriginalRootWithMissingOperator();
    check.hotSwapScript(scriptRoot, originalRoot);
    // Assert that the warning message is generated for the statement with a missing operator
    assertEquals("Is there a missing '+' on the previous line?", compiler.getWarnings()[0].getMessage());
  }

  @Test
  public void testVisit() {
    // Typical use case
    // The code contains a statement with no side effects that should generate a warning
    CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal traversal = createTraversalWithSideEffectFreeStatement();
    Node statement = createSideEffectFreeStatement();
    check.visit(traversal, statement, statement.getParent());
    // Assert that the warning message is generated for the side-effect free statement
    assertEquals("This code lacks side-effects. Is there a bug?", compiler.getWarnings()[0].getMessage());
    // Assert that the side-effect free statement is protected by the extern function call
    assertTrue(statement.getParent().getLastChild().isCall());

    // Edge case
    // The code contains a side-effect free statement that is already protected
    CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal traversal = createTraversalWithSideEffectFreeStatement();
    Node statement = createProtectedSideEffectFreeStatement();
    check.visit(traversal, statement, statement.getParent());
    // Assert that no warning message is generated
    assertEquals(0, compiler.getWarnings().length);

    // Error scenario
    // The code contains a statement with a missing operator that should generate a warning
    CheckSideEffects check = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal traversal = createTraversalWithMissingOperatorStatement();
    Node statement = createMissingOperatorStatement();
    check.visit(traversal, statement, statement.getParent());
    // Assert that the warning message is generated for the statement with a missing operator
    assertEquals("Is there a missing '+' on the previous line?", compiler.getWarnings()[0].getMessage());
  }

  private Node createExterns() {
    // Create a mock externs node
    return new Node(Token.EXTERN_FILE);
  }

  private Node createRoot() {
    // Create a mock root node
    return new Node(Token.ROOT);
  }

  private Node createRootWithProtectedStatement() {
    // Create a mock root node with a protected side-effect free statement
    Node root = new Node(Token.ROOT);
    Node statement = createSideEffectFreeStatement();
    Node name = new Node(Token.NAME, "JSCOMPILER_PRESERVE");
    Node call = new Node(Token.CALL, name);
    name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    call.putBooleanProp(Node.FREE_CALL, true);
    call.addChildToBack(statement);
    root.addChildToBack(call);
    return root;
  }

  private Node createRootWithMissingOperator() {
    // Create a mock root node with a statement that is missing an operator
    Node root = new Node(Token.ROOT);
    Node statement = createMissingOperatorStatement();
    root.addChildToBack(statement);
    return root;
  }

  private Node createScriptRoot() {
    // Create a mock script root node
    return new Node(Token.SCRIPT);
  }

  private Node createOriginalRoot() {
    // Create a mock original root node
    return new Node(Token.SCRIPT);
  }

  private Node createScriptRootWithProtection() {
    // Create a mock script root node with a protected side-effect free statement
    Node scriptRoot = new Node(Token.SCRIPT);
    Node statement = createSideEffectFreeStatement();
    Node name = new Node(Token.NAME, "JSCOMPILER_PRESERVE");
    Node call = new Node(Token.CALL, name);
    name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    call.putBooleanProp(Node.FREE_CALL, true);
    call.addChildToBack(statement);
    scriptRoot.addChildToBack(call);
    return scriptRoot;
  }

  private Node createOriginalRootWithProtectedStatement() {
    // Create a mock original root node with a protected side-effect free statement
    Node originalRoot = new Node(Token.SCRIPT);
    Node statement = createSideEffectFreeStatement();
    Node name = new Node(Token.NAME, "JSCOMPILER_PRESERVE");
    Node call = new Node(Token.CALL, name);
    name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    call.putBooleanProp(Node.FREE_CALL, true);
    call.addChildToBack(statement);
    originalRoot.addChildToBack(call);
    return originalRoot;
  }

  private Node createScriptRootWithMissingOperator() {
    // Create a mock script root node with a statement that is missing an operator
    Node scriptRoot = new Node(Token.SCRIPT);
    Node statement = createMissingOperatorStatement();
    scriptRoot.addChildToBack(statement);
    return scriptRoot;
  }

  private Node createOriginalRootWithMissingOperator() {
    // Create a mock original root node with a statement that is missing an operator
    Node originalRoot = new Node(Token.SCRIPT);
    Node statement = createMissingOperatorStatement();
    originalRoot.addChildToBack(statement);
    return originalRoot;
  }

  private Node createSideEffectFreeStatement() {
    // Create a mock side-effect free statement
    Node statement = new Node(Token.VAR);
    Node name1 = new Node(Token.NAME, "s");
    Node value1 = new Node(Token.STRING, "this string is ");
    Node name2 = new Node(Token.NAME, "x");
    Node value2 = new Node(Token.NUMBER, "42");
    statement.addChildToBack(name1);
    statement.addChildToBack(value1);
    statement.addChildToBack(name2);
    statement.addChildToBack(value2);
    return statement;
  }

  private Node createProtectedSideEffectFreeStatement() {
    // Create a mock protected side-effect free statement
    Node statement = new Node(Token.VAR);
    Node name1 = new Node(Token.NAME, "s");
    Node value1 = new Node(Token.STRING, "this string is ");
    Node name2 = new Node(Token.NAME, "x");
    Node value2 = new Node(Token.NUMBER, "42");
    statement.addChildToBack(name1);
    statement.addChildToBack(value1);
    statement.addChildToBack(name2);
    statement.addChildToBack(value2);
    Node name = new Node(Token.NAME, "JSCOMPILER_PRESERVE");
    Node call = new Node(Token.CALL, name);
    name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    call.putBooleanProp(Node.FREE_CALL, true);
    statement.getParent().addChildBefore(call, statement);
    call.addChildToBack(statement);
    return statement;
  }

  private Node createMissingOperatorStatement() {
    // Create a mock statement with a missing operator
    Node statement = new Node(Token.EXPR_RESULT);
    Node left = new Node(Token.NAME, "x");
    Node right = new Node(Token.NAME, "y");
    statement.addChildToBack(left);
    statement.addChildToBack(right);
    return statement;
  }

  private NodeTraversal createTraversalWithSideEffectFreeStatement() {
    // Create a mock traversal with a side-effect free statement
    Node externs = createExterns();
    Node root = createRoot();
    return new NodeTraversal(compiler, check, externs, root);
  }

  private NodeTraversal createTraversalWithMissingOperatorStatement() {
    // Create a mock traversal with a statement that is missing an operator
    Node externs = createExterns();
    Node root = createRoot();
    return new NodeTraversal(compiler, check, externs, root);
  }
}