import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.*;

public class GnuParserTest {

  @Test
  public void testFlatten_emptyOptions_emptyArguments() {
    Options options = new Options();
    String[] arguments = new String[0];

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, false);

    Assert.assertArrayEquals(new String[0], flattened);
  }

  @Test
  public void testFlatten_emptyOptions_nonEmptyArguments() {
    Options options = new Options();
    String[] arguments = new String[]{"arg1", "arg2", "arg3"};

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, false);

    Assert.assertArrayEquals(arguments, flattened);
  }

  @Test
  public void testFlatten_optionsWithoutLeadingHyphens_arguments() {
    Options options = new Options();
    options.addOption(Option.builder("D").hasArg().build());

    String[] arguments = new String[]{"Dproperty=value"};

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, false);

    Assert.assertArrayEquals(new String[]{"-D", "property=value"}, flattened);
  }

  @Test
  public void testFlatten_optionsWithLeadingHyphens_arguments() {
    Options options = new Options();
    options.addOption(Option.builder("f").build());

    String[] arguments = new String[]{"-f"};

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, false);

    Assert.assertArrayEquals(arguments, flattened);
  }

  @Test
  public void testFlatten_optionsWithDoubleHyphens_arguments() {
    Options options = new Options();
    options.addOption(Option.builder("D").hasArg().build());

    String[] arguments = new String[]{"--foo=value"};

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, false);

    Assert.assertArrayEquals(arguments, flattened);
  }

  @Test
  public void testFlatten_optionsWithDoubleHyphensAndNullValue_arguments() {
    Options options = new Options();
    options.addOption(Option.builder("D").hasArg().build());

    String[] arguments = new String[]{"--foo="};

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, false);

    Assert.assertArrayEquals(arguments, flattened);
  }

  @Test
  public void testFlatten_optionsWithDoubleHyphensAndEqualSign_arguments() {
    Options options = new Options();
    options.addOption(Option.builder("D").hasArg().build());

    String[] arguments = new String[]{"--foo"};

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, false);

    Assert.assertArrayEquals(arguments, flattened);
  }

  @Test
  public void testFlatten_optionsWithDoubleHyphensAndStopFlag_arguments() {
    Options options = new Options();
    options.addOption(Option.builder("D").hasArg().build());

    String[] arguments = new String[]{"--", "--foo=value"};

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, true);

    Assert.assertArrayEquals(new String[]{"--", "--foo=value"}, flattened);
  }

  @Test
  public void testFlatten_optionsWithDoubleHyphensAndStopFlagIncomplete_arguments() {
    Options options = new Options();
    options.addOption(Option.builder("D").hasArg().build());

    String[] arguments = new String[]{"--", "--foo:value"};

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, true);

    Assert.assertArrayEquals(new String[]{"--", "--foo:value"}, flattened);
  }

  @Test
  public void testFlatten_optionsWithDoubleHyphensAndStopFlagNested_arguments() {
    Options options = new Options();
    options.addOption(Option.builder("D").hasArg().build());

    String[] arguments = new String[]{"--", "--foo=value", "--bar=value"};

    GnuParser parser = new GnuParser();
    String[] flattened = parser.flatten(options, arguments, true);

    Assert.assertArrayEquals(new String[]{"--", "--foo=value", "--bar=value"}, flattened);
  }

}