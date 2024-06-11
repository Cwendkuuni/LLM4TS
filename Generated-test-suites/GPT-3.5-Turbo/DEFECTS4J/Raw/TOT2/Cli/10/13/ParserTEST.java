import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.cli.*;

public class ParserTest {

    private Parser parser;
    private Options options;
    private CommandLine cmd;

    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };

        options = new Options();
        options.addOption("a", "optionA", false, "Option A description");
        options.addOption(Option.builder("b").longOpt("optionB").required().hasArg().desc("Option B description").build());

        parser.setOptions(options);
        cmd = new CommandLine();
    }

    @Test
    public void testParse_NoArgs() throws ParseException {
        String[] args = {};
        cmd = parser.parse(options, args);
        assertNull(cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParse_ValidArgs() throws ParseException {
        String[] args = {"-a", "--optionB", "value"};
        cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingOptionValue() throws ParseException {
        String[] args = {"-a", "--optionB"};
        cmd = parser.parse(options, args);
    }

    @Test
    public void testParse_StopAtNonOption() throws ParseException {
        String[] args = {"-a", "--", "arg1", "arg2"};
        cmd = parser.parse(options, args, true);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertEquals("arg1", cmd.getArgs()[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException {
        String[] args = {"-c"};
        cmd = parser.parse(options, args);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options requiredOptions = new Options();
        requiredOptions.addOption("c", "requiredOption", true, "Required Option");
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        parser.setOptions(requiredOptions);
        cmd = parser.parse(requiredOptions, new String[] {});
    }

    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.setProperty("a", "valueA");
        properties.setProperty("b", "true");
        parser.processProperties(properties);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("valueA", cmd.getOptionValue("a"));
    }

    @Test
    public void testProcessOption_Args() throws ParseException {
        @SuppressWarnings("unchecked")
        ListIterator iter = Arrays.asList(new String[]{"value1", "value2"}).listIterator();
        Option option = Option.builder("c").hasArgs().build();
        parser.processOption("c", iter);
        assertTrue(cmd.hasOption("c"));
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessOption_MissingArgs() throws ParseException {
        @SuppressWarnings("unchecked")
        ListIterator iter = Arrays.asList(new String[]{}).listIterator();
        Option option = Option.builder("c").hasArgs().build();
        parser.processOption("c", iter);
    }

    @Test
    public void testProcessOption_NoArgs() throws ParseException {
        @SuppressWarnings("unchecked")
        ListIterator iter = Arrays.asList(new String[]{}).listIterator();
        Option option = Option.builder("c").build();
        parser.processOption("c", iter);
        assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testProcessOption_OptionGroup() throws ParseException {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("d").build();
        Option option2 = Option.builder("e").build();
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        parser.setOptions(options);

        parser.processOption("e", null);
        assertTrue(cmd.hasOption("e"));
        assertFalse(group.getSelected() == null);
        assertEquals("e", group.getSelected().getOpt());
    }

    @Test
    public void testProcessArgs() throws ParseException {
        ListIterator<String> iter = Arrays.asList(new String[]{"value1", "value2"}).listIterator();
        Option option = Option.builder("f").hasArgs().build();
        parser.processArgs(option, iter);
        assertTrue(cmd.hasOption("f"));
        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("f"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_Missing() throws ParseException {
        ListIterator<String> iter = Arrays.asList(new String[]{}).listIterator();
        Option option = Option.builder("f").hasArgs().build();
        parser.processArgs(option, iter);
    }
}