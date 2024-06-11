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
 Option option = Option.builder("o").build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 assertSame(option, commandLine.getOptions().get(0));
 }

 @Test
 public void testAddValue() {
 Option option = Option.builder("o").valueSeparator('=').build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.addValue(option, "value");
 List<String> values = commandLine.getValues(option);
 assertEquals(1, values.size());
 assertEquals("value", values.get(0));
 }

 @Test
 public void testGetUndefaultedValues() {
 Option option = Option.builder("o").valueSeparator('=').build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<Object> values = commandLine.getUndefaultedValues(option);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testSetDefaultValues() {
 Option option = Option.builder("o").valueSeparator('=').build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 List<String> defaults = Arrays.asList("default1", "default2");
 commandLine.setDefaultValues(option, defaults);
 List<Object> values = commandLine.getValues(option);
 assertEquals(2, values.size());
 assertEquals("default1", values.get(0));
 assertEquals("default2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 Option option = Option.builder("o").build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 assertThrows(IllegalStateException.class, () -> commandLine.addSwitch(option, true));
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertFalse(option.getValues().isEmpty());
 }

 @Test
 public void testSetDefaultSwitch() {
 Option option = Option.builder("o").build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(option.getValues().isEmpty());
 assertTrue(option.isSet());
 }

 @Test
 public void testAddProperty() {
 Option option = Option.builder("o").build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "value");
 assertEquals("value", commandLine.getProperty(Option.class, option, "property"));
 }

 @Test
 public void testAddPropertyDefault() {
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property"));
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 assertTrue(commandLine.looksLikeOption("--option"));
 assertTrue(commandLine.looksLikeOption("-o"));
 assertTrue(commandLine.looksLikeOption("--o"));
 assertFalse(commandLine.looksLikeOption("value"));
 assertFalse(commandLine.looksLikeOption(""));
 }

 @Test
 public void testGetCurrentOption() {
 Option option1 = Option.builder("o1").build();
 Option option2 = Option.builder("o2").build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.processOption("--o1");
 assertEquals(option1, commandLine.getCurrentOption());
 commandLine.processOption("--o2");
 assertEquals(option2, commandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 Option option1 = Option.builder("o1").build();
 Option option2 = Option.builder("o2").build();
 WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
 commandLine.addOption(option1);
 commandLine.addOption(option2);
 commandLine.setCurrentOption(option1);
 assertEquals(option1, commandLine.getCurrentOption());
 commandLine.setCurrentOption(option2);
 assertEquals(option2, commandLine.getCurrentOption());
 }
}