import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

public class PosixParserTest {

    @Test
    public void testFlatten_noArguments() {
        Options options = new Options();
        String[] arguments = new String[0];
        boolean stopAtNonOption = false;
        
        String[] result = new PosixParser().flatten(options, arguments, stopAtNonOption);
        
        Assert.assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_singleOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        
        String[] arguments = { "-a" };
        boolean stopAtNonOption = false;
        
        String[] result = new PosixParser().flatten(options, arguments, stopAtNonOption);
        
        Assert.assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_longOptionWithValue() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").longOpt("foo").hasArg().build());
        
        String[] arguments = { "--foo=value" };
        boolean stopAtNonOption = false;
        
        String[] result = new PosixParser().flatten(options, arguments, stopAtNonOption);
        
        String[] expected = { "--foo", "value" };
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_nonOptionStopEnabled() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        
        String[] arguments = { "-a", "arg1", "arg2", "-b" };
        boolean stopAtNonOption = true;
        
        String[] result = new PosixParser().flatten(options, arguments, stopAtNonOption);
        
        String[] expected = { "-a", "arg1", "arg2", "-b" };
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_nonOptionStopDisabled() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        
        String[] arguments = { "-a", "arg1", "arg2", "-b" };
        boolean stopAtNonOption = false;
        
        String[] result = new PosixParser().flatten(options, arguments, stopAtNonOption);
        
        String[] expected = { "-a", "arg1", "arg2", "-b" };
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_singleHyphen() {
        Options options = new Options();
        
        String[] arguments = { "-" };
        boolean stopAtNonOption = false;
        
        String[] result = new PosixParser().flatten(options, arguments, stopAtNonOption);
        
        Assert.assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_optionWithMultipleArguments() {
        Options options = new Options();
        options.addOption(Option.builder("a").hasArgs().build());
        
        String[] arguments = { "-a", "arg1", "arg2" };
        boolean stopAtNonOption = false;
        
        String[] result = new PosixParser().flatten(options, arguments, stopAtNonOption);
        
        Assert.assertArrayEquals(arguments, result);
    }
    
    @Test
    public void testFlatten_unknownOption() {
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        
        String[] arguments = { "-b" };
        boolean stopAtNonOption = false;
        
        String[] result = new PosixParser().flatten(options, arguments, stopAtNonOption);
        
        Assert.assertArrayEquals(arguments, result);
    }
}