import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import static org.junit.Assert.*;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
        options.addOption(Option.builder("f").desc("File option").build());
        options.addOption(Option.builder("d").desc("Directory option").build());
        options.addOption(Option.builder("v").desc("Verbose option").build());
    }
    
    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_OnlyOptions() {
        String[] arguments = {"-f", "--directory", "-v"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("--directory", result[1]);
        assertEquals("-v", result[2]);
    }
    
    @Test
    public void testFlatten_OptionsAndNonOptions_StopAtNonOption() {
        String[] arguments = {"-f", "file.txt", "nonOption"};
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
        assertEquals("nonOption", result[2]);
    }
    
    @Test
    public void testFlatten_OptionsAndNonOptions_NotStoppingAtNonOption() {
        String[] arguments = {"-f", "file.txt", "nonOption"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(4, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
        assertEquals("--", result[2]);
        assertEquals("nonOption", result[3]);
    }
    
    @Test
    public void testFlatten_OptionsWithValues() {
        String[] arguments = {"-f", "file.txt", "-d", "dir", "-v"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
        assertEquals("-d", result[2]);
        assertEquals("dir", result[3]);
        assertEquals("-v", result[4]);
    }
    
    @Test
    public void testFlatten_LongOptions() {
        String[] arguments = {"--file", "file.txt", "--directory=dir", "--verbose"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(6, result.length);
        assertEquals("--file", result[0]);
        assertEquals("file.txt", result[1]);
        assertEquals("--directory", result[2]);
        assertEquals("dir", result[3]);
        assertEquals("--verbose", result[4]);
    }
    
    @Test
    public void testFlatten_LongOptions_NotExistingOption() {
        String[] arguments = {"--name", "John"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("--name", result[0]);
        assertEquals("John", result[1]);
    }
    
    @Test
    public void testFlatten_BurstingOption() {
        String[] arguments = {"-fdv"};
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("-d", result[1]);
        assertEquals("-v", result[2]);
    }
}