import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
        public String get() {
            return "safe_name_id";
        }
    };
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    String fnName = "testFunction";
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertNotNull(result);
  }
  
}