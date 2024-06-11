import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.cli.Option;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup();
        Option option1 = new Option("a", "option 1");
        Option option2 = new Option("b", "option 2");
        group1.addOption(option1);
        group1.addOption(option2);
        options.addOptionGroup(group1);
        assertTrue(options.getOptionGroups().contains(group1));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1");
        options.addOption(option1);
        assertTrue(options.getOptions().contains(option1));
    }

    @Test
    public void testAddOptionWithArg() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1");
        option1.setHasArg(true);
        options.addOption(option1);
        assertTrue(options.getOptions().contains(option1));
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1");
        options.addOption(option1);
        assertTrue(options.getOptions().contains(option1));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1");
        options.addOption(option1);
        assertEquals(option1, options.getOption("a"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1");
        Option option2 = new Option("abc", "option 2");
        options.addOption(option1);
        options.addOption(option2);
        List<String> matchingOptions = options.getMatchingOptions("a");
        assertTrue(matchingOptions.contains("a"));
        assertTrue(matchingOptions.contains("abc"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1");
        options.addOption(option1);
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1");
        options.addOption(option1);
        assertTrue(options.hasLongOption("option 1"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option1 = new Option("a", "option 1");
        options.addOption(option1);
        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup();
        Option option1 = new Option("a", "option 1");
        Option option2 = new Option("b", "option 2");
        group1.addOption(option1);
        group1.addOption(option2);
        options.addOptionGroup(group1);
        assertEquals(group1, options.getOptionGroup(option1));
        assertEquals(group1, options.getOptionGroup(option2));
    }
}