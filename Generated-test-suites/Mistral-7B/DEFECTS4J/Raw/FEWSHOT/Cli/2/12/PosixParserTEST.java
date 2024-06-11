package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PosixParserTest {

 @Test
 public void testEmptyArguments() {
 String[] arguments = new String[] {};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[] {}, result);
 }

 @Test
 public void testSimpleArguments() {
 String[] arguments = new String[] {"-a", "-b", "--c=d", "--e"};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[] {"-a", "-b", "--c", "d", "--e"}, result);
 }

 @Test
 public void testLongOptions() {
 String[] arguments = new String[] {"--longOption1", "--longOption2=value"};
 Options options = new Options();
 options.addOption("longOption1", true, "l1");
 options.addOption("longOption2", true, "l2");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[] {"--longOption1", "--longOption2"}, result);
 }

 @Test
 public void testLongOptionsWithValues() {
 String[] arguments = new String[] {"--longOption1", "--longOption2=value"};
 Options options = new Options();
 options.addOption("longOption1", true, "l1");
 options.addOption("longOption2", true, "l2").setArgName("value");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[] {"--longOption1", "--longOption2=value"}, result);
 }

 @Test
 public void testBursting() {
 String[] arguments = new String[] {"-abc", "--def=ghij", "-pqrst"};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[] {"-a", "-b", "-c", "--d", "--e=ghij", "-p", "-q", "-r", "-s", "-t"}, result);
 }

 @Test
 public void testStopAtNonOption() {
 String[] arguments = new String[] {"-abc", "--def=ghij", "nonOption"};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[] {"-abc", "--def=ghij", "nonOption"}, result);
 }

 @Test
 public void testSpecialTokens() {
 String[] arguments = new String[] {"--foo=bar", "--", "-"};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[] {"--foo=bar", "--", "-"}, result);
 }

 @Test
 public void testProcessSingleHyphen() {
 String[] arguments = new String[] {"-", "-a"};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[] {"-", "-a"}, result);
 }

 @Test
 public void testProcessOptionToken() {
 String[] arguments = new String[] {"-a", "-o", "-o"};
 Options options = new Options();
 options.addOption("o", true, "opt");
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[] {"-a", "-o"}, result);
 assertNotNull(parser.currentOption);
 assertEquals("opt", parser.currentOption.getLongOpt());
 }

 @Test
 public void testBurstingWithStopAtNonOption() {
 String[] arguments = new String[] {"-abc", "--def=ghij", "nonOption"};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(new String[] {"-abc", "--def=ghij", "nonOption"}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOption() {
 String[] arguments = new String[] {"-abc"};
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(new String[] {"-abc"}, result);
 // the following statement should throw an IllegalArgumentException
 assertNull(parser.currentOption);
 }
}