package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.lang.IllegalArgumentException;

/**
 * JUnit 4 test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @org.junit.Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        Option expectedOption = new Option("optionLongName", "Option long name");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .required(false)
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .required(true)
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(Option.UNLIMITED_VALUES)
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasArgsWithSpecifiedNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(2)
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(1)
                .optionalArg(true)
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(Option.UNLIMITED_VALUES)
                .optionalArg(true)
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testHasOptionalArgsWithSpecifiedNumber()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(3)
                .optionalArg(true)
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(1)
                .type(String.class)
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        Option expectedOption = Option.builder("optionShortName")
                .longOpt("optionLongName")
                .argName("argName")
                .required(false)
                .args(1)
                .type(String.class)
                .description("description")
                .build();
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateWithUnsetLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create();
    }
}