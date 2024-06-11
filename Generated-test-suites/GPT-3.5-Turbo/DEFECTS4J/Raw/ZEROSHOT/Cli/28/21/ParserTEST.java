import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption("h", "help", false, "Prints help information");
        options.addOption("v", "version", false, "Prints version information");
        
        String[] args = new String[] {"-h"};
        
        CommandLineParser parser = new Parser();
        try {
            CommandLine cmd = parser.parse(options, args);
            assertTrue(cmd.hasOption("h"));
        } catch (ParseException ex) {
            fail("ParseException thrown");
        }
    }

    @Test
    public void testProcessArgs() {
        Option option = new Option("o", "output", true, "Sets the output file");
        option.setArgs(1);
        
        String[] args = new String[] {"-o", "output.txt"};
        
        ListIterator<String> iter = Arrays.asList(args).listIterator();
        
        Parser parser = new Parser();
        try {
            parser.processArgs(option, iter);
            assertEquals("output.txt", option.getValues()[0]);
        } catch (ParseException ex) {
            fail("ParseException thrown");
        }
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        options.addOption("v", "verbose", false, "Sets the output verbosity");
        
        String arg = "v";
        
        ListIterator<String> iter = Collections.singletonList(arg).listIterator();
        
        Parser parser = new Parser();
        
        try {
            parser.processOption(arg, iter);
            CommandLine cmd = parser.cmd;
            assertTrue(cmd.hasOption("v"));
        } catch (ParseException ex) {
            fail("ParseException thrown");
        }
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("l", "list", false, "Lists all files");
        options.addOption("f", "file", true, "Specifies a file to operate on");
        
        String[] arguments = new String[] {"-l", "-f", "input.txt"};
        boolean stopAtNonOption = false;
        
        Parser parser = new Parser();
        
        try {
            String[] result = parser.flatten(options, arguments, stopAtNonOption);
            assertArrayEquals(new String[] {"-l", "-f", "input.txt"}, result);
        } catch (ParseException ex) {
            fail("ParseException thrown");
        }
    }

}