import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

  @Test
  public void testParse() {
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    options.addOption(Option.builder("c").build());

    String[] args = new String[] {"-a", "-b", "-c", "value"};

    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args);
      assertTrue(cmd.hasOption("a"));
      assertTrue(cmd.hasOption("b"));
      assertTrue(cmd.hasOption("c"));
      assertEquals("value", cmd.getOptionValue("c"));
      assertFalse(cmd.hasOption("d"));
    } catch (ParseException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testParseWithProperties() {
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    options.addOption(Option.builder("c").build());

    String[] args = new String[] {"-c", "value"};

    Properties properties = new Properties();
    properties.setProperty("a", "true");
    properties.setProperty("b", "false");

    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args, properties);
      assertTrue(cmd.hasOption("a"));
      assertFalse(cmd.hasOption("b"));
      assertTrue(cmd.hasOption("c"));
      assertEquals("value", cmd.getOptionValue("c"));
      assertFalse(cmd.hasOption("d"));
    } catch (ParseException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testParseWithStopAtNonOption() {
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    options.addOption(Option.builder("c").build());

    String[] args = new String[] {"-a", "-b", "nonOption"};

    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine cmd = parser.parse(options, args, true);
      assertTrue(cmd.hasOption("a"));
      assertTrue(cmd.hasOption("b"));
      assertFalse(cmd.hasOption("nonOption"));
    } catch (ParseException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testParseWithMissingArgument() {
    Options options = new Options();
    options.addOption(Option.builder("a").hasArg().build());

    String[] args = new String[] {"-a"};

    CommandLineParser parser = new DefaultParser();
    try {
      parser.parse(options, args);
      fail("Expected MissingArgumentException");
    } catch (ParseException e) {
      assertEquals("Missing argument for option:a", e.getMessage());
    }
  }

  @Test
  public void testParseWithUnrecognizedOption() {
    Options options = new Options();
    options.addOption(Option.builder("a").build());

    String[] args = new String[] {"-b"};

    CommandLineParser parser = new DefaultParser();
    try {
      parser.parse(options, args);
      fail("Expected UnrecognizedOptionException");
    } catch (ParseException e) {
      assertEquals("Unrecognized option: b", e.getMessage());
    }
  }

  @Test
  public void testProcessArgs() {
    Options options = new Options();
    options.addOption(Option.builder("a").hasArgs().build());

    ListIterator iter = Arrays.asList(new String[]{"value1", "value2"}).listIterator();

    Parser parser = new DefaultParser();
    try {
      Option opt = new Option("a", null);
      parser.processArgs(opt, iter);
      assertTrue(opt.hasValue());
      assertEquals("value1", opt.getValue());
      assertEquals("value2", opt.getNextValue());
    } catch (ParseException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testProcessOption() {
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").hasArg().build());

    ListIterator iter = Arrays.asList(new String[]{"-a", "-b", "value"}).listIterator();

    Parser parser = new DefaultParser();
    try {
      CommandLine cmd = new CommandLine();
      parser.processOption("-a", iter);
      parser.processOption("-b", iter);
      assertTrue(cmd.hasOption("a"));
      assertTrue(cmd.hasOption("b"));
      assertEquals("value", cmd.getOptionValue("b"));
    } catch (ParseException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCheckRequiredOptions() {
    Options options = new Options();
    options.addOption(Option.builder("a").required().build());
    options.addOption(Option.builder("b").required().build());

    Parser parser = new DefaultParser();
    try {
      parser.setOptions(options);
      parser.checkRequiredOptions();
      fail("Expected MissingOptionException");
    } catch (MissingOptionException e) {
      assertEquals("Missing required option: ab", e.getMessage());
    }
  }

}