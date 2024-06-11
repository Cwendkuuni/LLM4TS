package org.junit.Test;import static org.junit.Assert.*;import static org.apache.commons.cli2.CliBuilder.aBooleanOption;import static org.apache.commons.cli2.CliBuilder.aOption;import static org.apache.commons.cli2.CliBuilder.aStringOption;import static org.apache.commons.cli2.CliBuilder.aValueOption;import static org.apache.commons.cli2.CliBuilder.parse;

import org.apache.commons.cli2.WriteableCommandLine;import org.junit.Test;

public class WriteableCommandLineTest {

   @Testpublic void testAddOption() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Option option = new Option("o", "option");
      cmd.addOption(option);
      assertTrue(cmd.getOptions().contains(option));
      assertFalse(cmd.getValueForOption("nonexistent").isPresent());
   }

   @Testpublic void testAddValue() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Option option = new Option("o", "option");
      cmd.addOption(option);
      cmd.addValue(option, "value1");
      List<Object> values = cmd.getValuesForOption(option);
      assertEquals(values.size(), 1);
      assertEquals(values.get(0), "value1");
      cmd.addValue(option, "value2");
      values = cmd.getValuesForOption(option);
      assertEquals(values.size(), 2);
      assertEquals(values.get(0), "value1");
      assertEquals(values.get(1), "value2");
   }

   @Test(expected = IllegalStateException.class)public void testAddSwitchTwice() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Option option = new Option("o", "option");
      cmd.addSwitch(option, true);
      cmd.addSwitch(option, true);
   }

   @Test(expected = IllegalStateException.class)public void testAddValueWithNullOption() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Object value = new Object();
      Option option = null;
      try {
         cmd.addValue(option, value);
      } catch (IllegalArgumentException e) {
         // Expected exception
      }
   }

   @Testpublic void testLooksLikeOption() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      assertTrue(cmd.looksLikeOption("--option"));
      assertFalse(cmd.looksLikeOption("value"));
   }

   @Testpublic void testAddValueWithDuplicateOption() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Option option = new Option("o", "option");
      cmd.addOption(option);
      cmd.addValue(option, "value1");
      List<Object> values = cmd.getValuesForOption(option);
      assertEquals(values.size(), 1);
      assertEquals(values.get(0), "value1");
      cmd.addValue(option, "value2");
      values = cmd.getValuesForOption(option);
      assertEquals(values.size(), 2);
      assertEquals(values.get(0), "value1");
      assertEquals(values.get(1), "value2");
   }

   @Testpublic void testAddValueWithNullValue() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Option option = new Option("o", "option");
      cmd.addOption(option);
      try {
         cmd.addValue(option, null);
      } catch (IllegalArgumentException e) {
         // Expected exception
      }
   }

   @Testpublic void testSetDefaultValues() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Option option = new Option("o", "option");
      List<Object> defaultValues = List.of("default1", "default2");
      cmd.setDefaultValues(option, defaultValues);
      List<Object> values = cmd.getValuesForOption(option);
      assertEquals(values.size(), 2);
      assertEquals(values.get(0), "default1");
      assertEquals(values.get(1), "default2");
   }

   @Testpublic void testAddSwitch() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Option option = new Option("o", "option");
      cmd.addSwitch(option, false);
      assertTrue(cmd.getValueForOption("o").isPresent());
      assertEquals(cmd.getValueForOption("o").get(), false);
      try {
         cmd.addSwitch(option, true);
      } catch (IllegalStateException e) {
         // Expected exception
      }
      assertTrue(cmd.getValueForOption("o").isPresent());
      assertEquals(cmd.getValueForOption("o").get(), false);
   }

   @Testpublic void testSetDefaultSwitch() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Option option = new Option("o", "option");
      cmd.setDefaultSwitch(option, true);
      assertTrue(cmd.getValueForOption("o").isPresent());
      assertEquals(cmd.getValueForOption("o").get(), true);
   }

   @Testpublic void testAddProperty() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      cmd.addProperty("property", "value");
      assertEquals(cmd.getProperty("property"), "value");
   }

   @Testpublic void testLooksLikeOptionWithLongArgument() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      assertTrue(cmd.looksLikeOption("--long-option"));
   }

   @Testpublic void testGetValuesForOptionWithNoValues() {
      WriteableCommandLine cmd = new WriteableCommandLine();
      Option option = new Option("o", "option");
      cmd.addOption(option);
      List<Object> values = cmd.getValuesForOption(option);
      assertEquals(values.size(), 0);
      assertNull(values.get(0));
   }

}