import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        parser = new DefaultParser();
        parser.setOptions(options);
    }

    @Test
    public void testParse() throws Exception
    {
        String[] args = {"--input", "test.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args, null, false);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("test.txt", cmd.getArgs().get(0));
        Assert.assertEquals("output.txt", cmd.getArgs().get(1));
        Assert.assertEquals(Arrays.asList("--input", new Option("i", "input")), cmd.getOptions());
        Assert.assertEquals(Arrays.asList("--output", new Option("o", "output")), cmd.getAlternativeOptions());
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception
    {
        String[] args = {"--input", "--output"};
        parser.parse(options, args, null, false);
        Assert.fail();
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws Exception
    {
        String[] args = {"--test", "--input", "test.txt"};
        parser.parse(options, args, null, false);
        Assert.fail();
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception
    {
        String[] args = {"--unknown"};
        parser.parse(options, args, null, false);
        Assert.fail();
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception
    {
        String[] args = {"--input", "test.txt"};
        options.addOption(new Option("o", "output").required(true));
        parser.parse(options, args, null, false);
        Assert.fail();
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredGroupOption() throws Exception
    {
        String[] args = {"--test"};
        options.addOptionGroup(new OptionGroup("test", "Test Group").required(true));
        parser.parse(options, args, null, false);
        Assert.fail();
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        String[] args = {"--input", "test.txt", "--output", "output.txt", "--property1=value1", "--property2=value2"};
        CommandLine cmd = parser.parse(options, args, new Properties(), false);

        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("test.txt", cmd.getArgs().get(0));
        Assert.assertEquals("output.txt", cmd.getArgs().get(1));

        Assert.assertEquals(2, cmd.getOptions().size());
        Option option1 = cmd.getOptionByName("input");
        Assert.assertEquals("--input", option1.getOpt());
        Assert.assertFalse(option1.hasValues());

        Option option2 = cmd.getOptionByName("output");
        Assert.assertEquals("--output", option2.getOpt());
        Assert.assertEquals("output.txt", option2.getValue());

        Option propertyOption1 = cmd.getOptionByName("property1");
        Assert.assertEquals("--property1", propertyOption1.getOpt());
        Assert.assertEquals("value1", propertyOption1.getValue());

        Option propertyOption2 = cmd.getOptionByName("property2");
        Assert.assertEquals("--property2", propertyOption2.getOpt());
        Assert.assertEquals("value2", propertyOption2.getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception
    {
        String[] args = {"--input", "test.txt", "--", "output.txt"};
        CommandLine cmd = parser.parse(options, args, null, true);

        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("test.txt", cmd.getArgs().get(0));
        Assert.assertEquals("output.txt", cmd.getArgs().get(1));
        Assert.assertEquals("--", cmd.getArgs().get(2));
    }
}