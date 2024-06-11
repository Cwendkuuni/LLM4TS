import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionToBlockMutatorTest {

   @Test
   public void testMutate() {
      AbstractCompiler compiler = new AbstractCompiler();
      FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, null);
      Node fnNode = new Node(Token.FUNCTION);
      Node callNode = new Node(Token.CALL);
      String fnName = "test";
      String resultName = "result";
      boolean needsDefaultResult = true;
      boolean isCallInLoop = true;

      Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName,
                        needsDefaultResult, isCallInLoop);

      // Add your assertions here
   }
}