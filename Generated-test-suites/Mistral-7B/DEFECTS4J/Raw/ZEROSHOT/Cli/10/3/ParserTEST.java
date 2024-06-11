import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Parser.class)
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = mock(Options.class);
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        parser.setOptions(options);
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        String[] arguments = {"arg1"};
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithOptions() throws ParseException {
        String[] arguments = {"-o", "opt1", "--opt2", "--arg=value"};
        options.addOption("o", "opt1", false, "Option 1");
        options.addOption("opt2", true, "Option 2 has an argument");
        options.addOption("a", "arg", true, "Option with an arg");

        CommandLine cmd = parser.parse(options, arguments);

        assertEquals(1, cmd.getOptions().size());
        Option opt1 = cmd.getOptionByName("opt1");
        assertEquals("opt1", opt1.getLongOpt());
        assertEquals(false, opt1.isRequired());
        assertNull(opt1.getValue());

        Option opt2 = cmd.getOptionByName("opt2");
        assertEquals("opt2", opt2.getLongOpt());
        assertEquals(true, opt2.isRequired());
        assertNull(opt2.getValue());

        Option argOption = cmd.getOptionByName("arg");
        assertEquals("arg", argOption.getLongOpt());
        assertEquals(true, argOption.isRequired());
        assertEquals("value", argOption.getValue());

        assertEquals(2, cmd.getArgs().size());
        assertEquals("--arg=value", cmd.getArgs().get(0));
        assertEquals("opt1", cmd.getArgs().get(1));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        options.addOption("r", "required", true, "Required option");
        String[] arguments = {"-o", "opt1", "--opt2"};
        assertThrows(ParseException.class, () -> parser.parse(options, arguments));
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        options.addOption("o", "opt1", false, "Option 1");
        String[] arguments = {"-x", "arg"};
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments));
    }
}