import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        Parser parser = new TestParser();

        String[] arguments = {"-a", "value1", "--option", "value2"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "value1", "--option", "value2"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        options.addOption("b", false, "Option b");

        Parser parser = new TestParser();

        String[] arguments = {"-a", "value1", "-b"};
        CommandLine commandLine = parser.parse(options, arguments);

        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("value1", commandLine.getOptionValue("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        Parser parser = new TestParser();

        String[] arguments = {"-c", "value"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option a").setRequired(true);
        options.addOption("b", false, "Option b");

        Parser parser = new TestParser();

        String[] arguments1 = {"-a", "value1"};
        CommandLine commandLine1 = parser.parse(options, arguments1);
        Assert.assertTrue(commandLine1.hasOption("a"));

        String[] arguments2 = {"-b"};
        CommandLine commandLine2 = parser.parse(options, arguments2);
        Assert.assertFalse(commandLine2.hasOption("a"));
        Assert.assertTrue(commandLine2.hasOption("b"));
    }

    private class TestParser extends Parser {

        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }

        @Override
        protected void processOption(String arg, ListIterator iter) throws ParseException {
        }
    }

}