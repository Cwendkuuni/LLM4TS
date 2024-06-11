package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {

  @Test
  public void testWithLongOpt() {
    Option option = OptionBuilder.withLongOpt("testLongOpt").create('t');
    
    Assert.assertEquals("testLongOpt", option.getLongOpt());
    Assert.assertEquals('t', option.getOpt());
  }

  @Test
  public void testHasArg() {
    Option option = OptionBuilder.hasArg().create('a');
    
    Assert.assertEquals(1, option.getArgs());
    Assert.assertTrue(option.hasArg());
  }

  @Test
  public void testHasArgWithSpecifiedArgValue() {
    Option option = OptionBuilder.hasArg(true).create('b');
    
    Assert.assertEquals(1, option.getArgs());
    Assert.assertTrue(option.hasArg());
  }

  @Test
  public void testWithArgName() {
    Option option = OptionBuilder.withArgName("testArg").create('c');
    
    Assert.assertEquals("testArg", option.getArgName());
  }

  @Test
  public void testIsRequired() {
    Option option = OptionBuilder.isRequired().create('r');

    Assert.assertTrue(option.isRequired());
  }

  @Test
  public void testWithOptionalArgValueSeparatorChar() {
    Option option = OptionBuilder.withValueSeparator(':').create('o');
    
    Assert.assertEquals(':', option.getValueSeparator());
  }

  @Test
  public void testWithOptionalArgValueSeparatorEquals() {
    Option option = OptionBuilder.withValueSeparator().create('e');
    
    Assert.assertEquals('=', option.getValueSeparator());
  }

  @Test
  public void testIsRequiredWithSpecifiedFlag() {
    Option option = OptionBuilder.isRequired(true).create('f');
    
    Assert.assertTrue(option.isRequired());
  }

  @Test
  public void testHasArgs() {
    Option option = OptionBuilder.hasArgs().create('s');
    
    Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    Assert.assertTrue(option.hasArgs());
  }

  @Test
  public void testHasArgsWithSpecifiedNum() {
    Option option = OptionBuilder.hasArgs(2).create('n');

    Assert.assertEquals(2, option.getArgs());
    Assert.assertTrue(option.hasArgs());
  }

  @Test
  public void testHasOptionalArg() {
    Option option = OptionBuilder.hasOptionalArg().create('p');
    
    Assert.assertEquals(1, option.getArgs());
    Assert.assertTrue(option.hasArg());
    Assert.assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgs() {
    Option option = OptionBuilder.hasOptionalArgs().create('q');
    
    Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    Assert.assertTrue(option.hasArgs());
    Assert.assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testHasOptionalArgsWithSpecifiedNumArgs() {
    Option option = OptionBuilder.hasOptionalArgs(3).create('m');

    Assert.assertEquals(3, option.getArgs());
    Assert.assertTrue(option.hasArgs());
    Assert.assertTrue(option.hasOptionalArg());
  }

  @Test
  public void testWithType() {
    Option option = OptionBuilder.withType(String.class).create('y');
    
    Assert.assertEquals(String.class, option.getType());
  }

  @Test
  public void testWithDescription() {
    Option option = OptionBuilder.withDescription("testDescription").create('d');
    
    Assert.assertEquals("testDescription", option.getDescription());
  }

  @Test
  public void testCreateWithChar() {
    Option option = OptionBuilder.create('x');
    
    Assert.assertEquals("x", option.getOpt());
  }

  @Test
  public void testCreate() {
    try {
      OptionBuilder.create();
      Assert.fail("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("must specify longopt", e.getMessage());
    }
  }

  @Test
  public void testCreateWithString() {
    Option option = OptionBuilder.create("testOpt");

    Assert.assertEquals("testOpt", option.getOpt());
  }
}