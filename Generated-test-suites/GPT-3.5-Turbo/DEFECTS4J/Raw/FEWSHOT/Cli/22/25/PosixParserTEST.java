package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
  
  @Test
  public void testFlatten() {
    Options options = new Options();
    options.addOption("a", false, "Description of option a");
    options.addOption("b", true, "Description of option b");

    String[] arguments = {"--a", "-b", "value"};

    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, true);

    assertEquals(3, result.length);
    assertEquals("--a", result[0]);
    assertEquals("-b", result[1]);
    assertEquals("value", result[2]);
  }

  @Test
  public void testFlatten_NoOption() {
    Options options = new Options();
    options.addOption("a", false, "Description of option a");

    String[] arguments = {"--b", "value"};

    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, true);

    assertEquals(3, result.length);
    assertEquals("--", result[0]);
    assertEquals("--b", result[1]);
    assertEquals("value", result[2]);
  }
  
  @Test
  public void testFlatten_NoStopAtNonOption() {
    Options options = new Options();
    options.addOption("a", false, "Description of option a");

    String[] arguments = {"--b", "value"};

    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, false);

    assertEquals(2, result.length);
    assertEquals("--b", result[0]);
    assertEquals("value", result[1]);
  }
  
  @Test
  public void testFlatten_SingleHyphen() {
    Options options = new Options();
    options.addOption("a", false, "Description of option a");

    String[] arguments = {"-"};

    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, true);

    assertEquals(1, result.length);
    assertEquals("-", result[0]);
  }
  
  @Test
  public void testFlatten_HyphenOption() {
    Options options = new Options();
    options.addOption("a", false, "Description of option a");

    String[] arguments = {"-a"};

    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, true);

    assertEquals(1, result.length);
    assertEquals("-a", result[0]);
  }
  
  @Test
  public void testFlatten_HyphenOptionValue() {
    Options options = new Options();
    options.addOption("a", true, "Description of option a");

    String[] arguments = {"-a", "value"};

    PosixParser parser = new PosixParser();
    String[] result = parser.flatten(options, arguments, true);

    assertEquals(2, result.length);
    assertEquals("-a", result[0]);
    assertEquals("value", result[1]);
  }

}