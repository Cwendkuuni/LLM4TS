import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        Option opt1 = new Option("a", "option a");
        Option opt2 = new Option("b", "option b");
        options.addOption(opt1);
        options.addOption(opt2);

        String[] arguments = {"-a", "-b"};

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", "option a");
        ListIterator iter = Arrays.asList("value1", "value2").listIterator();

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        parser.processArgs(option, iter);

        Assert.assertArrayEquals(option.getValues(), new String[]{"value1", "value2"});
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        Option opt1 = Option.builder("a").hasArg().argName("arg").desc("option a").build();
        options.addOption(opt1);

        String arg = "-a value";

        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        CommandLine cmd = new CommandLine();

        ListIterator iter = Arrays.asList(arg).listIterator();
        parser.processOption(arg, iter);
        parser.processArgs(opt1, iter);

        cmd.addOption(opt1);
        cmd.addArg("value");

        Assert.assertEquals(cmd, parser.parse(options, new String[]{}));
    }
}