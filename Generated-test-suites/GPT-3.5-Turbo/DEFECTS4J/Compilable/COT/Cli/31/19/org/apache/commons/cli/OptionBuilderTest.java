package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("option");
        Option option = OptionBuilder.create();
        assertEquals("option", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgFalse() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        assertFalse(option.hasArg());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("name");
        Option option = OptionBuilder.create();
        assertEquals("name", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescritpion() {
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create();
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasOptionalArgsNum() {
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create();
        assertTrue(option.hasOptionalArg());
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDefaultSeparater() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();
        assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('c');
        assertEquals("c", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharInvalid() {
        OptionBuilder.create('$');
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("option");
        Option option = OptionBuilder.create();
        assertNotNull(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMissingLongOpt() {
        OptionBuilder.create();
    }
}