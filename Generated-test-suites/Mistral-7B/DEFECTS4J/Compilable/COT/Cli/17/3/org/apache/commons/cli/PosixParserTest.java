package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlatten_EmptyArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[0];
 String[] expected = new String[0];
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_SingleArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"arg"};
 String[] expected = new String[]{"arg"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_TwoArgs_NoOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"arg1", "arg2"};
 String[] expected = new String[]{"arg1", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_TwoArgs_SingleOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", false, "Opt");
 String[] args = new String[]{"--opt"};
 String[] expected = new String[]{"--opt"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_TwoArgs_OptionWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt", "arg"};
 String[] expected = new String[]{"--opt", "arg"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_TwoArgs_OptionWithArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt", "arg1", "arg2"};
 String[] expected = new String[]{"--opt", "arg1", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_TwoArgs_SingleOptionWithArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt=arg1", "--opt=arg2"};
 String[] expected = new String[]{"--opt", "arg1", "--opt", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_TwoArgs_InvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"--invalid"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_TwoArgs_InvalidOptionWithArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--invalid=arg"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_TwoArgs_InvalidOptionWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--invalid=arg1", "--invalid=arg2"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_TwoArgswithStopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"arg1", "--invalid"};
 String[] result = parser.flatten(options, args, true);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testFlatten_ThreeArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"arg1", "arg2", "arg3"};
 String[] expected = new String[]{"arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_SingleOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", false, "Opt");
 String[] args = new String[]{"--opt", "arg1", "arg2"};
 String[] expected = new String[]{"--opt", "arg1", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_OptionWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt", "arg1", "arg2"};
 String[] expected = new String[]{"--opt", "arg1", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_OptionWithArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--opt", "arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_SingleOptionWithArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt=arg1", "--opt=arg2", "arg3"};
 String[] expected = new String[]{"--opt", "arg1", "--opt", "arg2", "arg3"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_InvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"arg1", "--invalid"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_InvalidOptionWithArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"arg1", "--invalid=arg"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_InvalidOptionWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"arg1", "--invalid=arg1", "--invalid=arg2"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_InvalidArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt", "arg1", "--arg"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testFlatten_ThreeArgs_DoubleDash() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"--", "arg1", "arg2"};
 String[] expected = new String[]{"--", "arg1", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_DoubleDashWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"--", "--arg", "arg2"};
 String[] expected = new String[]{"--", "--arg", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_DoubleDashWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"--", "--arg1", "--arg2", "arg2"};
 String[] expected = new String[]{"--", "--arg1", "--arg2", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_DoubleDashWithInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"--", "--invalid"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_DoubleDashWithInvalidOptionWithArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--", "--invalid=arg"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_DoubleDashWithInvalidOptionWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--", "--invalid=arg1", "--invalid=arg2"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testFlatten_ThreeArgs_BurstOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt---arg"};
 String[] expected = new String[]{"--opt", "--opt", "arg"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_BurstOptionWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt--arg"};
 String[] expected = new String[]{"--opt", "--opt", "arg"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_BurstOptionWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt--arg1--arg2"};
 String[] expected = new String[]{"--opt", "--opt", "arg1", "--opt", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_BurstInvalidOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt--invalid"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_BurstInvalidOptionWithArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt--invalid=arg"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlatten_ThreeArgs_BurstInvalidOptionWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"--opt--invalid=arg1", "--opt--invalid=arg2"};
 String[] result = parser.flatten(options, args, false);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testFlatten_ThreeArgs_StopAtNonOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = new String[]{"arg1", "--invalid", "arg2"};
 String[] expected = new String[]{"arg1", "--invalid"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_StopAtNonOption_BurstOption() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"arg1", "--opt--arg", "arg2"};
 String[] expected = new String[]{"arg1", "--opt--arg"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_StopAtNonOption_BurstOptionWithArg() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"arg1", "--opt--arg", "arg2"};
 String[] expected = new String[]{"arg1", "--opt--arg"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_ThreeArgs_StopAtNonOption_BurstOptionWithMultipleArgs() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("opt", true, "Opt");
 String[] args = new String[]{"arg1", "--opt--arg1--arg2", "arg2"};
 String[] expected = new String[]{"arg1", "--opt--arg1--arg2"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }
}