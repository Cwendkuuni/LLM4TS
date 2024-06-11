package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.jupiter.api.Assertions;// for assertions
import org.junit.jupiter.api.Test;

public class OptionGroupTest {

 // Test case for addOption() method
 @Test
 public void testAddOption() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "This is option1");
 Option option2 = new Option("o2", "option2", false, "This is option2");

 group.addOption(option1);
 group.addOption(option2);

 Collection names = group.getNames();
 Collection options = group.getOptions();

 // Verify number of options added
 Assertions.assertEquals(2, names.size());
 Assertions.assertEquals(2, options.size());

 // Verify options added are correct
 Option opt1 = (Option) options.iterator().next();
 Assertions.assertEquals("o1", opt1.getKey());
 Assertions.assertEquals("option1", opt1.getLongOpt());
 Assertions.assertTrue(opt1.isRequired());

 Option opt2 = (Option) options.iterator().next();
 Assertions.assertEquals("o2", opt2.getKey());
 Assertions.assertEquals("option2", opt2.getLongOpt());
 Assertions.assertFalse(opt2.isRequired());
 }

 // Test case for getNames() method
 @Test
 public void testGetNames() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "This is option1");
 Option option2 = new Option("o2", "option2", false, "This is option2");

 group.addOption(option1);
 group.addOption(option2);

 Collection names = group.getNames();

 // Verify names of options obtained
 Assertions.assertTrue(names.contains("o1"));
 Assertions.assertTrue(names.contains("o2"));
 }

 // Test case for getOptions() method
 @Test
 public void testGetOptions() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "This is option1");
 Option option2 = new Option("o2", "option2", false, "This is option2");

 group.addOption(option1);
 group.addOption(option2);

 Collection options = group.getOptions();

 // Verify options obtained
 Assertions.assertEquals(option1, options.iterator().next());
 Assertions.assertEquals(option2, options.iterator().next());
 }

 // Test case for setSelected() method
 @Test
 public void testSetSelected() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "This is option1");
 Option option2 = new Option("o2", "option2", false, "This is option2");

 group.addOption(option1);
 group.addOption(option2);

 // Test setting null as the selected option
 group.setSelected(null);
 Assertions.assertNull(group.getSelected());

 // Test setting already selected option
 group.setSelected(option1);
 Assertions.assertEquals(option1.getOpt(), group.getSelected());

 // Test setting other option
 group.setSelected(option2);
 Assertions.assertEquals(option2.getOpt(), group.getSelected());
 }

 // Test case for getSelected() method
 @Test
 public void testGetSelected() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("o1", "option1", true, "This is option1");
 Option option2 = new Option("o2", "option2", false, "This is option2");

 group.addOption(option1);
 group.addOption(option2);

 group.setSelected(option1);
 Assertions.assertEquals(option1.getOpt(), group.getSelected());

 group.setSelected(option2);
 Assertions.assertEquals(option2.getOpt(), group.getSelected());
 }

 // Test case for isRequired() method
 @Test
 public void testIsRequired() {
 OptionGroup group = new OptionGroup();
 group.setRequired(true);
 Assertions.assertTrue(group.isRequired());

 OptionGroup group2 = new OptionGroup();
 group2.setRequired(false);
 Assertions.assertFalse(group2.isRequired());
 }
}