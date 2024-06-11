import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Create options
        Options options = new Options();
        options.addOption(Option.builder("a").build());
        options.addOption(Option.builder("b").argName("arg").build());

        // Create arguments
        String[] arguments = {"-a", "-b", "value"};

        // Create PosixParser object
        PosixParser parser = new PosixParser();

        // Call flatten method and get result
        String[] result = parser.flatten(options, arguments, false);

        // Assert the result
        assert Arrays.equals(result, new String[]{"-a", "-b", "value"});
    }
}