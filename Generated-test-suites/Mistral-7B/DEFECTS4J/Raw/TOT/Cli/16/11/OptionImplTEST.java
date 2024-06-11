import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.util.HelpFormatterHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OptionImplTest {

    @Test
    public void testOptionImpl() {
        final Options options = new Options();
        final Option option = new Option("o", "output", true, "output file path")
                .required(false)
                .desc("specify output file path");
        options.addOption(option);

        final CommandLineParser parser = new DefaultParser();

        try {
            final String[] args = {"-o", "test.txt"};
            final WriteableCommandLine commandLine = parser.parse(options, args);
            Assert.assertEquals("test.txt", commandLine.getValueForOption("output"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Parsing failed: " + e.getMessage());
        }

        // Test required option
        try {
            final String[] args2 = {};
            final WriteableCommandLine commandLine2 = parser.parse(options, args2);
            Assert.fail("Parsing should have failed for required option");
        } catch (Exception e) {
            Assert.assertEquals("Option 'output' is required", e.getMessage());
        }

        // Test incorrect trigger
        final Option incorrectOption = new Option("x", "output", true, "output file path")
                .required(false)
                .desc("specify output file path");
        options.addOption(incorrectOption);

        try {
            final String[] args3 = {"-x", "test.txt"};
            parser.parse(options, args3);
            Assert.fail("Parsing should have failed for incorrect trigger");
        } catch (Exception e) {
            Assert.assertEquals("No handler for option: -x", e.getMessage());
        }

        // Test help
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }
}