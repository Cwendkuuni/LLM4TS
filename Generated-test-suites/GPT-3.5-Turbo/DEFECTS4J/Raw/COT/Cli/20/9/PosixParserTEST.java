import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

  @Test
  public void testInit() {
    PosixParser parser = new PosixParser();
    parser.init();
    // Verify that eatTheRest is false and
    // the tokens list is empty
    assert(parser.eatTheRest == false);
    assert(parser.tokens.isEmpty());
    // Verify that currentOption is null
    assert(parser.currentOption == null);
  }

  @Test
  public void testFlatten() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").hasArg().build());
    options.addOption(Option.builder("b").build());
    String[] args = {"-a", "value", "-b"};

    String[] result = parser.flatten(options, args, true);
    // Verify that the result contains the same
    // elements as the args array
    assert(result.length == args.length);
    for (int i = 0; i < args.length; i++) {
      assert(result[i] == args[i]);
    }
  }

  @Test
  public void testProcessOptionToken() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    String token = "-a";

    boolean stopAtNonOption = false;
    parser.processOptionToken(token, stopAtNonOption);
    // Verify that the currentOption is set
    assert(parser.currentOption != null);
    // Verify that the currentOption is the "a" option
    assert(parser.currentOption.getOpt().equals("a"));

    stopAtNonOption = true;
    parser.processOptionToken(token, stopAtNonOption);
    // Verify that eatTheRest is set to true
    assert(parser.eatTheRest == true);
  }

  @Test
  public void testProcess() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").hasArg().build());
    String token = "value";

    parser.process(token);
    // Verify that the token is added to the tokens list
    assert(parser.tokens.contains(token));

    parser.currentOption = options.getOption("a");
    parser.process(token);
    // Verify that the token is added to the tokens list
    assert(parser.tokens.contains(token));
    // Verify that currentOption is reset to null
    assert(parser.currentOption == null);
  }

  @Test
  public void testBurstToken() {
    PosixParser parser = new PosixParser();
    Options options = new Options();
    options.addOption(Option.builder("a").build());
    options.addOption(Option.builder("b").build());
    String token = "-ab";

    boolean stopAtNonOption = false;
    parser.burstToken(token, stopAtNonOption);
    // Verify that both options "a" and "b" are added
    // to the tokens list
    assert(parser.tokens.contains("-a"));
    assert(parser.tokens.contains("-b"));

    stopAtNonOption = true;
    parser.burstToken(token, stopAtNonOption);
    // Verify that eatTheRest is set to true
    assert(parser.eatTheRest == true);
  }

}