import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> idSupplier;
  private FunctionToBlockMutator mutator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    idSupplier = new Supplier<String>() {
      int id = 0;
      public String get() {
        return String.valueOf(id++);
      }
    };
    mutator = new FunctionToBlockMutator(compiler, idSupplier);
  }

  @Test
  public void testMutate() {
    String fnName = "testFunction";
    Node fnNode = new Node(Node.FUNCTION);
    Node callNode = new Node(Node.CALL);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // add test assertions here
  }
}