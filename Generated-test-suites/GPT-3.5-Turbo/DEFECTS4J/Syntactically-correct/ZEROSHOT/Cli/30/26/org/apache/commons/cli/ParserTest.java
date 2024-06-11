import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new ParserStub();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new ParserStub();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Parser parser = new ParserStub();
        parser.setOptions(options);
        assertEquals(parser.getRequiredOptions(), options.getRequiredOptions());
    }

    @Test
    public void testParseWithOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        String[] arguments = {"-a", "-b", "value"};
        Parser parser = new ParserStub();
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals(2, cmd.getOptions().length);
        assertEquals("a", cmd.getOptions()[0].getOpt());
        assertEquals(false, cmd.getOptions()[0].hasArg());
        assertEquals("b", cmd.getOptions()[1].getOpt());
        assertEquals(true, cmd.getOptions()[1].hasArg());
        assertEquals("value", cmd.getOptions()[1].getValue());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"-b"};
        try {
            Parser parser = new ParserStub();
            parser.parse(options, arguments);
        } catch (ParseException e) {
            assertEquals("Unrecognized option: -b", e.getMessage());
            throw e;
        }
    }

    class ParserStub extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return arguments;
        }
    }
}