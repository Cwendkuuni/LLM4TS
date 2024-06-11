import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArgName");
        Option option = OptionBuilder.create();
        assertEquals("testArgName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();
        assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(5);
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals(5, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithValueSeparatorWithSep() {
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
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("testLongOpt");
        Option option = OptionBuilder.create();
        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
}