import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

  @Test
  public void testFlatten_StopAtNonOption() {
    Options options = new Options();

    // Create options
    Option option1 = Option.builder("a")
                           .argName("arg1")
                           .hasArg()
                           .required()
                           .build();
    Option option2 = Option.builder("b")
                           .argName("arg2")
                           .hasArg()
                           .build();
    Option option3 = Option.builder("c")
                           .argName("arg3")
                           .hasArg()
                           .required()
                           .build();

    // Add options to the parser
    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);

    // Create the parser
    Parser parser = new PosixParser();

    // Test case 1: StopAtNonOption is true
    String[] args1 = {"-a", "value1", "-b", "value2", "-c", "value3", "non-option"};
    String[] expected1 = {"-a", "value1", "-b", "value2", "-c", "value3", "non-option"};

    assertArrayEquals(expected1, parser.flatten(options, args1, true));

    // Test case 2: StopAtNonOption is false
    String[] args2 = {"-a", "value1", "-b", "value2", "-c", "value3", "non-option"};
    String[] expected2 = {"-a", "value1", "-b", "value2", "-c", "value3", "non-option"};

    assertArrayEquals(expected2, parser.flatten(options, args2, false));
  }

  @Test
  public void testFlatten_MultipleOptions() {
    Options options = new Options();

    // Create options
    Option option1 = Option.builder("a")
                           .argName("arg1")
                           .hasArg()
                           .required()
                           .build();
    Option option2 = Option.builder("b")
                           .argName("arg2")
                           .hasArg()
                           .required()
                           .build();
    Option option3 = Option.builder("c")
                           .argName("arg3")
                           .hasArg()
                           .required()
                           .build();

    // Add options to the parser
    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);

    // Create the parser
    Parser parser = new PosixParser();

    // Test case 1: Multiple options provided with arguments
    String[] args1 = {"-a", "value1", "-b", "value2", "-c", "value3"};
    String[] expected1 = {"-a", "value1", "-b", "value2", "-c", "value3"};

    assertArrayEquals(expected1, parser.flatten(options, args1, true));

    // Test case 2: Multiple options provided without arguments
    String[] args2 = {"-a", "-b", "-c"};
    String[] expected2 = {"-a", "-b", "-c"};

    assertArrayEquals(expected2, parser.flatten(options, args2, true));
  }

  @Test
  public void testFlatten_LongOptionWithEquals() {
    Options options = new Options();

    // Create options
    Option option1 = Option.builder("a")
                           .longOpt("option1")
                           .argName("arg1")
                           .hasArg()
                           .required()
                           .build();
    Option option2 = Option.builder("b")
                           .longOpt("option2")
                           .argName("arg2")
                           .hasArg()
                           .required()
                           .build();
    Option option3 = Option.builder("c")
                           .longOpt("option3")
                           .argName("arg3")
                           .hasArg()
                           .required()
                           .build();

    // Add options to the parser
    options.addOption(option1);
    options.addOption(option2);
    options.addOption(option3);

    // Create the parser
    Parser parser = new PosixParser();

    // Test case: Long option with equals sign
    String[] args = {"--option1=value1", "--option2=value2", "--option3=value3"};
    String[] expected = {"--option1", "value1", "--option2", "value2", "--option3", "value3"};

    assertArrayEquals(expected, parser.flatten(options, args, true));
  }

}