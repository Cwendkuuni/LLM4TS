package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 @Test
 public void testFlatten() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 options.addOption("d", 3, "Option D with three arguments");

 String[] args = new String[]{"--a", "--b", "value", "-c", "--d", "arg1", "arg2", "arg3"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args);

 assertEquals(8, parsedArgs.length);
 assertEquals("--a", parsedArgs[0]);
 assertEquals("--b", parsedArgs[1]);
 assertEquals("value", parsedArgs[2]);
 assertEquals("-c", parsedArgs[3]);
 assertEquals("--d", parsedArgs[4]);
 assertEquals("arg1", parsedArgs[5]);
 assertEquals("arg2", parsedArgs[6]);
 assertEquals("arg3", parsedArgs[7]);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 options.addOption("d", 3, "Option D with three arguments");

 String[] args = new String[]{"--invalidOption"};
 PosixParser parser = new PosixParser();
 parser.parse(options, args);
 fail("ParseException should be thrown but wasn't");
 }

 @Test
 public void testFlattenWithNoOptions() throws ParseException {
 Options options = new Options();
 String[] args = new String[]{"--"};
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.parse(options, args);
 assertEquals(1, parsedArgs.length);
 assertEquals("--", parsedArgs[0]);
 }
}