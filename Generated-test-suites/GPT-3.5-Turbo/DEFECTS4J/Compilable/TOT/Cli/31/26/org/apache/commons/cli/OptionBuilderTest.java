package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("testOption", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArg_WithFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals(0, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArg");
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("testArg", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs_WithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals(3, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator('=');
        Option option = OptionBuilder.create();
        assertNotNull(option);
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testWithMultipleOptions() {
        OptionBuilder.withLongOpt("testOption1");
        Option option1 = OptionBuilder.create();
        assertNotNull(option1);
        assertEquals("testOption1", option1.getLongOpt());

        OptionBuilder.withLongOpt("testOption2");
        Option option2 = OptionBuilder.create();
        assertNotNull(option2);
        assertEquals("testOption2", option2.getLongOpt());
    }
}