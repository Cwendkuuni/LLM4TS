import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest
{
    @Test
    public void testAddOption()
    {
        Options options = new Options();
        Option opt = new Option("o", "opt", false, "Option test");
        options.addOption(opt);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("opt"));
        assertFalse(options.hasOption("nonexistent"));
        assertNull(options.getOption("nonexistent"));
        assertSame(opt, options.getOption("o"));
        assertSame(opt, options.getOption("opt"));
    }

    @Test
    public void testAddOptionWithValue()
    {
        Options options = new Options();
        Option opt = new Option("o", "opt", true, "Option test with value");
        options.addOption(opt);

        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("opt"));
        assertNull(options.getOption("nonexistent"));
        assertSame(opt, options.getOption("o"));
        assertSame(opt, options.getOption("opt"));

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(options.parseOptions(), new String[]{"-o", "value"});
            assertEquals("value", line.getOptionValue("opt"));
        }
        catch (ParseException e)
        {
            //ignore
        }
    }

    @Test
    public void testAddOptionGroup()
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "Group test");
        group.addOption(new Option("g1", "group1", false, "Group test option 1"));
        group.addOption(new Option("g2", "group2", false, "Group test option 2"));
        options.addOptionGroup(group);

        assertTrue(options.hasOption("g1"));
        assertTrue(options.hasOption("g2"));
        assertTrue(options.getOption("g1").isInOptionGroup("g"));
        assertTrue(options.getOption("g2").isInOptionGroup("g"));
        assertFalse(options.getOption("o").isInOptionGroup("g"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionGroupWithDuplicateOption()
    {
        Options options = new Options();
        Option opt = new Option("o", "opt", false, "Option test");
        options.addOption(opt);
        OptionGroup group = new OptionGroup("g", "Group test");
        group.addOption(new Option("o", "opt", false, "Group test option"));
        options.addOptionGroup(group);
    }

    @Test
    public void testGetOptions()
    {
        Options options = new Options();
        Option opt1 = new Option("o1", "opt1", false, "Option test 1");
        Option opt2 = new Option("o2", "opt2", false, "Option test 2");
        options.addOption(opt1);
        options.addOption(opt2);

        List<Option> optionsList = options.getOptions();
        assertEquals(2, optionsList.size());
        assertEquals(opt1, optionsList.get(0));
        assertEquals(opt2, optionsList.get(1));
    }

    @Test
    public void testGetRequiredOptions()
    {
        Options options = new Options();
        Option opt = new Option("o", "opt", true, "Required option test");
        options.addOption(opt);

        List<Option> requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals(opt, requiredOptions.get(0));
    }

    @Test
    public void testGetMatchingOptions()
    {
        Options options = new Options();
        Option opt1 = new Option("o1", "opt1", false, "Option test 1");
        Option opt2 = new Option("o2", "opt2", false, "Option test 2");
        Option opt3 = new Option("opt", "longOption", false, "Long option test");
        options.addOption(opt1);
        options.addOption(opt2);
        options.addOption(opt3);

        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertEquals(1, matchingOptions.size());
        assertEquals(opt3.getLongOpt(), matchingOptions.get(0));
    }

    @Test
    public void testGetOption()
    {
        Options options = new Options();
        Option opt1 = new Option("o1", "opt1", false, "Option test 1");
        Option opt2 = new Option("o2", "opt2", false, "Option test 2");
        options.addOption(opt1);
        options.addOption(opt2);

        Option option1 = options.getOption("o1");
        assertEquals(opt1, option1);

        Option option2 = options.getOption("o2");
        assertEquals(opt2, option2);

        Option optionNonexistent = options.getOption("nonexistent");
        assertNull(optionNonexistent);
    }

    @Test
    public void testToString()
    {
        Options options = new Options();
        Option opt1 = new Option("o1", "opt1", false, "Option test 1");
        Option opt2 = new Option("o2", "opt2", false, "Option test 2");
        options.addOption(opt1);
        options.addOption(opt2);

        String toString = options.toString();
        assertTrue(toString.contains("Options: [ short"));
        assertTrue(toString.contains("long []"));
    }
}