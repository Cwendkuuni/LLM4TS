import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionToBlockMutatorTest {
    
    @Test
    public void testMutate() {
        // Create a mock AbstractCompiler
        AbstractCompiler compiler = new AbstractCompiler();
        
        // Create a mock Supplier<String> for the safeNameIdSupplier
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "1";
            }
        };
        
        // Create a new instance of FunctionToBlockMutator
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
        
        // Create test inputs for the mutate method
        String fnName = "testFn";
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;
        
        // Call the mutate method
        Node mutatedBlock = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
        
        // Perform assertions on the mutated block
        // ...
    }
}