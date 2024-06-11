import org.junit.Test;
import static org.junit.Assert.*;

public class InlineObjectLiteralsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };
        
        InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
        assertNotNull(inlineObjectLiterals);
    }
}