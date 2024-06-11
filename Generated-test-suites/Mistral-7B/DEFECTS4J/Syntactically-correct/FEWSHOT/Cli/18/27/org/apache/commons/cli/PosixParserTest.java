package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class PosixParserTest {

 private Options createOptions() {
 Options options = new Options();
 options.addOption("o", "optionO", false, "Option O");
 options.addOption("a", "optionA", true, "Option A with argument");
 options.addOption("b", "optionB", "argB,argB1,argB2", "Option B with multiple arguments");
 return options;
 }

 @Test
 public void testFlatten_EmptyArguments() {
 PosixParser parser = new PosixParser();
 String[] arguments = {};
 String[] expected = {};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_SingleToken() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"arg1"};
 String[] expected = {"arg1"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_SingleHyphen() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-"};
 String[] expected = {"-"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_DoubleHyphen() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--"};
 String[] expected = {"--"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_SingleOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-o"};
 String[] expected = {"-o"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_OptionWithArgument() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-a", "argA"};
 String[] expected = {"-a", "argA"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_OptionWithMultipleArguments() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-b", "argB1", "argB2"};
 String[] expected = {"-b", "argB1", "argB2"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_OptionWithLongName() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--longOptionName"};
 String[] expected = {"--longOptionName"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_OptionWithLongNameAndArgument() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--longOptionName", "argLongOptionName"};
 String[] expected = {"--longOptionName", "argLongOptionName"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_Bursting() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-abc"};
 String[] expected = {"-a", "-c"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_BurstingWithArgument() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-abcd"};
 String[] expected = {"-a", "--d"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_BurstingWithLongName() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--longOption-a--longOption-c"};
 String[] expected = {"--longOption-a", "--longOption-c"};
 String[] result = parser.flatten(createOptions(), arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-a", "nonOption"};
 String[] expected = {"-a", "nonOption"};
 String[] result = parser.flatten(createOptions(), arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOption_Bursting() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-abc", "nonOption"};
 String[] expected = {"-a", "nonOption"};
 String[] result = parser.flatten(createOptions(), arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOption_LongName() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--longOptionA", "nonOption"};
 String[] expected = {"--longOptionA", "nonOption"};
 String[] result = parser.flatten(createOptions(), arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOption_LongNameWithArgument() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"--longOptionA", "argLongOptionA", "nonOption"};
 String[] expected = {"--longOptionA", "argLongOptionA", "nonOption"};
 String[] result = parser.flatten(createOptions(), arguments, true);
 assertArrayEquals(expected, result);
 }
}