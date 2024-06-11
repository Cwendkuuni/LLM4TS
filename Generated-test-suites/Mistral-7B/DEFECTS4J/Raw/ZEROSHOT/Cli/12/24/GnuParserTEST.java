package org.apache.commons.cli.test;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.GnuParser;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for GnuParser
 */
public class GnuParserTest {

 private Parser parser;

 @Before
 public void setUp() {
 parser = new GnuParser();
 }

 /**
 * Test flatten method with valid options and arguments
 */
 @Test
 public void testFlattenValid() {
 // create Options object
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug information");
 options.addOption("i", "input", true, "Input file name");

 // create test arguments
 String[] arguments = new String[]{"-D", "true", "-i", "test.txt"};
 String[] expected = new String[]{"-D", "true", "-i", "test.txt"};

 // call method under test and check result
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 /**
 * Test flatten method with valid long options
 */
 @Test
 public void testFlattenLongOptions() {
 // create Options object
 Options options = new Options();
 options.addOption("d", "debug", true, "Print debug information");
 options.addOption("input", "i", true, "Input file name");

 // create test arguments
 String[] arguments = new String[]{"--debug", "true", "--input", "test.txt"};
 String[] expected = new String[]{"--debug", "true", "--input", "test.txt"};

 // call method under test and check result
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 /**
 * Test flatten method with valid options and arguments containing --
 */
 @Test
 public void testFlattenTwoDashes() {
 // create Options object
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug information");
 options.addOption("i", "input", true, "Input file name");

 // create test arguments
 String[] arguments = new String[]{"--", "-D", "true", "-i", "test.txt"};
 String[] expected = new String[]{"--", "-D", "true", "-i", "test.txt"};

 // call method under test and check result
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 /**
 * Test flatten method with valid options and non-option arguments
 */
 @Test
 public void testFlattenNonOptionArguments() {
 // create Options object
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug information");
 options.addOption("i", "input", true, "Input file name");

 // create test arguments
 String[] arguments = new String[]{"-D", "true", "test.txt", "output.txt"};
 String[] expected = new String[]{"-D", "true", "test.txt", "output.txt"};

 // call method under test and check result
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 /**
 * Test flatten method with valid options and stopAtNonOption set to true
 */
 @Test
 public void testFlattenStopAtNonOption() {
 // create Options object
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug information");
 options.addOption("i", "input", true, "Input file name");

 // create test arguments
 String[] arguments = new String[]{"-D", "true", "-i", "test.txt", "output.txt"};
 String[] expected = new String[]{"-D", "true", "-i", "test.txt"};

 // call method under test and check result
 String[] result = parser.flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 /**
 * Test flatten method with invalid option
 */
 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 // create Options object
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug information");
 options.addOption("i", "input", true, "Input file name");

 // create test arguments
 String[] arguments = new String[]{"-X", "true", "test.txt"};

 // call method under test and check exception
 parser.flatten(options, arguments, false);
 }

 /**
 * Test flatten method with invalid long option
 */
 @Test(expected = ParseException.class)
 public void testFlattenInvalidLongOption() {
 // create Options object
 Options options = new Options();
 options.addOption("D", "debug", true, "Print debug information");
 options.addOption("i", "input", true, "Input file name");

 // create test arguments
 String[] arguments = new String[]{"--X", "true", "test.txt"};

 // call method under test and check exception
 parser.flatten(options, arguments, false);
 }

 /**
 * Test flatten method with no options
 */
 @Test
 public void testFlattenNoOptions() {
 // create Options object
 Options options = new Options();

 // create test arguments
 String[] arguments = new String[]{"test.txt", "output.txt"};
 String[] expected = new String[]{"test.txt", "output.txt"};

 // call method under test and check result
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }
}