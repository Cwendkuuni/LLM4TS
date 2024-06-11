package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 assertTrue(commandLine.getOptions().contains(option));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 List<String> values = commandLine.getValues(option);
 assertEquals(Arrays.asList("value1"), values);
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getValues(option);
 assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").longOpt("option").hasArg(true).build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 List<String> defaults = new ArrayList<>();
 defaults.add("value1");
 defaults.add("value2");
 commandLine.setDefaultValues(option, defaults);
 List<String> values = commandLine.getValues(option);
 assertEquals(defaults, values);
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").longOpt("option").build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addProperty("property", "value1");
 assertEquals("value1", commandLine.getProperties().getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertFalse(commandLine.looksLikeOption("--foo"));
 assertFalse(commandLine.looksLikeOption("-"));
 }
}