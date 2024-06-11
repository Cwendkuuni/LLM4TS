package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

  @Test
  public void testFlatten_basic() {
    Options options = new Options();
    options.addOption("f", "foo", false, "foo option");
    options.addOption("b", "bar", false, "bar option");

    String[] arguments = {"-f", "value1", "-b", "value2"};
    GnuParser parser = new GnuParser();
    String[] result = parser.flatten(options, arguments, false);

    assertEquals(result.length, 4);
    assertEquals(result[0], "-f");
    assertEquals(result[1], "value1");
    assertEquals(result[2], "-b");
    assertEquals(result[3], "value2");
  }

  @Test
  public void testFlatten_stopAtNonOption() {
    Options options = new Options();
    options.addOption("f", "foo", false, "foo option");
    options.addOption("b", "bar", false, "bar option");

    String[] arguments = {"-f", "value1", "--", "nonOption", "-b", "value2"};
    GnuParser parser = new GnuParser();
    String[] result = parser.flatten(options, arguments, true);

    assertEquals(result.length, 2);
    assertEquals(result[0], "-f");
    assertEquals(result[1], "value1");
  }

}