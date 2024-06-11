package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

 @Test
 public void testFlattenWithValidOptions() {
 Options options = new Options();
 String[] arguments = {"-Dproperty1=value1", "-Dproperty2=value2", "--foo", "-foo=value3", "-bar=value4", "--"};
 GnuParser parser = new GnuParser();
 String[] flattened = parser.flatten(options, arguments, false);
 String[] expected = {"-D", "property1=value1", "-D", "property2=value2", "-foo", "-foo", "value3", "-bar=value4"};
 assertArrayEquals(expected, flattened);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 String[] arguments = {"-Dproperty1=value1", "-Dproperty2=value2", "--foo", "-bar", "nonOption"};
 GnuParser parser = new GnuParser();
 String[] flattened = parser.flatten(options, arguments, true);
 String[] expected = {"-D", "property1=value1", "-D", "property2=value2", "--foo"};
 assertArrayEquals(expected, flattened);
 }

 @Test
 public void testFlattenWithEmptyArguments() {
 Options options = new Options();
 String[] arguments = {};
 GnuParser parser = new GnuParser();
 String[] flattened = parser.flatten(options, arguments, false);
 String[] expected = {};
 assertArrayEquals(expected, flattened);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithInvalidArgument() {
 Options options = new Options();
 String[] arguments = {"-invalidOption"};
 GnuParser parser = new GnuParser();
 parser.flatten(options, arguments, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithDashOnlyArgument() {
 Options options = new Options();
 String[] arguments = {"-"};
 GnuParser parser = new GnuParser();
 parser.flatten(options, arguments, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithTwoDashArgument() {
 Options options = new Options();
 String[] arguments = {"--"};
 GnuParser parser = new GnuParser();
 parser.flatten(options, arguments, false);
 }
}