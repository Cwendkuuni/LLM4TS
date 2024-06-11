import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FunctionToBlockMutatorTest {
    @Test
    public void testMutate() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "12345";
            }
        };
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
        
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;
        
        Node result = mutator.mutate("testFn", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
        
        // Assertions
        // TODO: Add assertions
    }
}