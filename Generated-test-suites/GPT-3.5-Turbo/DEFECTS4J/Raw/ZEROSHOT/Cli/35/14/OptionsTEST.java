import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class OptionsTest {

    private Options options;

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "option a");
        group.addOption(option);
        options.addOptionGroup(group);
        OptionGroup result = options.getOptionGroup(option);
        assertEquals(group, result);
    }

    @Test
    public void testAddOption() {
        String opt = "a";
        String description = "option a";
        options.addOption(opt, description);
        Option result = options.getOption(opt);
        assertEquals(opt, result.getOpt());
        assertEquals(description, result.getDescription());
    }

    @Test
    public void testAddOptionWithArg() {
        String opt = "a";
        boolean hasArg = true;
        String description = "option a";
        options.addOption(opt, hasArg, description);
        Option result = options.getOption(opt);
        assertEquals(opt, result.getOpt());
        assertEquals(hasArg, result.hasArg());
        assertEquals(description, result.getDescription());
    }

    @Test
    public void testAddOptionWithLongOpt() {
        String opt = "a";
        String longOpt = "option-a";
        boolean hasArg = true;
        String description = "option a";
        options.addOption(opt, longOpt, hasArg, description);
        Option result = options.getOption(opt);
        assertEquals(opt, result.getOpt());
        assertEquals(longOpt, result.getLongOpt());
        assertEquals(hasArg, result.hasArg());
        assertEquals(description, result.getDescription());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        options.addOption(option1);
        options.addOption(option2);
        List<Option> result = options.getOptions();
        List<Option> expected = new ArrayList<Option>();
        expected.add(option1);
        expected.add(option2);
        assertEquals(expected, result);
    }

    @Test
    public void testGetRequiredOptions() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        option1.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);
        List<Option> result = options.getRequiredOptions();
        List<Option> expected = new ArrayList<Option>();
        expected.add(option1);
        assertEquals(expected, result);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "option a");
        options.addOption(option);
        Option result = options.getOption("a");
        assertEquals(option, result);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("aa", "option aa");
        Option option2 = new Option("ab", "option ab");
        Option option3 = new Option("ba", "option ba");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        List<String> result = options.getMatchingOptions("a");
        List<String> expected = new ArrayList<String>();
        expected.add("aa");
        expected.add("ab");
        assertEquals(expected, result);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "option a");
        options.addOption(option);
        assertTrue(options.hasOption("a"));
    }

    @Test
    public void testHasLongOption() {
        Option option = new Option("a", "option a");
        options.addOption(option);
        assertTrue(options.hasLongOption("option a"));
    }

    @Test
    public void testHasShortOption() {
        Option option = new Option("a", "option a");
        options.addOption(option);
        assertTrue(options.hasShortOption("a"));
    }

    @Test
    public void testGetOptionGroup() {
        OptionGroup group = new OptionGroup();
        Option option = new Option("a", "option a");
        group.addOption(option);
        options.addOptionGroup(group);
        OptionGroup result = options.getOptionGroup(option);
        assertEquals(group, result);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option a");
        Option option2 = new Option("b", "option b");
        options.addOption(option1);
        options.addOption(option2);
        String result = options.toString();
        String expected = "[ Options: [ short {a=Option: a, option a}, {b=Option: b, option b} ] [ long {option a=Option: a, option a}, {option b=Option: b, option b}]";
        assertEquals(expected, result);
    }
}