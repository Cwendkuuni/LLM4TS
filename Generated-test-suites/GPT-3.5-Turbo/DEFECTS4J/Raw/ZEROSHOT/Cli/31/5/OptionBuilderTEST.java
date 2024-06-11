import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        assertEquals("test", OptionBuilder.accessLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.accessNumberOfArgs());
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder.hasArg(true);
        assertEquals(1, OptionBuilder.accessNumberOfArgs());
        OptionBuilder.hasArg(false);
        assertEquals(-1, OptionBuilder.accessNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("test");
        assertEquals("test", OptionBuilder.accessArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.accessRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.accessValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator();
        assertEquals('=', OptionBuilder.accessValueSeparator());
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder.isRequired(true);
        assertTrue(OptionBuilder.accessRequired());
        OptionBuilder.isRequired(false);
        assertFalse(OptionBuilder.accessRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        assertEquals(Integer.MAX_VALUE, OptionBuilder.accessNumberOfArgs());
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder.hasArgs(2);
        assertEquals(2, OptionBuilder.accessNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        assertEquals(1, OptionBuilder.accessNumberOfArgs());
        assertTrue(OptionBuilder.accessOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        assertEquals(Integer.MAX_VALUE, OptionBuilder.accessNumberOfArgs());
        assertTrue(OptionBuilder.accessOptionalArg());
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder.hasOptionalArgs(2);
        assertEquals(2, OptionBuilder.accessNumberOfArgs());
        assertTrue(OptionBuilder.accessOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Object.class);
        assertEquals(Object.class, OptionBuilder.accessType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("test");
        assertEquals("test", OptionBuilder.accessDescription());
    }

    @Test
    public void testCreateChar() {
        try {
            Option option = OptionBuilder.create('t');
            assertEquals("t", option.getOpt());
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testCreate() {
        try {
            Option option = OptionBuilder.create();
            assertNull(option.getOpt());
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testCreateString() {
        try {
            Option option = OptionBuilder.create("test");
            assertEquals("test", option.getOpt());
        } catch (IllegalArgumentException e) {
            fail("Should not throw exception");
        }
    }

    @Before
    public void setUp() {
        OptionBuilder.reset();
    }
}