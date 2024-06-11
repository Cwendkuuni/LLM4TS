package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.longopt);
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(0, OptionBuilder.valuesep);
        assertEquals(null, OptionBuilder.instance);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt");
        assertEquals("longOpt", OptionBuilder.longopt);
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(0, OptionBuilder.valuesep);
        assertEquals(builder, OptionBuilder.instance);
    }

    @Test
    public void testHasArg() {
        OptionBuilder builder = OptionBuilder.hasArg();
        assertEquals("longOpt", OptionBuilder.longopt);
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(0, OptionBuilder.valuesep);
        assertEquals(builder, OptionBuilder.instance);
    }

    @Test
    public void testHasArgWithDescription() {
        OptionBuilder builder = OptionBuilder.hasArg().withDescription("Description");
        assertEquals("longOpt", OptionBuilder.longopt);
        assertEquals("Description", OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(0, OptionBuilder.valuesep);
        assertEquals(builder, OptionBuilder.instance);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder builder = OptionBuilder.isRequired();
        assertEquals("longOpt", OptionBuilder.longopt);
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(true, OptionBuilder.required);
        assertEquals(0, OptionBuilder.valuesep);
        assertEquals(builder, OptionBuilder.instance);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.hasOptionalArg();
        assertEquals("longOpt", OptionBuilder.longopt);
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(0, OptionBuilder.valuesep);
        assertEquals(true, OptionBuilder.optionalArg);
        assertEquals(builder, OptionBuilder.instance);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder builder = OptionBuilder.hasArgs();
        assertEquals("longOpt", OptionBuilder.longopt);
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(0, OptionBuilder.valuesep);
        assertEquals(builder, OptionBuilder.instance);
    }

    @Test
    public void testHasArgsWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.hasArgs().withValueSeparator(';');
        assertEquals("longOpt", OptionBuilder.longopt);
        assertEquals(null, OptionBuilder.description);
        assertEquals(null, OptionBuilder.argName);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(null, OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(';', OptionBuilder.valuesep);
        assertEquals(builder, OptionBuilder.instance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreate() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt").hasArg().withDescription("Description");
        Option option = builder.create('c');
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Description", option.getDescription());
        assertEquals(null, option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertEquals(null, option.getType());
        assertEquals(false, option.isRequired());
        assertEquals(0, option.getValueSeparator());
        assertEquals(null, option.getValue(0));
    }

    @Test
    public void testCreateWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt").hasArg().withDescription("Description").withValueSeparator(';');
        Option option = builder.create('c');
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Description", option.getDescription());
        assertEquals(null, option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertEquals(null, option.getType());
        assertEquals(false, option.isRequired());
        assertEquals(';', option.getValueSeparator());
        String[] values = {"value1", "value2"};
        option.setValue(values);
        assertArrayEquals(values, option.getValues());
    }

    @Test
    public void testCreateWithArgName() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt").hasArg().withDescription("Description").withArgName("argName");
        Option option = builder.create('c');
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Description", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertEquals(null, option.getType());
        assertEquals(false, option.isRequired());
        assertEquals(0, option.getValueSeparator());
        String argValue = "argValue";
        option.setValue(argValue);
        assertEquals(argValue, option.getValue(0));
    }

    @Test
    public void testCreateWithOptionalArg() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt").hasOptionalArg();
        Option option = builder.create('c');
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(null, option.getDescription());
        assertEquals(null, option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertEquals(null, option.getType());
        assertEquals(false, option.isRequired());
        assertEquals(0, option.getValueSeparator());
    }

    @Test
    public void testCreateWithOptionalArgs() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt").hasOptionalArgs();
        Option option = builder.create('c');
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(null, option.getDescription());
        assertEquals(null, option.getArgName());
        assertEquals(Option.UNLIMITED_VALUES, option.getNumberOfArgs());
        assertEquals(null, option.getType());
        assertEquals(false, option.isRequired());
        assertEquals(0, option.getValueSeparator());
        String[] values = {"value1", "value2"};
        option.setValue(values);
        assertArrayEquals(values, option.getValues());
    }

    @Test
    public void testCreateWithType() {
        OptionBuilder builder = OptionBuilder.withLongOpt("longOpt").hasArg().withDescription("Description").withType(String.class);
        Option option = builder.create('c');
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Description", option.getDescription());
        assertEquals(null, option.getArgName());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertEquals(String.class, option.getType());
        assertEquals(false, option.isRequired());
        assertEquals(0, option.getValueSeparator());
        String argValue = "argValue";
        option.setValue(argValue);
        assertEquals(argValue, option.getValue(0));
    }

}