import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

public class PosixParserTest {

  @Test
  public void testFlatten() {
    PosixParser parser = new PosixParser();

    // 1. Test empty options and arguments
    Options options = new Options();
    String[] arguments = new String[0];
    String[] expected = new String[0];
    Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));

    // 2. Test options and arguments without stopAtNonOption
    options = new Options();
    Option option1 = new Option("a", "option1", false, "Option 1");
    options.addOption(option1);
    Option option2 = new Option("b", "option2", true, "Option 2");
    options.addOption(option2);
    arguments = new String[]{"-a", "-b", "value1", "value2"};
    expected = new String[]{"-a", "-b", "value1", "value2"};
    Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));

    // 3. Test options and arguments with stopAtNonOption
    options = new Options();
    option1 = new Option("a", "option1", false, "Option 1");
    options.addOption(option1);
    option2 = new Option("b", "option2", true, "Option 2");
    options.addOption(option2);
    arguments = new String[]{"-a", "-b", "value1", "value2"};
    expected = new String[]{"-a", "-b", "value1", "value2"};
    Assert.assertArrayEquals(expected, parser.flatten(options, arguments, true));

    // 4. Test bursting of tokens ("-abc" style)
    options = new Options();
    option1 = new Option("a", "option1", false, "Option 1");
    options.addOption(option1);
    option2 = new Option("b", "option2", true, "Option 2");
    options.addOption(option2);
    Option option3 = new Option("c", "option3", false, "Option 3");
    options.addOption(option3);
    arguments = new String[]{"-abc", "value"};
    expected = new String[]{"-a", "-b", "-c", "value"};
    Assert.assertArrayEquals(expected, parser.flatten(options, arguments, false));
  }
}