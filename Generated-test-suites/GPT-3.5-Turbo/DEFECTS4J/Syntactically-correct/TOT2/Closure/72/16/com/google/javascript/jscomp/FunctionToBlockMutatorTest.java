import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate() {
    AbstractCompiler compiler = new Compiler();
    Supplier<String> safeNameIdSupplier = new UniqueNameIdSupplier();
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node fnNode = new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction"));
    Node callNode = new Node(Token.CALL, new Node(Token.NAME, "testFunction"));
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node mutatedNode = mutator.mutate("testFunction", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Write your assertions here
  }

  // Add more test cases for other public methods in FunctionToBlockMutator class

}