package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("test").create('t');
        assertNotNull(option);
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create('t');
        assertNotNull(option);
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("arg").create('t');
        assertNotNull(option);
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create('t');
        assertNotNull(option);
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create('t');
        assertNotNull(option);
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create('t');
        assertNotNull(option);
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testCreate() {
        try {
            OptionBuilder.create('t');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("must specify longopt", e.getMessage());
        }
    }
}