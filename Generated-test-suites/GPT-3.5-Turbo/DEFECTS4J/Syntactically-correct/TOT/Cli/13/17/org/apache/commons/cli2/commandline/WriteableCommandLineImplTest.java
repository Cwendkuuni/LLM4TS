import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option2);

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");

        List<String> expectedValues1 = Collections.singletonList("value1");
        List<String> expectedValues2 = Collections.singletonList("value2");

        Assert.assertEquals(expectedValues1, commandLine.getValues(option1, new ArrayList()));
        Assert.assertEquals(expectedValues2, commandLine.getValues(option2, new ArrayList()));
        Assert.assertEquals(new ArrayList(), commandLine.getValues(option2, null));
    }

    @Test
    public void testAddSwitch() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        Assert.assertTrue(commandLine.getSwitch(option1, false));
        Assert.assertFalse(commandLine.getSwitch(option2, true));
        Assert.assertNull(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList());
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Assert.assertEquals("value1", commandLine.getProperty("property1", null));
        Assert.assertEquals("value2", commandLine.getProperty("property2", null));
        Assert.assertNull(commandLine.getProperty("property3", null));
    }

    @Test
    public void testLooksLikeOption() {
        Option option1 = new OptionBuilder().withPrefixes("-", "--").create();
        Option option2 = new OptionBuilder().withPrefixes("/").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertTrue(commandLine.looksLikeOption("-option1"));
        Assert.assertTrue(commandLine.looksLikeOption("--option1"));
        Assert.assertTrue(commandLine.looksLikeOption("/option2"));
        Assert.assertFalse(commandLine.looksLikeOption("option1"));
        Assert.assertFalse(commandLine.looksLikeOption("option2"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> expectedOptions = Arrays.asList(option1, option2);

        Assert.assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withPreferredName("option1").withTriggers("opt1", "opt2").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").withTriggers("opt3", "opt4").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<String> expectedTriggers = Arrays.asList("option1", "opt1", "opt2", "option2", "opt3", "opt4");

        Assert.assertEquals(expectedTriggers.size(), commandLine.getOptionTriggers().size());
        for (String trigger : expectedTriggers) {
            Assert.assertTrue(commandLine.getOptionTriggers().contains(trigger));
        }
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.setDefaultValues(option1, Arrays.asList("default1"));
        commandLine.setDefaultValues(option2, Arrays.asList("default2"));

        List<String> expectedValues1 = Arrays.asList("default1");
        List<String> expectedValues2 = Arrays.asList("default2");

        Assert.assertEquals(expectedValues1, commandLine.getValues(option1, null));
        Assert.assertEquals(expectedValues2, commandLine.getValues(option2, null));

        commandLine.setDefaultValues(option1, null);

        Assert.assertEquals(new ArrayList(), commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList());
        commandLine.setDefaultSwitch(option1, true);
        commandLine.setDefaultSwitch(option2, false);

        Assert.assertTrue(commandLine.getSwitch(option1, null));
        Assert.assertFalse(commandLine.getSwitch(option2, null));

        commandLine.setDefaultSwitch(option1, null);

        Assert.assertNull(commandLine.getSwitch(option1, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));

        List<String> expectedArgs = Arrays.asList("arg1", "arg2");

        Assert.assertEquals(expectedArgs, commandLine.getNormalised());
    }
}