import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSetOptions() {
        Options options = new Options();
        Parser parser = new ParserTestImpl();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        Parser parser = new ParserTestImpl();
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Parser parser = new ParserTestImpl();
        parser.setOptions(options);
        assertEquals(new ArrayList(options.getRequiredOptions()), parser.getRequiredOptions());
    }

    @Test
    public void testFlatten() throws ParseException {
        Options opts = new Options();
        String[] arguments = new String[]{};
        boolean stopAtNonOption = false;
        Parser parser = new ParserTestImpl();
        String[] expectedResult = new String[]{};
        assertArrayEquals(expectedResult, parser.flatten(opts, arguments, stopAtNonOption));
    }

    @Test
    public void testParse1() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{};
        Parser parser = new ParserTestImpl();
        CommandLine expectedCmd = new CommandLine();
        assertEquals(expectedCmd, parser.parse(options, arguments));
    }

    @Test
    public void testParse2() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{};
        Properties properties = new Properties();
        Parser parser = new ParserTestImpl();
        CommandLine expectedCmd = new CommandLine();
        assertEquals(expectedCmd, parser.parse(options, arguments, properties));
    }

    @Test
    public void testParse3() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{};
        boolean stopAtNonOption = false;
        Parser parser = new ParserTestImpl();
        CommandLine expectedCmd = new CommandLine();
        assertEquals(expectedCmd, parser.parse(options, arguments, stopAtNonOption));
    }

    @Test
    public void testParse4() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{};
        Properties properties = new Properties();
        boolean stopAtNonOption = false;
        Parser parser = new ParserTestImpl();
        CommandLine expectedCmd = new CommandLine();
        assertEquals(expectedCmd, parser.parse(options, arguments, properties, stopAtNonOption));
    }

    @Test
    public void testCheckRequiredOptions() {
        Parser parser = new ParserTestImpl();
        try {
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            return; // expected exception
        }
        fail("Expected MissingOptionException");
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option opt = new Option("test", true, "");
        ListIterator iter = new ArrayList().listIterator();
        Parser parser = new ParserTestImpl();
        parser.processArgs(opt, iter);
        assertEquals(new ArrayList(Arrays.asList(new String[]{""})), opt.getValues());
    }

    @Test
    public void testProcessOption() throws ParseException {
        String arg = "";
        ListIterator iter = new ArrayList().listIterator();
        Parser parser = new ParserTestImpl();
        try {
            parser.processOption(arg, iter);
        } catch (UnrecognizedOptionException e) {
            return; // expected exception
        }
        fail("Expected UnrecognizedOptionException");
    }

    private class ParserTestImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
            return new String[0];
        }
    }
}