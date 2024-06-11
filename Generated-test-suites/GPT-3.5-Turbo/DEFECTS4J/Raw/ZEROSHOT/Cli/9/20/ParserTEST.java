import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        parser.setOptions(options);

        Assert.assertNotNull(parser.getOptions());
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        parser.setOptions(options);

        Assert.assertNotNull(parser.getOptions());
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        parser.setOptions(options);

        Assert.assertNotNull(parser.getRequiredOptions());
        Assert.assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }

    @Test
    public void testParse() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        options.addOption(new Option("a", "option-a", false, "Option A"));
        options.addOption(new Option("b", "option-b", true, "Option B"));

        String[] arguments = new String[]{"-a"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        Option option = new Option("a", "option-a", true, "Option A");
        options.addOption(option);

        String[] arguments = new String[]{"value"};
        ListIterator iter = Arrays.asList(arguments).listIterator();

        parser.processArgs(option, iter);

        Assert.assertEquals("value", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        Option option = new Option("a", "option-a", true, "Option A");
        options.addOption(option);

        String[] arguments = new String[]{};
        ListIterator iter = Arrays.asList(arguments).listIterator();

        parser.processArgs(option, iter);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();
        Option option = new Option("a", "option-a", true, "Option A");
        options.addOption(option);

        String arg = "a";
        ListIterator iter = Arrays.asList(new String[]{arg}).listIterator();

        parser.processOption(arg, iter);

        Assert.assertTrue(parser.getRequiredOptions().isEmpty());
        Assert.assertNotNull(parser.getOptions().getOption("a"));
        Assert.assertTrue(parser.getOptions().getOption("a").hasArg());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        Options options = new Options();

        String arg = "x";
        ListIterator iter = Arrays.asList(new String[]{arg}).listIterator();

        parser.processOption(arg, iter);
    }
}