import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class CommandLineTest {

    @Test
    public void testHasOption_WhenOptionExists_ReturnsTrue() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        Option option = new Option(optionName);
        cmd.addOption(option);

        boolean result = cmd.hasOption(optionName);

        Assert.assertTrue(result);
    }

    @Test
    public void testHasOption_WhenOptionDoesNotExist_ReturnsFalse() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        Option option = new Option(optionName);
        cmd.addOption(option);

        boolean result = cmd.hasOption("nonExistentOption");

        Assert.assertFalse(result);
    }

    @Test
    public void testHasOption_WhenOptionExistsWithShortName_ReturnsTrue() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        char shortName = 't';
        Option option = new Option(optionName, shortName);
        cmd.addOption(option);

        boolean result = cmd.hasOption(shortName);

        Assert.assertTrue(result);
    }

    @Test
    public void testHasOption_WhenOptionDoesNotExistWithShortName_ReturnsFalse() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        char shortName = 't';
        Option option = new Option(optionName, shortName);
        cmd.addOption(option);

        boolean result = cmd.hasOption('n');

        Assert.assertFalse(result);
    }

    @Test
    public void testGetOptionValue_WhenOptionExists_ReturnsValue() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        String optionValue = "testValue";
        Option option = new Option(optionName, true);
        cmd.addOption(option);
        cmd.addArg(optionValue);

        String result = cmd.getOptionValue(optionName);

        Assert.assertEquals(optionValue, result);
    }

    @Test
    public void testGetOptionValue_WhenOptionDoesNotExist_ReturnsNull() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        String optionValue = "testValue";
        Option option = new Option(optionName, true);
        cmd.addOption(option);
        cmd.addArg(optionValue);

        String result = cmd.getOptionValue("nonExistentOption");

        Assert.assertNull(result);
    }

    @Test
    public void testGetOptionValue_WhenOptionExistsWithShortName_ReturnsValue() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        char shortName = 't';
        String optionValue = "testValue";
        Option option = new Option(optionName, true);
        option.setShortOpt(shortName);
        cmd.addOption(option);
        cmd.addArg(optionValue);

        String result = cmd.getOptionValue(shortName);

        Assert.assertEquals(optionValue, result);
    }

    @Test
    public void testGetOptionValue_WhenOptionDoesNotExistWithShortName_ReturnsNull() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        char shortName = 't';
        String optionValue = "testValue";
        Option option = new Option(optionName, true);
        option.setShortOpt(shortName);
        cmd.addOption(option);
        cmd.addArg(optionValue);

        String result = cmd.getOptionValue('n');

        Assert.assertNull(result);
    }

    @Test
    public void testGetOptionValue_WhenOptionExistsWithDefaultValue_ReturnsValue() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        String optionValue = "testValue";
        String defaultValue = "defaultValue";
        Option option = new Option(optionName, true);
        cmd.addOption(option);

        String result = cmd.getOptionValue(optionName, defaultValue);

        Assert.assertEquals(optionValue, result);
    }

    @Test
    public void testGetOptionValue_WhenOptionDoesNotExistWithDefaultValue_ReturnsDefaultValue() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        String defaultValue = "defaultValue";
        Option option = new Option(optionName, true);
        cmd.addOption(option);

        String result = cmd.getOptionValue("nonExistentOption", defaultValue);

        Assert.assertEquals(defaultValue, result);
    }

    @Test
    public void testGetOptionValue_WhenOptionExistsWithShortNameAndDefaultValue_ReturnsValue() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        char shortName = 't';
        String optionValue = "testValue";
        String defaultValue = "defaultValue";
        Option option = new Option(optionName, true);
        option.setShortOpt(shortName);
        cmd.addOption(option);
        cmd.addArg(optionValue);

        String result = cmd.getOptionValue(shortName, defaultValue);

        Assert.assertEquals(optionValue, result);
    }

    @Test
    public void testGetOptionValue_WhenOptionDoesNotExistWithShortNameAndDefaultValue_ReturnsDefaultValue() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        char shortName = 't';
        String defaultValue = "defaultValue";
        Option option = new Option(optionName, true);
        option.setShortOpt(shortName);
        cmd.addOption(option);

        String result = cmd.getOptionValue('n', defaultValue);

        Assert.assertEquals(defaultValue, result);
    }

    @Test
    public void testGetOptionValues_WhenOptionExists_ReturnsValues() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        String[] optionValues = {"value1", "value2"};
        Option option = new Option(optionName, true);
        cmd.addOption(option);
        cmd.addArg(optionValues[0]);
        cmd.addArg(optionValues[1]);

        String[] result = cmd.getOptionValues(optionName);

        Assert.assertArrayEquals(optionValues, result);
    }

    @Test
    public void testGetOptionValues_WhenOptionDoesNotExist_ReturnsNull() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        String[] optionValues = {"value1", "value2"};
        Option option = new Option(optionName, true);
        cmd.addOption(option);
        cmd.addArg(optionValues[0]);
        cmd.addArg(optionValues[1]);

        String[] result = cmd.getOptionValues("nonExistentOption");

        Assert.assertNull(result);
    }

    @Test
    public void testGetOptionValues_WhenOptionExistsWithShortName_ReturnsValues() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        char shortName = 't';
        String[] optionValues = {"value1", "value2"};
        Option option = new Option(optionName, true);
        option.setShortOpt(shortName);
        cmd.addOption(option);
        cmd.addArg(optionValues[0]);
        cmd.addArg(optionValues[1]);

        String[] result = cmd.getOptionValues(shortName);

        Assert.assertArrayEquals(optionValues, result);
    }

    @Test
    public void testGetOptionValues_WhenOptionDoesNotExistWithShortName_ReturnsNull() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        char shortName = 't';
        String[] optionValues = {"value1", "value2"};
        Option option = new Option(optionName, true);
        option.setShortOpt(shortName);
        cmd.addOption(option);
        cmd.addArg(optionValues[0]);
        cmd.addArg(optionValues[1]);

        String[] result = cmd.getOptionValues('n');

        Assert.assertNull(result);
    }

    @Test
    public void testGetArgs_WhenArgsExist_ReturnsArgsArray() {
        CommandLine cmd = new CommandLine();
        String[] args = {"arg1", "arg2"};
        cmd.addArg(args[0]);
        cmd.addArg(args[1]);

        String[] result = cmd.getArgs();

        Assert.assertArrayEquals(args, result);
    }

    @Test
    public void testGetArgList_WhenArgsExist_ReturnsArgList() {
        CommandLine cmd = new CommandLine();
        String[] args = {"arg1", "arg2"};
        cmd.addArg(args[0]);
        cmd.addArg(args[1]);

        List<String> result = cmd.getArgList();

        Assert.assertEquals(Arrays.asList(args), result);
    }

    @Test
    public void testIterator_WhenOptionsExist_ReturnsOptionIterator() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        Option option = new Option(optionName);
        cmd.addOption(option);

        Iterator<Option> iterator = cmd.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(option, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions_WhenOptionsExist_ReturnsOptionsArray() {
        CommandLine cmd = new CommandLine();
        String optionName = "testOption";
        Option option = new Option(optionName);
        cmd.addOption(option);

        Option[] options = cmd.getOptions();

        Assert.assertArrayEquals(new Option[]{option}, options);
    }
}