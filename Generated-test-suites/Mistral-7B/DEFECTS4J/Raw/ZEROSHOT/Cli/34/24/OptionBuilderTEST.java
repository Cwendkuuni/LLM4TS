package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionBuilder() throws Exception
    {
        Option option = null;

        // test withLongOpt
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("longOpt");
        option = optBuilder.create('l');
        Assert.assertEquals("longOpt", option.getLongOpt());

        // test hasArg
        optBuilder = OptionBuilder.hasArg();
        option = optBuilder.create('h');
        Assert.assertEquals(1, option.getArgs().length);

        // test hasArg with argument name
        optBuilder = OptionBuilder.hasArg().withArgName("help");
        option = optBuilder.create('h');
        Assert.assertEquals("help", option.getArgName());

        // test isRequired
        optBuilder = OptionBuilder.isRequired();
        option = optBuilder.create('r');
        Assert.assertTrue(option.isRequired());

        // test withValueSeparator
        optBuilder = OptionBuilder.withValueSeparator(':');
        option = optBuilder.create('D');
        Assert.assertEquals(':', option.getValueSeparator());

        // test withValueSeparator with no argument
        optBuilder = OptionBuilder.withValueSeparator();
        option = optBuilder.create('D');
        Assert.assertEquals('=', option.getValueSeparator());

        // test isRequired and hasArgs
        optBuilder = OptionBuilder.isRequired().hasArgs(2);
        option = optBuilder.create('i');
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(2, option.getArgs().length);

        // test hasOptionalArg
        optBuilder = OptionBuilder.hasOptionalArg();
        option = optBuilder.create('o');
        Assert.assertTrue(option.hasOptionalArg());

        // test hasOptionalArgs
        optBuilder = OptionBuilder.hasOptionalArgs(3);
        option = optBuilder.create('x');
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(3, option.getArgs().length);

        // test withType
        Class<String> stringClass = String.class;
        optBuilder = OptionBuilder.withType(String.class);
        option = optBuilder.create('s');
        Assert.assertEquals(String.class, option.getType());

        // test withDescription
        String description = "description";
        optBuilder = OptionBuilder.withDescription(description);
        option = optBuilder.create('d');
        Assert.assertEquals(description, option.getDescription());

        // test create with no settings
        optBuilder = OptionBuilder.getInstance();
        option = optBuilder.create('t');
        Assert.assertNull(option.getLongOpt());
        Assert.assertNull(option.getDescription());
        Assert.assertNull(option.getArgName());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValueSeparator());
        Assert.assertArrayEquals(new String[0], option.getArgs());

        // test create with all settings
        optBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("description")
                .isRequired()
                .hasArgs(2)
                .withValueSeparator(':')
                .withArgName("argName")
                .hasOptionalArg()
                .withType(int.class);
        option = optBuilder.create('a');
        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("description", option.getDescription());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals(int.class, option.getType());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertArrayEquals(new String[2], option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException1() throws Exception
    {
        OptionBuilder.getInstance().create('x');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException2() throws Exception
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("longOpt");
        optBuilder.create('x');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException3() throws Exception
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("longOpt");
        optBuilder.create('l');
        optBuilder.create('l');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException4() throws Exception
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("longOpt");
        optBuilder.isRequired().hasArgs().create('l');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException5() throws Exception
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("longOpt");
        optBuilder.hasOptionalArg().create('l');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException6() throws Exception
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("longOpt");
        optBuilder.create('l').create('l');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException7() throws Exception
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("longOpt");
        optBuilder.isRequired().hasArgs(0).create('l');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException8() throws Exception
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("longOpt");
        optBuilder.hasOptionalArg().create('l');
        optBuilder.create('l');
    }
}