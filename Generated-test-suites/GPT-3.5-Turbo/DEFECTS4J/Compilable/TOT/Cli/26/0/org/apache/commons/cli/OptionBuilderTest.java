package org.apache.commons.cli;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest {

   @Test
   public void testWithLongOpt() {
      OptionBuilder.withLongOpt("longopt");
      Option option = OptionBuilder.create();
      assert(option.getLongOpt().equals("longopt"));
   }

   @Test
   public void testHasArg() {
      OptionBuilder.hasArg();
      Option option = OptionBuilder.create();
      assert(option.getArgs() == 1);
   }

   @Test
   public void testHasArgWithFalse() {
      OptionBuilder.hasArg(false);
      Option option = OptionBuilder.create();
      assert(option.getArgs() == Option.UNINITIALIZED);
   }

   @Test
   public void testWithArgName() {
      OptionBuilder.withArgName("argName");
      Option option = OptionBuilder.create();
      assert(option.getArgName().equals("argName"));
   }

   @Test
   public void testIsRequired() {
      OptionBuilder.isRequired();
      Option option = OptionBuilder.create();
      assert(option.isRequired());
   }

   @Test
   public void testWithSeparator() {
      OptionBuilder.withValueSeparator(':');
      Option option = OptionBuilder.create();
      assert(option.getValueSeparator() == ':');
   }

   @Test
   public void testWithSeparatorDefault() {
      OptionBuilder.withValueSeparator();
      Option option = OptionBuilder.create();
      assert(option.getValueSeparator() == '=');
   }

   @Test
   public void testIsRequiredWithFalse() {
      OptionBuilder.isRequired(false);
      Option option = OptionBuilder.create();
      assert(option.isRequired() == false);
   }

   @Test
   public void testHasArgs() {
      OptionBuilder.hasArgs();
      Option option = OptionBuilder.create();
      assert(option.getArgs() == Option.UNLIMITED_VALUES);
   }

   @Test
   public void testHasArgsWithNum() {
      OptionBuilder.hasArgs(3);
      Option option = OptionBuilder.create();
      assert(option.getArgs() == 3);
   }

   @Test
   public void testHasOptionalArg() {
      OptionBuilder.hasOptionalArg();
      Option option = OptionBuilder.create();
      assert(option.getArgs() == 1);
      assert(option.hasOptionalArg());
   }

   @Test
   public void testHasOptionalArgs() {
      OptionBuilder.hasOptionalArgs();
      Option option = OptionBuilder.create();
      assert(option.getArgs() == Option.UNLIMITED_VALUES);
      assert(option.hasOptionalArg());
   }

   @Test
   public void testHasOptionalArgsWithNum() {
      OptionBuilder.hasOptionalArgs(2);
      Option option = OptionBuilder.create();
      assert(option.getArgs() == 2);
      assert(option.hasOptionalArg());
   }

   @Test
   public void testWithType() {
      OptionBuilder.withType(Object.class);
      Option option = OptionBuilder.create();
      assert(option.getType() == Object.class);
   }

   @Test
   public void testWithDescription() {
      OptionBuilder.withDescription("description");
      Option option = OptionBuilder.create();
      assert(option.getDescription().equals("description"));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreateWithChar() {
      OptionBuilder.create('o');
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreateWithoutLongOpt() {
      OptionBuilder.create();
   }

   @Test
   public void testCreateWithOpt() {
      OptionBuilder.withLongOpt("longopt");
      Option option = OptionBuilder.create("opt");
      assert(option.getOpt().equals("opt"));
   }

   @Test
   public void testCreateWithOptAndLongOpt() {
      OptionBuilder.withLongOpt("longopt");
      Option option = OptionBuilder.create("opt");
      assert(option.getOpt().equals("opt"));
      assert(option.getLongOpt().equals("longopt"));
   }
}