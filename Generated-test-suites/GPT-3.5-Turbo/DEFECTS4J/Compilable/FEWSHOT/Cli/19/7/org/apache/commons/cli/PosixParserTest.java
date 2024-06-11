package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

  @Test
  public void testFlatten() {
    Options options = new Options();
    options.addOption("f", false, "Test option f");
    options.addOption("a", true, "Test option a with required argument");
    options.addOption("b", true, "Test option b with required argument");
    options.addOption("c", false, "Test option c");

    PosixParser parser = new PosixParser();

    String[] args1 = {"-f", "-a", "value"};
    String[] expected1 = {"-f", "-a", "value"};
    assertArrayEquals(expected1, parser.flatten(options, args1, false));

    String[] args2 = {"-a", "value1", "-b", "value2"};
    String[] expected2 = {"-a", "value1", "-b", "value2"};
    assertArrayEquals(expected2, parser.flatten(options, args2, false));

    String[] args3 = {"-a", "value1", "non-option", "-b", "value2"};
    String[] expected3 = {"-a", "value1", "--", "non-option", "-b", "value2"};
    assertArrayEquals(expected3, parser.flatten(options, args3, true));

    String[] args4 = {"-c", "non-option"};
    String[] expected4 = {"-c", "non-option"};
    assertArrayEquals(expected4, parser.flatten(options, args4, false));
  }
}