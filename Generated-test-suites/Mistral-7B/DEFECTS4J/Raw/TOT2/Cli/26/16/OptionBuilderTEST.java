import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withValueSeparator(':')
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(1, option.getArgs().length);
        assertEquals("value", option.getValue(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .withType(Integer.class)
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(1, option.getArgs().length);
        assertEquals(null, option.getValue());
    }

    @Test
    public void testOptionBuilderWithOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasOptionalArg()
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithRequiredArg()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .isRequired()
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidArgName()
    {
        OptionBuilder.reset();
        OptionBuilder.withArgName("arg!");
        OptionBuilder.create();
    }

    @Test
    public void testOptionBuilderWithHasArgs()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArgs()
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt", option.getDescription());
        assertEquals("arg0, arg1, arg2, ...", option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithHasArgAndHasOptionalArgs()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .hasArg()
                .hasOptionalArgs(2)
                .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Option with long opt", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(3, option.getArgs().length);
        assertNull(option.getValue());
    }
}