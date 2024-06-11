import org.junit.Test;

public class FunctionToBlockMutatorTest {
    
    @Test
    public void testMutate() {
        // Test case 1
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> idSupplier = new Supplier<String>() {
            public String get() {
                return "123";
            }
        };
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, idSupplier);
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;
        Node result = mutator.mutate("fnName", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
        
        // Test case 2
        AbstractCompiler compiler2 = new AbstractCompiler();
        Supplier<String> idSupplier2 = new Supplier<String>() {
            public String get() {
                return "456";
            }
        };
        FunctionToBlockMutator mutator2 = new FunctionToBlockMutator(compiler2, idSupplier2);
        Node fnNode2 = new Node(Token.FUNCTION);
        Node callNode2 = new Node(Token.CALL);
        String resultName2 = "result2";
        boolean needsDefaultResult2 = false;
        boolean isCallInLoop2 = true;
        Node result2 = mutator2.mutate("fnName2", fnNode2, callNode2, resultName2, needsDefaultResult2, isCallInLoop2);
        
        // ... add more test cases if needed ...
    }
}