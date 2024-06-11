import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

    private Options options;

    @Before
    public void setup() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup_success() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");
        group.addOption(option1);
        group.addOption(option2);

        options.addOptionGroup(group);

        assertTrue(options.getOptionGroups().contains(group));
    }

    @Test
    public void testAddOption_success() {
        Option option = new Option("f", "file", true, "input file");

        options.addOption(option);

        assertTrue(options.getOptions().contains(option));
    }

    @Test
    public void testAddOption_multipleOptions_success() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");

        options.addOption(option1);
        options.addOption(option2);

        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetOption_success() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(option1, options.getOption("f"));
        assertEquals(option2, options.getOption("d"));
    }

    @Test
    public void testGetOption_partialName_success() {
        Option option1 = new Option("file", false, "input file");
        Option option2 = new Option("file2", false, "input file 2");
        Option option3 = new Option("directory", false, "input directory");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        assertEquals(2, options.getMatchingOptions("file").size());
        assertTrue(options.getMatchingOptions("file").contains("file"));
        assertTrue(options.getMatchingOptions("file").contains("file2"));
        assertEquals(1, options.getMatchingOptions("dire").size());
        assertTrue(options.getMatchingOptions("dire").contains("directory"));
    }

    @Test
    public void testHasOption_success() {
        Option option = new Option("f", "file", true, "input file");

        options.addOption(option);

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
    }

    @Test
    public void testHasOption_nonExistingOption_returnsFalse() {
        Option option = new Option("f", "file", true, "input file");

        options.addOption(option);

        assertFalse(options.hasOption("d"));
        assertFalse(options.hasOption("directory"));
    }

    @Test
    public void testHasLongOption_success() {
        Option option = new Option("f", "file", true, "input file");

        options.addOption(option);

        assertTrue(options.hasLongOption("file"));
    }

    @Test
    public void testHasShortOption_success() {
        Option option = new Option("f", "file", true, "input file");

        options.addOption(option);

        assertTrue(options.hasShortOption("f"));
    }

    @Test
    public void testGetOptionGroup_success() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("f", "file", true, "input file");
        group.addOption(option);

        options.addOptionGroup(group);

        assertEquals(group, options.getOptionGroup(option));
    }

    @Test
    public void testToString_success() {
        Option option1 = new Option("f", "file", true, "input file");
        Option option2 = new Option("d", "directory", false, "input directory");

        options.addOption(option1);
        options.addOption(option2);

        String expected = "[ Options: [ short {f=Option{ opt=f, longOpt=file, hasArg=true, description=input file, required=false }} ] [ long {file=Option{ opt=f, longOpt=file, hasArg=true, description=input file, required=false }" +
                ", directory=Option{ opt=d, longOpt=directory, hasArg=false, description=input directory, required=false }} ]";
        assertEquals(expected, options.toString());
    }
}