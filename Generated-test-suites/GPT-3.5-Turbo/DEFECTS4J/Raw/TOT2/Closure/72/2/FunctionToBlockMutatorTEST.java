import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.FunctionToBlockMutator;

public class FunctionToBlockMutatorTest {

    @Test
    public void testMutate() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, supplier);

        // Test basic functionality
        Node fnNode = createFunctionNode();
        Node callNode = createCallNode();
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;

        Node mutatedNode = mutator.mutate("testFunction", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        assertNotNull(mutatedNode);
        // Additional assertions can be added here
    }

    // Additional test cases for other public methods can be added here

    // Edge cases and exception handling scenarios can be tested here

}