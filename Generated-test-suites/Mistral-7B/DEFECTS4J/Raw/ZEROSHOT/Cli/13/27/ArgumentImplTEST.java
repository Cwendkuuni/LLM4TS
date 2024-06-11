import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    private ArgumentImpl argument;
    private CommandLineParser parser;

    @Before
    public void setUp() {
        List<String> defaultValues = Arrays.asList("default1", "default2");
        parser = new DefaultParser();
        argument = new ArgumentImpl("arg", "Description", 1, 2, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR,
                new MyValidator(), DEFAULT_CONSUME_REMAINING, defaultValues, 0);
    }

    @After
    public void tearDown() {
        argument = null;
        parser = null;
    }

    @Test
    public void testArgumentName() {
        assertEquals("arg", argument.getPreferredName());
    }

    @Test
    public void testArgumentDescription() {
        assertEquals("Description", argument.getDescription());
    }

    @Test
    public void testArgumentMinValue() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testArgumentMaxValue() {
        assertEquals(2, argument.getMaximum());
    }

    @Test
    public void testArgumentSeparator() {
        assertEquals(DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void testArgumentTriggers() {
        Set<DisplaySetting> triggers = argument.getTriggers();
        assertTrue(triggers.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentInvalidMinMax() {
        List<String> invalidDefaultValues = Arrays.asList("default1", "default2", "default3");
        ArgumentImpl invalidArgument = new ArgumentImpl("arg", "Description", 1, 0, DEFAULT_INITIAL_SEPARATOR,
                DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), DEFAULT_CONSUME_REMAINING, invalidDefaultValues, 0);
    }

    @Test
    public void testArgumentProcessValues() throws ParseException {
        WriteableCommandLine cmdLine = new BasicParseResult();
        List<String> args = Arrays.asList("--arg1 value1", "--arg1 value2", "--argConsumeAll");
        argument.process(cmdLine, args.listIterator(0), OptionBuilder.createOption(argument).build());
        List<String> cmdLineArgs = cmdLine.getParsedArgs();
        assertEquals(3, cmdLineArgs.size());
        assertEquals("value1", cmdLineArgs.get(1));
        assertEquals("value2", cmdLineArgs.get(2));
        assertEquals("--argConsumeAll", cmdLineArgs.get(3));
    }

    @Test(expected = OptionException.class)
    public void testArgumentProcessValuesException() throws ParseException {
        WriteableCommandLine cmdLine = new BasicParseResult();
        List<String> args = Arrays.asList("--arg1 value1", "--arg1 value2", "--argConsumeAll", "--argConsumerRemaining");
        argument.process(cmdLine, args.listIterator(0), OptionBuilder.createOption(argument).build());
        List<String> cmdLineArgs = cmdLine.getParsedArgs();
        assertEquals(4, cmdLineArgs.size());
        assertEquals("value1", cmdLineArgs.get(1));
        assertEquals("value2", cmdLineArgs.get(2));
        assertEquals("--argConsumeAll", cmdLineArgs.get(3));
        assertEquals("--argConsumerRemaining", cmdLineArgs.get(4));
    }

    @Test
    public void testArgumentCanProcess() throws ParseException {
        assertTrue(argument.canProcess(new BasicParseResult(), "--arg1 value1"));
    }

    @Test
    public void testArgumentCannotProcess() throws ParseException {
        assertFalse(argument.canProcess(new BasicParseResult(), "-h"));
    }

    @Test
    public void testArgumentValidate() throws ParseException {
        WriteableCommandLine cmdLine = new BasicParseResult();
        List<String> args = Arrays.asList("--arg1 value1", "--arg1 value2");
        argument.validate(cmdLine, OptionBuilder.createOption(argument).build());
    }

    @Test(expected = OptionException.class)
    public void testArgumentValidateException() throws ParseException {
        WriteableCommandLine cmdLine = new BasicParseResult();
        List<String> args = Arrays.asList("--arg1 value1", "--arg1 value2", "--arg1 value3");
        argument.validate(cmdLine, OptionBuilder.createOption(argument).build());
    }

    @Test
    public void testArgumentHelp() {
        HelpFormatter formatter = new HelpFormatter();
        HelpLine help = argument.helpLines(0, null, null).get(0);
        String helpText = formatter.printHelp("CommandLineTest", Arrays.asList(help), true);
        assertTrue(helpText.contains("arg"));
        assertTrue(helpText.contains("Description"));
        assertTrue(helpText.contains("--arg [ARG]"));
        assertTrue(helpText.contains("<ARG>"));
        assertTrue(helpText.contains("(1)"));
        assertTrue(helpText.contains("(2)"));
    }
}

class MyValidator implements Validator {

    @Override
    public void validate(List<String> values) throws InvalidArgumentException {
        // empty validation
    }
}