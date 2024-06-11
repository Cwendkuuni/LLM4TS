import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        assertEquals(-1, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('D');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithDefaultSeparator() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create('D');
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithFalse() {
        OptionBuilder.isRequired(false);
        Option option = OptionBuilder.create();
        assertFalse(option.isRequired());
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
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertEquals(Integer.MAX_VALUE, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(5);
        Option option = OptionBuilder.create();
        assertEquals(5, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder.withType(type);
        Option option = OptionBuilder.create();
        assertEquals(type, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getDescription());
    }

}