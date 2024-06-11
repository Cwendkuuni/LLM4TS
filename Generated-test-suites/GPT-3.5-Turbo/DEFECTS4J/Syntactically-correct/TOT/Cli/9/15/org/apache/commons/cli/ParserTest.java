import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ParserTest {

    @Test
    public void testParse_WithValidOptions_ReturnsCommandLineObject() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null; // Mock implementation
            }
        };
        
        Options options = new Options();
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Prints help message.")
                .build());
        
        try {
            String[] arguments = {"-h"};
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("h"));
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testParse_WithMissingRequiredOption_ThrowsMissingOptionException() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null; // Mock implementation
            }
        };
        
        Options options = new Options();
        options.addOption(Option.builder("f")
                .longOpt("file")
                .desc("Specifies file location.")
                .required()
                .build());
        
        try {
            String[] arguments = {};
            parser.parse(options, arguments);
            fail("MissingOptionException not thrown.");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: f", e.getMessage());
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testParse_WithInvalidOption_ThrowsUnrecognizedOptionException() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null; // Mock implementation
            }
        };
        
        Options options = new Options();
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Prints help message.")
                .build());
        
        try {
            String[] arguments = {"-v"};
            parser.parse(options, arguments);
            fail("UnrecognizedOptionException not thrown.");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -v", e.getMessage());
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testProcessOption_WithOptionGroupAndRequiredOption_CorrectlySetSelectedOption() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null; // Mock implementation
            }
        };
        
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.addOption(Option.builder("a")
                .longOpt("alpha")
                .desc("Option alpha.")
                .required()
                .build());
        group.addOption(Option.builder("b")
                .longOpt("beta")
                .desc("Option beta.")
                .required()
                .build());
        options.addOptionGroup(group);
        
        try {
            String[] arguments = {"-b"};
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue(cmd.hasOption("b"));
            assertFalse(cmd.hasOption("a"));
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testProcessArgs_WithOptionAndValue_AddsValueToOption() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null; // Mock implementation
            }
        };
        
        Options options = new Options();
        options.addOption(Option.builder("f")
                .longOpt("file")
                .desc("Specifies file location.")
                .hasArg()
                .build());
        
        try {
            String[] arguments = {"-f", "path/to/file.txt"};
            Option opt = options.getOption("f");
            ListIterator iter = Arrays.asList(arguments).listIterator();
            
            parser.processArgs(opt, iter);
            assertEquals("path/to/file.txt", opt.getValue());
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testProcessArgs_WithOptionAndMissingValue_ThrowsMissingArgumentException() {
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null; // Mock implementation
            }
        };
        
        Options options = new Options();
        options.addOption(Option.builder("f")
                .longOpt("file")
                .desc("Specifies file location.")
                .hasArg()
                .build());
        
        try {
            String[] arguments = {"-f"};
            Option opt = options.getOption("f");
            ListIterator iter = Arrays.asList(arguments).listIterator();
            
            parser.processArgs(opt, iter);
            fail("MissingArgumentException not thrown.");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:f", e.getMessage());
        } catch (ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}