import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionBuilderTest {
    // Test for withLongOpt method
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        Option option = optionBuilder.create('o');
        assertEquals("optionLongName", option.getLongOpt());
    }

    // Test for hasArg method
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('a');
        assertEquals(1, option.getArgs().length);
    }

    // Test for isRequired method
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('r');
        assertTrue(option.isRequired());
    }

    // Test for withValueSeparator method
    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('v');
        assertEquals(':', option.getValueSeparator());
    }

    // Test for hasArgs method
    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('h');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    // Test for hasOptionalArg method
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('o');
        assertTrue(option.isOptional());
    }

    // Test for withType method
    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option option = optionBuilder.create('s');
        assertEquals(String.class, option.getType());
    }

    // Test for withDescription method
    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("An option with description");
        Option option = optionBuilder.create('d');
        assertEquals("An option with description", option.getDescription());
    }

    // Test for create method with required argument
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithRequiredArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create('x');
    }

    // Test for create method with invalid long opt
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt-");
        Option option = optionBuilder.create('o');
    }
}