import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOption");
        // Check if longopt is set correctly
        assertEquals("testOption", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Check if numberOfArgs is set to 1
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgFalse() {
        OptionBuilder.hasArg(false);
        // Check if numberOfArgs is set to Option.UNINITIALIZED
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argument");
        // Check if argName is set correctly
        assertEquals("argument", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Check if required is set to true
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithArgSeparator() {
        OptionBuilder.withValueSeparator(':');
        // Check if valuesep is set to ':'
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testWithArgSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        // Check if valuesep is set to '=' as default
        assertEquals('=', OptionBuilder.valuesep);
    }

    @Test
    public void testIsRequiredFalse() {
        OptionBuilder.isRequired(false);
        // Check if required is set to false
        assertFalse(OptionBuilder.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Check if numberOfArgs is set to Option.UNLIMITED_VALUES
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(3);
        // Check if numberOfArgs is set to 3
        assertEquals(3, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Check if numberOfArgs is set to 1 and optionalArg is set to true
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Check if numberOfArgs is set to Option.UNLIMITED_VALUES and optionalArg is set to true
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(2);
        // Check if numberOfArgs is set to 2 and optionalArg is set to true
        assertEquals(2, OptionBuilder.numberOfArgs);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        // Check if type is set to String.class
        assertEquals(String.class, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("This is a test option");
        // Check if description is set correctly
        assertEquals("This is a test option", OptionBuilder.description);
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        // Check if option is created with the correct char
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreateWithoutLongOpt() {
        try {
            OptionBuilder.create();
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testOption");
        // Check if option is created with the correct string
        assertEquals("testOption", option.getOpt());
    }

}