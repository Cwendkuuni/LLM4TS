package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {
  
  // Helper method to compare strings
  private void assertStringEquals(String expected, String actual) {
    if (expected == null && actual == null) {
      return;
    }
    assertNotNull("Expected string is null", expected);
    assertNotNull("Actual string is null", actual);
    assertEquals(expected, actual);
  }
  
  // --- Test hasArg() ---
  @Test
  public void testHasArg() {
    Option option = OptionBuilder.hasArg().create('t');
    assertTrue(option.hasArg());
  }
  
  // --- Test hasArg(boolean) ---
  @Test
  public void testHasArgWithBooleanTrue() {
    Option option = OptionBuilder.hasArg(true).create('t');
    assertTrue(option.hasArg());
  }
  
  @Test
  public void testHasArgWithBooleanFalse() {
    Option option = OptionBuilder.hasArg(false).create('t');
    assertFalse(option.hasArg());
  }
  
  // --- Test withArgName() ---
  @Test
  public void testWithArgName() {
    String name = "argName";
    Option option = OptionBuilder.withArgName(name).create('t');
    assertStringEquals(name, option.getArgName());
  }
  
  // --- Test isRequired() ---
  @Test
  public void testIsRequired() {
    Option option = OptionBuilder.isRequired().create('t');
    assertTrue(option.isRequired());
  }
  
  // --- Test withValueSeparator(char) ---
  @Test
  public void testWithValueSeparator() {
    char sep = ':';
    Option option = OptionBuilder.withValueSeparator(sep).create('t');
    assertEquals(sep, option.getValueSeparator());
  }

  // --- Test withValueSeparator() ---
  @Test
  public void testWithValueSeparatorDefault() {
    Option option = OptionBuilder.withValueSeparator().create('t');
    assertEquals('=', option.getValueSeparator());
  }
  
  // --- Test isRequired(boolean) ---
  @Test
  public void testIsRequiredWithBooleanTrue() {
    Option option = OptionBuilder.isRequired(true).create('t');
    assertTrue(option.isRequired());
  }
  
  @Test
  public void testIsRequiredWithBooleanFalse() {
    Option option = OptionBuilder.isRequired(false).create('t');
    assertFalse(option.isRequired());
  }
  
  // --- Test hasArgs() ---
  @Test
  public void testHasArgs() {
    Option option = OptionBuilder.hasArgs().create('t');
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  // --- Test hasArgs(int) ---
  @Test
  public void testHasArgsWithNum() {
    int num = 2;
    Option option = OptionBuilder.hasArgs(num).create('t');
    assertEquals(num, option.getArgs());
  }
  
  // --- Test hasOptionalArg() ---
  @Test
  public void testHasOptionalArg() {
    Option option = OptionBuilder.hasOptionalArg().create('t');
    assertTrue(option.hasOptionalArg());
    assertTrue(option.hasArg());
  }
  
  // --- Test hasOptionalArgs() ---
  @Test
  public void testHasOptionalArgs() {
    Option option = OptionBuilder.hasOptionalArgs().create('t');
    assertTrue(option.hasOptionalArg());
    assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  // --- Test hasOptionalArgs(int) ---
  @Test
  public void testHasOptionalArgsWithNum() {
    int num = 3;
    Option option = OptionBuilder.hasOptionalArgs(num).create('t');
    assertTrue(option.hasOptionalArg());
    assertEquals(num, option.getArgs());
  }
  
  // --- Test withType() ---
  @Test
  public void testWithType() {
    Object type = new Object();
    Option option = OptionBuilder.withType(type).create('t');
    assertSame(type, option.getType());
  }
  
  // --- Test withDescription() ---
  @Test
  public void testWithDescription() {
    String desc = "description";
    Option option = OptionBuilder.withDescription(desc).create('t');
    assertStringEquals(desc, option.getDescription());
  }
  
  // --- Test create(char) ---
  @Test
  public void testCreateWithChar() {
    char opt = 't';
    Option option = OptionBuilder.create(opt);
    assertEquals(String.valueOf(opt), option.getOpt());
  }
  
  // --- Test create() ---
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }
  
  @Test
  public void testCreateWithLongOpt() {
    String longOpt = "test";
    OptionBuilder.withLongOpt(longOpt);
    Option option = OptionBuilder.create();
    assertStringEquals(longOpt, option.getLongOpt());
  }
  
  // --- Test create(String) ---
  @Test
  public void testCreateWithString() {
    String opt = "t";
    Option option = OptionBuilder.create(opt);
    assertStringEquals(opt, option.getOpt());
  }
}