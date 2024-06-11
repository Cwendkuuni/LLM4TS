package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    Option option = OptionBuilder.withLongOpt("testLongOpt").create();
    Assert.assertEquals("testLongOpt", option.getLongOpt());
  }

  @Test
  public void testHasArg() {
    Option option = OptionBuilder.hasArg().create();
    Assert.assertEquals(1, option.getArgs());
  }

  @Test
  public void testHasArgWithValueFalse() {
    Option option = OptionBuilder.hasArg(false).create();
    Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
  }

  @Test
  public void testWithArgName() {
    Option option = OptionBuilder.withArgName("testArgName").create();
    Assert.assertEquals("testArgName", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    Option option = OptionBuilder.isRequired().create();
    Assert.assertTrue(option.isRequired());
  }

  @Test
  public void testWithOptionalArg() {
    Option option = OptionBuilder.hasOptionalArg().create();
    Assert.assertTrue(option.hasOptionalArg());
    Assert.assertEquals(1, option.getArgs());
  }

  @Test
  public void testWithMultipleOptionalArgs() {
    Option option = OptionBuilder.hasOptionalArgs(3).create();
    Assert.assertTrue(option.hasOptionalArg());
    Assert.assertEquals(3, option.getArgs());
  }

  @Test
  public void testWithMultipleOptionalArgsAndType() {
    Option option = OptionBuilder.hasOptionalArgs(2).withType(Integer.class).create();
    Assert.assertTrue(option.hasOptionalArg());
    Assert.assertEquals(2, option.getArgs());
    Assert.assertEquals(Integer.class, option.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }

  @Test
  public void testCreateWithCharOpt() {
    Option option = OptionBuilder.create('t');
    Assert.assertEquals("t", option.getOpt());
  }

  @Test
  public void testCreateWithStringOpt() {
    Option option = OptionBuilder.create("testOpt");
    Assert.assertEquals("testOpt", option.getOpt());
  }

  @Test
  public void testCreateWithDescription() {
    Option option = OptionBuilder.withDescription("testDescription").create();
    Assert.assertEquals("testDescription", option.getDescription());
  }

  @Test
  public void testWithValueSeparator() {
    Option option = OptionBuilder.withValueSeparator(':').create();
    Assert.assertEquals(':', option.getValueSeparator());
  }

  @Test
  public void testWithValueSeparatorDefault() {
    Option option = OptionBuilder.withValueSeparator().create();
    Assert.assertEquals('=', option.getValueSeparator());
  }

  @Test
  public void testIsRequiredWithValueFalse() {
    Option option = OptionBuilder.isRequired(false).create();
    Assert.assertFalse(option.isRequired());
  }

  @Test
  public void testHasArgs() {
    Option option = OptionBuilder.hasArgs().create();
    Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }

  @Test
  public void testHasArgsWithValue() {
    Option option = OptionBuilder.hasArgs(2).create();
    Assert.assertEquals(2, option.getArgs());
  }

  @Test
  public void testCreateWithType() {
    Option option = OptionBuilder.withType(Double.class).create();
    Assert.assertEquals(Double.class, option.getType());
  }
}