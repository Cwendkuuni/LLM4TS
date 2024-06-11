package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 @Before
 public void setUp() {
 }

 @After
 public void tearDown() {
 }

 @Test
 public void testEmptyArgs() throws ParseException {
 Options options = new Options();
 String[] args = {};
 String[] expected = {};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testSingleArg() throws ParseException {
 Options options = new Options();
 String[] args = {"arg1"};
 String[] expected = {"arg1"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testSingleOption() throws ParseException {
 Options options = new Options().addOption("o", true, "option");
 String[] args = {"-o"};
 String[] expected = {"o"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testLongOption() throws ParseException {
 Options options = new Options().addOption("longOption", true, "long option");
 String[] args = {"--longOption"};
 String[] expected = {"longOption"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testLongOptionWithValue() throws ParseException {
 Options options = new Options().addOption("longOption", true, "long option");
 String[] args = {"--longOption", "value"};
 String[] expected = {"longOption", "value"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testMultipleOptions() throws ParseException {
 Options options = new Options()
 .addOption("o1", true, "option1")
 .addOption("o2", true, "option2")
 .addOption("o3", true, "option3");
 String[] args = {"-o1", "-o2", "--o3"};
 String[] expected = {"o1", "o2", "o3"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testMultipleOptionsWithValues() throws ParseException {
 Options options = new Options()
 .addOption("o1", true, "option1")
 .addOption("o2", true, "option2")
 .addOption("o3", true, "option3");
 String[] args = {"-o1", "v1", "-o2", "v2", "--o3", "v3"};
 String[] expected = {"o1", "v1", "o2", "v2", "o3", "v3"};
 String[] result = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options().addOption("o", true, "option");
 String[] args = {"--invalid"};
 }

 @Test(expected = ParseException.class)
 public void testInvalidLongOption() throws ParseException {
 Options options = new Options().addOption("o", true, "option");
 String[] args = {"--invalidOption"};
 }

 @Test(expected = ParseException.class)
 public void testStopAtNonOption() throws ParseException {
 Options options = new Options().addOption("o", true, "option");
 String[] args = {"arg1", "-o", "arg2"};
 }

 @Test(expected = ParseException.class)
 public void testStopAtNonOptionWithValue() throws ParseException {
 Options options = new Options().addOption("o", true, "option");
 String[] args = {"arg1", "--", "arg3", "arg4"};
 }

 @Test
 public void testStopAtNonOptionWithTokens() throws ParseException {
 Options options = new Options().addOption("o", true, "option");String[] args = {"arg1", "--", "arg3", "--token", "arg5"};
 String[] expected = {"arg1", "token"};
 String[] result = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, result);
 }
}