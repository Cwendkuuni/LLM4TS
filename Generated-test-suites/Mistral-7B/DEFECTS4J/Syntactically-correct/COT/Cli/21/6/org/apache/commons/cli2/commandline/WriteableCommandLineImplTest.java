import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("b", "b");
        writeableCommandLine.addOption(option);

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);

        boolean hasOption = writeableCommandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);

        Option resultOption = writeableCommandLine.getOption("a");
        Assert.assertEquals(option, resultOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);

        List unfaultedValues = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertTrue(unfaultedValues.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("p", "p");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");

        String propertyValue = writeableCommandLine.getProperty(option, "defaultValue");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("p", "p");
        writeableCommandLine.addOption(option);

        Map<String, String> properties = new HashMap<>();
        properties.put("property1", "value1");
        writeableCommandLine.addProperty(option, properties);

        Map<String, String> resultProperties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, resultProperties.size());
        Assert.assertEquals("value1", resultProperties.get("property1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        writeableCommandLine.addOption(new Option("b", "b"));
        writeableCommandLine.addOption(new Option("c", "c"));

        String resultToString = writeableCommandLine.toString();
        String expectedToString = "b b c";

        Assert.assertEquals(expectedToString, resultToString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option1 = new Option("b", "b");
        Option option2 = new Option("c", "c");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option1 = new Option("b", "b");
        Option option2 = new Option("c", "c");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("b", "c")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);

        List defaultValues = new ArrayList<String>();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        Option option = new Option("a", "a");
        writeableCommandLine.addOption(option);

        Boolean defaultSwitch = Boolean.TRUE;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = writeableCommandLine.getSwitch(option, Boolean.FALSE);
        Assert.assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<String>());
        writeableCommandLine.addOption(new Option("b", "b"));
        writeableCommandLine.addOption(new Option("c", "c"));

        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("b", normalised.get(0));
        Assert.assertEquals("c", normalised.get(1));
        Assert.assertEquals("a", normalised.get(2));
    }
}