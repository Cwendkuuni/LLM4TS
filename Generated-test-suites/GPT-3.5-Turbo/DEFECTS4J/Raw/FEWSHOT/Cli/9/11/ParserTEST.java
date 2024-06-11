import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_Options_StringArray() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("t", true, "Option t");

        String[] args = {"-f", "-t", "value"};
        CommandLine cmd = new CommandLine();
        cmd.addOption(options.getOption("f"));
        cmd.addOptionValue(options.getOption("t"), "value");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine result = parser.parse(options, args);

        assertEquals(cmd, result);
    }

    @Test
    public void testParse_Options_StringArray_Properties() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("t", true, "Option t");

        String[] args = {"-f", "-t", "value"};
        Properties properties = new Properties();
        properties.put("f", "true");
        properties.put("t", "value");

        CommandLine cmd = new CommandLine();
        cmd.addOption(options.getOption("f"));
        cmd.addOptionValue(options.getOption("t"), "value");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine result = parser.parse(options, args, properties);

        assertEquals(cmd, result);
    }

    @Test
    public void testParse_Options_StringArray_StopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("t", true, "Option t");

        String[] args = {"-f", "-t", "value", "nonOption"};
        CommandLine cmd = new CommandLine();
        cmd.addOption(options.getOption("f"));
        cmd.addOptionValue(options.getOption("t"), "value");
        cmd.addArg("nonOption");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine result = parser.parse(options, args, true);

        assertEquals(cmd, result);
    }

    @Test
    public void testParse_Options_StringArray_Properties_StopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("f", false, "Option f");
        options.addOption("t", true, "Option t");

        String[] args = {"-f", "-t", "value", "nonOption"};
        Properties properties = new Properties();
        properties.put("f", "true");
        properties.put("t", "value");

        CommandLine cmd = new CommandLine();
        cmd.addOption(options.getOption("f"));
        cmd.addOptionValue(options.getOption("t"), "value");
        cmd.addArg("nonOption");

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine result = parser.parse(options, args, properties, true);

        assertEquals(cmd, result);
    }
}