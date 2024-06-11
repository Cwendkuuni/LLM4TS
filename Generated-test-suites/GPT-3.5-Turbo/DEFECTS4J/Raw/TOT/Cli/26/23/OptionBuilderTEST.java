import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt");
        Option option = builder.create();

        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        Option option = builder.create();

        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder builder = OptionBuilder.hasArg(false);
        Option option = builder.create();

        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("argName");
        Option option = builder.create();

        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        Option option = builder.create();

        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        Option option = builder.create();

        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        Option option = builder.create();

        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithFalse() {
        OptionBuilder builder = OptionBuilder.isRequired(false);
        Option option = builder.create();

        assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        Option option = builder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder builder = OptionBuilder.hasArgs(2);
        Option option = builder.create();

        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        Option option = builder.create();

        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        Option option = builder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(3);
        Option option = builder.create();

        assertEquals(3, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.withType(Integer.class);
        Option option = builder.create();

        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("description");
        Option option = builder.create();

        assertEquals("description", option.getDescription());
    }

}