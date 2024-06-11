import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class FunctionToBlockMutatorTest {

    private AbstractCompiler compiler;
    private Supplier<String> safeNameIdSupplier;
    private FunctionToBlockMutator mutator;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        safeNameIdSupplier = mock(Supplier.class);
        mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    }

    @Test
    public void testMutate() {
        // Test input parameters
        String fnName = "";
        Node fnNode = createTestNode();
        Node callNode = createTestNode();
        String resultName = "";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;

        // Expected output
        Node mutatedNode = createTestNode();

        // Mock external dependencies
        when(compiler.getUniqueNameIdSupplier()).thenReturn(safeNameIdSupplier);

        // Perform the test
        Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Verify the result
        Assert.assertEquals(mutatedNode.toStringTree(), result.toStringTree());
    }

    private Node createTestNode() {
        Node node = new Node(Token.ADD);
        node.addChildToBack(new Node(Token.NAME, "a"));
        node.addChildToBack(new Node(Token.NUMBER, "1"));

        return node;
    }
}