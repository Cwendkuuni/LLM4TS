import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "Output directory path");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);

        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertEquals(option, cmd.getOption(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "output", true, "Output directory path");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);

        cmd.addValue(option, "/path/to/output");

        List values = cmd.getValues(option, null);
        Assert.assertEquals("/path/to/output", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);

        cmd.addSwitch(option, false);

        Boolean switchValue = cmd.getSwitch(option, true);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "Output directory path");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);

        boolean result = cmd.hasOption(option);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "Output directory path");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);

        Option result = cmd.getOption(option.getPreferredName());
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "Output directory path");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);
        cmd.addValue(option, "/path/to/output");

        List values = cmd.getValues(option, Collections.emptyList());
        Assert.assertEquals("/path/to/output", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "output", true, "Output directory path");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);
        cmd.addValue(option, "/path/to/output");

        List values = cmd.getUndefaultedValues(option);
        Assert.assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);
        cmd.addSwitch(option, false);

        Boolean switchValue = cmd.getSwitch(option, true);
        Assert.assertFalse(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("d", "debug", false, "Enable debug mode");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());

        cmd.addOption(option);
        cmd.addSwitch(option, false);
        cmd.addSwitch(option, false);
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "Output directory path");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("input.txt"));

        cmd.addOption(option);
        cmd.addValue(option, "/path/to/output");

        String result = cmd.toString();
        String expected = "output /path/to/output input.txt";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o", "output", true, "Output directory path");
        Option option2 = new Option("i", "input", true, "Input file path");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, Collections.emptyList());
        cmd.addOption(option1);
        cmd.addOption(option2);

        List options = cmd.getOptions();
        Assert.assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o", "output", true, "Output directory path");
        Option option2 = new Option("i", "input", true, "Input file path");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, Collections.emptyList());
        cmd.addOption(option1);
        cmd.addOption(option2);

        Set optionTriggers = cmd.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "output", true, "Output directory path");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);

        List defaultValues = Arrays.asList("/path/to/output1", "/path/to/output2");
        cmd.setDefaultValues(option, defaultValues);

        List values = cmd.getValues(option, Collections.emptyList());
        Assert.assertEquals(defaultValues.size(), values.size());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        cmd.addOption(option);

        cmd.setDefaultSwitch(option, true);

        Boolean switchValue = cmd.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }
}