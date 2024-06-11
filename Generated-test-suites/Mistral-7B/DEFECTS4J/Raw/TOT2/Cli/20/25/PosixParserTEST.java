import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", "alpha", true, "Option with an argument");
 options.addOption("b", "beta", false, "Option without an argument");
 }

 @Test
 public void testFlatten_SingleOption() {
 args = new String[]{"-a", "arg"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"-a", "arg"}, result);
 }

 @Test
 public void testFlatten_MultipleOptions() {
 args = new String[]{"-a", "--beta", "-c", "arg", "--", "arg2", "arg3"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"-a", "--beta", "-c", "--", "arg2", "arg3"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidOption() {
 args = new String[]{"-z", "--", "arg"};
 parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_StopAtNonOption() {
 args = new String[]{"-a", "arg1", "arg2", "-", "nonOption"};
 parser = new PosixParser();
 parser.flatten(options, args, true);
 }

 @Test
 public void testFlatten_LongOption() {
 args = new String[]{"--alpha", "arg"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--alpha", "arg"}, result);
 }

 @Test
 public void testFlatten_LongOptionWithValue() {
 args = new String[]{"--alpha", "arg1", "--beta"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"--alpha", "arg1", "--beta"}, result);
 }

 @Test
 public void testFlatten_EmptyArgs() {
 args = new String[0];
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[0], result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_MissingOption() {
 args = new String[]{"-z"};
 parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testFlatten_Bursting() {
 args = new String[]{"-abc"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"-a", "-b", "-c"}, result);
 }

 @Test
 public void testFlatten_BurstingWithValue() {
 args = new String[]{"-abcdef"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(new String[]{"-a", "-b", "-c", "--", "d", "e", "f"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_StopAtNonOptionBursting() {
 args = new String[]{"-abc", "nonOption"};
 parser = new PosixParser();
 parser.flatten(options, args, true);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidBursting() {
 args = new String[]{"-zabc"};
 parser = new PosixParser();
 parser.flatten(options, args, false);
 }
}