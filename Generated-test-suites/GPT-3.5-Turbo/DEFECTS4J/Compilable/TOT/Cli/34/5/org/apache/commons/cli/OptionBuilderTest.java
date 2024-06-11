package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Assert;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("longopt");
    Option option = OptionBuilder.create();
    Assert.assertEquals("longopt", option.getLongOpt());
  }

  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    Option option = OptionBuilder.create();
    Assert.assertTrue(option.hasArg());
  }

  @Test
  public void testHasArgWithFalse() {
    OptionBuilder.hasArg(false);
    Option option = OptionBuilder.create();
    Assert.assertFalse(option.hasArg());
  }

  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("argName");
    Option option = OptionBuilder.create();
    Assert.assertEquals("argName", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    Assert.assertTrue(option.isRequired());
  }

  @Test
  public void testWithOptionalArg() {
    OptionBuilder.hasOptionalArg();
    Option option = OptionBuilder.create();
    Assert.assertTrue(option.hasArg());
    Assert.assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testWithMultipleArgs() {
    OptionBuilder.hasArgs(2);
    Option option = OptionBuilder.create();
    Assert.assertTrue(option.hasArg());
    Assert.assertEquals(2, option.getArgs());
  }

  @Test
  public void testWithUnlimitedArgs() {
    OptionBuilder.hasArgs(Option.UNLIMITED_VALUES);
    Option option = OptionBuilder.create();
    Assert.assertTrue(option.hasArg());
    Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testWithOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    Option option = OptionBuilder.create();
    Assert.assertTrue(option.hasArg());
    Assert.assertTrue(option.hasOptionalArg());
    Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testWithOptionalArgsQuantity() {
    OptionBuilder.hasOptionalArgs(2);
    Option option = OptionBuilder.create();
    Assert.assertTrue(option.hasArg());
    Assert.assertTrue(option.hasOptionalArg());
    Assert.assertEquals(2, option.getArgs());
  }

  @Test
  public void testWithType() {
    OptionBuilder.withType(Integer.class);
    Option option = OptionBuilder.create();
    Assert.assertEquals(Integer.class, option.getType());
  }

  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("description");
    Option option = OptionBuilder.create();
    Assert.assertEquals("description", option.getDescription());
  }

}