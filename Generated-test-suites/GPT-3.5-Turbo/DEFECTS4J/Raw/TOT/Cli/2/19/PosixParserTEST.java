import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlattenSingleDash() {
        Options options = new Options();
        String[] arguments = { "-" };
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] { "-" }, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        Options options = new Options();
        String[] arguments = { "--" };
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] { "--" }, result);
    }

    @Test
    public void testFlattenOptionWithNoArg() {
        Options options = new Options();
        Option option = new Option("a", false, "Option with no argument");
        options.addOption(option);
        String[] arguments = { "-a" };
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] { "-a" }, result);
    }

    @Test
    public void testFlattenOptionWithArg() {
        Options options = new Options();
        Option option = new Option("a", true, "Option with argument");
        options.addOption(option);
        String[] arguments = { "-a", "argument" };
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] { "-a", "argument" }, result);
    }

    @Test
    public void testFlattenOptionWithMultipleArgs() {
        Options options = new Options();
        Option option = new Option("a", true, "Option with multiple arguments");
        options.addOption(option);
        String[] arguments = { "-a", "argument1", "argument2" };
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] { "-a", "argument1", "argument2" }, result);
    }

    @Test
    public void testFlattenOptionWithInvalidId() {
        Options options = new Options();
        String[] arguments = { "-x" };
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] { "-x" }, result);
    }

    @Test
    public void testFlattenBurstToken() {
        Options options = new Options();
        Option option = new Option("a", true, "Option with argument");
        options.addOption(option);
        String[] arguments = { "-abc" };
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] { "-a", "-b", "-c" }, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", false, "Option with no argument");
        options.addOption(option);
        String[] arguments = { "-a", "nonOption" };
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(new String[] { "-a", "--", "nonOption" }, result);
    }

    @Test
    public void testFlattenDoNotStopAtNonOption() {
        Options options = new Options();
        Option option = new Option("a", false, "Option with no argument");
        options.addOption(option);
        String[] arguments = { "-a", "nonOption" };
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(new String[] { "-a", "nonOption" }, result);
    }

}