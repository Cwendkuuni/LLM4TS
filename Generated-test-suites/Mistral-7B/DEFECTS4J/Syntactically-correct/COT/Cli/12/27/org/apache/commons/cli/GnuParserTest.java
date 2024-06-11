package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.GnuParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for GnuParser
 */
public class GnuParserTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("D", "debug", false, "Debug mode");
 options.addOption("h", "help", false, "Prints help");
 }

 @After
 public void tearDown() {
 // Empty
 }

 @Test
 public void testFlatten_NormalCase() {
 String[] args = {"-Dproperty", "value1", "-h", "value2", "-D", "anotherProperty", "value3"};
 Parser parser = new GnuParser();
 String[] expected = {"-D", "property", "value1", "-h", "value2", "-D", "anotherProperty"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_EmptyArguments() {
 Parser parser = new GnuParser();
 String[] result = parser.flatten(options, new String[0], false);
 assertArrayEquals(new String[0], result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_InvalidOption() {
 String[] args = {"-x", "value"};
 Parser parser = new GnuParser();
 parser.flatten(options, args, false);
 }

 @Test(expected = ParseException.class)
 public void testFlatten_MissingOption() {
 String[] args = {"value"};
 Parser parser = new GnuParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testFlatten_DashAsArgument() {
 String[] args = {"-", "-h"};
 Parser parser = new GnuParser();
 String[] expected = {"-", "-h"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_DoubleDash() {
 String[] args = {"--"};
 Parser parser = new GnuParser();
 String[] expected = {"--"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_DoubleDashWithArguments() {
 String[] args = {"--foo", "value"};
 Parser parser = new GnuParser();
 String[] expected = {"--foo", "value"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOption() {
 String[] args = {"-Dproperty", "value1", "-h", "value2", "-D", "anotherProperty", "value3", "nonOption"};
 Parser parser = new GnuParser();
 String[] expected = {"-D", "property", "value1", "-h", "value2", "-D", "anotherProperty"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }
}