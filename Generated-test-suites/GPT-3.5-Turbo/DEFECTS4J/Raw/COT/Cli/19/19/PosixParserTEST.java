
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlattenWithNoOptions() {
        Options options = new Options();
        String[] arguments = new String[]{"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlattenWithSingleOption() {
        Options options = new Options();
        options.addOption(new Option("a"));
        options.addOption(new Option("b"));
        
        String[] arguments = new String[]{"-a", "arg1", "-b", "arg2"};
        String[] expected = new String[]{"-a", "arg1", "-b", "arg2"};
        
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption(new Option("a"));
        options.addOption(new Option("b"));
        
        String[] arguments = new String[]{"-a", "arg1", "-b", "arg2", "nonOption"};
        String[] expected = new String[]{"-a", "arg1", "-b", "arg2", "nonOption"};
        
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithBursting() {
        Options options = new Options();
        options.addOption(Option.builder("o").hasArg().build());
        
        String[] arguments = new String[]{"-oarg1arg2"};
        String[] expected = new String[]{"-o", "arg1arg2"};
        
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

}
