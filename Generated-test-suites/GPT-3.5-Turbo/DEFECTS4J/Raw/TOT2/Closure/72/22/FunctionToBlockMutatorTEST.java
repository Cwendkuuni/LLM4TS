import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionToBlockMutatorTest {

    @Test
    public void testMutate_basicFunction() {
        // Create FunctionToBlockMutator instance
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "testId";
            }
        };
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

        // Create basic nodes for testing
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;

        Node mutatedNode = mutator.mutate("testFn", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
        
        // Assert statements for functionality
        assertNotNull(mutatedNode);
        assertEquals(Token.BLOCK, mutatedNode.getType());
    }

    @Test
    public void testMutate_edgeCases() {
        // Create FunctionToBlockMutator instance
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "testId";
            }
        };
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

        // Create nodes for testing edge cases
        Node fnNode = null;
        Node callNode = new Node(Token.CALL);
        String resultName = null;
        boolean needsDefaultResult = false;
        boolean isCallInLoop = true;

        Node mutatedNode = mutator.mutate("testFn", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
        
        // Assert statements for edge cases
        assertNull(mutatedNode);
    }

    @Test
    public void testMutate_exceptionHandling() {
        // Create FunctionToBlockMutator instance
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "testId";
            }
        };
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

        // Create nodes for testing exception handling
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = null;
        boolean needsDefaultResult = true;
        boolean isCallInLoop = true;

        Node mutatedNode = mutator.mutate("testFn", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
        
        // Assert statements for exception handling
        assertNotNull(mutatedNode);
        assertEquals(Token.BLOCK, mutatedNode.getType());
    }
}