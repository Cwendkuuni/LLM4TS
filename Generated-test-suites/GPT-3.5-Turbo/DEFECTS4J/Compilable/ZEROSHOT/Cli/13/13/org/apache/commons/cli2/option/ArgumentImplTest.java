package org.apache.commons.cli2.option;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ArgumentImplTest {
  
  private ArgumentImpl argument;

  @Before
  public void setUp() {
    argument = new ArgumentImpl("arg", "description", 0, Integer.MAX_VALUE, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, ArgumentImpl.DEFAULT_CONSUME_REMAINING, null, 0);
  }

  @Test
  public void testGetPreferredName() {
    assertEquals("arg", argument.getPreferredName());
  }

  @Test
  public void testGetInitialSeparator() {
    assertEquals('\0', argument.getInitialSeparator());
  }

  @Test
  public void testGetSubsequentSeparator() {
    assertEquals('\0', argument.getSubsequentSeparator());
  }

  @Test
  public void testGetConsumeRemaining() {
    assertEquals("--", argument.getConsumeRemaining());
  }

  @Test
  public void testGetDefaultValues() {
    assertNull(argument.getDefaultValues());
  }

  @Test
  public void testGetValidator() {
    assertNull(argument.getValidator());
  }

  @Test
  public void testIsRequired() {
    assertFalse(argument.isRequired());
  }

  @Test
  public void testProcess() {
    // To be tested
  }

  @Test
  public void testProcessValues() {
    // To be tested
  }

  @Test
  public void testCanProcess() {
    assertTrue(argument.canProcess(null, ""));
  }

  @Test
  public void testGetPrefixes() {
    assertNotNull(argument.getPrefixes());
  }

  @Test
  public void testGetTriggers() {
    assertNotNull(argument.getTriggers());
  }

  @Test
  public void testValidate() {
    // To be tested
  }

  @Test
  public void testValidate2() {
    // To be tested
  }

  @Test
  public void testAppendUsage() {
    StringBuffer buffer = new StringBuffer();
    argument.appendUsage(buffer, null, null);
    // To be tested
  }

  @Test
  public void testGetDescription() {
    assertEquals("description", argument.getDescription());
  }

  @Test
  public void testHelpLines() {
    assertNotNull(argument.helpLines(0, null, null));
  }

  @Test
  public void testGetMaximum() {
    assertEquals(Integer.MAX_VALUE, argument.getMaximum());
  }

  @Test
  public void testGetMinimum() {
    assertEquals(0, argument.getMinimum());
  }

  @Test
  public void testStripBoundaryQuotes() {
    assertEquals("token", argument.stripBoundaryQuotes("\"token\""));
  }

  @Test
  public void testDefaults() {
    // To be tested
  }

  @Test
  public void testDefaultValues() {
    // To be tested
  }
}