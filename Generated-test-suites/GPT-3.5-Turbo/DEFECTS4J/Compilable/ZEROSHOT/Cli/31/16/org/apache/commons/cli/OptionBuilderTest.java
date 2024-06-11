package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class OptionBuilderTest {
  
  Option option;
  
  @Before
  public void setUp() {
    option = null;
  }
  
  @Test
  public void testWithLongOpt() {
    option = OptionBuilder.withLongOpt("testLongOpt").create();
    assertEquals("testLongOpt", option.getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    option = OptionBuilder.hasArg().create();
    assertEquals(true, option.hasArg());
  }
  
  @Test
  public void testHasArgFalse() {
    option = OptionBuilder.hasArg(false).create();
    assertEquals(false, option.hasArg());
  }
  
  @Test
  public void testWithArgName() {
    option = OptionBuilder.withArgName("testArg").create();
    assertEquals("testArg", option.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    option = OptionBuilder.isRequired().create();
    assertEquals(true, option.isRequired());
  }
  
  @Test
  public void testWithSeparatorChar() {
    option = OptionBuilder.withValueSeparator(':').create();
    assertEquals(':', option.getValueSeparator());
  }
  
  @Test
  public void testWithSeparator() {
    option = OptionBuilder.withValueSeparator().create();
    assertEquals('=', option.getValueSeparator());
  }
  
  @Test
  public void testIsRequiredFalse() {
    option = OptionBuilder.isRequired(false).create();
    assertEquals(false, option.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    option = OptionBuilder.hasArgs().create();
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  @Test
  public void testHasArgsNum() {
    option = OptionBuilder.hasArgs(5).create();
    assertEquals(5, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    option = OptionBuilder.hasOptionalArg().create();
    assertEquals(true, option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    option = OptionBuilder.hasOptionalArgs().create();
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    assertEquals(true, option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsNum() {
    option = OptionBuilder.hasOptionalArgs(3).create();
    assertEquals(3, option.getArgs());
    assertEquals(true, option.hasOptionalArg());
  }
  
  @Test
  public void testWithType() {
    option = OptionBuilder.withType(Integer.class).create();
    assertEquals(Integer.class, option.getType());
  }
  
  @Test
  public void testWithDescription() {
    option = OptionBuilder.withDescription("testDesc").create();
    assertEquals("testDesc", option.getDescription());
  }
  
  @Test
  public void testCreateChar() {
    option = OptionBuilder.create('a');
    assertEquals("a", option.getOpt());
  }
  
  @Test
  public void testCreateNoOpt() {
    option = OptionBuilder.create();
    assertEquals(null, option.getOpt());
  }
  
  @Test
  public void testCreateString() {
    option = OptionBuilder.create("testOpt");
    assertEquals("testOpt", option.getOpt());
  }
  
}