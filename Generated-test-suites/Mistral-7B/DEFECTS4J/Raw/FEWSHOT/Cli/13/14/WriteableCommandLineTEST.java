package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName");
 commandLine.addOption(option);
 assertEquals(option, commandLine.getOption("optionName"));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName value");
 commandLine.addValue(option, "value1");
 assertEquals(Arrays.asList("value1"), commandLine.getValues("optionName"));
 }

 @Test
 public void testGetValues() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName value1 value2");
 assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues("optionName"));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName");
 List<String> defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 assertEquals(defaultValues, commandLine.getValues("optionName"));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName");
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.hasOption("optionName"));
 assertSame(option, commandLine.getOption("optionName"));
 assertTrue(commandLine.getOption("optionName").isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName");
 commandLine.addSwitch(option, true);
 assertThrows(IllegalStateException.class, () -> commandLine.addSwitch(option, true));
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("optionName").build();
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName");
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getOption("optionName").isSet());
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine commandLine = new Cli2Parser().parse("-Dproperty=value");
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getCommandLineProperties().get("property"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName value");
 assertFalse(commandLine.looksLikeOption("anotherOptionName"));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName value");
 assertTrue(commandLine.looksLikeOption("optionName"));
 }

 @Test
 public void testLooksLikeOptionEmpty() {
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName");
 assertFalse(commandLine.looksLikeOption(""));
 }

 @Test
 public void testLooksLikeOptionNull() {
 WriteableCommandLine commandLine = new Cli2Parser().parse("-optionName");
 assertFalse(commandLine.looksLikeOption(null));
 }
}