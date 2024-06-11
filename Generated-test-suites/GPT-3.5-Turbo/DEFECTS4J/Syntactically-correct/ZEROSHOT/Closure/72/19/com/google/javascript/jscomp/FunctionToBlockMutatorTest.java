import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Test
  public void testMutate() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFn";
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertNotNull(result);
  }

  @Test
  public void testAndInlineArguments() {
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node fnTemplateRoot = new Node(Token.FUNCTION);
    LinkedHashMap<String, Node> argMap = new LinkedHashMap();
    Set<String> namesToAlias = new HashSet<>();
    Node result = mutator.aliasAndInlineArguments(fnTemplateRoot, argMap, namesToAlias);
    assertNotNull(result);
  }

  @Test
  public void testReplaceReturns() {
    Node block = new Node(Token.BLOCK);
    String resultName = "result";
    String labelName = "label";
    boolean resultMustBeSet = true;
    Node result = FunctionToBlockMutator.replaceReturns(block, resultName, labelName, resultMustBeSet);
    assertNotNull(result);
  }

  @Test
  public void testConvertLastReturnToStatement() {
    Node block = new Node(Token.BLOCK);
    String resultName = "result";
    FunctionToBlockMutator.convertLastReturnToStatement(block, resultName);
  }

  @Test
  public void testCreateAssignStatementNode() {
    String name = "name";
    Node expression = new Node(Token.EXPR_RESULT);
    Node result = FunctionToBlockMutator.createAssignStatementNode(name, expression);
    assertNotNull(result);
  }

  @Test
  public void testGetReplacementReturnStatement() {
    Node node = new Node(Token.RETURN);
    String resultName = "result";
    Node result = FunctionToBlockMutator.getReplacementReturnStatement(node, resultName);
    assertNotNull(result);
  }

  @Test
  public void testHasReturnAtExit() {
    Node block = new Node(Token.BLOCK);
    boolean result = FunctionToBlockMutator.hasReturnAtExit(block);
    assertEquals(result, false);
  }

  @Test
  public void testReplaceReturnWithBreak() {
    Node current = new Node(Token.RETURN);
    Node parent = new Node(Token.BLOCK);
    String resultName = "result";
    String labelName = "label";
    Node result = FunctionToBlockMutator.replaceReturnWithBreak(current, parent, resultName, labelName );
    assertNotNull(result);
  }

}