import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OptionBuilderTest {
    // Test for withLongOpt method
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1");
        Option option = optionBuilder.create();
        assertEquals("option1", option.getLongOpt());
    }

    // Test for hasArg method with one argument
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertEquals(true, option.isRequired());
    }

    // Test for hasArg method with optional argument
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertEquals(false, option.isRequired());
    }

    // Test for hasArgs method with multiple arguments
    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create();
        assertEquals(2, option.getArgs().length);
        assertEquals(false, option.isRequired());
    }

    // Test for isRequired method
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertEquals(true, option.isRequired());
    }

    // Test for withValueSeparator method
    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    // Test for creating an Option with all settings
    @Test
    public void testCreateOption() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1")
                .withDescription("An option with long opt and description")
                .hasArg(true)
                .withValueSeparator(':')
                .isRequired();
        Option option = optionBuilder.create();
        assertEquals("option1", option.getLongOpt());
        assertEquals("An option with long opt and description", option.getDescription());
        assertEquals(Option.UNINITIALIZED + 1, option.getArgs().length);
        assertEquals(true, option.isRequired());
        assertEquals(':', option.getValueSeparator());
    }
}