package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {

    private OptionBuilder optionBuilder;

    @Before
    public void setup() {
        optionBuilder = new OptionBuilder();
    }

    @Test
    public void testReset() {
        optionBuilder.withLongOpt("longOpt1");
        optionBuilder.hasArg();
        optionBuilder.withArgName("argName1");
        optionBuilder.isRequired();
        optionBuilder.hasArgs(3);
        optionBuilder.withType(String.class);
        optionBuilder.withDescription("description1");
        Option option = optionBuilder.create('o');

        optionBuilder.reset();

        assertNull(optionBuilder.longopt);
        assertEquals("arg", optionBuilder.argName);
        assertNull(optionBuilder.description);
        assertNull(optionBuilder.type);
        assertFalse(optionBuilder.required);
        assertEquals(Option.UNINITIALIZED, optionBuilder.numberOfArgs);
    }

    @Test
    public void testWithLongOpt() {
        Option option = optionBuilder.withLongOpt("longOpt1").create('o');
        assertEquals("longOpt1", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = optionBuilder.create('o');
        assertFalse(option.hasArg());

        optionBuilder.hasArg();
        option = optionBuilder.create('o');
        assertTrue(option.hasArg());
    }

    @Test
    public void testHasArgWithArgName() {
        Option option = optionBuilder.create('o');
        assertFalse(option.hasArg());

        optionBuilder.hasArg();
        optionBuilder.withArgName("argName1");
        option = optionBuilder.create('o');
        assertTrue(option.hasArg());
        assertEquals("argName1", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = optionBuilder.create('o');
        assertFalse(option.isRequired());

        optionBuilder.isRequired();
        option = optionBuilder.create('o');
        assertTrue(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        Option option = optionBuilder.create('o');
        assertEquals(Option.UNINITIALIZED, option.getArgs());

        optionBuilder.hasArgs();
        option = optionBuilder.create('o');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        Option option = optionBuilder.create('o');
        assertEquals(Option.UNINITIALIZED, option.getArgs());

        optionBuilder.hasArgs(3);
        option = optionBuilder.create('o');
        assertEquals(3, option.getArgs().length);
    }

    @Test
    public void testHasOptionalArg() {
        Option option = optionBuilder.create('o');
        assertFalse(option.hasOptionalArg());

        optionBuilder.hasOptionalArg();
        option = optionBuilder.create('o');
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        Option option = optionBuilder.create('o');
        assertFalse(option.hasOptionalArgs());

        optionBuilder.hasOptionalArgs();
        option = optionBuilder.create('o');
        assertTrue(option.hasOptionalArgs());
    }

    @Test
    public void testHasOptionalArgsWithNum() {
        Option option = optionBuilder.create('o');
        assertFalse(option.hasOptionalArgs());

        optionBuilder.hasOptionalArgs(3);
        option = optionBuilder.create('o');
        assertTrue(option.hasOptionalArgs());
        assertEquals(3, option.getOptionalArgs().length);
    }

    @Test
    public void testWithType() {
        Option option = optionBuilder.create('o');
        assertNull(option.getType());

        optionBuilder.withType(Integer.class);
        option = optionBuilder.create('o');
        assertSame(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        Option option = optionBuilder.create('o');
        assertNull(option.getDescription());

        optionBuilder.withDescription("description1");
        option = optionBuilder.create('o');
        assertEquals("description1", option.getDescription());
    }

    @Test
    public void testCreateWithLongOpt() {
        Option option = optionBuilder.withLongOpt("longOpt1").create('o');
        assertEquals("longOpt1", option.getLongOpt());
    }

    @Test
    public void testCreateWithLongOptAndDescription() {
        Option option = optionBuilder.withLongOpt("longOpt1").withDescription("description1").create('o');
        assertEquals("longOpt1", option.getLongOpt());
        assertEquals("description1", option.getDescription());
    }

    @Test
    public void testCreateWithLongOptArgName() {
        Option option = optionBuilder.withLongOpt("longOpt1").withArgName("argName1").create('o');
        assertEquals("longOpt1", option.getLongOpt());
        assertEquals("argName1", option.getArgName());
    }

    @Test
    public void testCreateWithLongOptArgNameDescription() {
        Option option = optionBuilder.withLongOpt("longOpt1").withArgName("argName1").withDescription("description1").create('o');
        assertEquals("longOpt1", option.getLongOpt());
        assertEquals("argName1", option.getArgName());
        assertEquals("description1", option.getDescription());
    }

    @Test
    public void testCreateWithShortOptRequired() {
        Option option = optionBuilder.isRequired().create('o');
        assertTrue(option.isRequired());
    }

    @Test
    public void testCreateWithShortOptNoArg() {
        Option option = optionBuilder.create('o');
        assertFalse(option.hasArg());
    }

    @Test
    public void testCreateWithShortOptOneArg() {
        Option option = optionBuilder.hasArg().create('o');
        assertTrue(option.hasArg());
    }

    @Test
    public void testCreateWithShortOptOneArgName() {
        Option option = optionBuilder.hasArg().withArgName("argName1").create('o');
        assertTrue(option.hasArg());
        assertEquals("argName1", option.getArgName());
    }

    @Test
    public void testCreateWithShortOptOneArgNameDescription() {
        Option option = optionBuilder.hasArg().withArgName("argName1").withDescription("description1").create('o');
        assertTrue(option.hasArg());
        assertEquals("argName1", option.getArgName());
        assertEquals("description1", option.getDescription());
    }

    @Test
    public void testCreateWithShortOptMultipleArgs() {
        Option option = optionBuilder.hasArgs(3).create('o');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testCreateWithShortOptMultipleArgsValueSeparator() {
        Option option = optionBuilder.hasArgs(3).withValueSeparator(':').create('o');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateWithShortOptOptionalArg() {
        Option option = optionBuilder.hasOptionalArg().create('o');
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testCreateWithShortOptOptionalArgs() {
        Option option = optionBuilder.hasOptionalArgs(3).create('o');
        assertTrue(option.hasOptionalArgs());
        assertEquals(3, option.getOptionalArgs().length);
    }

    @Test
    public void testCreateWithShortOptOptionalArgsValueSeparator() {
        Option option = optionBuilder.hasOptionalArgs(3).withValueSeparator(':').create('o');
        assertTrue(option.hasOptionalArgs());
        assertEquals(3, option.getOptionalArgs().length);
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateWithShortOptType() {
        Option option = optionBuilder.withType(String[].class).create('o');
        assertEquals(String[].class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithShortOptNoLongOpt() {
        optionBuilder.reset();
        optionBuilder.create('o');
    }

}