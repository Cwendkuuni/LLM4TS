package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

   @Test
   public void testWithLongOpt() {
       OptionBuilder.withLongOpt("myLongOpt");
       Option option = OptionBuilder.create();
       assertEquals("myLongOpt", option.getLongOpt());
   }

   @Test
   public void testHasArg() {
       OptionBuilder.hasArg();
       Option option = OptionBuilder.create();
       assertTrue(option.hasArg());
       assertEquals(1, option.getArgs());
   }

   @Test
   public void testHasArgFalse() {
       OptionBuilder.hasArg(false);
       Option option = OptionBuilder.create();
       assertFalse(option.hasArg());
       assertEquals(-1, option.getArgs());
   }

   @Test
   public void testWithArgName() {
       OptionBuilder.withArgName("myArg");
       Option option = OptionBuilder.create();
       assertEquals("myArg", option.getArgName());
   }

   @Test
   public void testIsRequired() {
       OptionBuilder.isRequired();
       Option option = OptionBuilder.create();
       assertTrue(option.isRequired());
   }

   @Test
   public void testWithValueSeparator() {
       OptionBuilder.withValueSeparator(':');
       Option option = OptionBuilder.create();
       assertEquals(':', option.getValueSeparator());
   }

   @Test
   public void testWithValueSeparatorDefault() {
       OptionBuilder.withValueSeparator();
       Option option = OptionBuilder.create();
       assertEquals('=', option.getValueSeparator());
   }

   @Test
   public void testIsRequiredFalse() {
       OptionBuilder.isRequired(false);
       Option option = OptionBuilder.create();
       assertFalse(option.isRequired());
   }

   @Test
   public void testHasArgs() {
       OptionBuilder.hasArgs();
       Option option = OptionBuilder.create();
       assertEquals(Integer.MAX_VALUE, option.getArgs());
   }

   @Test
   public void testHasArgsSpecific() {
       OptionBuilder.hasArgs(3);
       Option option = OptionBuilder.create();
       assertEquals(3, option.getArgs());
   }

   @Test
   public void testHasOptionalArg() {
       OptionBuilder.hasOptionalArg();
       Option option = OptionBuilder.create();
       assertTrue(option.hasArg());
       assertTrue(option.hasOptionalArg());
   }

   @Test
   public void testHasOptionalArgs() {
       OptionBuilder.hasOptionalArgs();
       Option option = OptionBuilder.create();
       assertEquals(Integer.MAX_VALUE, option.getArgs());
       assertTrue(option.hasOptionalArg());
   }

   @Test
   public void testHasOptionalArgsSpecific() {
       OptionBuilder.hasOptionalArgs(5);
       Option option = OptionBuilder.create();
       assertEquals(5, option.getArgs());
       assertTrue(option.hasOptionalArg());
   }

   @Test
   public void testWithType() {
       OptionBuilder.withType(String.class);
       Option option = OptionBuilder.create();
       assertEquals(String.class, option.getType());
   }

   @Test
   public void testWithDescription() {
       OptionBuilder.withDescription("myDescription");
       Option option = OptionBuilder.create();
       assertEquals("myDescription", option.getDescription());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreateCharInvalid() {
       OptionBuilder.create('$');
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreateNoLongOpt() {
       OptionBuilder.create();
   }

   @Test
   public void testCreateChar() {
       OptionBuilder.withLongOpt("myLongOpt");
       Option option = OptionBuilder.create('a');
       assertEquals('a', option.getOpt());
       assertEquals("myLongOpt", option.getLongOpt());
   }

   @Test
   public void testCreateString() {
       Option option = OptionBuilder.create("myOpt");
       assertEquals("myOpt", option.getOpt());
       assertNull(option.getLongOpt());
   }

}