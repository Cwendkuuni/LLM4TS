import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

    @Test
    public void testMutate_basic() {
        // Test basic functionality of the mutate method
        // Create an instance of FunctionToBlockMutator
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test";
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

        // Create sample input nodes
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = "result";
        boolean needsDefaultResult = false;
        boolean isCallInLoop = false;

        // Test the mutate method
        Node result = mutator.mutate("fnName", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Check if the result is not null
        assertNotNull(result);
        // Additional assertions can be added here based on the expected behavior
    }

    @Test
    public void testReplaceReturns_emptyBlock() {
        // Test replaceReturns method with an empty block
        Node emptyBlock = new Node(Token.BLOCK);
        String resultName = "result";
        String labelName = "label";

        // Test the replaceReturns method
        Node result = FunctionToBlockMutator.replaceReturns(emptyBlock, resultName, labelName, true);

        // Check if the result is not null
        assertNotNull(result);
        // Additional assertions can be added here based on the expected behavior
    }

    // Add more test cases for other methods in FunctionToBlockMutator as needed

}