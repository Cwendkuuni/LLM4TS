package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = new Option("longOpt", "option description");
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder("arg").required(false).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder("arg").required(true).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder("arg").required(false).build();
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("arg", "option description")
                .argName("arg")
                .required(false)
                .valueSeparator(' ')
                .numberOfArgs(Option.UNLIMITED_VALUES);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasArgsWithSpecifiedNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = new Option("arg", "option description")
                .argName("arg")
                .required(false)
                .valueSeparator(' ')
                .numberOfArgs(2);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option expectedOption = new Option("arg", "option description")
                .argName("arg")
                .required(false)
                .valueSeparator(':')
                .numberOfArgs(1);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option expectedOption = new Option("arg", "option description")
                .argName("argName")
                .required(false)
                .valueSeparator(' ')
                .numberOfArgs(1);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testIsRequiredWithSpecifiedValue() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired().withDescription("option description");
        Option expectedOption = new Option("arg", "option description")
                .argName("arg")
                .required(true)
                .valueSeparator(' ')
                .numberOfArgs(1);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = new Option("arg", "option description")
                .argName("arg")
                .required(false)
                .valueSeparator(' ')
                .numberOfArgs(Option.UNLIMITED_VALUES);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testHasOptionalArgsWithSpecifiedNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option expectedOption = new Option("arg", "option description")
                .argName("arg")
                .required(false)
                .valueSeparator(' ')
                .numberOfArgs(3);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = new Option("arg", "option description")
                .argName("arg")
                .required(false)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .type(String.class);
        Option actualOption = optionBuilder.create();
        Assert.assertEquals(expectedOption, actualOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.reset();
        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(-1);
        optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator((char) 0);
        optionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(new Object());
        optionBuilder.create();
    }

    @Test
    public void testCreateMultipleOptions() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("opt1")
                .hasArg()
                .withLongOpt("opt2")
                .hasOptionalArg()
                .withLongOpt("opt3")
                .hasArgs(3);
        Option[] options = new Option[3];
        options[0] = optionBuilder.create('o', "opt1");
        options[1] = optionBuilder.create('o', "opt2");
        options[2] = optionBuilder.create('o', "opt3");

        Assert.assertEquals(3, options.length);
        Arrays.stream(options).forEach(option -> {
            Assert.assertEquals("arg", option.getArgName());
            Assert.assertEquals(false, option.isRequired());
            Assert.assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        });
    }

    
}