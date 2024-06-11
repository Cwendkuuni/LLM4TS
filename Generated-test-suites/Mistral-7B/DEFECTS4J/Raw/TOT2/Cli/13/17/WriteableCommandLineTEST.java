package org.apache.commons.cli2.test;

import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Values;
import java.util.Arrays;
import static org.apache.commons.cli2.util.ValuesBuilder.*;

public class WriteableCommandLineTest {

   private WriteableCommandLine commandLine;

   @org.junit.Before
   public void setUp() {
      this.commandLine = new WriteableCommandLineImpl(); // Assuming WriteableCommandLineImpl is a concrete implementation
   }

   @Test
   public void testAddOption() {
      Option option = new Option("o", "optionName", true, "Option description");
      commandLine.addOption(option);
      assertTrue(commandLine.getOptions().contains(option));
   }

   @Test
   public void testAddValue() {
      Option option = new Option("o", "optionName", true, "Option description");
      commandLine.addOption(option);
      commandLine.addValue(option, "value1");
      assertEquals("value1", commandLine.getOptionValues(option).get(0));
   }

   @Test(expected = IllegalStateException.class)
   public void testAddValueToNonexistentOption() {
      Option option = new Option("o", "optionName1", true, "Option description1");
      commandLine.addOption(option);
      commandLine.addValue(option, "value2"); // option name should be "optionName"
      throw new IllegalStateException("Test for adding value to nonexistent Option");
   }

   @Test
   public void testSetDefaultValues() {
      Option option = new Option("o", "optionName", true, "Option description");
      List<Object> defaultValues = Arrays.asList("value1", "value2");
      commandLine.setDefaultValues(option, defaultValues);
      Values values = commandLine.getOptionValues(option);
      assertEquals(defaultValues.size(), values.size());
      for (int i = 0; i < values.size(); i++) {
         assertEquals(defaultValues.get(i), values.get(i));
      }
   }

   @Test
   public void testSetDefaultValuesForSwitchOption() {
      Option option = new Option("o", "optionName", false, "Option description");
      List<Boolean> defaultValues = Arrays.asList(true, false);
      commandLine.setDefaultSwitch(option, defaultValues.get(0));
      assertEquals(defaultValues.get(0), commandLine.getOptionValue(option));
      commandLine.setDefaultSwitch(option, defaultValues.get(1));
      assertEquals(defaultValues.get(1), commandLine.getOptionValue(option));
   }

   @Test(expected = IllegalStateException.class)
   public void testSetDefaultValuesForNonexistentOption() {
      Option option = new Option("o", "optionName1", true, "Option description1");
      List<Object> defaultValues = Arrays.asList("value1", "value2");
      commandLine.setDefaultValues(option, defaultValues); // option name should be "optionName"
      throw new IllegalStateException("Test for setting default values for nonexistent Option");
   }

   @Test
   public void testSetDefaultValuesForOptionWithExistingValues() {
      Option option = new Option("o", "optionName", true, "Option description");
      commandLine.addValue(option, "value3");
      List<Object> defaultValues = Arrays.asList("value1", "value2");
      commandLine.setDefaultValues(option, defaultValues);
      assertEquals(defaultValues.size(), commandLine.getOptionValues(option).size());
      for (int i = 0; i < defaultValues.size(); i++) {
         assertEquals(defaultValues.get(i), commandLine.getOptionValues(option).get(i));
      }
      assertEquals("value3", commandLine.getOptionValues(option).get(defaultValues.size()));
   }

   @Test
   public void testLooksLikeOption() {
      String argument = "optionName";
      assertTrue(commandLine.looksLikeOption(argument));
   }

   @Test
   public void testLooksLikeOptionDoesNotMatch() {
      String argument = "valueName";
      assertFalse(commandLine.looksLikeOption(argument));
   }
}