package org.apache.commons.cli;
import org.junit.*;
import static org.junit.Assert.*;

public class ParserTest {

  @Test
  public void testSetOptions() {
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, 
                    boolean stopAtNonOption) {
        return null;
      }
    };

    Options options = new Options();
    options.addOption("a", true, "Option A");
    options.addOption("b", false, "Option B");
    parser.setOptions(options);

    assertEquals(options, parser.getOptions());
    assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
  }

  @Test
  public void testParse() throws ParseException {
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, 
                    boolean stopAtNonOption) {
        return null;
      }
    };

    Options options = new Options();
    options.addOption("a", true, "Option A");
    options.addOption("b", false, "Option B");
    parser.setOptions(options);

    String[] arguments = {"-a", "valueA", "-b"};
    CommandLine cmd = parser.parse(options, arguments);

    assertTrue(cmd.hasOption("a"));
    assertEquals("valueA", cmd.getOptionValue("a"));
    assertTrue(cmd.hasOption("b"));
  }

  @Test(expected = ParseException.class)
  public void testParseMissingOptionValue() throws ParseException {
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, 
                    boolean stopAtNonOption) {
        return null;
      }
    };

    Options options = new Options();
    options.addOption("a", true, "Option A");
    options.addOption("b", false, "Option B");
    parser.setOptions(options);

    String[] arguments = {"-a"};
    CommandLine cmd = parser.parse(options, arguments);
  }

  @Test(expected = UnrecognizedOptionException.class)
  public void testParseUnrecognizedOption() throws ParseException {
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, 
                    boolean stopAtNonOption) {
        return null;
      }
    };

    Options options = new Options();
    options.addOption("a", true, "Option A");
    options.addOption("b", false, "Option B");
    parser.setOptions(options);

    String[] arguments = {"-c"};
    CommandLine cmd = parser.parse(options, arguments);
  }

  @Test(expected = MissingOptionException.class)
  public void testCheckRequiredOptions() throws ParseException {
    Parser parser = new Parser() {
      protected String[] flatten(Options opts, String[] arguments, 
                    boolean stopAtNonOption) {
        return null;
      }
    };

    Options options = new Options();
    Option requiredOption = new Option("a", true, "Required Option A");
    requiredOption.setRequired(true);
    options.addOption(requiredOption);
    parser.setOptions(options);

    String[] arguments = {};
    CommandLine cmd = parser.parse(options, arguments);
    parser.checkRequiredOptions();
  }
}