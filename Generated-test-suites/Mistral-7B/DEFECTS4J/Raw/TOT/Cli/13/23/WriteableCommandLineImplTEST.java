package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

 private WriteableCommandLine commandLine;
 private Option option;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
 option = new Option("o", "output", true, "Output file");
 }

 @Test
 public void testAddOption() {
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 commandLine.addOption(option);
 commandLine.addValue(option, "test.txt");
 assertEquals(1, commandLine.getValues(option, Collections.emptyList()).size());
 assertEquals("test.txt", commandLine.getValues(option, Collections.emptyList()).get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testHasOption() {
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 commandLine.addOption(option);
 Option retrievedOption = commandLine.getOption("o");
 assertEquals(option, retrievedOption);
 }

 @Test
 public void testGetValues() {
 commandLine.addOption(option);
 commandLine.addValue(option, "test1.txt");
 commandLine.addValue(option, "test2.txt");
 List values = commandLine.getValues(option, Collections.emptyList());
 assertEquals(2, values.size());
 assertEquals("test1.txt", values.get(0));
 assertEquals("test2.txt", values.get(1));
 }

 @Test
 public void testGetSwitch() {
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testGetSwitchFalse() {
 commandLine.addOption(option);
 assertFalse(commandLine.getSwitch(option, true));
 }

 @Test
 public void testGetProperty() {
 commandLine.addProperty("test", "testValue");
 assertEquals("testValue", commandLine.getProperty("test", "defaultValue"));
 }

 @Test
 public void testGetProperties() {
 commandLine.addProperty("test1", "testValue1");
 commandLine.addProperty("test2", "testValue2");
 Set properties = commandLine.getProperties();
 assertEquals(2, properties.size());
 assertTrue(properties.contains("test1"));
 assertTrue(properties.contains("test2"));
 }

 @Test
 public void testLooksLikeOption() {
 assertTrue(commandLine.looksLikeOption("o"));
 assertFalse(commandLine.looksLikeOption("notAnOption"));
 }

 @Test(expected = ParseException.class)
 public void testParseException() {
 commandLine.parse();
 }
}