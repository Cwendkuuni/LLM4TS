import org.junit.Test;
import static org.junit.Assert.*;

public class InlineObjectLiteralsTest {

    @Test
    public void testProcess() {
        InlineObjectLiterals inliner = new InlineObjectLiterals(new AbstractCompiler(), new Supplier<String>() {
            public String get() {
                return "test";
            }
        });

        // Add test cases here
        assertTrue(true);

    }

}