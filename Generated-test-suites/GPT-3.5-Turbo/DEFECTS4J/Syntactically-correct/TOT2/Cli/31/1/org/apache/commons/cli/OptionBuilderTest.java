import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Before
    public void setUp() throws Exception {
        OptionBuilder.reset();
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.create().getArgs());

        OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArg");
        assertEquals("testArg", OptionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.create().isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.create().getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.create().isRequired());

        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.create().isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgsWithNumber() {
        OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.create().hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.create().getArgs());
    }

    @Test
    public void testHasOptionalArgsWithNumber() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.create().getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.create().getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test description");
        assertEquals("test description", OptionBuilder.create().getDescription());
    }

    @Test
    public void testCreateWithCharacter() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("test");
        Option option = OptionBuilder.create();
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
    }
}