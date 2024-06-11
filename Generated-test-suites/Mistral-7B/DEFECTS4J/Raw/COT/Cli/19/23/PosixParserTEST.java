package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

 private String[] testData = new String[]{"--foo=bar", "--foo", "-foo", "-foo bar", "-f -o", "--", "-a --b=c --d=e", "-abcd"};
 private Options testOptions = new Options();

 @Test
 public void testFlatten() throws ParseException {
 for (String data : testData) {
 List<String> result = new PosixParser().flatten(testOptions, new String[]{data}, false);
 printResult("testFlatten", data, result);
 }
 }

 @Test
 public void testFlattenWithStopAtNonOption() throws ParseException {
 for (String data : testData) {
 List<String> result = new PosixParser().flatten(testOptions, new String[]{data}, true);
 printResult("testFlattenWithStopAtNonOption", data, result);
 }
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 testOptions.addOption("newOption", false, "noArg");
 List<String> result = new PosixParser().flatten(testOptions, new String[]{"--newOption=x"}, false);
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() throws ParseException {
 List<String> result = new PosixParser().flatten(testOptions, new String[]{"--missingOption"}, false);
 }

 @Test(expected = ParseException.class)
 public void testEmptyOption() throws ParseException {
 List<String> result = new PosixParser().flatten(testOptions, new String[]{"--"}, false);
 }

 @Test(expected = ParseException.class)
 public void testInvalidArgument() throws ParseException {
 testOptions.addOption("foo", false, "arg");
 List<String> result = new PosixParser().flatten(testOptions, new String[]{"--foo=123456789012345678901234567890"}, false);
 }

 @Test
 public void testBurstToken() {
 String token = "--foo-bar-baz";
 List<String> result = new PosixParser().burstToken(token, false);
 printResult("testBurstToken", token, result);
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 String token = "--foo-bar-baz";
 List<String> result = new PosixParser().burstToken(token, true);
 printResult("testBurstTokenWithStopAtNonOption", token, result);
 }

 private void printResult(String testName, String input, List<String> output) {
 System.out.println(testName + " : input [ " + Arrays.toString(new String[]{input}) + " ] output [ " + Arrays.toString(output.toArray(new String[output.size()])) + " ]");
 Assert.assertArrayEquals(new String[]{"--foo", "bar"}, output.toArray());
 }

}