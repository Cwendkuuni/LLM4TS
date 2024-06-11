import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "arg2"};

        String[] flattenedArguments = posixParser.flatten(options, arguments, true);

        Assert.assertEquals(4, flattenedArguments.length);
        Assert.assertEquals("-a", flattenedArguments[0]);
        Assert.assertEquals("arg1", flattenedArguments[1]);
        Assert.assertEquals("arg2", flattenedArguments[2]);
        Assert.assertEquals("--", flattenedArguments[3]);
    }

    @Test
    public void testFlattenWithoutStopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-a", "arg1", "arg2"};

        String[] flattenedArguments = posixParser.flatten(options, arguments, false);

        Assert.assertEquals(3, flattenedArguments.length);
        Assert.assertEquals("-a", flattenedArguments[0]);
        Assert.assertEquals("arg1", flattenedArguments[1]);
        Assert.assertEquals("arg2", flattenedArguments[2]);
    }

    @Test
    public void testFlattenWithOptionArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "arg1"};

        String[] flattenedArguments = posixParser.flatten(options, arguments, false);

        Assert.assertEquals(2, flattenedArguments.length);
        Assert.assertEquals("-a", flattenedArguments[0]);
        Assert.assertEquals("arg1", flattenedArguments[1]);
    }

    @Test
    public void testFlattenWithOptionArgAndMultipleArgs() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = {"-a", "arg1", "arg2"};

        String[] flattenedArguments = posixParser.flatten(options, arguments, false);

        Assert.assertEquals(3, flattenedArguments.length);
        Assert.assertEquals("-a", flattenedArguments[0]);
        Assert.assertEquals("arg1", flattenedArguments[1]);
        Assert.assertEquals("arg2", flattenedArguments[2]);
    }

    @Test
    public void testFlattenWithOptionArgs() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a", "arg1"};

        String[] flattenedArguments = posixParser.flatten(options, arguments, false);

        Assert.assertEquals(2, flattenedArguments.length);
        Assert.assertEquals("-a", flattenedArguments[0]);
        Assert.assertEquals("arg1", flattenedArguments[1]);
    }

    @Test
    public void testFlattenWithBurstToken() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] arguments = {"-abarg"};

        String[] flattenedArguments = posixParser.flatten(options, arguments, false);

        Assert.assertEquals(3, flattenedArguments.length);
        Assert.assertEquals("-a", flattenedArguments[0]);
        Assert.assertEquals("-b", flattenedArguments[1]);
        Assert.assertEquals("arg", flattenedArguments[2]);
    }

    @Test
    public void testFlattenWithNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-a", "arg1", "non-option"};

        String[] flattenedArguments = posixParser.flatten(options, arguments, false);

        Assert.assertEquals(4, flattenedArguments.length);
        Assert.assertEquals("-a", flattenedArguments[0]);
        Assert.assertEquals("arg1", flattenedArguments[1]);
        Assert.assertEquals("--", flattenedArguments[2]);
        Assert.assertEquals("non-option", flattenedArguments[3]);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");

        String[] arguments = {"-b"};

        String[] flattenedArguments = posixParser.flatten(options, arguments, false);

        Assert.assertEquals(1, flattenedArguments.length);
        Assert.assertEquals("-b", flattenedArguments[0]);
    }
}