import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ParserTest {
    
    @Test
    public void testParse() {
        Options options = new Options();
        options.addOption(Option.builder("h").longOpt("help").desc("Print help message").build());
        
        String[] arguments = new String[] {"-h"};
        
        Parser parser = new Parser() {
            
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return arguments;
            }
        };
        
        try {
            CommandLine cmd = parser.parse(options, arguments);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}