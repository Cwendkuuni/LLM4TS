import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        // Test withLongOpt method
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create();

        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        // Test hasArg method
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();

        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgBoolean() {
        // Test hasArg method with boolean argument
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        Option option = optionBuilder.create();

        assertEquals(1, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        // Test withArgName method
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        Option option = optionBuilder.create();

        assertEquals("testArgName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        // Test isRequired method
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();

        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        // Test withValueSeparator method with char argument
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();

        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        // Test withValueSeparator method without argument
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Option option = optionBuilder.create();

        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredBoolean() {
        // Test isRequired method with boolean argument
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        Option option = optionBuilder.create();

        assertTrue(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        // Test hasArgs method
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsInt() {
        // Test hasArgs method with int argument
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();

        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        // Test hasOptionalArg method
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();

        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        // Test hasOptionalArgs method
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsInt() {
        // Test hasOptionalArgs method with int argument
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        Option option = optionBuilder.create();

        assertEquals(2, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        // Test withType method
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create();

        assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        // Test withDescription method
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        Option option = optionBuilder.create();

        assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testCreateChar() {
        // Test create method with char argument
        Option option = OptionBuilder.create('t');

        assertEquals("t", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharInvalid() {
        // Test create method with invalid char argument
        OptionBuilder.create('-');
    }

    @Test
    public void testCreate() {
        // Test create method
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create();

        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt() {
        // Test create method when longOpt is not set
        OptionBuilder.create();
    }

    @Test
    public void testCreateString() {
        // Test create method with string argument
        Option option = OptionBuilder.create("t");

        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreateAllProperties() {
        // Test create method with all properties set
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
                .hasArgs(2)
                .isRequired()
                .withType(Integer.class)
                .withDescription("testDescription");
        Option option = optionBuilder.create();

        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals(2, option.getArgs());
        assertTrue(option.isRequired());
        assertEquals(Integer.class, option.getType());
        assertEquals("testDescription", option.getDescription());
    }
}