package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

   private PosixParser parser;

   @Before
   public void setup() {
      parser = new PosixParser();
   }

   @Test
   public void testFlatten_WithNoArguments_ShouldReturnEmptyArray() {
      Options options = new Options();
      String[] arguments = {};
      String[] expected = {};

      String[] result = parser.flatten(options, arguments, false);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_WithNoOptions_ShouldReturnSameArgumentsArray() {
      Options options = new Options();
      String[] arguments = {"arg1", "arg2"};
      String[] expected = {"arg1", "arg2"};

      String[] result = parser.flatten(options, arguments, false);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_WithStopAtNonOption_ShouldReturnArgumentsBeforeNonOption() {
      Options options = new Options();
      options.addOption("a", false, "option a");
      options.addOption("b", false, "option b");
      options.addOption("c", false, "option c");

      String[] arguments = {"arg1", "-a", "arg2", "-b", "arg3", "-c", "arg4"};
      String[] expected = {"arg1", "-a", "arg2"};

      String[] result = parser.flatten(options, arguments, true);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_WithLongOptionWithEqualSign_ShouldSeparateOptionAndValue() {
      Options options = new Options();
      options.addOption("o", true, "option with value");

      String[] arguments = {"-o=value"};
      String[] expected = {"-o", "value"};

      String[] result = parser.flatten(options, arguments, false);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_WithLongOptionWithoutEqualSign_ShouldNotSeparateOptionAndValue() {
      Options options = new Options();
      options.addOption("o", true, "option with value");

      String[] arguments = {"-ovalue"};
      String[] expected = {"-ovalue"};

      String[] result = parser.flatten(options, arguments, false);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_WithSingleHyphen_ShouldReturnSameArray() {
      Options options = new Options();
      String[] arguments = {"-"};
      String[] expected = {"-"};

      String[] result = parser.flatten(options, arguments, false);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_WithInvalidOptionAndStopAtNonOption_ShouldReturnArgumentsIncludingInvalidOption() {
      Options options = new Options();
      options.addOption("a", false, "option a");

      String[] arguments = {"-x", "arg1", "-a", "arg2"};
      String[] expected = {"-x", "arg1", "-a", "arg2"};

      String[] result = parser.flatten(options, arguments, true);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_WithInvalidOptionAndNotStopAtNonOption_ShouldReturnArgumentsIncludingInvalidOption() {
      Options options = new Options();
      options.addOption("a", false, "option a");

      String[] arguments = {"-x", "arg1", "-a", "arg2"};
      String[] expected = {"-x", "-a", "arg1", "arg2"};

      String[] result = parser.flatten(options, arguments, false);

      assertArrayEquals(expected, result);
   }

   @Test
   public void testFlatten_WithBurstingRequired_ShouldSeparateOptions() {
      Options options = new Options();
      options.addOption("a", false, "option a");
      options.addOption("b", false, "option b");
      options.addOption("c", false, "option c");

      String[] arguments = {"-abc"};
      String[] expected = {"-a", "-b", "-c"};

      String[] result = parser.flatten(options, arguments, false);

      assertArrayEquals(expected, result);
   }

}