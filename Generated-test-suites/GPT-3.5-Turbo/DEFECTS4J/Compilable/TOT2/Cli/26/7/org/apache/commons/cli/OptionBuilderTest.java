package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("testArgName").create();
        assertEquals("testArgName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("testDescription").create();
        assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.create();
        assertNull(option.getOpt());
    }

    @Test
    public void testCreateWithString() {
        Option option = OptionBuilder.create("testString");
        assertEquals("testString", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    // Add more test cases for edge cases and exception handling scenarios if needed

}