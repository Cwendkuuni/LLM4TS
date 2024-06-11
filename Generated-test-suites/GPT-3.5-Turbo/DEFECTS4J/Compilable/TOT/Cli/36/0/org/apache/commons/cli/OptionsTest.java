package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

   @Test
   public void testAddOptionGroup() {
      // Test adding an OptionGroup to Options
      Options options = new Options();
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "apple", false, "option for apple");
      Option option2 = new Option("b", "banana", false, "option for banana");
      group.addOption(option1);
      group.addOption(option2);
      options.addOptionGroup(group);

      assertTrue(options.getOptionGroups().contains(group));
   }

   @Test
   public void testAddOption() {
      // Test adding an Option to Options
      Options options = new Options();
      Option option1 = new Option("f", "file", true, "option for file");
      Option option2 = new Option("d", "directory", false, "option for directory");
      options.addOption(option1);
      options.addOption(option2);

      assertTrue(options.hasOption("f"));
      assertTrue(options.hasOption("file"));
      assertTrue(options.hasOption("d"));
      assertTrue(options.hasOption("directory"));
   }

   @Test
   public void testGetOptions() {
      // Test getting the options in Options
      Options options = new Options();
      Option option1 = new Option("f", "file", true, "option for file");
      Option option2 = new Option("d", "directory", false, "option for directory");
      options.addOption(option1);
      options.addOption(option2);

      assertEquals(2, options.getOptions().size());
   }

   @Test
   public void testGetRequiredOptions() {
      // Test getting the required options in Options
      Options options = new Options();
      Option option1 = new Option("f", "file", true, "option for file");
      Option option2 = new Option("d", "directory", false, "option for directory");
      option1.setRequired(true);
      options.addOption(option1);
      options.addOption(option2);

      assertEquals(1, options.getRequiredOptions().size());
   }

   @Test
   public void testGetOption() {
      // Test getting an option in Options
      Options options = new Options();
      Option option1 = new Option("f", "file", true, "option for file");
      Option option2 = new Option("d", "directory", false, "option for directory");
      options.addOption(option1);
      options.addOption(option2);

      assertEquals(option1, options.getOption("f"));
      assertEquals(option2, options.getOption("d"));
   }

   @Test
   public void testGetMatchingOptions() {
      // Test getting matching options in Options
      Options options = new Options();
      Option option1 = new Option("a", "apple", false, "option for apple");
      Option option2 = new Option("b", "banana", false, "option for banana");
      options.addOption(option1);
      options.addOption(option2);

      assertEquals(1, options.getMatchingOptions("app").size());
      assertEquals("apple", options.getMatchingOptions("app").get(0));
   }

   @Test
   public void testHasOption() {
      // Test checking if an option exists in Options
      Options options = new Options();
      Option option1 = new Option("f", "file", true, "option for file");
      Option option2 = new Option("d", "directory", false, "option for directory");
      options.addOption(option1);
      options.addOption(option2);

      assertTrue(options.hasOption("f"));
      assertTrue(options.hasOption("file"));
      assertTrue(options.hasOption("d"));
      assertTrue(options.hasOption("directory"));
   }

   @Test
   public void testHasLongOption() {
      // Test checking if a long option exists in Options
      Options options = new Options();
      Option option1 = new Option("f", "file", true, "option for file");
      Option option2 = new Option("d", "directory", false, "option for directory");
      options.addOption(option1);
      options.addOption(option2);

      assertTrue(options.hasLongOption("file"));
      assertTrue(options.hasLongOption("directory"));
   }

   @Test
   public void testHasShortOption() {
      // Test checking if a short option exists in Options
      Options options = new Options();
      Option option1 = new Option("f", "file", true, "option for file");
      Option option2 = new Option("d", "directory", false, "option for directory");
      options.addOption(option1);
      options.addOption(option2);

      assertTrue(options.hasShortOption("f"));
      assertTrue(options.hasShortOption("d"));
   }

   @Test
   public void testGetOptionGroup() {
      // Test getting the OptionGroup of an Option in Options
      Options options = new Options();
      OptionGroup group = new OptionGroup();
      Option option1 = new Option("a", "apple", false, "option for apple");
      Option option2 = new Option("b", "banana", false, "option for banana");
      group.addOption(option1);
      group.addOption(option2);
      options.addOptionGroup(group);

      assertEquals(group, options.getOptionGroup(option2));
   }
   
   @Test
   public void testToString() {
      // Test converting Options object to String
      Options options = new Options();
      Option option1 = new Option("f", "file", true, "option for file");
      Option option2 = new Option("d", "directory", false, "option for directory");
      options.addOption(option1);
      options.addOption(option2);
      
      String expected = "[ Options: [ short {f=option for file, d=option for directory} ] [ long {file=option for file, directory=option for directory} ]";
      assertEquals(expected, options.toString());
   }
}