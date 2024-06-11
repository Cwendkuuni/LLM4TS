package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class OptionGroupTest {

 @Test
 public void testAddOption() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 group.addOption(option1);
 group.addOption(option2);

 assertEquals(2, group.getOptions().size());
 }

 @Test
 public void testGetNames() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 group.addOption(option1);
 group.addOption(option2);

 assertEquals(group.getNames().size(), 2);
 assertEquals(group.getNames().contains("opt1"), true);
 assertEquals(group.getNames().contains("opt2"), true);
 }

 @Test
 public void testSetSelected() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");

 group.addOption(option1);
 group.addOption(option2);

 assertNull(group.getSelected());

 group.setSelected(option1);
 assertEquals(option1.getKey(), group.getSelected());

 group.setSelected(option2);
 assertEquals(option2.getKey(), group.getSelected());

 assertThrows(AlreadySelectedException.class, () -> group.setSelected(option1));
 }

 @Test
 public void testIsRequired() {
 OptionGroup group = new OptionGroup();

 group.setRequired(true);
 assertTrue(group.isRequired());

 group.setRequired(false);
 assertFalse(group.isRequired());
 }

 @Test
 public void testToString() {
 OptionGroup group = new OptionGroup();
 Option option1 = new Option("opt1", "Option 1");
 Option option2 = new Option("opt2", "Option 2");
 Option option3 = new Option("opt3", "Option 3");

 group.addOption(option1);
 group.addOption(option2);
 group.addOption(option3);

 assertEquals("[--opt1 Option 1, --opt2 Option 2, --opt3 Option 3]", group.toString());
 }
}