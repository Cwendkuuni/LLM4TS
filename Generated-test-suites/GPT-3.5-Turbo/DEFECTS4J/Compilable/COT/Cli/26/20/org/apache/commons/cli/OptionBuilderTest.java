package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("longopt").create();
        Assert.assertEquals("longopt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        Assert.assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgBoolean() {
        Option option = OptionBuilder.hasArg(true).create();
        Assert.assertEquals(1, option.getArgs());
        option = OptionBuilder.hasArg(false).create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("arg").create();
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithValueSeparatorChar() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator().create();
        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredBoolean() {
        Option option = OptionBuilder.isRequired(true).create();
        Assert.assertTrue(option.isRequired());
        option = OptionBuilder.isRequired(false).create();
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        Option option = OptionBuilder.hasArgs().create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsInt() {
        Option option = OptionBuilder.hasArgs(2).create();
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = OptionBuilder.hasOptionalArg().create();
        Assert.assertEquals(1, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        Option option = OptionBuilder.hasOptionalArgs().create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsInt() {
        Option option = OptionBuilder.hasOptionalArgs(2).create();
        Assert.assertEquals(2, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(String.class).create();
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("description").create();
        Assert.assertEquals("description", option.getDescription());
    }

    @Test
    public void testCreateChar() {
        Option option = OptionBuilder.create('c');
        Assert.assertEquals("c", option.getOpt());
    }

    @Test
    public void testCreateNull() {
        try {
            OptionBuilder.create();
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("must specify longopt", e.getMessage());
        }
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("opt");
        Assert.assertEquals("opt", option.getOpt());
    }
}