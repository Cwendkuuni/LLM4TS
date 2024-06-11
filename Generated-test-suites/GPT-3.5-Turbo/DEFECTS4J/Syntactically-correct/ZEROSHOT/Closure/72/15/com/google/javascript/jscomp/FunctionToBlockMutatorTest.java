import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      int count = 0;
      public String get() {
        return "id" + count++;
      }
    };

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "test";
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(result);
  }
}