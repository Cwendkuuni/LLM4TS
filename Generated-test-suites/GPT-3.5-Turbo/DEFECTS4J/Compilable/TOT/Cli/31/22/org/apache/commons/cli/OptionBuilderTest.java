package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    /**
     * Test for withLongOpt(String newLongopt) method
     */

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOption");
        Option option = OptionBuilder.create();
        assertEquals("testOption", option.getLongOpt());
    }

    /**
     * Test for hasArg() method
     */

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create('t');
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }

    /**
     * Test for hasArg(boolean hasArg) method - with hasArg set to true
     */

    @Test
    public void testHasArgTrue() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create('t');
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
    }


    /**
     * Test for hasArg(boolean hasArg) method - with hasArg set to false
     */

    @Test
    public void testHasArgFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create('t');
        assertFalse(option.hasArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    /**
     * Test for withArgName(String name) method
     */

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testName");
        Option option = OptionBuilder.create('t');
        assertEquals("testName", option.getArgName());
    }

    /**
     * Test for isRequired() method
     */

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create('t');
        assertTrue(option.isRequired());
    }

    /**
     * Test for withValueSeparator(char sep) method
     */

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create('t');
        assertEquals(':', option.getValueSeparator());
    }

    /**
     * Test for withValueSeparator() method - with default separator '='
     */

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create('t');
        assertEquals('=', option.getValueSeparator());
    }

    /**
     * Test for isRequired(boolean newRequired) method
     */

    @Test
    public void testIsRequiredTrue() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create('t');
        assertTrue(option.isRequired());
    }

    /**
     * Test for isRequired(boolean newRequired) method - with newRequired set to false
     */

    @Test
    public void testIsRequiredFalse() {
        OptionBuilder.isRequired(false);
        Option option = OptionBuilder.create('t');
        assertFalse(option.isRequired());
    }

    /**
     * Test for hasArgs() method
     */

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create('t');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    /**
     * Test for hasArgs(int num) method
     */

    @Test
    public void testHasArgsNum() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create('t');
        assertEquals(3, option.getArgs());
    }

    /**
     * Test for hasOptionalArg() method
     */

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create('t');
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    /**
     * Test for hasOptionalArgs() method
     */

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create('t');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    /**
     * Test for hasOptionalArgs(int numArgs) method
     */

    @Test
    public void testHasOptionalArgsNumArgs() {
        OptionBuilder.hasOptionalArgs(4);
        Option option = OptionBuilder.create('t');
        assertEquals(4, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    /**
     * Test for withType(Object newType) method
     */

    @Test
    public void testWithType() {
        Object type = new Object();
        OptionBuilder.withType(type);
        Option option = OptionBuilder.create('t');
        assertEquals(type, option.getType());
    }

    /**
     * Test for withDescription(String newDescription) method
     */

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create('t');
        assertEquals("testDescription", option.getDescription());
    }

    /**
     * Test for create(char opt) method
     */

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('t');
        assertEquals("t", option.getOpt());
        assertNull(option.getLongOpt());
    }

    /**
     * Test for create() method - missing longOpt
     */

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMissingLongOpt() {
        OptionBuilder.create();
    }

    /**
     * Test for create(String opt) method
     */

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("test");
        assertEquals("test", option.getOpt());
        assertNull(option.getLongOpt());
    }

}