import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {
    @Test
    public void testFlatten() {
        String[] arguments = {"-f", "-abc", "--option1=value1", "--option2", "arg1", "arg2"};
        Options options = new Options();
        Option option1 = Option.builder().longOpt("option1").hasArg().build();
        Option option2 = Option.builder().longOpt("option2").build();
        options.addOption(option1);
        options.addOption(option2);
        
        PosixParser parser = new PosixParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);
        
        assertEquals(6, flattenedArguments.length);
        assertEquals("-f", flattenedArguments[0]);
        assertEquals("-a", flattenedArguments[1]);
        assertEquals("-b", flattenedArguments[2]);
        assertEquals("-c", flattenedArguments[3]);
        assertEquals("--option1", flattenedArguments[4]);
        assertEquals("value1", flattenedArguments[5]);
    }
}