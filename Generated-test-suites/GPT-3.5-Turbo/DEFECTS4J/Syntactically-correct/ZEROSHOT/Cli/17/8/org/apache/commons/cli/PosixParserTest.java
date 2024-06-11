import org.junit.*;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b with argument");
        options.addOption("c", true, "option c");
        options.addOption("d", false, "option d");
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "arg1", "-b", "arg2", "non-option"};
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"-a", "arg1", "-b", "arg2"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_DontStopAtNonOption() {
        String[] args = {"-a", "arg1", "-b", "arg2", "non-option"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-a", "arg1", "-b", "arg2", "non-option"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NoArguments() {
        String[] args = {};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] args = {"-"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_DoubleHyphen() {
        String[] args = {"--"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_ShortOptionWithoutArgument() {
        String[] args = {"-a"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-a"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_ShortOptionWithArgument() {
        String[] args = {"-b", "arg1"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-b", "arg1"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_ShortOptionWithMultipleArguments() {
        String[] args = {"-c", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-c", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_ShortOptionNotRecognized_StopAtNonOption() {
        String[] args = {"-x", "arg1"};
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"-x", "arg1"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_ShortOptionNotRecognized_DontStopAtNonOption() {
        String[] args = {"-x", "arg1"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-x", "arg1"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_LongOptionWithArgument() {
        String[] args = {"--b", "arg1"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--b", "arg1"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_LongOptionWithMultipleArguments() {
        String[] args = {"--c", "arg1", "arg2"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--c", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_LongOptionNotRecognized_StopAtNonOption() {
        String[] args = {"--x", "arg1"};
        String[] result = parser.flatten(options, args, true);
        String[] expected = {"--x", "arg1"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_LongOptionNotRecognized_DontStopAtNonOption() {
        String[] args = {"--x", "arg1"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"--x", "arg1"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = {"-a", "-b", "arg1"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-a", "-b", "arg1"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_MultipleShortOptionsGrouped() {
        String[] args = {"-ab", "arg1"};
        String[] result = parser.flatten(options, args, false);
        String[] expected = {"-a", "-b", "arg1"};
        assertArrayEquals(expected, result);
    }

}