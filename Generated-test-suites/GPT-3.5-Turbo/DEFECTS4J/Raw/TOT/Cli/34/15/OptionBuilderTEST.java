import org.junit.*;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("example");
        Option option = OptionBuilder.create();

        assertEquals("example", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();

        assertEquals(true, option.hasArg());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();

        assertEquals(false, option.hasArg());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        Option option = OptionBuilder.create();

        assertEquals("name", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();

        assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();

        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();

        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithFalse() {
        OptionBuilder.isRequired(false);
        Option option = OptionBuilder.create();

        assertEquals(false, option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2);
        Option option = OptionBuilder.create();

        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();

        assertEquals(true, option.hasArg());
        assertEquals(true, option.getOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.getOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create();

        assertEquals(3, option.getArgs());
        assertEquals(true, option.getOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();

        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("example description");
        Option option = OptionBuilder.create();

        assertEquals("example description", option.getDescription());
    }
}