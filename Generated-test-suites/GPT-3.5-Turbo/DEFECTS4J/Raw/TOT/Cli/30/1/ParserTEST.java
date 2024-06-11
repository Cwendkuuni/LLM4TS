import static org.junit.Assert.*;

import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                // Implement custom flatten method for testing
                return new String[0];
            }
        };
    }

    @Test
    public void testParse_noArguments_noOptions() {
        Options options = new Options();
        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments);
        
        assertNotNull(cmd);
        assertEquals(0, cmd.getOptions().length);
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParse_nonOptionArgument_stopAtNonOptionTrue() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments, true);
        
        assertNotNull(cmd);
        assertEquals(0, cmd.getOptions().length);
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test(expected = ParseException.class)
    public void testParse_nonOptionArgument_stopAtNonOptionFalse() throws ParseException {
        Options options = new Options();
        String[] arguments = {"arg1"};
        parser.parse(options, arguments, false);
    }

    @Test
    public void testParse_singleDashArgument_stopAtNonOptionTrue() {
        Options options = new Options();
        String[] arguments = {"-"};
        CommandLine cmd = parser.parse(options, arguments, true);
        
        assertNotNull(cmd);
        assertEquals(0, cmd.getOptions().length);
        assertEquals(1, cmd.getArgs().length);
        assertEquals("-", cmd.getArgs()[0]);
    }

    @Test
    public void testParse_singleDashArgument_stopAtNonOptionFalse() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-"};
        CommandLine cmd = parser.parse(options, arguments, false);
        assertEquals(0, cmd.getOptions().length);
        assertEquals(1, cmd.getArgs().length);
        assertEquals("-", cmd.getArgs()[0]);
    }

    @Test
    public void testParse_optionArgument() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertEquals(1, cmd.getOptions().length);
        assertTrue(cmd.hasOption("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-b"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_requiredOption_missing() {
        Options options = new Options();
        Option opt = new Option("r", true, "Required option");
        opt.setRequired(true);
        options.addOption(opt);
        String[] arguments = {};
        
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            assertTrue(e.getMissingOptions().contains("r"));
        }
    }

    @Test
    public void testParse_optionGroup() {
        Options options = new Options();
        
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", false, "Option A"));
        group.addOption(new Option("b", false, "Option B"));
        options.addOptionGroup(group);
        
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertEquals(1, cmd.getOptions().length);
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParse_optionWithArgument() {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().desc("Option with argument").build());
        String[] arguments = {"-f", "filename"};
        CommandLine cmd = parser.parse(options, arguments);

        assertNotNull(cmd);
        assertEquals(1, cmd.getOptions().length);
        assertTrue(cmd.hasOption("f"));
        assertEquals("filename", cmd.getOptionValue("f"));
    }
}