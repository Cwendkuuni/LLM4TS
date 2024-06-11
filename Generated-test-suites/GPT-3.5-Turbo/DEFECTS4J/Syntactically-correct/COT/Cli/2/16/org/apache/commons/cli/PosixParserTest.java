import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_basicArguments() {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());
        options.addOption(Option.builder("o").hasArg().build());
        options.addOption(Option.builder("v").build());
        
        String[] arguments = {"-f", "-o", "output.txt", "-v"};
        String[] expected = {"-f", "-o", "output.txt", "-v"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());
        options.addOption(Option.builder("o").hasArg().build());
        options.addOption(Option.builder("v").build());
        
        String[] arguments = {"-f", "-o", "output.txt", "-v", "file1", "file2"};
        String[] expected = {"-f", "-o", "output.txt", "-v"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_longOptionWithValue() {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());
        options.addOption(Option.builder("o").longOpt("output").hasArg().build());
        options.addOption(Option.builder("v").build());
        
        String[] arguments = {"-f", "--output=output.txt", "-v"};
        String[] expected = {"-f", "--output", "output.txt", "-v"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_invalidOption() {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());
        
        String[] arguments = {"-f", "-x"};
        String[] expected = {"-f", "-x"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_burstToken() {
        Options options = new Options();
        options.addOption(Option.builder("f").required().build());
        options.addOption(Option.builder("o").longOpt("output").hasArg().build());
        options.addOption(Option.builder("v").build());
        
        String[] arguments = {"-f", "-ov"};
        String[] expected = {"-f", "-o", "-v"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }

}