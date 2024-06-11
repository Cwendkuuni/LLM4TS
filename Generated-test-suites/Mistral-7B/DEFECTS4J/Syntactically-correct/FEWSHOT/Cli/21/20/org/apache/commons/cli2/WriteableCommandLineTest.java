package org.apache.commons.cli2;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class WriteableCommandLineTest {

 @Test
 public void testAddOption() {
 WriteableCommandLine commandLine = createMockCommandLine();
 Option option = Option.builder("option").build();
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 assertEquals(option, commandLine.getOptions().get(0));
 }

 @Test
 public void testAddValue() {
 WriteableCommandLine commandLine = createMockCommandLine();
 Option option = Option.builder("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 List<String> values = commandLine.getOptionValues("option");
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testGetUndefaultedValues() {
 WriteableCommandLine commandLine = createMockCommandLine();
 Option option = Option.builder("option").build();
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List<String> values = commandLine.getUndefaultedValues(option);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testSetDefaultValues() {
 WriteableCommandLine commandLine = createMockCommandLine();
 Option option = Option.builder("option").build();
 List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
 commandLine.setDefaultValues(option, defaultValues);
 List<String> values = commandLine.getOptionValues("option");
 assertEquals(2, values.size());
 assertEquals("defaultValue1", values.get(0));
 assertEquals("defaultValue2", values.get(1));
 }

 @Test
 public void testAddSwitch() {
 WriteableCommandLine commandLine = createMockCommandLine();
 Option option = Option.builder("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(option.isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 WriteableCommandLine commandLine = createMockCommandLine();
 Option option = Option.builder("option").build();
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testSetDefaultSwitch() {
 WriteableCommandLine commandLine = createMockCommandLine();
 Option option = Option.builder("option").build();
 commandLine.setDefaultSwitch(option, true);
 assertFalse(option.isSet());
 }

 @Test
 public void testAddProperty() {
 WriteableCommandLine commandLine = createMockCommandLine();
 Option option = Option.builder("option").build();
 commandLine.addOption(option);
 commandLine.addProperty(option, "property", "value");
 assertEquals("value", commandLine.getOptionProperty(option, "property"));
 }

 @Test
 public void testAddPropertyDefault() {
 WriteableCommandLine commandLine = createMockCommandLine();
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getOptionProperty("option", "property"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 WriteableCommandLine commandLine = createMockCommandLine();
 assertFalse(commandLine.looksLikeOption("argument"));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 WriteableCommandLine commandLine = createMockCommandLine();
 commandLine.addOption(Option.builder("option").build());
 assertTrue(commandLine.looksLikeOption("option"));
 }

 @Test
 public void testCurrentOptionNull() {
 WriteableCommandLine commandLine = createMockCommandLine();
 assertNull(commandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 WriteableCommandLine commandLine = createMockCommandLine();
 Option option = Option.builder("option").build();
 commandLine.setCurrentOption(option);
 assertEquals(option, commandLine.getCurrentOption());
 }

 private WriteableCommandLine createMockCommandLine() {
 return new MockWriteableCommandLine();
 }
}