package org.apache.commons.cli.test;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli.Option.*;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderWithLongOptAndDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt")
                .withDescription("This is a test option");

        Option option = optionBuilder.create();

        Assert.assertEquals("longOpt", option.getLongOpt());
        Assert.assertEquals("This is a test option", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");

        Option option = optionBuilder.create();

        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();

        Option option = optionBuilder.create();

        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderWithOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();

        Option option = optionBuilder.create();

        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');

        Option option = optionBuilder.create();

        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);

        Option option = optionBuilder.create();

        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testOptionBuilderWithMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();

        optionBuilder.create();
    }

    @Test
    public void testOptionBuilderWithOptionalArgAndValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();

        Option option = optionBuilder.create();

        option.setValue("testValue");

        Assert.assertEquals("testValue", option.getValue());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderWithMultiValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(';');

        Option option = optionBuilder.create();

        option.setValue("testValue1");
        option.setValue("testValue2");

        Assert.assertEquals("testValue1;testValue2", option.getValueString());
    }

    @Test
    public void testOptionBuilderWithUnlimitedArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();

        Option option = optionBuilder.create();

        option.setValue("testValue1");
        option.setValue("testValue2");

        Assert.assertEquals(2, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderWithRequiredArg() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();

        Option option = optionBuilder.create();

        Assert.assertFalse(option.isOptional());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithInvalidValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator('A');

        optionBuilder.create();
    }

    @Test
    public void testOptionBuilderWithUnlimitedOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();

        Option option = optionBuilder.create();

        option.setValue("testValue1");
        option.setValue("testValue2");
        option.setValue("testValue3");

        Assert.assertArrayEquals(new String[]{"testValue1", "testValue2", "testValue3"}, option.getArgs());
    }

    @Test
    public void testOptionBuilderWithCustomArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("myArgName");

        Option option = optionBuilder.create();

        Assert.assertEquals("myArgName", option.getArgName());
    }
}