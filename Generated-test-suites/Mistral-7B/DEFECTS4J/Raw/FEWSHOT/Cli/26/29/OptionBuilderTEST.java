package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testReset() {
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        OptionBuilder.hasArgs(2);
        OptionBuilder.hasOptionalArgs();
        OptionBuilder.withDescription("Description");
        OptionBuilder.withLongOpt("longopt");
        OptionBuilder.withType(new Integer(1));

        OptionBuilder.reset();

        assertEquals(null, OptionBuilder.longopt);
        assertEquals("arg", OptionBuilder.argName);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertNull(OptionBuilder.type);
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder withLongOpt = optionBuilder.withLongOpt("testLongOpt");

        assertEquals("testLongOpt", withLongOpt.longopt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasArg = optionBuilder.hasArg();

        assertEquals(1, hasArg.numberOfArgs);
    }

    @Test
    public void testHasArg_withArgName() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasArgWithArgName = optionBuilder.hasArg().withArgName("testArgName");

        assertEquals("testArgName", hasArgWithArgName.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder isRequired = optionBuilder.isRequired();

        assertTrue(isRequired.required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasArgs = optionBuilder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, hasArgs.numberOfArgs);
    }

    @Test
    public void testHasArgs_withNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasArgsWithNumArgs = optionBuilder.hasArgs(3);

        assertEquals(3, hasArgsWithNumArgs.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasOptionalArg = optionBuilder.hasOptionalArg();

        assertTrue(hasOptionalArg.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasOptionalArgs = optionBuilder.hasOptionalArgs();

        assertEquals(Option.UNLIMITED_VALUES, hasOptionalArgs.numberOfArgs);
        assertTrue(hasOptionalArgs.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_withNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder hasOptionalArgsWithNumArgs = optionBuilder.hasOptionalArgs(2);

        assertEquals(2, hasOptionalArgsWithNumArgs.numberOfArgs);
        assertTrue(hasOptionalArgsWithNumArgs.optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Object type = new Integer(1);
        OptionBuilder withType = optionBuilder.withType(type);

        assertSame(type, withType.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "Test Description";
        OptionBuilder withDescription = optionBuilder.withDescription(description);

        assertEquals(description, withDescription.description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_noLongOptSet() {
        OptionBuilder.reset();
        OptionBuilder.instance().create();
    }

    @Test
    public void testCreate_withLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withLongOpt("testLongOpt");
        Option option = optionBuilder.create('t');

        assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testCreate_withArgName() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArg().withArgName("testArgName");
        Option option = optionBuilder.create('t');

        assertEquals("testArgName", option.getArgName());
    }

    @Test
    public void testCreate_isRequired() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.isRequired();
        Option option = optionBuilder.create('t');

        assertTrue(option.isRequired());
    }

    @Test
    public void testCreate_hasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('t');

        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testCreate_hasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs();
        Option option = optionBuilder.create('t');

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    @Test
    public void testCreate_hasArgs_withNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs(2);
        Option option = optionBuilder.create('t');

        assertEquals(2, option.getArgs().length);
    }

    @Test
    public void testCreate_withType() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withType(new Integer(1));
        Option option = optionBuilder.create('t');

        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testCreate_withDescription() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withDescription("Test Description");
        Option option = optionBuilder.create('t');

        assertEquals("Test Description", option.getDescription());
    }
}