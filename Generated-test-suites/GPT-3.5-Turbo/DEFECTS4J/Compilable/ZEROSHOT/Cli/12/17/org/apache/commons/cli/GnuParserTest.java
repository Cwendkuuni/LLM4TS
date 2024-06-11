package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {

  @Test
  public void testFlatten() {
    String[] arguments = {"-a", "-b", "--c", "value", "-d", "arg1", "-Dproperty=value"};
    Options options = new Options();
    options.addOption("a", false, "");
    options.addOption("b", false, "");
    options.addOption("c", true, "");
    options.addOption("d", true, "");
    options.addOption("D", true, "");

    GnuParser parser = new GnuParser();
    String[] result = parser.flatten(options, arguments, true);

    assertEquals(result.length, 7);
    assertEquals(result[0], "-a");
    assertEquals(result[1], "-b");
    assertEquals(result[2], "--c");
    assertEquals(result[3], "value");
    assertEquals(result[4], "-d");
    assertEquals(result[5], "arg1");
    assertEquals(result[6], "-Dproperty=value");
  }

  @Test
  public void testFlattenStopAtNonOption() {
    String[] arguments = {"-a", "arg1", "-b", "--", "-Dproperty=value"};
    Options options = new Options();
    options.addOption("a", false, "");
    options.addOption("b", false, "");
    options.addOption("D", true, "");

    GnuParser parser = new GnuParser();
    String[] result = parser.flatten(options, arguments, true);

    assertEquals(result.length, 4);
    assertEquals(result[0], "-a");
    assertEquals(result[1], "arg1");
    assertEquals(result[2], "-b");
    assertEquals(result[3], "-Dproperty=value");
  }

  @Test
  public void testFlattenNoStop() {
    String[] arguments = {"-a", "arg1", "-b", "--", "-Dproperty=value"};
    Options options = new Options();
    options.addOption("a", false, "");
    options.addOption("b", false, "");
    options.addOption("D", true, "");

    GnuParser parser = new GnuParser();
    String[] result = parser.flatten(options, arguments, false);

    assertEquals(result.length, 6);
    assertEquals(result[0], "-a");
    assertEquals(result[1], "arg1");
    assertEquals(result[2], "-b");
    assertEquals(result[3], "--");
    assertEquals(result[4], "-Dproperty");
    assertEquals(result[5], "value");
  }

}