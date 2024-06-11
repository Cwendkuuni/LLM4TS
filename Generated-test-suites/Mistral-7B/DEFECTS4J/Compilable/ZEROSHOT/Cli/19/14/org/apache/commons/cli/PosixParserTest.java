package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 @Test
 public void testEmptyArguments() throws ParseException {
 Options options = new Options();
 String[] arguments = new String[0];
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[0], parsedArguments);
 }

 @Test
 public void testSingleArgument() throws ParseException {
 Options options = new Options();
 String[] arguments = {"arg1"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"arg1"}, parsedArguments);
 }

 @Test
 public void testDoubleHyphen() throws ParseException {
 Options options = new Options();
 String[] arguments = {"--"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"--"}, parsedArguments);
 }

 @Test
 public void testSingleOption() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "o");
 String[] arguments = {"-o"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"-o"}, parsedArguments);
 }

 @Test
 public void testSingleOptionWithValue() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "o");
 String[] arguments = {"-o", "value"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"-o", "value"}, parsedArguments);
 }

 @Test
 public void testMultipleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "o");
 options.addOption("option2", true, "i");
 String[] arguments = {"-oi", "value1", "-o"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"-o", "-i", "value1"}, parsedArguments);
 }

 @Test
 public void testMultipleOptionsWithValues() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "o");
 options.addOption("option2", true, "i");
 String[] arguments = {"-oi", "value1", "-o", "value2"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"-o", "-i", "value1", "-o", "value2"}, parsedArguments);
 }

 @Test
 public void testLongOption() throws ParseException {
 Options options = new Options();
 options.addOption("longOption1", true, "l");
 String[] arguments = {"--longOption1"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"--longOption1"}, parsedArguments);
 }

 @Test
 public void testLongOptionWithValue() throws ParseException {
 Options options = new Options();
 options.addOption("longOption1", true, "l");
 String[] arguments = {"--longOption1=value"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"--longOption1", "value"}, parsedArguments);
 }

 @Test
 public void testLongOptionWithMultipleValues() throws ParseException {
 Options options = new Options();
 options.addOption("longOption1", true, "l");
 String[] arguments = {"--longOption1=value1", "--longOption1=value2"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);
 Assert.assertArrayEquals(new String[]{"--longOption1", "value1", "--longOption1", "value2"}, parsedArguments);
 }

 @Test (expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 String[] arguments = {"-invalidOption"};
 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("option1", true, "o");
 String[] arguments = {"-i", "-o"};
 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, true);
 Assert.assertArrayEquals(new String[]{"-i"}, parsedArguments);
 }
}