import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setUp() {
    functionToBlockMutator = new FunctionToBlockMutator(new AbstractCompiler(), () -> "safeNameId");
  }

  @Test
  public void testMutate_withValidFunctionAndCallNode_returnsMutatedBlock() {
    // Arrange
    String fnName = "testFunction";
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    Node expectedBlock = new Node(Token.BLOCK);
    
    // Act
    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    
    // Assert
    assertNotNull(mutatedBlock);
    assertEquals(expectedBlock, mutatedBlock);
  }

  @Test
  public void testMutate_withEmptyFunctionAndCallNode_returnsMutatedBlock() {
    // Arrange
    String fnName = "emptyFunction";
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    Node expectedBlock = new Node(Token.BLOCK);
    
    // Act
    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    
    // Assert
    assertNotNull(mutatedBlock);
    assertEquals(expectedBlock, mutatedBlock);
  }

  @Test
  public void testMutate_withValidFunctionAndEmptyCallNode_returnsMutatedBlock() {
    // Arrange
    String fnName = "testFunction";
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.EMPTY);
    Node expectedBlock = new Node(Token.BLOCK);
    
    // Act
    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    
    // Assert
    assertNotNull(mutatedBlock);
    assertEquals(expectedBlock, mutatedBlock);
  }

}