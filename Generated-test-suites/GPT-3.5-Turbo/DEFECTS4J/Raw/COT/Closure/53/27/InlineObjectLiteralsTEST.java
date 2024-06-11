
import static org.junit.Assert.assertEquals;
import org.junit.Test;
    
public class InlineObjectLiteralsTest {
    
    @Test
    public void testProcess() {
        // Setup
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "1";
            }
        };
        InlineObjectLiterals inliner = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    
        // Verify
        assertEquals("JSCompiler_object_inline_a_1", inliner.process("a"));
    }
}
