import org.junit.Test;
import org.apache.commons.cli.*;

import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse_withOptionsAndArguments_shouldReturnValidCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = new String[]{"-a", "-b", "value"};

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments);

        assert commandLine.hasOption("a");
        assert commandLine.hasOption("b");
        assert commandLine.getOptionValue("b").equals("value");
    }

    @Test
    public void testParse_withProperties_shouldSetOptionValuesFromProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = new String[]{"-a"};

        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, arguments, properties);

        assert commandLine.hasOption("a");
        assert commandLine.hasOption("b");
        assert commandLine.getOptionValue("b").equals("value");
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_withRequiredOptionMissing_shouldThrowMissingOptionException() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());

        String[] arguments = new String[]{};

        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testProcessArgs_withOptionAndArguments_shouldAddValuesToOption() throws ParseException {
        Option option = Option.builder("a").hasArgs().build();

        String[] arguments = new String[]{"value1", "value2"};

        Parser parser = new DefaultParser();
        parser.processArgs(option, Arrays.asList(arguments).listIterator());

        assert option.getValues().length == 2;
        assert option.getValues()[0].equals("value1");
        assert option.getValues()[1].equals("value2");
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_withRequiredArgumentMissing_shouldThrowMissingArgumentException() throws ParseException {
        Option option = Option.builder("a").required().hasArgs().build();

        String[] arguments = new String[]{};

        Parser parser = new DefaultParser();
        parser.processArgs(option, Arrays.asList(arguments).listIterator());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_withUnrecognizedOption_shouldThrowUnrecognizedOptionException() throws ParseException {
        String option = "c";

        Parser parser = new DefaultParser();
        parser.processOption(option, Arrays.asList().listIterator());
    }

    @Test
    public void testProcessOption_withValidOption_shouldAddOptionToCommandLine() throws ParseException {
        Option option = Option.builder("a").build();

        String arg = "arg";

        DefaultParser parser = new DefaultParser();
        parser.processOption("a", Arrays.asList().listIterator());

        assert parser.getCommandLine().hasOption("a");
    }

    @Test
    public void testCheckRequiredOptions_withAllRequiredOptionsPresent_shouldNotThrowMissingOptionException() throws MissingOptionException {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").required().build());

        Parser parser = new DefaultParser();
        CommandLine commandLine = new CommandLine();
        parser.setOptions(options);
        parser.setCommandLine(commandLine);

        parser.checkRequiredOptions();
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_withMissingRequiredOption_shouldThrowMissingOptionException() throws MissingOptionException {
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").required().build());

        Parser parser = new DefaultParser();
        CommandLine commandLine = new CommandLine();
        parser.setOptions(options);
        parser.setCommandLine(commandLine);

        parser.checkRequiredOptions();
    }

}