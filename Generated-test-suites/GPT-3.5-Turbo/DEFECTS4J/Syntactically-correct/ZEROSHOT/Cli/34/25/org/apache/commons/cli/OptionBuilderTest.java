import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", optionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgWithBool() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        assertEquals(1, optionBuilder.create().getArgs());
        
        optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", optionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', optionBuilder.create().getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertEquals('=', optionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequiredBool() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        assertTrue(optionBuilder.create().isRequired());
        
        optionBuilder = OptionBuilder.isRequired(false);
        assertFalse(optionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertEquals(2, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertTrue(optionBuilder.create().hasOptionalArg());
        assertEquals(1, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertTrue(optionBuilder.create().hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        assertTrue(optionBuilder.create().hasOptionalArg());
        assertEquals(2, optionBuilder.create().getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, optionBuilder.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", optionBuilder.create().getDescription());
    }

    @Test
    public void testCreateWithChar() {
        OptionBuilder optionBuilder = OptionBuilder.create('t');
        assertEquals("t", optionBuilder.create().getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithCharInvalid() {
        OptionBuilder optionBuilder = OptionBuilder.create(' ');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        OptionBuilder optionBuilder = OptionBuilder.create("test");
        assertEquals("test", optionBuilder.create().getOpt());
    }
}