package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("longOption");
        Option option = OptionBuilder.create('l');
        assertEquals("longOption", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create('a');
        assertTrue(option.isRequired());
        assertEquals(Option.VALUE_REQUIRED, option.getArgs());
    }

    @Test
    public void testHasArgWithName() {
        OptionBuilder.hasArg().withArgName("argName");
        Option option = OptionBuilder.create('a');
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create('r');
        assertTrue(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create('h');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(2);
        Option option = OptionBuilder.create('n');
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create('o');
        assertEquals(Option.VALUE_OPTIONAL, option.getArgs());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create('p');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        OptionBuilder.hasOptionalArgs(3);
        Option option = OptionBuilder.create('q');
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        Option option = OptionBuilder.create('i');
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        Option option = OptionBuilder.create('d');
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreate() {
        OptionBuilder.withLongOpt("longOption")
                .hasArg().isRequired().hasOptionalArg();
        Option option = OptionBuilder.create('x');
        assertEquals("longOption", option.getLongOpt());
        assertTrue(option.isRequired());
        assertTrue(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }
}