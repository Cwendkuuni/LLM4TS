import org.apache.commons.cli.Option;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('a');
        assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasArg_withArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("name");
        Option option = optionBuilder.create('a');
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('a');
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('a');
        assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create('a');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('D');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithValueSeparator_default() {
        OptionBuilder optionBuilder = OptionBuilder.create('D');
        Option option = optionBuilder.create('D');
        assertEquals('=', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutDescription() {
        OptionBuilder optionBuilder = OptionBuilder.create('a');
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option option = optionBuilder.create('a');
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.TYPE);
        Option option = optionBuilder.create('a');
        assertEquals(Integer.TYPE, option.getType());
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOption");
        Option option = optionBuilder.create('a');
        assertEquals("longOption", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withDescription("description");
        Option option = optionBuilder.create('a');
        assertNull(option.getDescription());
        OptionBuilder.reset();
    }
}