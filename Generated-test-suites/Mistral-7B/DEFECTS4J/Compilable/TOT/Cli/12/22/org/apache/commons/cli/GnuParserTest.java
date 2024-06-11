package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

 @Test
 public void testFlattenWithOptionsAndValues() {
 Options options = new Options();
 options.addOption("d", "debug", false, "Print debug information.");
 options.addOption("h", "help", false, "Print this help message.");
 options.addOption("o", "output", true, "output directory");

 String[] arguments = new String[]{"-d", "--help", "-o", "output/directory", "--", "file1.txt", "file2.txt"};
 String[] expected = new String[]{"-d", "--", "--help", "-o", "output/directory", "file1.txt", "file2.txt"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptions() {
 Options options = new Options();
 options.addOption("debug", true, "Print debug information.");
 options.addOption("help", false, "Print this help message.");
 options.addOption("output-directory", true, "output directory");

 String[] arguments = new String[]{"--debug", "--help", "--output-directory", "output/directory", "--", "file1.txt", "file2.txt"};
 String[] expected = new String[]{"--debug", "--", "--help", "--output-directory", "output/directory", "file1.txt", "file2.txt"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithDashOption() {
 Options options = new Options();
 options.addOption("d", "debug", false, "Print debug information.");
 options.addOption("h", "help", false, "Print this help message.");
 options.addOption("o", "output", true, "output directory");

 String[] arguments = new String[]{"--", "-d", "output/directory", "file1.txt", "file2.txt"};
 String[] expected = new String[]{"--", "-d", "output/directory", "file1.txt", "file2.txt"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithNonOption() {
 Options options = new Options();
 options.addOption("d", "debug", false, "Print debug information.");
 options.addOption("h", "help", false, "Print this help message.");
 options.addOption("o", "output", true, "output directory");

 String[] arguments = new String[]{"--", "file1.txt", "file2.txt"};
 GnuParser parser = new GnuParser();
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testFlattenWithTwoDashes() {
 Options options = new Options();
 options.addOption("d", "debug", false, "Print debug information.");
 options.addOption("h", "help", false, "Print this help message.");
 options.addOption("o", "output", true, "output directory");

 String[] arguments = new String[]{"--", "--"};
 String[] expected = new String[]{"--", "--"};
 GnuParser parser = new GnuParser();
 String[] result = parser.flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }
}