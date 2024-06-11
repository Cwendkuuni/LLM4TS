import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class ParserTest {

    @Test
    public void testParserParseWithOptionsAndArguments() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Prints help information");
        options.addOption("v", "version", false, "Prints version information");
        String[] args = {"-h", "arg1", "arg2"};
        
        Parser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("h"));
        assertEquals(2, cmd.getArgs().length);
    }
    
    @Test
    public void testParserParseWithProperties() throws Exception {
        Options options = new Options();
        options.addOption("f", true, "File path");
        Properties properties = new Properties();
        properties.setProperty("f", "test.txt");
        String[] args = {};
        
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);
        
        assertTrue(cmd.hasOption("f"));
        assertEquals("test.txt", cmd.getOptionValue("f"));
    }
    
    @Test
    public void testParserProcessArgs() throws Exception {
        Options options = new Options();
        Option option = Option.builder("o")
                              .hasArgs()
                              .build();
        options.addOption(option);
        String[] args = {"-o", "arg1", "arg2"};
        
        Parser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("o"));
        assertEquals(2, cmd.getOptionValues("o").length);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testParserProcessArgsMissing() throws Exception {
        Options options = new Options();
        Option option = Option.builder("o")
                              .hasArgs()
                              .build();
        options.addOption(option);
        String[] args = {"-o"};
        
        Parser parser = new PosixParser();
        parser.parse(options, args);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParserUnrecognizedOption() throws Exception {
        Options options = new Options();
        String[] args = {"-x"};
        
        Parser parser = new GnuParser();
        parser.parse(options, args);
    }
    
}