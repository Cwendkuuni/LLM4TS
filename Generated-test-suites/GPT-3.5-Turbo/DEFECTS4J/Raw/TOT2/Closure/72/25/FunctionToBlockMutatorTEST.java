import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

    @Test
    public void testMutate() {
        // Create test instance of FunctionToBlockMutator
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = () -> "test_id";
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

        // Test case for basic functionality
        String fnName = "testFn";
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = "result";
        boolean needsDefaultResult = false;
        boolean isCallInLoop = false;
        Node mutatedNode = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Verify that mutatedNode is not null
        assertNotNull(mutatedNode);
    }

    @Test
    public void testReplaceReturns() {
        // Create a test block node
        Node block = new Node(Token.BLOCK);

        // Test case for resultMustBeSet true
        String resultName = "result";
        String labelName = "testLabel";
        boolean resultMustBeSet = true;
        Node replacedNode = FunctionToBlockMutator.replaceReturns(block, resultName, labelName, resultMustBeSet);

        // Verify that replacedNode is not null
        assertNotNull(replacedNode);

        // Test case for resultMustBeSet false
        resultMustBeSet = false;
        Node replacedNode2 = FunctionToBlockMutator.replaceReturns(block, resultName, labelName, resultMustBeSet);

        // Verify that replacedNode2 is not null
        assertNotNull(replacedNode2);
    }

    @Test
    public void testGetReplacementReturnStatement() {
        // Create a test return node
        Node returnNode = new Node(Token.RETURN, Node.newString("test"));

        // Test case for result name null
        String resultName = null;
        Node replacementNode = FunctionToBlockMutator.getReplacementReturnStatement(returnNode, resultName);

        // Verify that replacementNode is not null
        assertNotNull(replacementNode);

        // Test case for result name not null
        resultName = "result";
        Node replacementNode2 = FunctionToBlockMutator.getReplacementReturnStatement(returnNode, resultName);

        // Verify that replacementNode2 is not null
        assertNotNull(replacementNode2);
    }
}