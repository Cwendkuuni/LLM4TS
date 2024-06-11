import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

  private PosixParser parser = new PosixParser();

  @Test
  public void testFlatten_StopAtNonOption_True() {
    Options options = new Options();
    String[] arguments = {"--arg1", "value1", "-abc", "arg2"};
    boolean stopAtNonOption = true;

    String[] result = parser.flatten(options, arguments, stopAtNonOption);

    Assert.assertArrayEquals(new String[]{"--arg1", "value1", "-abc", "arg2"}, result);
  }

  @Test
  public void testFlatten_StopAtNonOption_False() {
    Options options = new Options();
    String[] arguments = {"--arg1", "value1", "-abc", "arg2"};
    boolean stopAtNonOption = false;

    String[] result = parser.flatten(options, arguments, stopAtNonOption);

    Assert.assertArrayEquals(new String[]{"--arg1", "value1", "-abc", "arg2"}, result);
  }

  @Test
  public void testInit() {
    parser.init();

    Assert.assertEquals(false, parser.eatTheRest);
    Assert.assertEquals(0, parser.tokens.size());
    Assert.assertEquals(null, parser.currentOption);
  }

  @Test
  public void testProcess() {
    parser.currentOption = new Option("test", "test", false, "test");
    parser.currentOption.setArgs(1);
    String value = "test";

    parser.process(value);

    Assert.assertEquals(null, parser.currentOption);
    Assert.assertEquals(1, parser.tokens.size());
    Assert.assertEquals("--", parser.tokens.get(0));
    Assert.assertEquals(value, parser.tokens.get(1));
  }

  @Test
  public void testProcessOptionToken_OptionExists() {
    Options options = new Options();
    options.addOption(new Option("abc", "abc", false, "abc"));
    boolean stopAtNonOption = false;
    String token = "-abc";

    parser.processOptionToken(token, stopAtNonOption);

    Assert.assertEquals(options.getOption("abc"), parser.currentOption);
    Assert.assertEquals(1, parser.tokens.size());
    Assert.assertEquals(token, parser.tokens.get(0));
  }

  @Test
  public void testProcessOptionToken_OptionDoesNotExist_StopAtNonOptionTrue() {
    Options options = new Options();
    boolean stopAtNonOption = true;
    String token = "-def";

    parser.processOptionToken(token, stopAtNonOption);

    Assert.assertEquals(null, parser.currentOption);
    Assert.assertEquals(1, parser.tokens.size());
    Assert.assertEquals(token, parser.tokens.get(0));
  }

  @Test
  public void testProcessOptionToken_OptionDoesNotExist_StopAtNonOptionFalse() {
    Options options = new Options();
    boolean stopAtNonOption = false;
    String token = "-def";

    parser.processOptionToken(token, stopAtNonOption);

    Assert.assertEquals(null, parser.currentOption);
    Assert.assertEquals(1, parser.tokens.size());
    Assert.assertEquals(token, parser.tokens.get(0));
  }

  @Test
  public void testBurstToken() {
    Options options = new Options();
    boolean stopAtNonOption = false;
    String token = "-abc";

    parser.burstToken(token, stopAtNonOption);

    Assert.assertEquals(3, parser.tokens.size());
    Assert.assertEquals("-a", parser.tokens.get(0));
    Assert.assertEquals("-b", parser.tokens.get(1));
    Assert.assertEquals("-c", parser.tokens.get(2));
  }
}