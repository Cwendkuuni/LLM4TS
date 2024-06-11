import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PosixParserTest {
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten_StopAtNonOption_FlagSet() {
        String[] arguments = {"-a", "arg1", "--", "-b", "arg2"};
        String[] expected = {"-a", "arg1", "--", "-b", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_StopAtNonOption_FlagNotSet() {
        String[] arguments = {"-a", "-b", "arg1"};
        String[] expected = {"-a", "-b", "arg1"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_HyphenToken() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_ValidOption() {
        options.addOption(new Option("a", false, "Option A"));
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_FlagSet() {
        String[] arguments = {"-s", "arg1"};
        String[] expected = {"-s", "arg1"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_InvalidOption_StopAtNonOption_FlagNotSet() {
        String[] arguments = {"-s", "arg1"};
        String[] expected = {"-s"};
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_RequiresBursting() {
        options.addOption(new Option("a", false, "Option A"));
        options.addOption(new Option("b", true, "Option B"));
        String[] arguments = {"-abvalue"};
        String[] expected = {"-a", "-bvalue"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NotHandledByPreviousRules() {
        String[] arguments = {"arg1", "-a"};
        String[] expected = {"arg1", "-a"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(expected, result);
    }
}