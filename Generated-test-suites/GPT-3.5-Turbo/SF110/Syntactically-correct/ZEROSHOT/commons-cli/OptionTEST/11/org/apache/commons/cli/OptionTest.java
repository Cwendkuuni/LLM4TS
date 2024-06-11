package org.apache.commons.cli;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() throws Exception {
        option = new Option("o", "option", true, "Option for testing");
    }

    @Test
    public void testGetOpt() {
        Assert.assertEquals("o", option.getOpt());
    }

    @Test
    public void testGetType() {
        Assert.assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        option.setType(String.class);
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Assert.assertEquals("option", option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("newOption");
        Assert.assertEquals("newOption", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Assert.assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Assert.assertTrue(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("Option for testing", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("New description");
        Assert.assertEquals("New description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testSetRequired() {
        option.setRequired(true);
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        option.setArgName("newArg");
        Assert.assertEquals("newArg", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        option.setArgName("arg");
        Assert.assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        option.setArgs(2);
        Assert.assertTrue(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        option.setArgs(2);
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        option.setValueSeparator(':');
        Assert.assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        option.setArgs(3);
        Assert.assertEquals(3, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        Option noArgsOption = new Option("n", "noargs", false, "Option with no args");
        noArgsOption.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessingWithArgs() {
        Option multiArgOption = new Option("m", "multiarg", true, "Option with multiple args");
        multiArgOption.setArgs(3);
        multiArgOption.addValueForProcessing("value1");
        multiArgOption.addValueForProcessing("value2");
        multiArgOption.addValueForProcessing("value3");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");
        Assert.assertEquals(expectedValues, multiArgOption.getValuesList());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWhenListIsFull() {
        Option fullListOption = new Option("f", "full", true, "Option with full list");
        fullListOption.setArgs(2);
        fullListOption.addValueForProcessing("value1");
        fullListOption.addValueForProcessing("value2");
        fullListOption.addValueForProcessing("value3");
    }

    @Test
    public void testGetValueWithNoValues() {
        Assert.assertNull(option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Assert.assertEquals("defaultValue", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValuesWithNoValues() {
        Assert.assertNull(option.getValues());
    }

    @Test
    public void testGetValuesList() {
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        Assert.assertEquals(expectedValues, option.getValuesList());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("[ option: o option [ARG] :: Option for testing ]", option.toString());
    }

    @Test
    public void testEqualsWithSameObject() {
        Assert.assertTrue(option.equals(option));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Assert.assertFalse(option.equals("option"));
    }

    @Test
    public void testEqualsWithDifferentOpt() {
        Option otherOption = new Option("x", "option", true, "Option for testing");
        Assert.assertFalse(option.equals(otherOption));
    }

    @Test
    public void testEqualsWithDifferentLongOpt() {
        Option otherOption = new Option("o", "otherOption", true, "Option for testing");
        Assert.assertFalse(option.equals(otherOption));
    }

    @Test
    public void testEqualsWithSameOptAndLongOpt() {
        Option otherOption = new Option("o", "option", true, "Another option for testing");
        Assert.assertTrue(option.equals(otherOption));
    }

    @Test
    public void testHashCode() {
        Option otherOption = new Option("o", "option", true, "Option for testing");
        Assert.assertEquals(option.hashCode(), otherOption.hashCode());
    }

    @Test
    public void testClone() {
        Option cloneOption = (Option) option.clone();
        Assert.assertEquals(option.getOpt(), cloneOption.getOpt());
        Assert.assertEquals(option.getLongOpt(), cloneOption.getLongOpt());
        Assert.assertEquals(option.getDescription(), cloneOption.getDescription());
        Assert.assertEquals(option.isRequired(), cloneOption.isRequired());
        Assert.assertEquals(option.getArgName(), cloneOption.getArgName());
        Assert.assertEquals(option.hasArgName(), cloneOption.hasArgName());
        Assert.assertEquals(option.hasArgs(), cloneOption.hasArgs());
        Assert.assertEquals(option.getArgs(), cloneOption.getArgs());
        Assert.assertEquals(option.getValueSeparator(), cloneOption.getValueSeparator());
        Assert.assertEquals(option.hasValueSeparator(), cloneOption.hasValueSeparator());
        Assert.assertEquals(option.values, cloneOption.values);
    }

    @Test
    public void testClearValues() {
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        Assert.assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
}