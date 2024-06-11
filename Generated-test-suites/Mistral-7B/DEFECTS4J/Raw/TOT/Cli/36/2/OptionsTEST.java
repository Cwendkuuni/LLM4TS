import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

    @Test
    public void testShortOption() {
        Options options = new Options();
        options.addOption("o", "output", true, "Output directory");

        String[] args = {"-o", "outputDirectory"};
        try {
            options.parse(args);
            Assert.assertEquals("outputDirectory", options.getOptionValue("o"));
        } catch (ParseException e) {
            // This should not be called
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    @Test
    public void testShortOptionWithArgument() {
        Options options = new Options();
        options.addOption("o", "output", true, "Output directory");

        String[] args = {"-o", "outputDirectory", "testOutput"};
        try {
            options.parse(args);
            Assert.assertEquals("testOutput", options.getOptionValue("o"));
        } catch (ParseException e) {
            // This should not be called
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    @Test
    public void testShortOptionMissing() {
        Options options = new Options();
        options.addOption("o", "output", true, "Output directory");

        String[] args = {"--inputFile", "inputFile.txt"};

        try {
            options.parse(args);
            Assert.assertNull(options.getOptionValue("o"));
        } catch (ParseException e) {
            // This is the expected outcome
        }
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("outputDir", true, "Output directory");

        String[] args = {"--outputDir", "outputDirectory"};
        try {
            options.parse(args);
            Assert.assertEquals("outputDirectory", options.getOptionValue("outputDir"));
        } catch (ParseException e) {
            // This should not be called
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    @Test
    public void testLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("outputDir", true, "Output directory");

        String[] args = {"--outputDir", "outputDirectory", "testOutput"};
        try {
            options.parse(args);
            Assert.assertEquals("testOutput", options.getOptionValue("outputDir"));
        } catch (ParseException e) {
            // This should not be called
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    @Test
    public void testLongOptionMissing() {
        Options options = new Options();
        options.addOption("outputDir", true, "Output directory");

        String[] args = {"--inputFile", "inputFile.txt"};

        try {
            options.parse(args);
            Assert.assertNull(options.getOptionValue("outputDir"));
        } catch (ParseException e) {
            // This is the expected outcome
        }
    }

    @Test
    public void testRequiredOption() {
        Options options = new Options();
        options.addOptionGroup(new OptionGroup("group1", "Options", true,
                "Options for command line tool"));
        options.addOption("inputFile", true, "Input file");

        String[] args = {};
        try {
            options.parse(args);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("No input file specified", e.getMessage());
        }
    }

    @Test
    public void testRequiredOptionWithValue() {
        Options options = new Options();
        options.addOptionGroup(new OptionGroup("group1", "Options", true,
                "Options for command line tool"));
        options.addOption("inputFile", true, "Input file");

        String[] args = {"--inputFile", "inputFile.txt"};
        try {
            options.parse(args);
            Assert.assertEquals("inputFile.txt", options.getOptionValue("inputFile"));
        } catch (ParseException e) {
            // This should not be called
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testIllegalOption() {
        Options options = new Options();
        options.addOption("o", "output", true, "Output directory");

        String[] args = {"--invalidOption"};
        options.parse(args);
    }

    @Test(expected = ParseException.class)
    public void testIllegalOptionWithArgument() {
        Options options = new Options();
        options.addOption("o", "output", true, "Output directory");

        String[] args = {"--invalidOption", "argument"};
        options.parse(args);
    }
}