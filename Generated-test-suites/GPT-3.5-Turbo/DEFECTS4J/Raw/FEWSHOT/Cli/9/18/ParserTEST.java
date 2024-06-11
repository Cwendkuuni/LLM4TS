import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParseWithoutProperties(){
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        option1.setRequired(true);
        options.addOption(option1);

        Option option2 = new Option("o", "output", true, "output file");
        option2.setRequired(false);
        options.addOption(option2);

        Option option3 = new Option("d", "debug", false, "enable debug logging");
        option3.setRequired(false);
        options.addOption(option3);

        String[] args = new String[]{"-f", "input.txt", "-o", "output.txt"};

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));

        assertTrue(cmd.hasOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("o"));

        assertFalse(cmd.hasOption("d"));
    }

    @Test
    public void testParseWithProperties(){
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        option1.setRequired(true);
        options.addOption(option1);

        Option option2 = new Option("o", "output", true, "output file");
        option2.setRequired(false);
        options.addOption(option2);

        Option option3 = new Option("d", "debug", false, "enable debug logging");
        option3.setRequired(false);
        options.addOption(option3);

        String[] args = new String[]{"-f", "input.txt"};

        Properties properties = new Properties();
        properties.put("o", "output.txt");
        properties.put("d", "true");

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args, properties);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));

        assertTrue(cmd.hasOption("o"));
        assertEquals("output.txt", cmd.getOptionValue("o"));

        assertTrue(cmd.hasOption("d"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption(){
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        option1.setRequired(true);
        options.addOption(option1);

        Option option2 = new Option("o", "output", true, "output file");
        option2.setRequired(true);
        options.addOption(option2);

        String[] args = new String[]{"-f", "input.txt"};

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParseOptionWithArgument(){
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        option1.setRequired(false);
        options.addOption(option1);

        String[] args = new String[]{"-f", "input.txt"};

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("f"));
        assertEquals("input.txt", cmd.getOptionValue("f"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseOptionWithoutArgument(){
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        option1.setRequired(true);
        options.addOption(option1);

        String[] args = new String[]{"-f"};

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParseOptionWithoutArgumentButWithOptionalArg(){
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        option1.setOptionalArg(true);
        option1.setRequired(false);
        options.addOption(option1);

        String[] args = new String[]{"-f"};

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("f"));
        assertNull(cmd.getOptionValue("f"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption(){
        Options options = new Options();

        Option option1 = new Option("f", "file", true, "input file");
        option1.setRequired(false);
        options.addOption(option1);

        String[] args = new String[]{"-i", "input.txt"};

        Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, args);
    }
    
    private class ParserImpl extends Parser {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return null;
        }
    }
}