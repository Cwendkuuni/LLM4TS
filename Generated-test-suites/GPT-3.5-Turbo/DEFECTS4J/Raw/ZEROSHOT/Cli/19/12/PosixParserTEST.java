import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        
        Option option1 = new Option("a", "option 1");
        Option option2 = new Option("b", "option 2");
        Option option3 = new Option("c", "option 3");
        
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        
        PosixParser parser = new PosixParser();
        
        String[] args1 = new String[]{"-a", "value1", "-b", "value2", "-c"};
        String[] result1 = parser.flatten(options, args1, true);
        String[] expected1 = new String[]{"-a", "value1", "-b", "value2", "-c"};
        assertArrayEquals(expected1, result1);
        
        String[] args2 = new String[]{"-a", "value1", "-b", "value2", "value3"};
        String[] result2 = parser.flatten(options, args2, false);
        String[] expected2 = new String[]{"-a", "value1", "-b", "value2", "value3"};
        assertArrayEquals(expected2, result2);
        
        String[] args3 = new String[]{"--option1=value1", "--option2=value2", "-c", "value3"};
        String[] result3 = parser.flatten(options, args3, true);
        String[] expected3 = new String[]{"--option1", "value1", "--option2", "value2", "-c", "value3"};
        assertArrayEquals(expected3, result3);
    }
    
}