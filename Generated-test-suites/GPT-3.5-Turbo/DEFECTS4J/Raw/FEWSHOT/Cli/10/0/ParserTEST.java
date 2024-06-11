import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", true, "Directory option");
        
        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        
        String[] arguments = {"-f", "--directory", "dir"};
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine cmd = parser.parse(options, arguments);
        
        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption("directory"));
        assertEquals("dir", cmd.getOptionValue("directory"));
    }


}