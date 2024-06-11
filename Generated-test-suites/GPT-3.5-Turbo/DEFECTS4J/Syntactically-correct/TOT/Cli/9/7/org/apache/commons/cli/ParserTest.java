import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ParserTest {

    private Parser parser;
    private CommandLine cmd;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").create("v"));
        options.addOption(OptionBuilder.withLongOpt("output").hasArg().create("o"));
        parser.setOptions(options);
    }

    @Test
    public void testParse_ShouldParseOptionsSuccessfully() throws ParseException {
        String[] arguments = new String[] { "-v", "--output", "file.txt" };
        cmd = parser.parse(parser.getOptions(), arguments);
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("file.txt", cmd.getOptionValue("o"));
    }

    @Test
    public void testParse_ShouldThrowException_WhenUnknownOptionIsProvided() throws ParseException {
        exception.expect(UnrecognizedOptionException.class);
        exception.expectMessage("Unrecognized option: -u");
        String[] arguments = new String[] { "-u" };
        parser.parse(parser.getOptions(), arguments);
    }

    @Test
    public void testParse_ShouldThrowException_WhenRequiredOptionIsNotProvided() throws ParseException {
        exception.expect(MissingOptionException.class);
        exception.expectMessage("Missing required option: o");
        String[] arguments = new String[] {};
        parser.parse(parser.getOptions(), arguments);
    }

    @Test
    public void testParse_ShouldThrowException_WhenRequiredOptionHasNoValue() throws ParseException {
        exception.expect(MissingArgumentException.class);
        exception.expectMessage("Missing argument for option:o");
        String[] arguments = new String[] { "-o" };
        parser.parse(parser.getOptions(), arguments);
    }

    @Test
    public void testParse_ShouldStopAtNonOption_WhenStopAtNonOptionIsTrue() throws ParseException {
        String[] arguments = new String[] { "-v", "input.txt", "--output", "file.txt" };
        cmd = parser.parse(parser.getOptions(), arguments, true);
        assertTrue(cmd.hasOption("v"));
        assertFalse(cmd.hasOption("o"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("input.txt", cmd.getArgs()[0]);
        assertEquals("file.txt", cmd.getArgs()[1]);
    }

    @Test
    public void testProcessProperties_ShouldSetOptionValuesFromProperties() throws ParseException {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").create("v"));
        options.addOption(OptionBuilder.withLongOpt("output").hasArg().create("o"));
        parser.setOptions(options);
        Properties properties = new Properties();
        properties.setProperty("v", "true");
        properties.setProperty("o", "file.txt");
        parser.processProperties(properties);
        assertTrue(cmd.hasOption("v"));
        assertTrue(cmd.hasOption("o"));
        assertEquals("file.txt", cmd.getOptionValue("o"));
    }

    @Test
    public void testCheckRequiredOptions_ShouldThrowException_WhenRequiredOptionsNotPresent() throws ParseException {
        exception.expect(MissingOptionException.class);
        exception.expectMessage("Missing required option: o");
        parser.checkRequiredOptions();
    }

    @Test
    public void testCheckRequiredOptions_ShouldNotThrowException_WhenRequiredOptionsPresent() throws ParseException {
        String[] arguments = new String[] { "-o", "file.txt" };
        cmd = parser.parse(parser.getOptions(), arguments);
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessArgs_ShouldAddArgumentValuesToOption() throws ParseException {
        Option option = new Option("o", true, "output file");
        option.setArgName("file");
        parser.processArgs(option, Arrays.asList("file.txt").listIterator());
        assertTrue(option.hasArg());
        assertEquals("file.txt", option.getValues()[0]);
    }

    @Test
    public void testProcessArgs_ShouldThrowException_WhenArgumentValueRequiredButNotProvided() throws ParseException {
        exception.expect(MissingArgumentException.class);
        exception.expectMessage("Missing argument for option:o");
        Option option = new Option("o", true, "output file");
        option.setArgName("file");
        parser.processArgs(option, Arrays.asList().listIterator());
    }

    @Test
    public void testProcessOption_ShouldThrowException_WhenOptionIsUnrecognized() throws ParseException {
        exception.expect(UnrecognizedOptionException.class);
        exception.expectMessage("Unrecognized option: -u");
        parser.processOption("-u", Arrays.asList().listIterator());
    }

    @Test
    public void testProcessOption_ShouldSetOptionOnCommandLine() throws ParseException {
        Option option = new Option("o", true, "output file");
        parser.processOption("-o", Arrays.asList().listIterator());
        assertTrue(cmd.hasOption("o"));
    }

}