import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class FunctionToBlockMutatorTest {

    private FunctionToBlockMutator mutator;
    private Node fnNode;
    private Node callNode;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            public String get() {
                return "id";
            }
        };
        mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
        fnNode = new Node(Token.FUNCTION);
        callNode = new Node(Token.CALL);
    }

    @Test
    public void testMutate_singleParameter_noResult() {
        // Arrange
        String fnName = "";
        String resultName = "";
        boolean needsDefaultResult = false;
        boolean isCallInLoop = false;

        // Act
        Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Assert
        assertNotNull(mutatedNode);
        assertEquals(fnNode, mutatedNode);
    }

    @Test
    public void testMutate_multipleParameters_withResult() {
        // Arrange
        String fnName = "foo";
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = true;

        // Act
        Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Assert
        assertNotNull(mutatedNode);
        assertEquals(fnNode, mutatedNode);
    }

    @Test
    public void testMutate_noParameters_withResult_notInLoop() {
        // Arrange
        String fnName = "";
        String resultName = "result";
        boolean needsDefaultResult = false;
        boolean isCallInLoop = false;

        // Act
        Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Assert
        assertNotNull(mutatedNode);
        assertEquals(fnNode, mutatedNode);
    }

    @Test
    public void testMutate_noParameters_noResult_inLoop() {
        // Arrange
        String fnName = "";
        String resultName = "";
        boolean needsDefaultResult = false;
        boolean isCallInLoop = true;

        // Act
        Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Assert
        assertNotNull(mutatedNode);
        assertEquals(fnNode, mutatedNode);
    }

}