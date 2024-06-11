package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

  @Test
  public void testFlattenTypical() {
    Options options = new Options();
    options.addOption("D", true, "Set a system property");
    options.addOption("h", false, "Print help");
    options.addOption("v", false, "Print version");

    GnuParser parser = new GnuParser();

    String[] arguments = {"-Dvalue", "--", "-h", "file.txt"};

    String[] expected = {"-Dvalue", "--", "-h", "file.txt"};
    String[] result = parser.flatten(options, arguments, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlattenNoOption() {
    Options options = new Options();
    options.addOption("D", true, "Set a system property");

    GnuParser parser = new GnuParser();

    String[] arguments = {"value"};

    String[] expected = {"value"};
    String[] result = parser.flatten(options, arguments, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlattenStopAtNonOption() {
    Options options = new Options();

    GnuParser parser = new GnuParser();

    String[] arguments = {"-Dvalue", "--", "-h", "file.txt"};

    String[] expected = {"-D", "value", "--", "-h", "file.txt"};
    String[] result = parser.flatten(options, arguments, true);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlattenDash() {
    Options options = new Options();

    GnuParser parser = new GnuParser();

    String[] arguments = {"-"};

    String[] expected = {"-"};
    String[] result = parser.flatten(options, arguments, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlattenDoubleDash() {
    Options options = new Options();

    GnuParser parser = new GnuParser();

    String[] arguments = {"--"};

    String[] expected = {"--"};
    String[] result = parser.flatten(options, arguments, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlattenWithAbbreviation() {
    Options options = new Options();
    options.addOption("D", true, "Set a system property");
    options.addOption("de", true, "Set system property for development environment");

    GnuParser parser = new GnuParser();

    String[] arguments = {"--dev=value"};

    String[] expected = {"-de", "value"};
    String[] result = parser.flatten(options, arguments, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlattenWithInvalidOption() {
    Options options = new Options();
    options.addOption("D", true, "Set a system property");

    GnuParser parser = new GnuParser();

    String[] arguments = {"-Dvalue", "-h"};

    String[] expected = {"-Dvalue", "-h"};
    String[] result = parser.flatten(options, arguments, false);

    assertArrayEquals(expected, result);
  }

  @Test
  public void testFlattenEmptyArguments() {
    Options options = new Options();

    GnuParser parser = new GnuParser();

    String[] arguments = {};

    String[] expected = {};
    String[] result = parser.flatten(options, arguments, false);

    assertArrayEquals(expected, result);
  }

}