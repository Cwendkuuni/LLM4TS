import org.junit.Assert;
import org.junit.Test;

public class InlineObjectLiteralsTest {

    @Test
    public void testProcess() {
        // Create dummy AbstractCompiler and Supplier objects
        AbstractCompiler compiler = new AbstractCompiler() {
            @Override
            public CodingConvention getCodingConvention() {
                return null;
            }
        };

        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "";
            }
        };

        // Create InlineObjectLiterals object
        InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

        // Create dummy nodes (externs and root)
        Node externs = new Node();
        Node root = new Node();

        // Call process method
        inlineObjectLiterals.process(externs, root);

        // Assert that the process method is executed without error
        Assert.assertTrue(true);
    }
}