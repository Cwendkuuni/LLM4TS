import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private FunctionToBlockMutator mutator;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    safeNameIdSupplier = new Supplier<String>() {
      int count = 0;
      public String get() {
        return String.valueOf(count++);
      }
    };
    mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutate_noArgs() {
    // Test case with no arguments
    Node fnNode = new Node(Token.FUNCTION);
    Node callNode = new Node(Token.CALL, new Node(Token.NAME, "foo"));
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node result = mutator.mutate("testFunction", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertEquals(fnNode, result);
  }

  @Test
  public void testMutate_withArgs() {
    // Test case with arguments
    Node fnNode = new Node(Token.FUNCTION, new Node(Token.PARAM_LIST, new Node(Token.NAME, "arg")));
    Node callNode = new Node(Token.CALL, new Node(Token.NAME, "foo"), new Node(Token.NAME, "bar"));
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;
    Node result = mutator.mutate("testFunction", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
    assertEquals(fnNode, result);
  }

}