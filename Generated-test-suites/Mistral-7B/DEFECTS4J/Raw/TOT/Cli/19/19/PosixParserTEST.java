import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link PosixParser} class.
 */
public class PosixParserTest {

    /**
     * Test flattening of a simple command line with no special options.
     */
    @Test
    public void testSimpleCommandLine() {
        String[] args = {"java", "MyApp"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "MyApp"}, flattenedArgs);
    }

    /**
     * Test flattening of a command line with single option.
     */
    @Test
    public void testSingleOption() {
        String[] args = {"java", "--option"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--option"}, flattenedArgs);
    }

    /**
     * Test flattening of a command line with single argument option.
     */
    @Test
    public void testSingleArgumentOption() {
        String[] args = {"java", "--option=value"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--option=value"}, flattenedArgs);
    }

    /**
     * Test flattening of a command line with multiple options.
     */
    @Test
    public void testMultipleOptions() {
        String[] args = {"java", "--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option1 description");
        options.addOption("option2", true, "Option2 description");
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--option1", "--option2"}, flattenedArgs);
    }

    /**
     * Test flattening of a command line with multiple arguments for an option.
     */
    @Test
    public void testMultipleArgumentsForOption() {
        String[] args = {"java", "--option=value1", "--option=value2"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--option=value1", "--option=value2"}, flattenedArgs);
    }

    /**
     * Test flattening of a command line with long option.
     */
    @Test
    public void testLongOption() {
        String[] args = {"java", "--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--long-option"}, flattenedArgs);
    }

    /**
     * Test flattening of a command line with long option and argument.
     */
    @Test
    public void testLongOptionWithArgument() {
        String[] args = {"java", "--long-option=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option description");
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--long-option=value"}, flattenedArgs);
    }

    /**
     * Test flattening of a command line with multiple long options.
     */
    @Test
    public void testMultipleLongOptions() {
        String[] args = {"java", "--long-option1", "--long-option2"};
        Options options = new Options();
        options.addOption("longOption1", true, "Long option1 description");
        options.addOption("longOption2", true, "Long option2 description");
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--long-option1", "--long-option2"}, flattenedArgs);
    }

    /**
     * Test flattening of a command line with multiple long options and arguments.
     */
    @Test
    public void testMultipleLongOptionsWithArguments() {
        String[] args = {"java", "--long-option1=value1", "--long-option2=value2"};
        Options options = new Options();
        options.addOption("longOption1", true, "Long option1 description");
        options.addOption("longOption2", true, "Long option2 description");
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--long-option1=value1", "--long-option2=value2"}, flattenedArgs);
    }

    /**
     * Test flattening of a command line with a long option that conflicts with a short option.
     */
    @Test(expected = ParseException.class)
    public void testLongOptionConflictWithShortOption() {
        String[] args = {"java", "--option", "value", "--long-option"};
        Options options = new Options();
        options.addOption("option", true, "Option description");
        options.addOption("longOption", true, "Long option description");
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test flattening of a command line with a non-option argument.
     */
    @Test
    public void testNonOptionArgument() {
        String[] args = {"java", "arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "arg1", "arg2"}, flattenedArgs);
    }
}