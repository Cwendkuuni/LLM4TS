import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse_NoOptions_NoArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = {};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParse_WithOption_NoArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "option_a", false, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] args = {"-a"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParse_WithOption_WithArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "option_a", true, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] args = {"-a", "value"};

        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParse_WithRequiredOption_OptionMissing() {
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required Option");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new DefaultParser();
        String[] args = {};

        try {
            parser.parse(options, args);
            fail("ParseException should be thrown");
        } catch (ParseException e) {
            assertEquals("Missing required option: r", e.getMessage());
        }
    }

    @Test
    public void testProcessArgs_OptionWithArgument_NoArguments() {
        Options options = new Options();
        Option option = new Option("a", "option_a", true, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();
        ListIterator iter = Arrays.asList(new String[]{}).listIterator();

        try {
            parser.processArgs(option, iter);
            fail("MissingArgumentException should be thrown");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:a", e.getMessage());
        }
    }

    @Test
    public void testProcessArgs_OptionWithArgument_WithArguments() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "option_a", true, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();
        ListIterator iter = Arrays.asList(new String[]{"value1", "value2"}).listIterator();

        parser.processArgs(option, iter);

        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
    }

    @Test
    public void testProcessOption_OptionExistsAndHasArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "option_a", true, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();
        ListIterator iter = Arrays.asList(new String[]{"-a", "value"}).listIterator();

        parser.processOption("-a", iter);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testProcessOption_OptionExistsWithoutArgument() throws ParseException {
        Options options = new Options();
        Option option = new Option("a", "option_a", false, "Option A");
        options.addOption(option);
        Parser parser = new DefaultParser();
        ListIterator iter = Arrays.asList(new String[]{"-a"}).listIterator();

        parser.processOption("-a", iter);

        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testProcessOption_OptionDoesNotExist() {
        Options options = new Options();
        Parser parser = new DefaultParser();
        ListIterator iter = Arrays.asList(new String[]{"-x"}).listIterator();

        try {
            parser.processOption("-x", iter);
            fail("UnrecognizedOptionException should be thrown");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -x", e.getMessage());
        }
    }

}