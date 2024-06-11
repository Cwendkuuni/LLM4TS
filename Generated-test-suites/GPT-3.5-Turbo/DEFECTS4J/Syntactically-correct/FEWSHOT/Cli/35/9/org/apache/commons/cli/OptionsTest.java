import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("h", "help", false, "display help");
        options.addOption("v", "version", false, "display version");
        options.addOption("f", "file", true, "specify file");
        options.addOption("o", "output", true, "specify output");
    }

    @Test
    public void testAddOption() {
        assertFalse(options.hasOption("n"));
        options.addOption("n", "name", true, "specify name");
        assertTrue(options.hasOption("n"));
    }

    @Test
    public void testGetOption() {
        Option helpOption = options.getOption("h");
        assertEquals("h", helpOption.getOpt());
        assertEquals("help", helpOption.getLongOpt());
        assertEquals("display help", helpOption.getDescription());

        Option fileOption = options.getOption("f");
        assertEquals("f", fileOption.getOpt());
        assertEquals("file", fileOption.getLongOpt());
        assertEquals("specify file", fileOption.getDescription());
    }

    @Test
    public void testHasOption() {
        assertTrue(options.hasOption("h"));
        assertFalse(options.hasOption("n"));
    }

    @Test
    public void testHasLongOption() {
        assertTrue(options.hasLongOption("help"));
        assertFalse(options.hasLongOption("name"));
    }

    @Test
    public void testHasShortOption() {
        assertTrue(options.hasShortOption("h"));
        assertFalse(options.hasShortOption("n"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(new Option("a", "apple", false, "apple option"));
        group.addOption(new Option("b", "banana", false, "banana option"));
        options.addOptionGroup(group);

        Option appleOption = options.getOption("a");
        OptionGroup appleGroup = options.getOptionGroup(appleOption);
        assertTrue(appleGroup.isRequired());
        assertEquals("apple option", appleOption.getDescription());

        Option bananaOption = options.getOption("b");
        OptionGroup bananaGroup = options.getOptionGroup(bananaOption);
        assertTrue(bananaGroup.isRequired());
        assertEquals("banana option", bananaOption.getDescription());
    }

    @Test
    public void testGetOptions() {
        assertEquals(4, options.getOptions().size());
    }

    @Test
    public void testGetMatchingOptions() {
        assertEquals(3, options.getMatchingOptions("help").size());
        assertEquals(0, options.getMatchingOptions("name").size());
    }

    @Test
    public void testGetRequiredOptions() {
        assertEquals(0, options.getRequiredOptions().size());
        options.getOption("h").setRequired(true);
        options.getOption("f").setRequired(true);
        assertEquals(2, options.getRequiredOptions().size());
    }

    @Test
    public void testToString() {
        String expected = "[ Options: [ short {h=Option(opt=h, longOpt=help, hasArg=false, description=display help), v=Option(opt=v, longOpt=version, hasArg=false, description=display version), f=Option(opt=f, longOpt=file, hasArg=true, description=specify file), o=Option(opt=o, longOpt=output, hasArg=true, description=specify output) } ] [ long {help=Option(opt=h, longOpt=help, hasArg=false, description=display help), version=Option(opt=v, longOpt=version, hasArg=false, description=display version), file=Option(opt=f, longOpt=file, hasArg=true, description=specify file), output=Option(opt=o, longOpt=output, hasArg=true, description=specify output) } ]]";
        assertEquals(expected, options.toString());
    }
}