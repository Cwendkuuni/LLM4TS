package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OptionTest {

    @Test
    public void testGetOpt() {
        Option option = new Option("o", "description");
        Assert.assertEquals("o", option.getOpt());
    }

    @Test
    public void testGetType() {
        Option option = new Option("o", "description");
        Assert.assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("o", "description");
        option.setType(String.class);
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("o", "longOpt", true, "description");
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("o", "description");
        option.setLongOpt("longOpt");
        Assert.assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("o", "description");
        option.setOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("o", "longOpt", true, "description");
        Assert.assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("o", "description");
        Assert.assertFalse(option.hasArg());
        Option optionWithArg = new Option("o", true, "description");
        Assert.assertTrue(optionWithArg.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("o", "description");
        Assert.assertEquals("description", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("o", "description");
        option.setDescription("newDescription");
        Assert.assertEquals("newDescription", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("o", "description");
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("o", "description");
        option.setRequired(true);
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("o", "description");
        option.setArgName("argName");
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("o", "description");
        Assert.assertFalse(option.hasArgName());
        Option optionWithArgName = new Option("o", "description");
        optionWithArgName.setArgName("argName");
        Assert.assertTrue(optionWithArgName.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("o", "description");
        Assert.assertFalse(option.hasArgs());
        Option optionWithMultipleArgs = new Option("o", true, "description");
        optionWithMultipleArgs.setArgs(2);
        Assert.assertTrue(optionWithMultipleArgs.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("o", "description");
        option.setArgs(2);
        Assert.assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("o", "description");
        option.setValueSeparator(',');
        Assert.assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option option = new Option("o", "description");
        Assert.assertFalse(option.hasValueSeparator());
        Option optionWithValueSeparator = new Option("o", "description");
        optionWithValueSeparator.setValueSeparator(',');
        Assert.assertTrue(optionWithValueSeparator.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option = new Option("o", "description");
        Assert.assertEquals(-1, option.getArgs());
        Option optionWithArgs = new Option("o", true, "description");
        optionWithArgs.setArgs(3);
        Assert.assertEquals(3, optionWithArgs.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgsAllowed() {
        Option option = new Option("o", "description");
        option.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("o", true, "description");
        option.addValueForProcessing("value");
        List<String> values = option.getValuesList();
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWhenListIsFull() {
        Option option = new Option("o", true, "description");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test
    public void testGetValue() {
        Option option = new Option("o", true, "description");
        option.addValueForProcessing("value");
        Assert.assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueAtIndex() {
        Option option = new Option("o", true, "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("o", true, "description");
        Assert.assertEquals("defaultValue", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", true, "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        String[] expectedValues = {"value1", "value2"};
        Assert.assertArrayEquals(expectedValues, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("o", true, "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List<String> values = option.getValuesList();
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", true, "description");
        option.setLongOpt("longOpt");
        option.setDescription("description");
        option.setType(String.class);
        Assert.assertEquals("[ option: o longOpt [ARG] :: description :: class java.lang.String ]", option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("o", true, "description");
        Option option2 = new Option("o", true, "description");
        Option option3 = new Option("o", false, "description");
        Option option4 = new Option("p", true, "description");
        option1.setLongOpt("longOpt");
        option2.setLongOpt("longOpt");
        option3.setLongOpt("longOpt");
        option4.setLongOpt("longOpt");
        Assert.assertTrue(option1.equals(option2));
        Assert.assertFalse(option1.equals(option3));
        Assert.assertFalse(option1.equals(option4));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("o", true, "description");
        Option option2 = new Option("o", true, "description");
        option1.setLongOpt("longOpt");
        option2.setLongOpt("longOpt");
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("o", true, "description");
        option1.addValueForProcessing("value1");
        Option option2 = (Option) option1.clone();
        option2.addValueForProcessing("value2");
        Assert.assertEquals(1, option1.getValuesList().size());
        Assert.assertEquals(2, option2.getValuesList().size());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("o", true, "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        Assert.assertEquals(0, option.getValuesList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("o", true, "description");
        option.addValue("value");
    }
}