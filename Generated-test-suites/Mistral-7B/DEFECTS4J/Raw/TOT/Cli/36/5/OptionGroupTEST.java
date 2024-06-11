package org.apache.commons.cli.tests;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OptionGroupTest {

 @Test
 void testAddOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1", true);
 Option option2 = new Option("option2", "description2", false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);

 List<Option> options = optionGroup.getOptions();

 Assertions.assertEquals(2, options.size());
 Assertions.assertEquals(option1, options.get(0));
 Assertions.assertEquals(option2, options.get(1));
 }

 @Test
 void testGetNames() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1", true);
 Option option2 = new Option("option2", "description2", false);
 Option option3 = new Option("option3", "description3", false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 Collection<String> names = optionGroup.getNames();

 Assertions.assertEquals(3, names.size());
 Assertions.assertTrue(names.contains("option1"));
 Assertions.assertTrue(names.contains("option2"));
 Assertions.assertTrue(names.contains("option3"));
 }

 @Test
 void testSetSelected() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1", true);
 Option option2 = new Option("option2", "description2", false);
 Option option3 = new Option("option3", "description3", false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 try {
 optionGroup.setSelected(option1);
 } catch (AlreadySelectedException e) {
 // expected
 }

 optionGroup.setSelected(option1);
 String selected = optionGroup.getSelected();

 Assertions.assertEquals("option1", selected);
 }

 @Test
 void testSetSelectedAlreadySelected() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1", true);
 Option option2 = new Option("option2", "description2", false);
 Option option3 = new Option("option3", "description3", false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 optionGroup.setSelected(option1);

 Assertions.assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
 }

 @Test
 void testIsRequired() {
 OptionGroup optionGroup = new OptionGroup();
 OptionGroup requiredOptionGroup = new OptionGroup();
 optionGroup.setRequired(false);
 requiredOptionGroup.setRequired(true);

 Assertions.assertFalse(optionGroup.isRequired());
 Assertions.assertTrue(requiredOptionGroup.isRequired());
 }

 @Test
 void testToString() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1", true);
 Option option2 = new Option("option2", "description2", false);
 Option option3 = new Option("option3", "description3", false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.addOption(option3);

 String actual = optionGroup.toString();
 String expected = "[--option1 description1, --option2 description2, --option3 description3]";

 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testToStringWithNullOptions() {
 OptionGroup optionGroup = new OptionGroup();

 String actual = optionGroup.toString();
 String expected = "[ ]";

 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testToStringWithEmptyOptions() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = new Option("option", "description", false);

 optionGroup.addOption(option);

 String actual = optionGroup.toString();
 String expected = "[--option description]";

 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testToStringWithShortOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = new Option("o", "description", false);

 optionGroup.addOption(option);

 String actual = optionGroup.toString();
 String expected = "[--o description]";

 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testToStringWithLongOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option = new Option("long-option", "description", false);

 optionGroup.addOption(option);

 String actual = optionGroup.toString();
 String expected = "[--long-option description]";

 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testToStringWithLongOptionAndShortOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option shortOption = new Option("s", "short option description", false);
 Option longOption = new Option("long-option", "long option description", false);

 optionGroup.addOption(shortOption);
 optionGroup.addOption(longOption);

 String actual = optionGroup.toString();
 String expected = "[--long-option long option description, -s short option description]";

 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testToStringWithSelectedOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option option1 = new Option("option1", "description1", false);
 Option option2 = new Option("option2", "description2", false);
 Option selectedOption = new Option("option3", "description3", false);

 optionGroup.addOption(option1);
 optionGroup.addOption(option2);
 optionGroup.setSelected(selectedOption);

 String actual = optionGroup.toString();
 String expected = "[--option3 description3, --option1 description1, --option2 description2]";

 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testToStringWithSelectedOptionAndLongOption() {
 OptionGroup optionGroup = new OptionGroup();
 Option shortOption = new Option("s", "short option description", false);
 Option longOption = new Option("long-option", "long option description", false);
 Option selectedOption = new Option("option3", "description3", false);

 optionGroup.addOption(shortOption);
 optionGroup.addOption(longOption);
 optionGroup.setSelected(selectedOption);

 String actual = optionGroup.toString();
 String expected = "[--option3 description3, --long-option long option description, -s short option description]";

 Assertions.assertEquals(expected, actual);
 }
}