import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class PosixParserTest {

    private static Options options;
    private static PosixParser parser;
    
    @BeforeClass
    public static void setUp() {
        options = new Options();
        parser = new PosixParser();
        
        Option help = new Option("h", "help", false, "display help message");
        Option version = new Option("v", "version", false, "display version");
        Option output = Option.builder("o")
                .longOpt("output")
                .argName("file")
                .hasArg()
                .desc("output file path")
                .build();
                
        options.addOption(help);
        options.addOption(version);
        options.addOption(output);
    }
    
    @Test
    public void testFlatten_ValidOptions_Success() {
        String[] args = {"-h", "-v", "-o", "output.txt"};
        String[] tokens = parser.flatten(options, args, true);
        
        assertEquals(4, tokens.length);
        assertEquals("-h", tokens[0]);
        assertEquals("-v", tokens[1]);
        assertEquals("-o", tokens[2]);
        assertEquals("output.txt", tokens[3]);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_Success() {
        String[] args = {"-h", "-v", "nonOption", "-o", "output.txt"};
        String[] tokens = parser.flatten(options, args, true);
        
        assertEquals(3, tokens.length);
        assertEquals("-h", tokens[0]);
        assertEquals("-v", tokens[1]);
        assertEquals("nonOption", tokens[2]);
    }
    
    @Test
    public void testFlatten_noArgs_Success() {
        String[] args = {"-h", "-v"};
        String[] tokens = parser.flatten(options, args, true);
        
        assertEquals(2, tokens.length);
        assertEquals("-h", tokens[0]);
        assertEquals("-v", tokens[1]);
    }
    
    @Test
    public void testFlatten_InvalidOption_Success() {
        String[] args = {"-h", "-v", "-x"};
        String[] tokens = parser.flatten(options, args, true);
        
        assertEquals(3, tokens.length);
        assertEquals("-h", tokens[0]);
        assertEquals("-v", tokens[1]);
        assertEquals("-x", tokens[2]);
    }
    
    @Test
    public void testFlatten_NoArguments_Success() {
        String[] args = {};
        String[] tokens = parser.flatten(options, args, true);
        
        assertEquals(0, tokens.length);
    }
    
    @Test
    public void testFlatten_SpecialToken_Success() {
        String[] args = {"--help", "--output=output.txt"};
        String[] tokens = parser.flatten(options, args, true);
        
        assertEquals(2, tokens.length);
        assertEquals("--help", tokens[0]);
        assertEquals("--output=output.txt", tokens[1]);
    }
    
    @Test
    public void testFlatten_SingleHyphen_Success() {
        String[] args = {"-"};
        String[] tokens = parser.flatten(options, args, true);
        
        assertEquals(1, tokens.length);
        assertEquals("-", tokens[0]);
    }
}