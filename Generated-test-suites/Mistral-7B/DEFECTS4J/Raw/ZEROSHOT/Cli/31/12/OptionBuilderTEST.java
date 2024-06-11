package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionBuilder()
    {
        OptionOptionBuilder = OptionBuilder.withLongOpt("option1")
                                           .isRequired()
                                           .hasArg()
                                           .withArgName("arg1")
                                           .withType(String.class)
                                           .create('o');

        Assert.assertEquals("option1", OptionOptionBuilder.getLongOpt());
        Assert.assertEquals("Option with long option 'option1'", OptionOptionBuilder.getDescription());
        Assert.assertFalse(OptionOptionBuilder.getOptionalArg());
        Assert.assertEquals(1, OptionOptionBuilder.getArgs().numberOfArgs);
        Assert.assertEquals(String.class, OptionOptionBuilder.getType());
        Assert.assertNull(OptionOptionBuilder.getValueSeparator());
        Assert.assertEquals("arg1", OptionOptionBuilder.getArgName());
        Assert.assertTrue(OptionOptionBuilder.isRequired());

        OptionOptionBuilder2 = OptionBuilder.withLongOpt("option2")
                                           .hasArg(false)
                                           .withArgName("arg2")
                                           .withValueSeparator(':')
                                           .create('p');

        Assert.assertEquals("option2", OptionOptionBuilder2.getLongOpt());
        Assert.assertEquals("Option with long option 'option2'", OptionOptionBuilder2.getDescription());
        Assert.assertFalse(OptionOptionBuilder2.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, OptionOptionBuilder2.getArgs().numberOfArgs);
        Assert.assertNull(OptionOptionBuilder2.getType());
        Assert.assertEquals(':', OptionOptionBuilder2.getValueSeparator());
        Assert.assertEquals("arg2", OptionOptionBuilder2.getArgName());
        Assert.assertFalse(OptionOptionBuilder2.getOptionalArg());

        OptionOptionBuilder3 = OptionBuilder.withLongOpt("option3")
                                           .hasArgs()
                                           .withValueSeparator(':')
                                           .create('t');

        Assert.assertEquals("option3", OptionOptionBuilder3.getLongOpt());
        Assert.assertEquals("Option with long option 'option3'", OptionOptionBuilder3.getDescription());
        Assert.assertFalse(OptionOptionBuilder3.isRequired());
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionOptionBuilder3.getArgs().numberOfArgs);
        Assert.assertNull(OptionOptionBuilder3.getType());
        Assert.assertEquals(':', OptionOptionBuilder3.getValueSeparator());
        Assert.assertNull(OptionOptionBuilder3.getArgName());
        Assert.assertTrue(OptionOptionBuilder3.getOptionalArg());
    }
}