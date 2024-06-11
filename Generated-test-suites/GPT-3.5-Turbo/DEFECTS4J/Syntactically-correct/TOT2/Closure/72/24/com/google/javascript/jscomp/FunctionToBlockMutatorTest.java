import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.javascript.jscomp.FunctionToBlockMutator;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate_basicFunctionality() {
    // Create a mock AbstractCompiler and Supplier<String>
    AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = Mockito.mock(Supplier.class);

    // Initialize FunctionToBlockMutator
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    // Create test data
    String fnName = "testFunction";
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    // Perform mutation
    Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Assertion
    assertNotNull(mutatedNode);
  }

  // Additional test cases for edge cases and exception handling scenarios can be added here

}