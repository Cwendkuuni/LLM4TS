import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {
    private AbstractCompiler compiler;
    private Supplier<String> safeNameIdSupplier;
    private InlineObjectLiterals inliner;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        safeNameIdSupplier = new Supplier<String>() {
            private int count = 0;

            @Override
            public String get() {
                return String.valueOf(count++);
            }
        };
        inliner = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    }

    @Test
    public void testProcess() {
        // create a dummy externs and root node
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);

        // invoke the process method
        inliner.process(externs, root);

        // assertions for the transformed code
        // TODO: add appropriate assertions

        // assertions for code changes
        assertTrue(compiler.hasCodeChanged());
    }
}