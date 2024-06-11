import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class FunctionToBlockMutatorTest {

    @Test
    public void testMutate() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "1";
            }
        };
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

        String fnName = "test";
        Node fnNode = createTestNode();
        Node callNode = createTestNode();
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;

        Node result = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        Assert.assertNotNull(result);
        // TODO: Add more assertions
    }

    private Node createTestNode() {
        return new Node(Node.EMPTY_TOKEN);
    }
}