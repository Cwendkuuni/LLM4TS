import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

    @Test
    public void testMutate_basicFunctionality() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(new AbstractCompiler(), new Supplier<String>() {
            @Override
            public String get() {
                return "safeName";
            }
        });

        String fnName = "testFunction";
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;

        Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Perform assertions here
        // Example: assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testMutate_edgeCases() {
        // Test edge cases for mutate method
    }

    @Test
    public void testMutate_exceptionsHandling() {
        // Test exception handling scenarios for mutate method
    }
}