package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();
        assertEquals(null, OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.type);
        assertEquals(false, OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.longopt = "myOption";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArgWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
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
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(';');
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.valuesep = ';';
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
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNLIMITED_VALUES;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test
    public void testHasArgsWithNumArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 5;
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
    public void testHasOptionalArgsWithNumArgs() {
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
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Option description");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.description = "Option description";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testOption");
        OptionBuilder.reset();
        Option expectedOption = OptionBuilder.create('t');
        assertEquals("testOption", expectedOption.getLongOpt());
        assertEquals("testOption", expectedOption.getOpt());
        assertEquals("test option", expectedOption.getDescription());
        assertEquals("test", expectedOption.getOpt());
        assertEquals("testOption", expectedOption.getName());
        assertEquals("arg", expectedOption.getArgName());
        assertEquals(false, expectedOption.isRequired());
        assertEquals(Option.UNINITIALIZED, expectedOption.getArgs());
        assertNull(expectedOption.getType());
        assertEquals(' ', expectedOption.getValueSeparator());
        assertNull(expectedOption.getValue(0));
    }

    @Test
    public void testCreateWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName")
                .withLongOpt("testOption");
        OptionBuilder.reset();
        Option expectedOption = OptionBuilder.create('t');
        assertEquals("testOption", expectedOption.getLongOpt());
        assertEquals("test option", expectedOption.getDescription());
        assertEquals("test", expectedOption.getOpt());
        assertEquals("testOption", expectedOption.getName());
        assertEquals("argName", expectedOption.getArgName());
        assertEquals(false, expectedOption.isRequired());
        assertEquals(Option.UNINITIALIZED, expectedOption.getArgs());
        assertNull(expectedOption.getType());
        assertNull(expectedOption.getValue(0));
    }

    @Test
    public void testCreateWithArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg()
                .withLongOpt("testOption");
        OptionBuilder.reset();
        Option expectedOption = OptionBuilder.create('t');
        assertEquals("testOption", expectedOption.getLongOpt());
        assertEquals("test option", expectedOption.getDescription());
        assertEquals("test", expectedOption.getOpt());
        assertEquals("testOption", expectedOption.getName());
        assertEquals("arg", expectedOption.getArgName());
        assertEquals(true, expectedOption.isRequired());
        assertEquals(1, expectedOption.getArgs());
        assertNull(expectedOption.getType());
        assertEquals("arg", expectedOption.getValue(0));
    }

    @Test
    public void testCreateWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',')
                .withLongOpt("testOption");
        OptionBuilder.reset();
        Option expectedOption = OptionBuilder.create('t');
        assertEquals("testOption", expectedOption.getLongOpt());
        assertEquals("test option", expectedOption.getDescription());
        assertEquals("test", expectedOption.getOpt());
        assertEquals("testOption", expectedOption.getName());
        assertEquals("arg", expectedOption.getArgName());
        assertEquals(false, expectedOption.isRequired());
        assertEquals(Option.UNINITIALIZED, expectedOption.getArgs());
        assertEquals(',', expectedOption.getValueSeparator());
        assertNull(expectedOption.getType());
        assertNull(expectedOption.getValue(0));
    }

    @Test
    public void testCreateWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class)
                .withLongOpt("testOption");
        OptionBuilder.reset();
        Option expectedOption = OptionBuilder.create('t');
        assertEquals("testOption", expectedOption.getLongOpt());
        assertEquals("test option", expectedOption.getDescription());
        assertEquals("test", expectedOption.getOpt());
        assertEquals("testOption", expectedOption.getName());
        assertEquals("arg", expectedOption.getArgName());
        assertEquals(false, expectedOption.isRequired());
        assertEquals(Option.UNINITIALIZED, expectedOption.getArgs());
        assertEquals(Integer.class, expectedOption.getType());
        assertNull(expectedOption.getValue(0));
    }

    @Test
    public void testCreateWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Option")
                .withLongOpt("testOption");
        OptionBuilder.reset();
        Option expectedOption = OptionBuilder.create('t');
        assertEquals("testOption", expectedOption.getLongOpt());
        assertEquals("Test Option", expectedOption.getDescription());
        assertEquals("test", expectedOption.getOpt());
        assertEquals("testOption", expectedOption.getName());
        assertEquals("arg", expectedOption.getArgName());
        assertEquals(false, expectedOption.isRequired());
        assertEquals(Option.UNINITIALIZED, expectedOption.getArgs());
        assertNull(expectedOption.getType());
        assertNull(expectedOption.getValue(0));
    }
}