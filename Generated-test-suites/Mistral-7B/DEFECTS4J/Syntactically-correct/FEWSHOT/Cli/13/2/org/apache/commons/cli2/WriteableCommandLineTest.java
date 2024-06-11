package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WriteableCommandLineTest {

 interface TestWriteableCommandLine extends WriteableCommandLine {
  List<String> options = new ArrayList<>();
  List<String> values = new ArrayList<>();
  List<Boolean> switches = new ArrayList<>();
  List<String> properties = new ArrayList<>();
 }

 @Test
 public void testAddOption() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = Option.builder("o").build();
 cmd.addOption(option);
 assertTrue(cmd.getOptions().contains(option));
 }

 @Test(expected = NullPointerException.class)
 public void testAddOptionNullOption() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = null;
 cmd.addOption(option);
 }

 @Test
 public void testAddValue() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = Option.builder("o").build();
 cmd.addOption(option);
 cmd.addValue(option, "val1");
 assertEquals("val1", cmd.getValueForOption("o"));
 }

 @Test
 public void testAddValueNullOption() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = null;
 cmd.addValue(option, "val1");
 assertNull(cmd.getValueForOption("o"));
 }

 @Test
 public void testAddValueMultipleValues() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = Option.builder("o").build();
 cmd.addOption(option);
 cmd.addValue(option, "val1");
 cmd.addValue(option, "val2");
 assertEquals(Arrays.asList("val1", "val2"), cmd.getValuesForOption("o"));
 }

 @Test
 public void testSetDefaultValues() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = Option.builder("o").build();
 List<Object> defaultValues = Arrays.asList("val1", "val2");
 cmd.setDefaultValues(option, defaultValues);
 assertEquals(defaultValues, cmd.getValuesForOption("o"));
 }

 @Test(expected = NullPointerException.class)
 public void testSetDefaultValuesNullOption() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = null;
 List<Object> defaultValues = Arrays.asList("val1", "val2");
 cmd.setDefaultValues(option, defaultValues);
 }

 @Test
 public void testAddSwitch() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = Option.builder("o").build();
 cmd.addSwitch(option, true);
 assertTrue(cmd.getSwitchForOption("o"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = Option.builder("o").build();
 cmd.addSwitch(option, true);
 cmd.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = Option.builder("o").build();
 cmd.setDefaultSwitch(option, true);
 assertTrue(cmd.getSwitchForOption("o"));
 }

 @Test(expected = NullPointerException.class)
 public void testSetDefaultSwitchNullOption() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 Option option = null;
 cmd.setDefaultSwitch(option, true);
 }

 @Test
 public void testAddProperty() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();cmd.addProperty("p", "val1");
 assertEquals("val1", cmd.getProperty("p"));
 }

 @Test(expected = NullPointerException.class)
 public void testAddPropertyNullProperty() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 String property = null;
 cmd.addProperty(property, "val1");
 }

 @Test
 public void testLooksLikeOption() {
 TestWriteableCommandLine cmd = new TestWriteableCommandLine();
 assertTrue(cmd.looksLikeOption("--option"));
 assertTrue(cmd.looksLikeOption("-o"));
 assertFalse(cmd.looksLikeOption("--arg"));
 assertFalse(cmd.looksLikeOption("-a"));
 }
}