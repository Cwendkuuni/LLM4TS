import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OptionsTest
{
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
    }

    // Test addOption method for single character short option without argument

    @Test
    public void testAddOptionShortWithNoArg()
    {
        options.addOption('a', "optionA");
        Option optA = options.getOption("optionA");
        assertEquals("optionA", optA.getLongOpt());
        assertFalse(optA.hasArg());
    }

    @Test
    public void testHasShortOptionWithNoArg()
    {
        options.addOption('a', "optionA");
        assertTrue(options.hasOption('a'));
        assertTrue(options.hasOption("optionA"));
    }

    @Test
    public void testHasShortOptionWithNoArg_Invalid()
    {
        options.addOption('a', "optionA");
        assertFalse(options.hasOption('z'));
        assertFalse(options.hasOption("invalidOption"));
    }

    @Test
    public void testHasLongOptionWithNoArg()
    {
        options.addOption('a', "optionA");
        assertFalse(options.hasLongOption("a"));
        assertTrue(options.hasLongOption("optionA"));
    }

    // Test addOption method for single character short option with argument

    @Test
    public void testAddOptionShortWithArg()
    {
        options.addOption('a', "optionA", true, "argA");
        Option optA = options.getOption("optionA");
        assertEquals("optionA", optA.getLongOpt());
        assertTrue(optA.hasArg());
        assertEquals("argA", optA.getArgName());
    }

    @Test
    public void testHasShortOptionWithArg()
    {
        options.addOption('a', "optionA", true, "argA");
        assertTrue(options.hasOption('a'));
        assertTrue(options.hasOption("optionA"));
    }

    @Test
    public void testHasShortOptionWithArg_Invalid()
    {
        options.addOption('a', "optionA", true, "argA");
        assertFalse(options.hasOption('z'));
        assertFalse(options.hasOption("invalidOption"));
    }

    @Test
    public void testHasLongOptionWithArg()
    {
        options.addOption('a', "optionA", true, "argA");
        assertFalse(options.hasLongOption("a"));
        assertTrue(options.hasLongOption("optionA"));
    }

    @Test(expected = ParseException.class)
    public void testAddOptionShortArgMismatch()
    {
        options.addOption('a', "optionA", false, "argA");
        options.addOption('a', "optionA", true, "argB");
    }

    @Test
    public void testAddOptionLongOnly()
    {
        options.addOption(new Option("longOption", true, "long"));
        Option longOption = options.getOption("longOption");
        assertNotNull(longOption);
        assertTrue(longOption.hasArg());
        assertTrue(longOption.isLongOptParsedDefaultValueSet());
    }

    // Test addOptionGroup method

    @Test
    public void testAddOptionGroup()
    {
        OptionGroup group = new OptionGroup("group1", "Group 1");
        options.addOptionGroup(group);
        assertEquals("group1", options.getOptionGroup("group1").getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionGroupNullGroup()
    {
        options.addOptionGroup(null);
    }

    // Test getOptions method

    @Test
    public void testGetOptions()
    {
        options.addOption('a', "optionA");
        List<Option> optionsList = options.getOptions();
        assertEquals(1, optionsList.size());
    }

    @Test
    public void testGetRequiredOptions()
    {
        options.addOption('a', "optionA", true);
        List<Option> requiredOptionsList = options.getRequiredOptions();
        assertEquals(1, requiredOptionsList.size());
    }

    // Test getOption method

    @Test
    public void testGetOption()
    {
        Option optA = new Option('a', "optionA");
        options.addOption(optA);
        Option retrievedOptA = options.getOption("optionA");
        assertEquals(optA, retrievedOptA);
    }

    @Test
    public void testGetOptionWithNoMatch()
    {
        Option optA = new Option('a', "optionA");
        options.addOption(optA);
        Option retrievedOptB = options.getOption("optionB");
        assertNull(retrievedOptB);
    }

    // Test getMatchingOptions method

    @Test
    public void testGetMatchingOptions()
    {
        options.addOption('a', "optionA");
        options.addOption('b', "optionB");
        options.addOption('c', "optionC");
        List<String> matchingOptions = options.getMatchingOptions("opt");
        assertEquals(2, matchingOptions.size());
        assertTrue(matchingOptions.contains("optionA"));
        assertTrue(matchingOptions.contains("optionB"));
    }

    @Test
    public void testGetMatchingOptionsEmpty()
    {
        List<String> matchingOptions = options.getMatchingOptions("xyz");
        assertEquals(0, matchingOptions.size());
    }
}