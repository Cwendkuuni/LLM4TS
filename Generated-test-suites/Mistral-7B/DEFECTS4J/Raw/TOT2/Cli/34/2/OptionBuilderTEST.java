import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.withLongOpt("newLongopt");
        assertEquals(expectedOptionBuilder, optionBuilder.withLongOpt("newLongopt"));
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.hasArg();
        assertEquals(expectedOptionBuilder, optionBuilder.hasArg());
    }

    @Test
    public void testEmptyLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.withLongOpt("");
        assertEquals(expectedOptionBuilder, optionBuilder.withLongOpt(""));
    }

    @Test
    public void testNullDescription() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        try {
            OptionBuilder.withDescription(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNullLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        try {
            OptionBuilder.withLongOpt(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testInvalidType() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        try {
            OptionBuilder.withType(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNoArgRequired() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.isRequired();
        OptionBuilder withArgOptionBuilder = OptionBuilder.hasArg();
        OptionBuilder expectedWithArgOptionBuilder = OptionBuilder.isRequired().hasArg();
        assertEquals(expectedWithArgOptionBuilder, optionBuilder.withLongOpt("test")
                .isRequired().hasArg());
        assertEquals(expectedOptionBuilder, expectedWithArgOptionBuilder.withoutArg());
        assertNotSame(expectedOptionBuilder, optionBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withValueSeparator('%');
    }
}