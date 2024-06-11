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
    public void testHasArgWithNum() {
        Option option = OptionBuilder.hasArgs(2).create();
        assertEquals(2, option.getArgs());
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
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        Option option = OptionBuilder.withValueSeparator().create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsolation() {
        OptionBuilder.withLongOpt("test1").create();
        Option option = OptionBuilder.withLongOpt("test2").create();
        assertEquals("test2", option.getLongOpt());
    }
}