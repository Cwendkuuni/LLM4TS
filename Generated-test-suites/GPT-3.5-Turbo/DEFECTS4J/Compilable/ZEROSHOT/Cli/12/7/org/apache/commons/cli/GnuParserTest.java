package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class GnuParserTest {

  @Test
  public void testFlatten_WithNoOptions_ReturnsSameArguments() {
    Options options = new Options();
    String[] arguments = {"arg1", "arg2"};
    GnuParser parser = new GnuParser();
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(arguments, result);
  }

  @Test
  public void testFlatten_WithOptionAndNoArgument_ReturnsOptionAndRestOfArguments() {
    Options options = new Options();
    options.addOption("a", false, "Test option");
    String[] arguments = {"-a", "arg1", "arg2"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-a", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithOptionAndNoMatchingOption_ReturnsOptionAndRestOfArguments() {
    Options options = new Options();
    String[] arguments = {"-a", "arg1", "arg2"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-a", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithOptionAndMatchingOption_ReturnsOptionOnly() {
    Options options = new Options();
    options.addOption("a", false, "Test option");
    String[] arguments = {"-a"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-a"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithOptionAndMatchingOptionAndValue_ReturnsOptionAndValue() {
    Options options = new Options();
    options.addOption("a", true, "Test option");
    String[] arguments = {"-a", "value"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-a", "value"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithOptionAndMatchingOptionAndNoValue_ReturnsOptionOnly() {
    Options options = new Options();
    options.addOption("a", true, "Test option");
    String[] arguments = {"-a"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-a"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithOptionAndMatchingOptionAndNoValue_StopAtNonOption_ReturnsOptionOnly() {
    Options options = new Options();
    options.addOption("a", true, "Test option");
    String[] arguments = {"-a", "nonOption"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-a"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithOptionAndMatchingOptionAndNoValue_NotStopAtNonOption_ReturnsOptionAndNonOption() {
    Options options = new Options();
    options.addOption("a", true, "Test option");
    String[] arguments = {"-a", "nonOption"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-a", "nonOption"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithDoubleDash_StopFlattening_ReturnsDoubleDashAndRestOfArguments() {
    Options options = new Options();
    String[] arguments = {"--", "arg1", "arg2"};
    GnuParser parser = new GnuParser();
    String[] expected = {"--", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithSingleDash_ReturnsSingleDash() {
    Options options = new Options();
    String[] arguments = {"-"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithArgument_ReturnsSameArgument() {
    Options options = new Options();
    String[] arguments = {"arg"};
    GnuParser parser = new GnuParser();
    String[] expected = {"arg"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithStopAtNonOption_ReturnsOptionAndRestOfArguments() {
    Options options = new Options();
    options.addOption("a", false, "Test option");
    String[] arguments = {"-a", "nonOption"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-a", "nonOption"};
    String[] result = parser.flatten(options, arguments, true);
    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlatten_WithoutStopAtNonOption_ReturnsAllArguments() {
    Options options = new Options();
    options.addOption("a", false, "Test option");
    String[] arguments = {"-a", "nonOption"};
    GnuParser parser = new GnuParser();
    String[] expected = {"-a", "nonOption"};
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(expected, result);
  }

}