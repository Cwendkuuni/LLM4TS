import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import java.util.Properties;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };

        options = new Options();
    }

    @Test
    public void testParse() throws ParseException {
        String[] arguments = {"-a", "value1", "arg1", "-b", "value2", "--option", "value3"};

        CommandLine result = parser.parse(options, arguments);

        Assert.assertEquals("value1", result.getOptionValue("a"));
        Assert.assertEquals("value2", result.getOptionValue("b"));
        Assert.assertEquals("value3", result.getOptionValue("option"));
        Assert.assertEquals("arg1", result.getArgs()[0]);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Option option = new Option("a", true, "");
        option.setRequired(true);
        options.addOption(option);

        String[] arguments = {};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Option option = new Option("a", true, "");
        options.addOption(option);

        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("a", true, "");
        options.addOption(option);

        String[] arguments = {"-a", "value"};

        CommandLine result = parser.parse(options, arguments);

        Assert.assertEquals("value", result.getOptionValue("a"));
    }
}