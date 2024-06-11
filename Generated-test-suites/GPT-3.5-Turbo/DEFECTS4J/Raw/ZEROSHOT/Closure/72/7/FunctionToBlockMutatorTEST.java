import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

    private AbstractCompiler compiler;
    private Supplier<String> safeNameIdSupplier;
    private FunctionToBlockMutator mutator;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "12345";
            }
        };
        mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    }

    @Test
    public void testMutate() {
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        Node result = mutator.mutate("test", fnNode, callNode, "result", true, false);
        assertNotNull(result);
    }
}