import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "optional", false, "Optional option");
        cmd.addOption(option);
        Assert.assertTrue(cmd.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithDuplicateTrigger() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option1 = new Option("option", "opt", false, "Option 1");
        cmd.addOption(option1);
        Option option2 = new Option("option", "opt", false, "Option 2");
        cmd.addOption(option2);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        cmd.addValue(option, "val1");
        List values = cmd.getValues(option, null);
        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0), "val1");
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        Boolean value = cmd.getSwitch(option, false);
        Assert.assertTrue(value);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        Assert.assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        Assert.assertEquals(cmd.getOption("opt"), option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        cmd.addValue(option, "val1");
        cmd.addValue(option, "val2");
        List values = cmd.getValues(option, null);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values.get(0), "val1");
        Assert.assertEquals(values.get(1), "val2");
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        cmd.addValue(option, "val1");
        List values = cmd.getUndefaultedValues(option);
        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0), "val1");
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, false);
        Boolean value = cmd.getSwitch(option, false);
        Assert.assertFalse(value);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        cmd.addProperty(option, "prop", "propValue");
        String value = cmd.getProperty(option, "prop", "defaultValue");
        Assert.assertEquals(value, "propValue");
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Assert.assertTrue(cmd.looksLikeOption("--opt"));
        Assert.assertTrue(cmd.looksLikeOption("-opt"));
        Assert.assertFalse(cmd.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        cmd.addOption(new Option("option", "opt", false, "Option"));
        cmd.addOption(new Option("anotherOption", "anotherOpt", false, "Another option"));
        cmd.addOption(new Option("arg", "arg", true, "An argument"));
        Assert.assertEquals(cmd.toString(), "--option opt --anotherOption anotherOpt -arg arg");
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        cmd.addOption(new Option("option", "opt", false, "Option"));
        List options = cmd.getOptions();
        Assert.assertEquals(options.size(), 1);
        Assert.assertEquals(options.get(0), cmd.getOption("opt"));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        cmd.addOption(new Option("option1", "opt1", false, "Option 1"));
        cmd.addOption(new Option("option2", "opt2", false, "Option 2"));
        Set triggers = cmd.getOptionTriggers();
        Assert.assertEquals(triggers.size(), 2);
        Assert.assertTrue(triggers.containsAll(Arrays.asList("opt1", "opt2")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        List defaults = new ArrayList();
        cmd.setDefaultValues(option, defaults);
        List values = cmd.getValues(option, null);
        Assert.assertEquals(values.size(), 0);
        Assert.assertEquals(defaults.size(), 0);

        defaults.add("default1");
        cmd.setDefaultValues(option, defaults);
        values = cmd.getValues(option, null);
        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0), "default1");
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Option option = new Option("option", "opt", false, "Option");
        cmd.addOption(option);
        cmd.setDefaultSwitch(option, null);
        Boolean value = cmd.getSwitch(option, false);
        Assert.assertNull(value);

        cmd.setDefaultSwitch(option, Boolean.TRUE);
        value = cmd.getSwitch(option, false);
        Assert.assertTrue(value);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        cmd.addOption(new Option("option1", "opt1", false, "Option 1"));
        cmd.addOption(new Option("option2", "opt2", false, "Option 2"));
        List normalised = cmd.getNormalised();
        Assert.assertEquals(normalised.size(), 0);

        cmd.addArgument("arg1");
        normalised = cmd.getNormalised();
        Assert.assertEquals(normalised.size(), 1);
        Assert.assertEquals(normalised.get(0), "arg1");

        cmd.addOption(new Option("option3", "opt3", false, "Option 3"));
        normalised = cmd.getNormalised();
        Assert.assertEquals(normalised.size(), 2);
        Assert.assertEquals(normalised.get(1), "--option3 opt3");
    }
}