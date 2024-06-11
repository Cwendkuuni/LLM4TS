package org.apache.commons.cli.test;

import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlatten_EmptyArgs() {
 PosixParser parser = new PosixParser();
 String[] args = new String[0];
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 assertArrayEqual(new String[0], result);
 }

 @Test
 public void testFlatten_SimpleArgs() {
 PosixParser parser = new PosixParser();
 String[] args = {"--option1", "-o", "value1", "arg1", "--option2"};
 Options options = new Options();
 options.addOption("option1", true, "o");
 options.addOption("option2", 2, "v");
 String[] result = parser.flatten(options, args, false);
 assertArrayEqual(new String[]{"--option1", "-o", "value1", "arg1", "--option2", "--", "v", "2"}, result);
 }

 @Test
 public void testFlatten_LongOptionWithArg() {
 PosixParser parser = new PosixParser();
 String[] args = {"--longOptionWithArg=value"};
 Options options = new Options();
 options.addOption("longOptionWithArg", true, "l");
 String[] result = parser.flatten(options, args, false);
 assertArrayEqual(new String[]{"--longOptionWithArg", "--", "value"}, result);
 }

 @Test
 public void testFlatten_LongOptionWithoutArg() {
 PosixParser parser = new PosixParser();
 String[] args = {"--longOptionWithoutArg"};
 Options options = new Options();
 options.addOption("longOptionWithoutArg", true, "l");
 String[] result = parser.flatten(options, args, false);
 assertArrayEqual(new String[]{"--longOptionWithoutArg"}, result);
 }

 @Test
 public void testFlatten_ShortOptionWithArg() {
 PosixParser parser = new PosixParser();
 String[] args = {"-o", "value1", "--option2"};
 Options options = new Options();
 options.addOption("o", true, "o");
 options.addOption("option2", 2, "v");
 String[] result = parser.flatten(options, args, false);
 assertArrayEqual(new String[]{"--option2", "--", "v", "2", "-o", "value1"}, result);
 }

 @Test
 public void testFlatten_ShortOptionWithoutArg() {
 PosixParser parser = new PosixParser();
 String[] args = {"-o", "--option2"};
 Options options = new Options();
 options.addOption("o", true, "o");
 options.addOption("option2", true, "v");
 String[] result = parser.flatten(options, args, false);
 assertArrayEqual(new String[]{"--option2"}, result);
 }

 @Test
 public void testFlatten_SingleHyphen() {
 PosixParser parser = new PosixParser();
 String[] args = {"-"};
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 assertArrayEqual(new String[]{"-"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_UnknownOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"-z"};
 Options options = new Options();
 String[] result = null;
 try {
 result = parser.flatten(options, args, false);
 } catch (ParseException e) {
 // expected
 }
 assertArrayEqual(new String[0], result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidShortOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"-xx"};
 Options options = new Options();
 String[] result = null;
 try {
 result = parser.flatten(options, args, false);
 } catch (ParseException e) {
 // expected
 }
 assertArrayEqual(new String[0], result);
 }

 @Test
 public void testFlatten_StopAtNonOption() {
 PosixParser parser = new PosixParser();
 String[] args = {"-o", "value1", "--option2", "nonOption"};
 Options options = new Options();
 options.addOption("o", true, "o");
 options.addOption("option2", 2, "v");
 String[] result = parser.flatten(options, args, true);
 assertArrayEqual(new String[]{"--option2", "--", "nonOption"}, result);
 }

 private void assertArrayEqual(String[] expected, String[] actual) {
 assertArrayEquals(expected, actual, "Arrays do not match");
 }

}