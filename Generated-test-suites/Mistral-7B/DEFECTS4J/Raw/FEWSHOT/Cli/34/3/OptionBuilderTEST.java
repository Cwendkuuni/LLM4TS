package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        assertEquals(expectedOptionBuilder.longopt, optionBuilder.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.argName = "argName";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.required = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.valuesep = ':';
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.valuesep = '=';
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.required = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNLIMITED_VALUES;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 2;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
        expectedOptionBuilder.optionalArg = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNLIMITED_VALUES;
        expectedOptionBuilder.optionalArg = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 3;
        expectedOptionBuilder.optionalArg = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.type = String.class;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Description");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.description = "Description";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testCreateChar() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("longopt", option.getLongOpt());
    }

    @Test
    public void testCreateString() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        Option option = OptionBuilder.create("a");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertEquals("longopt", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt() throws IllegalArgumentException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithDescription() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt").withDescription("Description");
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("longopt", option.getLongOpt());
        assertEquals("Description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithEmptyStringLongOpt() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullLongOpt() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithValueSeparator() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt").withValueSeparator(':');
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("longopt", option.getLongOpt());
        assertEquals(":", option.getValueSeparator());
    }

    @Test
    public void testCreateWithArgName() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt").withArgName("argName");
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("longopt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testCreateWithRequiredArg() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt").isRequired();
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("longopt", option.getLongOpt());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateWithOptionalArg() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt").hasOptionalArg();
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("longopt", option.getLongOpt());
        assertTrue(option.isOptional());
    }

    @Test
    public void testCreateWithArgs() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt").hasArgs(2);
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("longopt", option.getLongOpt());
        assertEquals(2, option.getArgs().length);
    }

    @Test
    public void testCreateWithType() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt").withType(String[].class);
        Option option = OptionBuilder.create('a');
        assertNotNull(option);
        assertEquals("longopt", option.getLongOpt());
        assertEquals(String[].class, option.getType());
    }
}