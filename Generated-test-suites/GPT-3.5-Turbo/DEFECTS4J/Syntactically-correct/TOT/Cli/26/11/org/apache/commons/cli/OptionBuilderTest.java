import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();
        assertEquals("testOption", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());

        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();
        assertFalse(option.hasArg());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArg");
        Option option = OptionBuilder.create();
        assertEquals("testArg", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();
        assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArgs());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArgs());
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testWithVersion() {
        OptionBuilder.withType("testType");
        Option option = OptionBuilder.create();
        assertEquals("testType", option.getType());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }

    // Add additional test cases for each method if required

}