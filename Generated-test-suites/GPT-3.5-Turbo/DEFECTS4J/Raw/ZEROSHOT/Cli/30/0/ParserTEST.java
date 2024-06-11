import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Properties;

import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };

        options = new Options();
    }

    @Test
    public void testParse() throws ParseException {
        String[] args = {"-a", "arg1", "-b", "arg2"};

        CommandLine cmd = parser.parse(options, args);

        assertEquals(2, cmd.getOptions().length);
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("arg2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("a", "arg1");
        properties.setProperty("b", "arg2");

        CommandLine cmd = parser.parse(options, new String[0], properties);

        assertEquals(2, cmd.getOptions().length);
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("arg2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"-a", "arg1", "nonOption"};

        CommandLine cmd = parser.parse(options, args, true);

        assertEquals(1, cmd.getOptions().length);
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals(1, cmd.getArgs().length);
        assertEquals("nonOption", cmd.getArgs()[0]);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "option a");

        ListIterator<String> iter = Arrays.asList("arg1", "arg2").listIterator();

        parser.processArgs(option, iter);

        assertEquals(2, option.getValues().length);
        assertEquals("arg1", option.getValue(0));
        assertEquals("arg2", option.getValue(1));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = new Option("a", true, "option a");

        ListIterator<String> iter = Collections.<String>emptyList().listIterator();

        parser.processArgs(option, iter);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = Option.builder("a").hasArg().build();

        ListIterator<String> iter = Collections.singletonList("arg1").listIterator();

        parser.processOption("a", iter);

        assertEquals(1, option.getValues().length);
        assertEquals("arg1", option.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        ListIterator<String> iter = Collections.singletonList("unknown").listIterator();

        parser.processOption("unknown", iter);
    }

    @Test
    public void testUpdateRequiredOptions() throws ParseException {
        Option option = Option.builder("a").required().build();

        parser.getOptions().addOption(option);

        assertEquals(1, parser.getRequiredOptions().size());

        parser.updateRequiredOptions(option);

        assertEquals(0, parser.getRequiredOptions().size());
    }

    @Test
    public void testUpdateRequiredOptionsInGroup() throws ParseException {
        Option option1 = Option.builder("a").required().build();
        Option option2 = Option.builder("b").required().build();

        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        group.addOption(option1);
        group.addOption(option2);

        parser.getOptions().addOptionGroup(group);

        assertEquals(2, parser.getRequiredOptions().size());

        parser.updateRequiredOptions(option1);

        assertEquals(1, parser.getRequiredOptions().size());
        assertEquals(option2, group.getSelected());
    }

}