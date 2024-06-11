import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create();
        assertEquals("longopt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithArg() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithoutArg() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        assertFalse(option.hasArg());
        assertEquals(-1, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        Option option = OptionBuilder.create();
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequiredWithRequired() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequiredWithoutRequired() {
        OptionBuilder.isRequired(false);
        Option option = OptionBuilder.create();
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        assertEquals(Integer.MAX_VALUE, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create();
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArgs());
        assertEquals(Integer.MAX_VALUE, option.getArgs());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArgs());
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Option option = OptionBuilder.create();
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create();
        assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithCharOpt() {
        Option option = OptionBuilder.create('c');
        assertEquals('c', option.getOpt());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testCreateWithStringOpt() {
        Option option = OptionBuilder.create("opt");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
    }
}