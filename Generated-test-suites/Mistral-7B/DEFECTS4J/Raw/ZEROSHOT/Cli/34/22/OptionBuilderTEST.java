package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{

    /**
     * Test OptionBuilder withLongOpt method.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.longopt = "optionLong";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder hasArg method.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder hasArg method with false argument.
     */
    @Test
    public void testHasArgFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNINITIALIZED;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder withArgName method.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.argName = "argName";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder isRequired method.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.required = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder withValueSeparator method.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(';');
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.valuesep = ';';
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder withValueSeparator method with empty argument.
     */
    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.valuesep = '=';
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder isRequired method with false argument.
     */
    @Test
    public void testIsRequiredFalse()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.required = false;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder hasArgs method.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNLIMITED_VALUES;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder hasArgs method with specific number of args.
     */
    @Test
    public void testHasArgsSpecific()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 2;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder hasOptionalArg method.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 1;
        expectedOptionBuilder.optionalArg = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder hasOptionalArgs method.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = Option.UNLIMITED_VALUES;
        expectedOptionBuilder.optionalArg = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder hasOptionalArgs method with specific number of optional args.
     */
    @Test
    public void testHasOptionalArgsSpecific()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.numberOfArgs = 3;
        expectedOptionBuilder.optionalArg = true;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder withType method.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.TYPE);
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.type = Integer.TYPE;
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test OptionBuilder withDescription method.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        OptionBuilder expectedOptionBuilder = new OptionBuilder();
        expectedOptionBuilder.description = "description";
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    /**
     * Test create method with long opt and description.
     */
    @Test
    public void testCreateWithLongOptAndDescription()
    {
        String longOpt = "longOpt";
        String description = "description";
        Option option = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .create();
        Option expectedOption = new Option(longOpt, description);
        assertEquals(expectedOption, option);
    }

    /**
     * Test create method with long opt.
     */
    @Test
    public void testCreateWithLongOpt()
    {
        String longOpt = "longOpt";
        Option option = OptionBuilder.withLongOpt(longOpt).create();
        Option expectedOption = new Option(longOpt, null);
        assertEquals(expectedOption, option);
    }

    /**
     * Test create method with char opt and description.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithCharOptAndDescription()
    {
        char opt = 'opt';
        String description = "description";
        OptionBuilder.withLongOpt(null);
        Option option = OptionBuilder.create(opt).withDescription(description).create();
    }

    /**
     * Test create method with char opt.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithCharOpt()
    {
        char opt = 'opt';
        OptionBuilder.withLongOpt(null);
        Option option = OptionBuilder.create(opt).create();
    }

    /**
     * Test create method with description.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithDescription()
    {
        String description = "description";
        OptionBuilder.withLongOpt(null);
        Option option = OptionBuilder.create().withDescription(description).create();
    }

    /**
     * Test create method without long opt or description.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOptOrDescription()
    {
        Option option = OptionBuilder.create().create();
    }
}