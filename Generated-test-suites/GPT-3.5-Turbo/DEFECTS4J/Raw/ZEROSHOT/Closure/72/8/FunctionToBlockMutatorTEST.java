import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate() {
    // Prepare test data
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      @Override
      public String get() {
        return "testId123";
      }
    };
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFn";
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    // Run the test
    Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Verify the result
    assertNotNull(result);
    // Additional assertions can be added based on the expected behavior of the mutate method
  }

}