
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", optionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithFalseBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(-1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", optionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparatorWithChar() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Integer.MAX_VALUE, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
        assertEquals(3, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertEquals(1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertEquals(Integer.MAX_VALUE, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        assertEquals(3, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertEquals(String.class, optionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", optionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create('c');
        assertEquals("c", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testCreateWithString() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create("testOpt");
        assertEquals("testOpt", option.getOpt());
    }
}
