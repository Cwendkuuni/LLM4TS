package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OptionGroupTest {

 private OptionGroup optionGroup;

 @Before
 public void setUp() {
 optionGroup = new OptionGroup();
 Option option1 = new Option("o1", "option1", false, "d1");
 Option option2 = new Option("o2", "option2", false, "d2");
 Option option3 = new Option("o3", "option3", false, "d3");

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);
 }

 @Test
 public void testGetNames() {
 List<String> optionsNames = Arrays.asList("o1", "o2", "o3");
 assertEquals(optionsNames, optionGroup.getNames());
 }

 @Test
 public void testGetOptions() {
 List<Option> options = Arrays.asList(option1, option2, option3);
 assertEquals(options, optionGroup.getOptions());
 }

 @Test
 public void testSetSelectedNull() {
 optionGroup.setSelected(null);
 assertNull(optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedValidOption() {
 optionGroup.setSelected(option1);
 assertEquals("o1", optionGroup.getSelected());
 }

 @Test
 public void testSetSelectedAlreadySelected() {
 optionGroup.setSelected(option1);
 assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
 }

 @Test
 public void testIsRequiredFalse() {
 optionGroup.setRequired(false);
 assertFalse(optionGroup.isRequired());
 }

 @Test
 public void testIsRequiredTrue() {
 optionGroup.setRequired(true);
 assertTrue(optionGroup.isRequired());
 }

 @Test
 public void testToString() {
 String expected = "[-o1 option1, -o2 option2, --o3 option3]";
 assertEquals(expected, optionGroup.toString());
 }

 @Test
 public void testGetNamesEmptyGroup() {
 OptionGroup emptyGroup = new OptionGroup();
 assertEquals(Collections.emptyList(), emptyGroup.getNames());
 }

}