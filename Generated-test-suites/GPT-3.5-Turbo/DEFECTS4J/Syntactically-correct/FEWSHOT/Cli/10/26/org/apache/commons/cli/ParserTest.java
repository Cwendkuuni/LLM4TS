import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse_noArguments() {
        Options options = new Options();
        CommandLine expected = new CommandLine();
        CommandLine actual = new Parser().parse(options, new String[0]);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_singleOptionNoArgument() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        CommandLine expected = new CommandLine();
        expected.addOption(new Option("a", false));
        CommandLine actual = new Parser().parse(options, new String[]{"-a"});
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_singleOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        CommandLine expected = new CommandLine();
        expected.addOption(new Option("a", true));
        expected.getOption("a").addValue("value");
        CommandLine actual = new Parser().parse(options, new String[]{"-a", "value"});
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_multipleOptionsWithArguments() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        CommandLine expected = new CommandLine();
        expected.addOption(new Option("a", true));
        expected.addOption(new Option("b", true));
        expected.getOption("a").addValue("value");
        expected.getOption("b").addValue("value");
        CommandLine actual = new Parser().parse(options, new String[]{"-a", "value", "-b", "value"});
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_stopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        CommandLine expected = new CommandLine();
        expected.addOption(new Option("a", false));
        expected.addArg("nonOption");
        CommandLine actual = new Parser().parse(options, new String[]{"-a", "nonOption"}, true);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_requiredOptionMissing() {
        Options options = new Options();
        options.addRequiredOption("a", true, "option a");
        try {
            new Parser().parse(options, new String[0]);
            // If no ParseException is thrown, the test fails
            fail();
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }
}