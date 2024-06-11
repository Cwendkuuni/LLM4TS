package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlattenEmptyArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {};
 String[] expected = {};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenSingleOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", false, "a option");
 String[] arguments = {"-a"};
 String[] expected = {"-a"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenSingleValueOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "a option");
 String[] arguments = {"-a", "value"};
 String[] expected = {"-a", "value"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMultiValueOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "a option");
 String[] arguments = {"-a", "value1", "-a", "value2"};
 String[] expected = {"-a", "value1", "-a", "value2"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOption", false, "long option");
 String[] arguments = {"--longOption"};
 String[] expected = {"--longOption"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongValueOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOption", true, "long option");
 String[] arguments = {"--longOption", "value"};
 String[] expected = {"--longOption", "value"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongMultiValueOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOption", true, "long option");
 String[] arguments = {"--longOption", "value1", "--longOption", "value2"};
 String[] expected = {"--longOption", "value1", "--longOption", "value2"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMixedOptionsAndArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "a option");
 options.addOption("b", false, "b option");
 String[] arguments = {"-a", "value1", "-b", "value2"};
 String[] expected = {"-a", "value1", "-b"};
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {"-x"};
 parser.flatten(options, arguments, false);
 fail("Expected ParseException to be thrown");
 }

 @Test
 public void testFlattenStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "a option");
 String[] arguments = {"-a", "--optionX", "value"};
 String[] expected = {"-a", "--optionX"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOptionWithArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "a option");
 String[] arguments = {"-a", "--optionX", "value1", "--optionX", "value2"};
 String[] expected = {"-a", "--optionX", "--optionX"};
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }
}