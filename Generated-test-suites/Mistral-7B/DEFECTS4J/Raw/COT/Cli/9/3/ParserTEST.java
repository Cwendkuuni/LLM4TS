import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void testSetOptions() throws ParseException {
        Parser parser = new MyParser();
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Parser parser = new MyParser();
        Assert.assertNotNull(parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Parser parser = new MyParser();
        Options options = new Options();
        List<String> requiredOptions = new ArrayList<>();
        options.addOption("a", true, "Description of option a");
        options.addRequiredOption("b", false, "Description of option b");
        parser.setOptions(options);
        List<String> actual = parser.getRequiredOptions();
        List<String> expected = requiredOptions;
        expected.add("b");
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testFlattenWithNullOptions() {
        Parser parser = new MyParser();
        String[] arguments = new String[]{"arg1", "arg2"};
        parser.flatten(null, arguments, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithEmptyArguments() {
        Parser parser = new MyParser();
        Options options = new Options();
        parser.setOptions(options);
        String[] arguments = new String[0];
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        Parser parser = new MyParser();
        Options options = new Options();
        options.addOption("a", true, "Description of option a");
        options.addRequiredOption("b", false, "Description of option b");
        String[] arguments = new String[]{"-a", "--b", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("arg3", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArguments() throws ParseException {
        Parser parser = new MyParser();
        Options options = new Options();
        options.addOption("a", true, "Description of option a");
        options.addRequiredOption("b", false, "Description of option b");
        String[] arguments = new String[]{"arg1", "--c"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new MyParser();
        Options options = new Options();
        options.addOption("a", true, "Description of option a");
        String[] arguments = new String[]{"--x"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new MyParser();
        Options options = new Options();
        options.addOption("a", true, "Description of option a");
        options.addRequiredOption("b", false, "Description of option b");
        String[] arguments = new String[]{"-a"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new MyParser();
        Options options = new Options();
        options.addOption("a", true, "Description of option a");
        options.addOption("b", true, "Description of option b");
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("b", "value2");
        CommandLine cmd = parser.parse(options, null, properties, false);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("value1", cmd.getOptionValue("a"));
        Assert.assertEquals("value2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Parser parser = new MyParser();
        Options options = new Options();
        options.addOption("a", true, "Description of option a");
        options.addRequiredOption("b", false, "Description of option b");
        String[] arguments = new String[]{"--", "-a", "arg3", "arg4"};
        CommandLine cmd = parser.parse(options, arguments, true);
        Assert.assertTrue(cmd.getArgs().length == 2);
        Assert.assertEquals("arg3", cmd.getArgs()[0]);
        Assert.assertEquals("arg4", cmd.getArgs()[1]);
    }

    @Test
    public void testProcessProperties() {
        Parser parser = new MyParser();
        Options options = new Options();
        Properties properties = new Properties();
        properties.put("a", "value1");
        properties.put("b", "value2");
        parser.processProperties(properties);
        Option aOption = options.getOption("a");
        Assert.assertTrue(aOption.hasValues());
        Assert.assertEquals(1, aOption.getValues().length);
        Assert.assertEquals("value1", aOption.getValues()[0]);

        Option bOption = options.getOption("b");
        Assert.assertTrue(bOption.hasValues());
        Assert.assertEquals(1, bOption.getValues().length);
        Assert.assertEquals("value2", bOption.getValues()[0]);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingOptions() throws ParseException {
        Parser parser = new MyParser();
        Options options = new Options();
        options.addOption("a", true, "Description of option a");
        options.addRequiredOption("b", false, "Description of option b");
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }

    private static class MyParser extends Parser {
        // empty implementation
    }

}