import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        
        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", true, "option b");
        Option option3 = new Option("c", false, "option c");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-a", "-b value", "-c"};
        String[] expected = {"-a", "-b", "value", "-c"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGobble() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-a", "-b value", "-c"};
        String[] expected = {"-a", "-b", "value", "-c"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessNonOptionToken() {
        Options options = new Options();
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-a", "-b value", "-c", "value", "--", "-d"};
        String[] expected = {"-a", "-b", "value", "-c", "--", "-d"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        
        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", true, "option b");
        
        options.addOption(option1);
        options.addOption(option2);
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-a", "-b value", "-c"};
        String[] expected = {"-a", "-b", "value", "-c"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        
        Option option1 = new Option("a", false, "option a");
        Option option2 = new Option("b", true, "option b");
        
        options.addOption(option1);
        options.addOption(option2);
        
        PosixParser parser = new PosixParser();
        
        String[] arguments = {"-abvalue", "-c"};
        String[] expected = {"-a", "-b", "value", "-c"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        Assert.assertArrayEquals(expected, result);
    }

}