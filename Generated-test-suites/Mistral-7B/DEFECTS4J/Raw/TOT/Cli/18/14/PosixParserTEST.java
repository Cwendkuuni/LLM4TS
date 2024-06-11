import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testSimpleOption() throws ParseException {
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a"}, parsedArgs);
        Assert.assertTrue(options.isSet("a"));
    }

    @Test
    public void testOptionWithArg() throws ParseException {
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "an option with argument");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "value"}, parsedArgs);
        Assert.assertEquals("value", options.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        String[] args = {"-x"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "an option with argument");

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
        Assert.fail();
    }

    @Test
    public void testMultipleOptions() throws ParseException {
        String[] args = {"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "an option");
        options.addOption("b", true, "another option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b"}, parsedArgs);
        Assert.assertTrue(options.isSet("a"));
        Assert.assertTrue(options.isSet("b"));
    }

    @Test
    public void testLongOption() throws ParseException {
        String[] args = {"--longOption", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "a long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"--longOption"}, parsedArgs);
        Assert.assertEquals("value", options.getOptionValue("longOption"));
    }

    @Test
    public void testDoubleDashOption() throws ParseException {
        String[] args = {"--", "value"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"--"}, parsedArgs);
        Assert.assertNull(options.getOptionValue("value"));
    }

    @Test
    public void testBurstyOption() throws ParseException {
        String[] args = {"-abc", "123"};
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "-c"}, parsedArgs);
        Assert.assertTrue(options.isSet("a"));
        Assert.assertTrue(options.isSet("b"));
        Assert.assertTrue(options.isSet("c"));
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        String[] args = {"-a", "-b", "noOption"};
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        Assert.assertArrayEquals(new String[]{"-a", "-b", "noOption"}, parsedArgs);
        Assert.assertEquals(3, parsedArgs.length);
    }
}