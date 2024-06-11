package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(new Option.Builder("optionA")
 .longOpt("longOptionA")
 .required(false)
 .desc("An option with a long name and two synonyms")
 .build(), new ArrayList<>());
 option = new Option.Builder("optionB")
 .longOpt("longOptionB")
 .required(true)
 .desc("An option that must be present")
 .build();
 commandLine.addOption(option);
 commandLine.addOption(option); // add an option twice to test error handling
 }

 @Test
 public void testAddOption() {
 assertEquals(1, commandLine.getOptions().size());
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testAddValue() {
 List values = commandLine.getValues(option, null);
 assertNull(values);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 assertFalse(commandLine.getSwitch(option, false));
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 assertTrue(commandLine.getSwitch(option, true));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, true);
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption(option));
 assertFalse(commandLine.hasOption(new Option.Builder("nonexistent").build()));
 }

 @Test
 public void testGetOption() {
 Option retrievedOption = commandLine.getOption("longOptionA");
 assertEquals(option, retrievedOption);
 }

 @Test
 public void testGetValues() {
 List values = commandLine.getValues(option, null);
 assertNull(values);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetUndefaultedValues() {
 List values = commandLine.getUndefaultedValues(option);
 assertEquals(Collections.EMPTY_LIST, values);
 }

 @Test
 public void testGetSwitch() {
 assertFalse(commandLine.getSwitch(option, false));
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testGetProperty() {
 assertNull(commandLine.getProperty("nonexistentProperty"));
 commandLine.addProperty(option, "propertyA", "valueA");
 assertEquals("valueA", commandLine.getProperty("propertyA"));
 }

 @Test
 public void testGetProperties() {
 assertEquals(Collections.EMPTY_SET, commandLine.getProperties());
 commandLine.addProperty(option, "propertyA", "valueA");
 commandLine.addProperty(option, "propertyB", "valueB");
 Set properties = commandLine.getProperties();
 assertEquals(2, properties.size());
 assertTrue(properties.contains("propertyA"));
 assertTrue(properties.contains("propertyB"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("longOptionA"));
 assertFalse(commandLine.looksLikeOption("nonexistent"));
 }

 @Test
 public void testToString() {
 List arguments = new ArrayList();
 arguments.add("argument1");
 arguments.add("argument2");
 commandLine.setNormalised(arguments);
 String expected = "argument1 argument2";
 assertEquals(expected, commandLine.toString());
 }

 @Test
 public void testGetOptionsList() {
 assertEquals(Collections.unmodifiableList(Arrays.asList(option, option)), commandLine.getOptions());
 }

 @Test
 public void testGetOptionTriggersSet() {
 Set expected = Collections.unmodifiableSet(Arrays.asList("longOptionA", "longOptionB"));
 assertEquals(expected, commandLine.getOptionTriggers());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionTwice() {
 commandLine.addOption(option);
 commandLine.addOption(option);
 }

 @Test
 public void testSetDefaultValues() {
 List defaultValues = new ArrayList();
 defaultValues.add("defaultValue1");
 defaultValues.add("defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 List values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("defaultValue1", values.get(0));
 assertEquals("defaultValue2", values.get(1));
 }

 @Test
 public void testSetDefaultSwitch() {
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testSetDefaultValuesAndSwitch() {
 List defaultValues = new ArrayList();
 defaultValues.add("defaultValue1");
 commandLine.setDefaultValues(option, defaultValues);
 commandLine.setDefaultSwitch(option, true);
 List values = commandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("defaultValue1", values.get(0));
 assertTrue(commandLine.getSwitch(option, false));
 assertFalse(commandLine.getSwitch(option, true));
 }
}