import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    // Test for withLongOpt method
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option option = optionBuilder.create('o');
        assertEquals("optionLong", option.getLongOpt());
    }

    // Test for hasArg method
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('a');
        assertEquals(1, option.getArgs().length);
    }

    // Test for hasOptionalArg method
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('o');
        assertTrue(option.isOptionalArg());
    }

    // Test for create method with required option
    @Test
    public void testCreateRequiredOption() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create('r');
        assertTrue(option.isRequired());
    }

    // Test for create method with optional option
    @Test
    public void testCreateOptionalOption() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('o');
        assertFalse(option.isRequired());
    }

    // Test for create method with argument
    @Test
    public void testCreateOptionWithArgument() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName")
                .hasArg();
        Option option = optionBuilder.create('a');
        assertEquals("argName", option.getArgName());
    }

    // Test for create method with value separator
    @Test
    public void testCreateOptionWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':')
                .hasArgs(2);
        Option option = optionBuilder.create('s');
        assertEquals(2, option.getArgs().length);
    }

    // Test for create method with type
    @Test
    public void testCreateOptionWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class)
                .hasArgs();
        Option option = optionBuilder.create('i');
        assertEquals(Integer.class, option.getType());
    }

    // Test for create method with description
    @Test
    public void testCreateOptionWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test option")
                .hasArg();
        Option option = optionBuilder.create('t');
        assertEquals("Test option", option.getDescription());
    }
}