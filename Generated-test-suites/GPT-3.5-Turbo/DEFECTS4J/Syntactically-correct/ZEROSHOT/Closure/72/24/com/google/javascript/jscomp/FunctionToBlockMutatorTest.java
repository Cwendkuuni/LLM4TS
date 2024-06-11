import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      @Override
      public String get() {
        return "test";
      }
    };

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node mutatedNode = mutator.mutate("fnName", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Add assertions to test the mutation of the node
  }

}