package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {
    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option actualOption = optionBuilder.create();
        String expectedLongOpt = "optionLong";
        Assert.assertEquals(expectedLongOpt, actualOption.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test
    public void testCreateWithDescription() {
        String description = "This is an option description.";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(description);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(description, actualOption.getDescription());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option actualOption = optionBuilder.create();
        int expectedNumberOfArgs = 1;
        Assert.assertEquals(expectedNumberOfArgs, actualOption.getArgs().length);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option actualOption = optionBuilder.create();
        boolean expectedOptionalArg = true;
        Assert.assertEquals(expectedOptionalArg, actualOption.isOptionalArg());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option actualOption = optionBuilder.create();
        int expectedNumberOfArgs = Option.UNLIMITED_VALUES;
        Assert.assertEquals(expectedNumberOfArgs, actualOption.getArgs().length);
    }

    @Test
    public void testCreateWithArgName() {
        String argName = "argName";
        OptionBuilder optionBuilder = OptionBuilder.withArgName(argName);
        Option actualOption = optionBuilder.create();
        String expectedArgName = argName;
        Assert.assertEquals(expectedArgName, actualOption.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option actualOption = optionBuilder.create();
        boolean expectedRequired = true;
        Assert.assertEquals(expectedRequired, actualOption.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        char valueSeparator = '_';
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(valueSeparator);
        Option actualOption = optionBuilder.create();
        char expectedValueSeparator = valueSeparator;
        Assert.assertEquals(expectedValueSeparator, actualOption.getValueSeparator());
    }

    @Test
    public void testHasArgsWithSpecifiedNumber() {
        int numArgs = 2;
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(numArgs);
        Option actualOption = optionBuilder.create();
        int expectedNumberOfArgs = numArgs;
        Assert.assertEquals(expectedNumberOfArgs, actualOption.getArgs().length);
    }

    @Test
    public void testWithType() {
        Class<Integer> intType = Integer.class;
        OptionBuilder optionBuilder = OptionBuilder.withType(intType);
        Option actualOption = optionBuilder.create();
        Class<Integer> expectedType = intType;
        Assert.assertEquals(expectedType, actualOption.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithUninitializedNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.create();
    }

    
}