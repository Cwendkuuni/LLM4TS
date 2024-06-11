import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("longopt").create();
        assertEquals("longopt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertEquals(true, option.hasArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        Option option = OptionBuilder.hasArg(true).create();
        assertEquals(true, option.hasArg());
        assertEquals(1, option.getArgs());

        option = OptionBuilder.hasArg(false).create();
        assertEquals(false, option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("argName").create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator().create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        Option option = OptionBuilder.isRequired(true).create();
        assertEquals(true, option.isRequired());

        option = OptionBuilder.isRequired(false).create();
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        assertEquals(true, option.hasArgs());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        Option option = OptionBuilder.hasArgs(3).create();
        assertEquals(true, option.hasArgs());
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        assertEquals(true, option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        assertEquals(true, option.hasArgs());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        Option option = OptionBuilder.hasOptionalArgs(2).create();
        assertEquals(true, option.hasArgs());
        assertEquals(2, option.getArgs());
        assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(String.class).create();
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("description").create();
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('o');
        assertEquals("o", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharInvalid() {
        OptionBuilder.create('$');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("option");
        assertEquals("option", option.getOpt());
    }
}