package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

  @Test
  public void testFlatten_WithNoArguments_ReturnsEmptyArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {};
    Options options = new Options();

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(0, flattenedArguments.length);
  }

  @Test
  public void testFlatten_WithValidOption_ReturnsOptionInFlattenedArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"-a"};
    Options options = new Options();
    options.addOption("a", false, "Option A");

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(1, flattenedArguments.length);
    assertEquals("-a", flattenedArguments[0]);
  }

  @Test
  public void testFlatten_WithMultipleValidOptions_ReturnsOptionsInFlattenedArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"-a", "-b", "-c"};
    Options options = new Options();
    options.addOption("a", false, "Option A");
    options.addOption("b", false, "Option B");
    options.addOption("c", false, "Option C");

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(3, flattenedArguments.length);
    assertEquals("-a", flattenedArguments[0]);
    assertEquals("-b", flattenedArguments[1]);
    assertEquals("-c", flattenedArguments[2]);
  }

  @Test
  public void testFlatten_WithInvalidOption_IgnoresOptionAndReturnsSameArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"-a", "-b", "-c"};
    Options options = new Options();
    options.addOption("a", false, "Option A");
    options.addOption("c", false, "Option C");

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(2, flattenedArguments.length);
    assertEquals("-a", flattenedArguments[0]);
    assertEquals("-c", flattenedArguments[1]);
  }

  @Test
  public void testFlatten_WithOptionWithArgument_ReturnsOptionAndArgumentInFlattenedArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"-a", "value"};
    Options options = new Options();
    options.addOption("a", true, "Option A");

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(2, flattenedArguments.length);
    assertEquals("-a", flattenedArguments[0]);
    assertEquals("value", flattenedArguments[1]);
  }

  @Test
  public void testFlatten_WithOptionWithMultipleArguments_ReturnsOptionAndArgumentsInFlattenedArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"-a", "value1", "value2"};
    Options options = new Options();
    options.addOption("a", true, "Option A");

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(3, flattenedArguments.length);
    assertEquals("-a", flattenedArguments[0]);
    assertEquals("value1", flattenedArguments[1]);
    assertEquals("value2", flattenedArguments[2]);
  }

  @Test
  public void testFlatten_WithStopAtNonOptionTrue_ReturnsRemainingArgumentsIgnoringOptions() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"-a", "value1", "non-option", "-b", "value2"};
    Options options = new Options();
    options.addOption("a", true, "Option A");

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(2, flattenedArguments.length);
    assertEquals("non-option", flattenedArguments[0]);
    assertEquals("-b", flattenedArguments[1]);
  }

  @Test
  public void testFlatten_WithStopAtNonOptionFalse_ReturnsRemainingArgumentsIncludingOptions() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"-a", "value1", "non-option", "-b", "value2"};
    Options options = new Options();
    options.addOption("a", true, "Option A");

    String[] flattenedArguments = parser.flatten(options, arguments, false);

    assertEquals(5, flattenedArguments.length);
    assertEquals("-a", flattenedArguments[0]);
    assertEquals("value1", flattenedArguments[1]);
    assertEquals("non-option", flattenedArguments[2]);
    assertEquals("-b", flattenedArguments[3]);
    assertEquals("value2", flattenedArguments[4]);
  }

  @Test
  public void testFlatten_WithSpecialTokenStartsWithDoubleHyphen_AddsTokenAndValueToFlattenedArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"--option=value"};
    Options options = new Options();

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(2, flattenedArguments.length);
    assertEquals("--option", flattenedArguments[0]);
    assertEquals("value", flattenedArguments[1]);
  }

  @Test
  public void testFlatten_WithSpecialTokenStartsWithSingleHyphen_AddsTokenToFlattenedArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"-option"};
    Options options = new Options();

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(1, flattenedArguments.length);
    assertEquals("-option", flattenedArguments[0]);
  }

  @Test
  public void testFlatten_WithSpecialTokenStartsWithMultipleHyphens_AddsTokenToFlattenedArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"---option"};
    Options options = new Options();

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(1, flattenedArguments.length);
    assertEquals("---option", flattenedArguments[0]);
  }

  @Test
  public void testFlatten_WithSpecialTokenOnly_AddsTokenToFlattenedArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"--"};
    Options options = new Options();

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(1, flattenedArguments.length);
    assertEquals("--", flattenedArguments[0]);
  }

  @Test
  public void testFlatten_WithSpecialTokenAndValue_AddsTokenAndValueToFlattenedArray() {
    PosixParser parser = new PosixParser();
    String[] arguments = {"--option", "value"};
    Options options = new Options();

    String[] flattenedArguments = parser.flatten(options, arguments, true);

    assertEquals(2, flattenedArguments.length);
    assertEquals("--option", flattenedArguments[0]);
    assertEquals("value", flattenedArguments[1]);
  }

}