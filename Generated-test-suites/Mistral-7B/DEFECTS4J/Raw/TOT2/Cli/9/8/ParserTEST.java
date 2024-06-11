import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseBasicUsage() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        Parser parser = new DefaultParser();

        String[] arguments = {"-a", "valueA"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "optionO", true, "Option O");
        Parser parser = new DefaultParser();

        String[] arguments = {"--optionO", "valueO"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("valueO", cmd.getOptionValue("o"));
        Assert.assertNull(cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("o", "optionO", true, "Option O");
        Parser parser = new DefaultParser();

        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments, false);

        // this test should throw a ParseException since an option is missing
    }

    @Test
    public void testParseLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("longOptionA", "longOptionA", true, "Long Option A");
        Parser parser = new DefaultParser();

        String[] arguments = {"--longOptionA", "valueA"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("valueA", cmd.getOptionValue("longOptionA"));
    }

    @Test(expected = ParseException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = {"--unrecognizedOption", "value"};
        CommandLine cmd = parser.parse(options, arguments, false);

        // this test should throw a ParseException since the option is unrecognized
    }

    @Test
    public void testParseMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        Parser parser = new DefaultParser();

        String[] arguments = {"-a", "valueA", "-b", "valueB"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseOptionsWithValues() throws ParseException {
        Options options = new Options();
        options.addOption("o", "optionO", true, "Option O");
        options.addOption("v", "optionV", true, "Version option, with a value");

        Parser parser = new DefaultParser();

        String[] arguments = {"--optionO", "optionValueO", "--optionV", "1.2.3"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("optionValueO", cmd.getOptionValue("o"));
        Assert.assertEquals("1.2.3", cmd.getOptionValue("v"));
    }

    @Test
    public void testParseOptionsWithGroups() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group of options");
        group.addOption("g1", "groupOption1");
        group.addOption("g2", "groupOption2");

        options.addOptionGroup(group);
        options.addOption("o", "optionO", true, "Option O");

        Parser parser = new DefaultParser();

        String[] arguments = {"--group", "--groupOption1", "--optionO", "optionValueO"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("optionValueO", cmd.getOptionValue("o"));
        Assert.assertTrue(group.isSelected());
    }

    @Test
    public void testParseOptionsWithMultipleValues() throws ParseException {
        Options options = new Options();
        options.addOption("v", "values", true, "Value option, can have multiple values");

        Parser parser = new DefaultParser();

        String[] arguments = {"--values", "value1", "--values", "value2"};
        CommandLine cmd = parser.parse(options, arguments, false);

        List values = cmd.getValuesForOption("values");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }
}