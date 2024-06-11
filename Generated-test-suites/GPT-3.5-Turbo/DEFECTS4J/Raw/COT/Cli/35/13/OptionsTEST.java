import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        Option option1 = new Option("f", "file", true, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        assertTrue(options.getRequiredOptions().contains(group));
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.getOption("f").hasArg());
        assertFalse(options.getOption("d").hasArg());
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("f", true, "File option");
        assertEquals(1, options.getOptions().size());
        assertTrue(options.hasOption("f"));
        assertTrue(options.getOption("f").hasArg());
    }

    @Test
    public void testAddOptionWithLongOption() {
        Options options = new Options();
        options.addOption("f", "file", true, "File option");
        assertEquals(1, options.getOptions().size());
        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("file"));
        assertTrue(options.getOption("f").hasArg());
    }

    @Test
    public void testAddOptionWithLongOptionAndNoArg() {
        Options options = new Options();
        options.addOption("d", "dir", false, "Directory option");
        assertEquals(1, options.getOptions().size());
        assertTrue(options.hasOption("d"));
        assertFalse(options.hasOption("dir"));
        assertFalse(options.getOption("d").hasArg());
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        options.addOption("f", true, "File option");
        options.addOption("d", "dir", false, "Directory option");
        Option option1 = options.getOption("f");
        Option option2 = options.getOption("dir");
        assertNotNull(option1);
        assertNotNull(option2);
        assertEquals("f", option1.getOpt());
        assertEquals("dir", option2.getLongOpt());
    }

    @Test
    public void testGetMissingOption() {
        Options options = new Options();
        options.addOption("f", true, "File option");
        Option option = options.getOption("d");
        assertNull(option);
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("f", true, "File option");
        options.addOption("fd", true, "Directory option");
        options.addOption("ff", true, "File file option");
        options.addOption("dir", false, "Directory option");
        List<String> matchingOptions = options.getMatchingOptions("f");
        assertEquals(3, matchingOptions.size());
        assertTrue(matchingOptions.contains("f"));
        assertTrue(matchingOptions.contains("fd"));
        assertTrue(matchingOptions.contains("ff"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("f", true, "File option");
        options.addOption("d", "dir", false, "Directory option");
        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("dir"));
        assertFalse(options.hasOption("test"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        options.addOption("f", true, "File option");
        options.addOption("d", "dir", false, "Directory option");
        assertTrue(options.hasLongOption("dir"));
        assertFalse(options.hasLongOption("test"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        options.addOption("f", true, "File option");
        options.addOption("d", "dir", false, "Directory option");
        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("test"));
    }
}