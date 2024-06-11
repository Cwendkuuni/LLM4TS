
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParseWithNoArguments() {
        // Arrange
        Options options = new Options();
        Parser parser = new DefaultParser();

        // Act
        CommandLine cmd = parser.parse(options, new String[0]);

        // Assert
        // Verify that the command line object is not null
        assert(cmd != null);
        // Verify that no options or arguments are present in the command line object
        assert(cmd.getOptions().length == 0);
        assert(cmd.getArgList().isEmpty());
    }

    @Test
    public void testParseWithOptionsAndArguments() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();

        // Act
        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1", "arg2"});

        // Assert
        // Verify that the command line object is not null
        assert(cmd != null);
        // Verify that the specified option is present in the command line object
        assert(cmd.hasOption("a"));
        assert(cmd.getOptionValue("a") == null);
        // Verify that the specified arguments are present in the command line object
        assert(cmd.getArgList().size() == 2);
        assert(cmd.getArgList().get(0).equals("arg1"));
        assert(cmd.getArgList().get(1).equals("arg2"));
    }

    @Test
    public void testParseWithRequiredOptionMissing() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", "optionA", true, "Option A");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();

        // Act and Assert
        // Verify that a MissingOptionException is thrown
        try {
            parser.parse(options, new String[0]);
            assert(false);
        } catch (MissingOptionException e) {
            assert(true);
        }
    }

    @Test
    public void testParseWithProperties() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", "optionA", true, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "valueA");

        // Act
        CommandLine cmd = parser.parse(options, new String[0], properties);

        // Assert
        // Verify that the specified option with valueA is present in the command line object
        assert(cmd.hasOption("a"));
        assert(cmd.getOptionValue("a").equals("valueA"));
    }

    @Test
    public void testParseWithStopAtNonOption() {
        // Arrange
        Options options = new Options();
        Option optionA = new Option("a", "optionA", true, "Option A");
        Option optionB = new Option("b", "optionB", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);
        Parser parser = new DefaultParser();

        // Act
        CommandLine cmd = parser.parse(options, new String[]{"-a", "arg1", "-b", "arg2", "nonOption"});

        // Assert
        // Verify that the specified options and arguments are present in the command line object,
        // and the nonOption argument is not present
        assert(cmd.hasOption("a"));
        assert(cmd.getOptionValue("a").equals("arg1"));
        assert(cmd.hasOption("b"));
        assert(cmd.getOptionValue("b").equals("arg2"));
        assert(cmd.getArgList().isEmpty());
    }

    @Test
    public void testParseWithUnrecognizedOption() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", "optionA", false, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();

        // Act and Assert
        // Verify that an UnrecognizedOptionException is thrown
        try {
            parser.parse(options, new String[]{"-b"});
            assert(false);
        } catch (UnrecognizedOptionException e) {
            assert(true);
        }
    }

    @Test
    public void testParseWithOptionAndArgumentValuesContainingQuotes() {
        // Arrange
        Options options = new Options();
        Option option = new Option("a", "optionA", true, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();

        // Act
        CommandLine cmd = parser.parse(options, new String[]{"-a", "\"value1\"", "\"value2\""});

        // Assert
        // Verify that the specified option with quoted values is present in the command line object
        assert(cmd.hasOption("a"));
        assert(cmd.getOptionValue("a").equals("value1,value2"));
    }
}

