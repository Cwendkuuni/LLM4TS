package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithDefaults() {
 OptionBuilder optionBuilder = OptionBuilder.create();
 Option option = optionBuilder.create('testOption');

 assertNotNull(option);
 assertEquals("", option.getLongOpt());
 assertEquals("testOption", option.getOpt());
 assertEquals("", option.getDescription());
 assertEquals(Option.NO_ARGUMENTS, option.getNumberOfArgs());
 assertEquals(Option.TYPE_NONE, option.getType());
 assertNull(option.getValue());
 assertNull(option.getValueSeparator());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 }

 @Test
 public void testOptionBuilderWithValues() {
 OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
 .isRequired()
 .hasArg("testArg")
 .withDescription("Test Option")
 .hasOptionalArg()
 .withValueSeparator(':')
 .withType(int.class);
 Option option = optionBuilder.create('testOption');

 assertEquals("testLongOpt", option.getLongOpt());
 assertEquals("Test Option", option.getDescription());
 assertEquals("testOption", option.getOpt());
 assertEquals("testArg", option.getArgName());
 assertEquals(Option.VALUE_SEPARATOR_COLON, option.getValueSeparator());
 assertEquals(int.class, option.getType());
 assertFalse(option.isRequired());
 assertTrue(option.hasOptionalArg());
 }
}