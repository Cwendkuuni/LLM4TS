package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        Option expectedOption = new Option(null, "optionLongName", false, null);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create();
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .build();
        Option actualOption = optionBuilder.create('optionShortName');
        Assert.assertEquals(expectedOption, actualOption);
        Assert.assertNotNull(actualOption.getValue());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(true)
                .build();
        Option actualOption = optionBuilder.create('optionShortName');
        Assert.assertEquals(expectedOption, actualOption);
        Assert.assertNull(actualOption.getValue());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .build();
        Option actualOption = optionBuilder.create('optionShortName');
        Assert.assertEquals(expectedOption, actualOption);
        Assert.assertNull(actualOption.getValue());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(';');
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .valueSeparator(';')
                .build();
        Option actualOption = optionBuilder.create('optionShortName');
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(Option.UNLIMITED_VALUES)
                .build();
        Option actualOption = optionBuilder.create('optionShortName');
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(-1);
        optionBuilder.create('optionShortName');
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(Option.UNINITIALIZED)
                .type(String.class)
                .build();
        Option actualOption = optionBuilder.create('optionShortName');
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("option description");
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(Option.UNINITIALIZED)
                .build().desc("option description");
        Option actualOption = optionBuilder.create('optionShortName');
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testCreateWithLongOptAndShortOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName")
                .withShortOpt('o');
        Option expectedOption = Option.builder("o")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .build();
        Option actualOption = optionBuilder.create('o');
        Assert.assertEquals(expectedOption, actualOption);
    }

    
}