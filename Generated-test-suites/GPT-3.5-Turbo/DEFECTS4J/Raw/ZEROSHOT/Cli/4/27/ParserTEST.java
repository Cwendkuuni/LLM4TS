import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.util.Properties;


public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = {};
        Properties properties = new Properties();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        CommandLine result = parser.parse(options, arguments, properties);

        // TODO: Add assertions
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("o", "option", true, "option");
        ListIterator iterator = null;

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        parser.processArgs(option, iterator);

        // TODO: Add assertions
    }

    @Test
    public void testProcessOption() throws ParseException {
        String arg = "-o";
        ListIterator iterator = null;

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };

        parser.processOption(arg, iterator);

        // TODO: Add assertions
    }

}