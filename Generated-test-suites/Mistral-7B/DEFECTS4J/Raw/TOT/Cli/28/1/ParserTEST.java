import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws Exception
    {
        String[] args = {"-a", "1", "--b=2", "-c", "3"};
        Options options = new Options();
        Option aOption = new Option("a", "a", true, "value for option a");
        Option bOption = new Option("b", "b", true, "value for option b");
        Option cOption = new Option("c", "c", false, "value for option c");
        options.addOption(aOption);
        options.addOption(bOption);
        options.addOption(cOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null, false);

        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(null, cmd.getOptionValue("c"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception
    {
        String[] args = {"-a", "1", "--b=2"};
        Options options = new Options();
        Option aOption = new Option("a", "a", true, "value for option a");
        Option bOption = new Option("b", "b", true, "value for option b");
        Option cOption = new Option("c", "c", true, "value for option c");
        options.addOption(aOption);
        options.addOption(bOption);
        options.addOption(cOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args, null, false);

        fail("MissingRequiredOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingOptionArgument() throws Exception
    {
        String[] args = {"-a", "-"};
        Options options = new Options();
        Option aOption = new Option("a", "a", true, "value for option a");
        options.addOption(aOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args, null, false);

        fail("MissingArgumentException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception
    {
        String[] args = {"--x=5"};
        Options options = new Options();
        Option aOption = new Option("a", "a", true, "value for option a");
        options.addOption(aOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, args, null, false);

        fail("UnrecognizedOptionException expected");
    }

    @Test
    public void testParseWithProperties() throws Exception
    {
        Properties properties = new Properties();
        properties.setProperty("myOption", "value");
        String[] args = {"--myOption"};
        Options options = new Options();
        Option myOption = new Option("m", "myOption", true, "value for myOption");
        options.addOption(myOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals("value", cmd.getOptionValue("myOption"));
    }

    @Test
    public void testParseWithOptionGroups() throws Exception
    {
        String[] args = {"--group1", "--option1", "--group2", "--option2"};
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "Option group 1");
        Option option1 = new Option("o", "option1", true, "value for option1");
        group1.addOption(option1);
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2", "Option group 2");
        Option option2 = new Option("o", "option2", true, "value for option2");
        group2.addOption(option2);
        options.addOptionGroup(group2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null, false);

        assertTrue(group1.isSelected());
        assertTrue(group2.isSelected());
        assertEquals(true, cmd.hasOption("option1"));
        assertEquals(true, cmd.hasOption("option2"));
    }
}