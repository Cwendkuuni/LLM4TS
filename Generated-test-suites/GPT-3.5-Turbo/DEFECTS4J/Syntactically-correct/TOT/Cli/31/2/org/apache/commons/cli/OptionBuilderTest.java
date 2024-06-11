import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    // Test case for withLongOpt() method
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // Assertion to check if longopt is set correctly
        assertEquals("test", OptionBuilder.getLongopt());
    }

    // Test case for hasArg() method
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // Assertion to check if numberOfArgs is set correctly
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    // Test case for hasArg(boolean hasArg) method
    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // Assertion to check if numberOfArgs is set correctly
        assertEquals(1, OptionBuilder.getNumberOfArgs());

        OptionBuilder.hasArg(false);
        // Assertion to check if numberOfArgs is set correctly
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    // Test case for withArgName() method
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        // Assertion to check if argName is set correctly
        assertEquals("test", OptionBuilder.getArgName());
    }

    // Test case for isRequired() method
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // Assertion to check if required is set correctly
        assertTrue(OptionBuilder.isRequired());
    }

    // Test case for withValueSeparator(char sep) method
    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        // Assertion to check if valuesep is set correctly
        assertEquals(':', OptionBuilder.getValuesep());
    }

    // Test case for withValueSeparator() method
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        // Assertion to check if valuesep is set correctly
        assertEquals('=', OptionBuilder.getValuesep());
    }

    // Test case for isRequired(boolean newRequired) method
    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        // Assertion to check if required is set correctly
        assertTrue(OptionBuilder.isRequired());

        OptionBuilder.isRequired(false);
        // Assertion to check if required is set correctly
        assertFalse(OptionBuilder.isRequired());
    }

    // Test case for hasArgs() method
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // Assertion to check if numberOfArgs is set correctly
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    // Test case for hasArgs(int num) method
    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(5);
        // Assertion to check if numberOfArgs is set correctly
        assertEquals(5, OptionBuilder.getNumberOfArgs());
    }

    // Test case for hasOptionalArg() method
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // Assertion to check if numberOfArgs is set correctly
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        // Assertion to check if optionalArg is set correctly
        assertTrue(OptionBuilder.isOptionalArg());
    }

    // Test case for hasOptionalArgs() method
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // Assertion to check if numberOfArgs is set correctly
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        // Assertion to check if optionalArg is set correctly
        assertTrue(OptionBuilder.isOptionalArg());
    }

    // Test case for hasOptionalArgs(int numArgs) method
    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        // Assertion to check if numberOfArgs is set correctly
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        // Assertion to check if optionalArg is set correctly
        assertTrue(OptionBuilder.isOptionalArg());
    }

    // Test case for withType() method
    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        // Assertion to check if type is set correctly
        assertEquals(String.class, OptionBuilder.getType());
    }

    // Test case for withDescription() method
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test description");
        // Assertion to check if description is set correctly
        assertEquals("test description", OptionBuilder.getDescription());
    }

    // Test case for create(char opt) method
    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('a');
        // Assertion to check if the Option instance is not null
        assertNotNull(option);
        // Assertion to check if the option has the correct option character
        assertEquals("a", option.getOpt());
    }

    // Test case for create() method
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    // Test case for create() method
    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        // Assertion to check if the Option instance is not null
        assertNotNull(option);
        // Assertion to check if the option has the correct long option
        assertEquals("test", option.getLongOpt());
    }

    // Test case for create(String opt) method
    @Test
    public void testCreateWithStringOpt() {
        Option option = OptionBuilder.create("test");
        // Assertion to check if the Option instance is not null
        assertNotNull(option);
        // Assertion to check if the option has the correct option
        assertEquals("test", option.getOpt());
    }

}