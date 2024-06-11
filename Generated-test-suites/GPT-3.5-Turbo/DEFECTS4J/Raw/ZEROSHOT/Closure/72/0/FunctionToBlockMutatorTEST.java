import org.junit.Assert;
import org.junit.Test;

public class FunctionToBlockMutatorTest {
  
  @Test
  public void testMutate() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> idSupplier = new Supplier<String>() {
      public String get() {
        return "1";
      }
    };
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, idSupplier);
    
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node result = mutator.mutate("fnName", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    
    // Add test assertions here
    
  }
  
}