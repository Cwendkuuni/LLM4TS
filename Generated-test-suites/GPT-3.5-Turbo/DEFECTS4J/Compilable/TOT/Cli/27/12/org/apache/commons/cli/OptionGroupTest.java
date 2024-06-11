package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.Collection;
import org.apache.commons.cli.*;

public class OptionGroupTest {

   private OptionGroup optionGroup;
   private Option option1, option2, option3;

   @Before
   public void setUp() {
      optionGroup = new OptionGroup();
      option1 = new Option("a", "apple", false, "Option 1");
      option2 = new Option("b", "banana", false, "Option 2");
      option3 = new Option("c", "cherry", false, "Option 3");
   }

   // Test addOption() method
   @Test
   public void testAddOption() {
      optionGroup.addOption(option1);
      assertTrue(optionGroup.getOptions().contains(option1));
   }

   // Test getNames() method
   @Test
   public void testGetNames() {
      optionGroup.addOption(option1);
      optionGroup.addOption(option2);
      Collection<String> names = optionGroup.getNames();
      assertTrue(names.contains("a") && names.contains("b"));
      assertFalse(names.contains("c"));
   }

   // Test getOptions() method
   @Test
   public void testGetOptions() {
      optionGroup.addOption(option1);
      optionGroup.addOption(option2);
      Collection<Option> options = optionGroup.getOptions();
      assertTrue(options.contains(option1) && options.contains(option2));
      assertFalse(options.contains(option3));
   }

   // Test setSelected() method - normal case
   @Test
   public void testSetSelectedNormal() throws AlreadySelectedException {
      optionGroup.addOption(option1);
      optionGroup.setSelected(option1);
      assertEquals("a", optionGroup.getSelected());
   }

   // Test setSelected() method - reselect option
   @Test(expected=AlreadySelectedException.class)
   public void testSetSelectedReselect() throws AlreadySelectedException {
      optionGroup.addOption(option1);
      optionGroup.addOption(option2);
      optionGroup.setSelected(option1);
      optionGroup.setSelected(option2);
   }

   // Test getSelected() method
   @Test
   public void testGetSelected() throws AlreadySelectedException {
      optionGroup.addOption(option1);
      optionGroup.setSelected(option1);
      assertEquals("a", optionGroup.getSelected());
   }

   // Test setRequired() method
   @Test
   public void testSetRequired() {
      optionGroup.setRequired(true);
      assertTrue(optionGroup.isRequired());
   }

   // Test toString() method
   @Test
   public void testToString() {
      optionGroup.addOption(option1);
      optionGroup.addOption(option2);
      optionGroup.addOption(option3);
      String expected = "[ -a Option 1, -b Option 2, -c Option 3]";
      assertEquals(expected, optionGroup.toString());
   }
}