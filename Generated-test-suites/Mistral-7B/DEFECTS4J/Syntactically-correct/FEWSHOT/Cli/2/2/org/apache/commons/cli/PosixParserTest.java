package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

 private Options createTestOptions() {
 Options options = new Options();
 options.addRequiredOption("a", "alpha", true, "Alpha option");
 options.addOption("b", "beta", false, "Beta option");
 options.addOption("c", "char", 'c', "Character option");
 options.addOptionGroup()
 .addOption(new Option("g", "group", false, "Group option"))
 .addOption(new Option("h", "help", false, "Prints this message"));
 return options;
 }

 @Test
 public void testFlatten() {
 PosixParser parser = new PosixParser();
 String[] args = {"--alpha", "-a", "--beta", "-b", "--char", "-c", "-g", "--group", "-h", "help", "-", "--"};
 String[] expected = {"--alpha", "-a", "--beta", "-b", "--char", "-c", "-g", "--group", "--help"};
 String[] result = parser.flatten(createTestOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--alpha", "-a", "--beta", "-b", "--char", "-c", "-g", "--group", "--help", "option"};
 String[] expected = {"--alpha", "-a", "--beta", "-b", "--char", "-c", "--group", "--help", "option"};
 String[] result = parser.flatten(createTestOptions(), args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithSingleHyphen() {
 PosixParser parser = new PosixParser();
 String[] args = {"--", "-"};
 String[] expected = {"--"};
 String[] result = parser.flatten(createTestOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultipleHyphens() {
 PosixParser parser = new PosixParser();
 String[] args = {"---", "--"};
 String[] expected = {"--", "--"};
 String[] result = parser.flatten(createTestOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--long-option-with-arg=value"};
 String[] expected = {"--long-option-with-arg", "value"};
 String[] result = parser.flatten(createTestOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptionAndValue() {
 PosixParser parser = new PosixParser();
 String[] args = {"--long-option-with-arg=value", "-a"};
 String[] expected = {"--long-option-with-arg", "value", "-a"};
 String[] result = parser.flatten(createTestOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptionGroup() {
 PosixParser parser = new PosixParser();
 String[] args = {"--group", "--help"};
 String[] expected = {"--group", "--help"};
 String[] result = parser.flatten(createTestOptions(), args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithInvalidOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"--invalid"};
 String[] expected = {};
 String[] result = parser.flatten(createTestOptions(), args, false);
 assertArrayEquals(expected, result);
 }
}