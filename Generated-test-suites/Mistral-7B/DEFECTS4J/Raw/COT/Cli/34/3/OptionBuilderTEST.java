package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        Assert.assertEquals("optionLongName", optionBuilder.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithLongOptNull() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        // An assertion error is thrown when the test case fails
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Assert.assertEquals(Option.UNINITIALIZED + 1, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgFalse() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
        Assert.assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        Assert.assertEquals("argName", optionBuilder.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasArgNameNull() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName(null);
        // An assertion error is thrown when the test case fails
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Assert.assertTrue(optionBuilder.isRequired());
    }

    @Test
    public void testIsRequiredFalse() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
        Assert.assertFalse(optionBuilder.isRequired());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Assert.assertEquals(':', optionBuilder.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        Assert.assertEquals('=', optionBuilder.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgsNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        Assert.assertEquals(5, optionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Assert.assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Assert.assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Assert.assertEquals(3, optionBuilder.getNumberOfArgs());
        Assert.assertTrue(optionBuilder.isOptionalArg());
    }

    @Test
    public void testWithType() {
        String expectedClass = String.class.getName();
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Assert.assertEquals(expectedClass, optionBuilder.getType().getName());
    }

    @Test
    public void testWithDescription() {
        String expectedDescription = "option description";
        OptionBuilder optionBuilder = OptionBuilder.withDescription(expectedDescription);
        Assert.assertEquals(expectedDescription, optionBuilder.getDescription());
    }

    @Test
    public void testCreateWithChar() throws IllegalArgumentException {
        String longOpt = "optionLongName";
        String description = "option description";
        Option option = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .hasArg()
                .create('o');

        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertEquals(description, option.getDescription());
        Assert.assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
        Assert.assertFalse(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMissingLongOpt() throws IllegalArgumentException {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create('o');
        // An assertion error is thrown when the test case fails
    }

    @Test
    public void testCreateWithString() throws IllegalArgumentException {
        String longOpt = "optionLongName";
        String description = "option description";
        Option option = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .hasArg()
                .create(longOpt);

        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertEquals(description, option.getDescription());
        Assert.assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testCreateWithInvalidChar() throws IllegalArgumentException {
        String longOpt = "optionLongName";

        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(longOpt)
                .hasArg();

        List<Character> invalidChars = new ArrayList<>();
        invalidChars.add((char) 0);
        invalidChars.add((char) 32);

        for (char c : invalidChars) {
            try {
                Option option = optionBuilder.create(c);
                // An assertion error is thrown when the test case fails
            } catch (IllegalArgumentException e) {
                continue;
            }
            Assert.fail("IllegalArgumentException not thrown for invalid char " + c);
        }
    }

    @Test
    public void testCreateWithLongOptAndDescriptionNull() throws IllegalArgumentException {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLongName");
        Option option = optionBuilder.create('o');

        Assert.assertEquals("optionLongName", option.getLongOpt());
        Assert.assertNull(option.getDescription());
    }

    @Test
    public void testCreateWithArg() throws IllegalArgumentException {
        String longOpt = "optionLongName";
        String description = "option description";
        String argName = "argName";

        Option option = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .withArgName(argName)
                .isRequired()
                .hasArg()
                .create('o');

        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertEquals(description, option.getDescription());
        Assert.assertEquals(argName, option.getArgName());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
    }

    @Test
    public void testCreateWithEmptyArgName() throws IllegalArgumentException {
        String longOpt = "optionLongName";
        String description = "option description";

        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .withArgName("")
                .isRequired()
                .hasArg();

        Option option = optionBuilder.create('o');

        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertEquals(description, option.getDescription());
        Assert.assertNull(option.getArgName());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED + 1, option.getArgCount());
    }

    @Test
    public void testCreateWithOptionalArg() throws IllegalArgumentException {
        String longOpt = "optionLongName";
        String description = "option description";

        Option option = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .isOptionalArg()
                .create('o');

        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertEquals(description, option.getDescription());
        Assert.assertTrue(option.isOptionalArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgCount());
    }

    @Test
    public void testCreateWithOptionalArgs() throws IllegalArgumentException {
        String longOpt = "optionLongName";
        String description = "option description";

        Option option = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .isOptionalArg()
                .hasOptionalArgs(2)
                .create('o');

        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertEquals(description, option.getDescription());
        Assert.assertTrue(option.isOptionalArg());
        Assert.assertEquals(2, option.getArgCount());
    }

    @Test
    public void testCreateWithType() throws IllegalArgumentException {
        String longOpt = "optionLongName";
        String description = "option description";
        boolean isOptionalArg = false;
        int numberOfArgs = Option.UNINITIALIZED + 1;
        Class<?> type = Integer.class;

        Option option = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .isOptionalArg(isOptionalArg)
                .hasArgs(numberOfArgs)
                .withType(type)
                .create('o');

        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertEquals(description, option.getDescription());
        Assert.assertEquals(isOptionalArg, option.isOptionalArg());
        Assert.assertEquals(numberOfArgs, option.getArgCount());
        Assert.assertEquals(type, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType() throws IllegalArgumentException {
        String longOpt = "optionLongName";
        String description = "option description";
        boolean isOptionalArg = false;
        int numberOfArgs = Option.UNINITIALIZED + 1;
        String invalidType = "java.lang.String";

        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(longOpt)
                .withDescription(description)
                .isOptionalArg(isOptionalArg)
                .hasArgs(numberOfArgs)
                .withType(Class.forName(invalidType));

        Option option = optionBuilder.create('o');
        // An assertion error is thrown when the test case fails
    }

    @Test
    public void testReset() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create('o');

        Assert.assertNull(optionBuilder.getLongOpt());
        Assert.assertNull(optionBuilder.getDescription());
        Assert.assertNull(optionBuilder.getArgName());
        Assert.assertFalse(optionBuilder.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        Assert.assertNull(optionBuilder.getType());
        Assert.assertFalse(optionBuilder.isOptionalArg());
        Assert.assertNull(optionBuilder.getValueSeparator());

        optionBuilder.reset();

        Assert.assertNull(optionBuilder.getLongOpt());
        Assert.assertNull(optionBuilder.getDescription());
        Assert.assertNull(optionBuilder.getArgName());
        Assert.assertFalse(optionBuilder.isRequired());
        Assert.assertEquals(Option.UNINITIALIZED, optionBuilder.getNumberOfArgs());
        Assert.assertNull(optionBuilder.getType());
        Assert.assertFalse(optionBuilder.isOptionalArg());
        Assert.assertNull(optionBuilder.getValueSeparator());
    }

    
}