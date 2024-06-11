import org.junit.Test;

import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("testLongOpt");
        assertEquals("testLongOpt", OptionBuilder.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder builder = OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder builder = OptionBuilder.withArgName("testArgName");
        assertEquals("testArgName", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder builder = OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithBoolean() {
        OptionBuilder builder = OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsWithInt() {
        OptionBuilder builder = OptionBuilder.hasArgs(3);
        assertEquals(3, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs();
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithInt() {
        OptionBuilder builder = OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.getNumberOfArgs());
        assertTrue(OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder builder = OptionBuilder.withType(Integer.class);
        assertEquals(Integer.class, OptionBuilder.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder builder = OptionBuilder.withDescription("testDescription");
        assertEquals("testDescription", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        try {
            Option option = OptionBuilder.create('t');
            assertEquals("t", option.getOpt());
            assertNull(option.getLongOpt());
            assertEquals(OptionBuilder.getDescription(), option.getDescription());
            assertFalse(option.isRequired());
            assertFalse(option.hasArg());
            assertEquals(OptionBuilder.getNumberOfArgs(), option.getArgs());
            assertNull(option.getType());
            assertEquals(OptionBuilder.getValueSeparator(), option.getValueSeparator());
            assertEquals(OptionBuilder.getArgName(), option.getArgName());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }

    @Test
    public void testCreateWithoutOpt() {
        try {
            OptionBuilder.reset();
            Option option = OptionBuilder.create();
            assertNull(option.getOpt());
            assertNotNull(option.getLongOpt());
            assertEquals(OptionBuilder.getLongOpt(), option.getLongOpt());
            assertEquals(OptionBuilder.getDescription(), option.getDescription());
            assertTrue(option.isRequired());
            assertFalse(option.hasArg());
            assertEquals(OptionBuilder.getNumberOfArgs(), option.getArgs());
            assertNull(option.getType());
            assertEquals(OptionBuilder.getValueSeparator(), option.getValueSeparator());
            assertEquals(OptionBuilder.getArgName(), option.getArgName());
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateWithString() {
        try {
            Option option = OptionBuilder.create("testOption");
            assertEquals("testOption", option.getOpt());
            assertNull(option.getLongOpt());
            assertEquals(OptionBuilder.getDescription(), option.getDescription());
            assertFalse(option.isRequired());
            assertFalse(option.hasArg());
            assertEquals(OptionBuilder.getNumberOfArgs(), option.getArgs());
            assertNull(option.getType());
            assertEquals(OptionBuilder.getValueSeparator(), option.getValueSeparator());
            assertEquals(OptionBuilder.getArgName(), option.getArgName());
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }
}