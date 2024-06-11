import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse_OptionsAndArgumentsGiven_ShouldReturnCorrectCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        options.addOption(Option.builder("c").build());
        String[] arguments = {"-a", "-b", "argument1", "-c", "argument2"};

        CommandLine expectedCmd = new CommandLine();
        expectedCmd.addOption(Option.builder("a").build());
        expectedCmd.addOption(Option.builder("b").build());
        expectedCmd.addArg("argument1");
        expectedCmd.addOption(Option.builder("c").build());
        expectedCmd.addArg("argument2");

        Parser parser = new ParserImpl();
        CommandLine actualCmd = parser.parse(options, arguments);

        Assert.assertEquals(expectedCmd, actualCmd);
    }

    @Test
    public void testParse_MissingRequiredOption_ShouldThrowMissingOptionException() {
        Options options = new Options();
        options.addOption(Option.builder("a").required(true).build());
        String[] arguments = {};

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, arguments);
            Assert.fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            Assert.assertTrue(e.getMissingOptions().contains("a"));
        }
    }

    @Test
    public void testParse_UnrecognizedOption_ShouldThrowUnrecognizedOptionException() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        String[] arguments = {"-a", "-b"};

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, arguments);
            Assert.fail("Expected UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: -b", e.getMessage());
            Assert.assertEquals("-b", e.getOption());
        }
    }

    @Test
    public void testParse_ArgumentValueNotProvidedForOption_ShouldThrowMissingArgumentException() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArg(true).build());
        String[] arguments = {"-a"};

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, arguments);
            Assert.fail("Expected MissingArgumentException");
        } catch (MissingArgumentException e) {
            Assert.assertEquals("Missing argument for option: -a", e.getMessage());
        }
    }

    @Test
    public void testParse_StopAtNonOptionEnabled_UnrecognizedArgument_ShouldAddToArgsList() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-a", "-b", "argument1", "-c", "argument2", "extraArgument"};

        CommandLine expectedCmd = new CommandLine();
        expectedCmd.addOption(Option.builder("a").build());
        expectedCmd.addOption(Option.builder("b").build());
        expectedCmd.addArg("argument1");
        expectedCmd.addOption(Option.builder("c").build());
        expectedCmd.addArg("argument2");
        expectedCmd.addArg("extraArgument");

        Parser parser = new ParserImpl();
        parser.parse(options, arguments, true);

        Assert.assertEquals(expectedCmd, parser.getCmd());
    }

    @Test
    public void testFlatten_StopAtNonOptionEnabled_UnrecognizedArgument_ShouldStopFlattening() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-a", "-b", "argument1", "-c", "argument2"};

        String[] expectedFlattenedArguments = {"-a", "-b", "argument1", "-c", "argument2"};

        Parser parser = new ParserImpl();
        String[] actualFlattenedArguments = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expectedFlattenedArguments, actualFlattenedArguments);
    }

    @Test
    public void testFlatten_StopAtNonOptionDisabled_UnrecognizedArgument_ShouldAddToFlattenedArguments() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").build());
        String[] arguments = {"-a", "-b", "argument1", "-c", "argument2"};

        String[] expectedFlattenedArguments = {"-a", "-b", "argument1", "-c", "argument2"};

        Parser parser = new ParserImpl();
        String[] actualFlattenedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedFlattenedArguments, actualFlattenedArguments);
    }
}