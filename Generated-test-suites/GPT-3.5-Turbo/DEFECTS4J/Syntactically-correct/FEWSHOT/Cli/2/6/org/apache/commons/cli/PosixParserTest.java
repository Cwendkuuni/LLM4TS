import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("f", false, "Test option");
        options.addOption("o", true, "Test option with argument");
        options.addOption("h", false, "Help option");
        
        String[] args1 = {"-f"};
        String[] args2 = {"-o", "arg"};
        String[] args3 = {"--help"};
        String[] args4 = {"-foo"};
        String[] args5 = {"--option", "arg"};
        
        PosixParser parser = new PosixParser();
        
        String[] result1 = parser.flatten(options, args1, false);
        Assert.assertArrayEquals(new String[]{"-f"}, result1);
        
        String[] result2 = parser.flatten(options, args2, false);
        Assert.assertArrayEquals(new String[]{"-o", "arg"}, result2);
        
        String[] result3 = parser.flatten(options, args3, false);
        Assert.assertArrayEquals(new String[]{"--help"}, result3);
        
        String[] result4 = parser.flatten(options, args4, false);
        Assert.assertArrayEquals(new String[]{"-foo"}, result4);
        
        String[] result5 = parser.flatten(options, args5, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg"}, result5);
    }
  
}