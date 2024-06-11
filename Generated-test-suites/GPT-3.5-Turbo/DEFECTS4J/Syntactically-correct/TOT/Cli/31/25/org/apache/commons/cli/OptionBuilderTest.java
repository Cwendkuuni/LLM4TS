import org.junit.Test;
import org.apache.commons.cli.Option;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create("test");
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.reset();
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create("test");
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());

        OptionBuilder.reset();
        OptionBuilder.hasArg(false);
        option = OptionBuilder.create("test");
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.reset();
        OptionBuilder.withArgName("name");
        Option option = OptionBuilder.create("test");
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.reset();
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create("test");
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.reset();
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create("test");
        assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithChar() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testCreateWithString() {
        OptionBuilder.reset();
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create("test");
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.reset();
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('t');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.reset();
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create("test");
        assertTrue(option.hasArgs());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.reset();
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create("test");
        assertTrue(option.hasArgs());
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create("test");
        assertTrue(option.hasOptionalArgs());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.reset();
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create("test");
        assertTrue(option.hasOptionalArgs());
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.reset();
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create("test");
        assertEquals(Integer.class, option.getType());
    }
}