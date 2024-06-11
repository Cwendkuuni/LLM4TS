package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("test");
        assertEquals("test", optionBuilder.create().getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertEquals(1, optionBuilder.create().getArgs());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, optionBuilder.create().getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("name");
        assertEquals("name", optionBuilder.create().getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertTrue(optionBuilder.create().isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertEquals("description", optionBuilder.create().getDescription());
    }

    // Add more test cases for the remaining methods

}