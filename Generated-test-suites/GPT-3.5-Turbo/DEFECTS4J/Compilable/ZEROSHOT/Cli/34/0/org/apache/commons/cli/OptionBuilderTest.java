package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOpt");
        Option option = OptionBuilder.create();
        Assert.assertEquals("testOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        Assert.assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithArg() {
        OptionBuilder.hasArg(true);
        Option option = OptionBuilder.create();
        Assert.assertEquals(1, option.getArgs());
    }

    @Test
    public void testHasArgWithoutArg() {
        OptionBuilder.hasArg(false);
        Option option = OptionBuilder.create();
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArg");
        Option option = OptionBuilder.create();
        Assert.assertEquals("testArg", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testWithSeparator() {
        OptionBuilder.withValueSeparator(':');
        Option option = OptionBuilder.create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithDefaultSeparator() {
        OptionBuilder.withValueSeparator();
        Option option = OptionBuilder.create();
        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testIsRequiredWithRequired() {
        OptionBuilder.isRequired(true);
        Option option = OptionBuilder.create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequiredWithNotRequired() {
        OptionBuilder.isRequired(false);
        Option option = OptionBuilder.create();
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        Option option = OptionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum() {
        OptionBuilder.hasArgs(3);
        Option option = OptionBuilder.create();
        Assert.assertEquals(3, option.getArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        Option option = OptionBuilder.create();
        Assert.assertEquals(1, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        Option option = OptionBuilder.create();
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(2);
        Option option = OptionBuilder.create();
        Assert.assertEquals(2, option.getArgs());
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        Option option = OptionBuilder.create();
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("testDescription");
        Option option = OptionBuilder.create();
        Assert.assertEquals("testDescription", option.getDescription());
    }

    @Test
    public void testCreateWithChar() {
        Option option = OptionBuilder.create('t');
        Assert.assertEquals("t", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder.withLongOpt("testOpt");
        Option option = OptionBuilder.create();
        Assert.assertEquals("testOpt", option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullOpt() {
        OptionBuilder.create(null);
    }

    @Test
    public void testCreateWithOpt() {
        Option option = OptionBuilder.create("testOpt");
        Assert.assertEquals("testOpt", option.getOpt());
    }

}