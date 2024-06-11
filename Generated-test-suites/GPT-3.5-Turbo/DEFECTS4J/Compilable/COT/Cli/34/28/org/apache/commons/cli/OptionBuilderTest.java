package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = OptionBuilder.hasArg().create();
        Assert.assertEquals(1, option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testHasArgWithFalse() {
        Option option = OptionBuilder.hasArg(false).create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testWithArgName() {
        Option option = OptionBuilder.withArgName("testArgName").create();
        Assert.assertEquals("testArgName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        Option option = OptionBuilder.isRequired().create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithDefaultValueSeparator() {
        Option option = OptionBuilder.withValueSeparator().create();
        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparator() {
        Option option = OptionBuilder.withValueSeparator(':').create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testIsOptionalArg() {
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
    public void testHasOptionalArgsWithNum() {
        Option option = OptionBuilder.hasOptionalArgs(2).create();
        Assert.assertEquals(2, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        Option option = OptionBuilder.withType(Integer.class).create();
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        Option option = OptionBuilder.withDescription("testDescription").create();
        Assert.assertEquals("testDescription", option.getDescription());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithOpt() {
        OptionBuilder.create('t');
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreate() {
        Option option = OptionBuilder.withLongOpt("testLongOpt").create();
        Assert.assertEquals("testLongOpt", option.getLongOpt());
    }

    @Test
    public void testCreateWithOptString() {
        Option option = OptionBuilder.create("t");
        Assert.assertEquals("t", option.getOpt());
    }
}