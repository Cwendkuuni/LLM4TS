import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        List options = writeableCommandLine.getOptions();
        Map nameToOption = writeableCommandLine.getNameToOption();

        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
        Assert.assertEquals(option, nameToOption.get("opt"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);

        writeableCommandLine.addSwitch(option, true);
        switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        Option returnedOption = writeableCommandLine.getOption("opt");
        Assert.assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        List emptyValues = writeableCommandLine.getValues(option, Collections.emptyList());
        Assert.assertTrue(emptyValues.isEmpty());
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List unfaultedValues = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, unfaultedValues.size());

        writeableCommandLine.setDefaultValues(option, new ArrayList() {{add("defaultValue1"); add("defaultValue2");}});
        unfaultedValues = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, unfaultedValues.size());
        Assert.assertFalse(unfaultedValues.contains("defaultValue1"));
        Assert.assertFalse(unfaultedValues.contains("defaultValue2"));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        writeableCommandLine.addOption(option);

        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertNull(switchValue);

        writeableCommandLine.addSwitch(option, true);
        switchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertTrue(switchValue);

        switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        Option propertyOption = new Option("prop", "property");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(propertyOption);

        writeableCommandLine.addProperty(option, "property", "value");

        String propertyValue = writeableCommandLine.getProperty("opt.property");
        Assert.assertEquals("value", propertyValue);

        propertyValue = writeableCommandLine.getProperty("opt.nonexistentproperty", "defaultValue");
        Assert.assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Option option = new Option("opt", "option");
        Option propertyOption = new Option("prop", "property");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(propertyOption);

        writeableCommandLine.addProperty(option, "property", "value");

        Properties properties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.getProperty("property"));

        properties = writeableCommandLine.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("value", properties.getProperty("opt.property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Set prefixes = writeableCommandLine.getPrefixes();
        Assert.assertEquals(0, prefixes.size());

        writeableCommandLine.setPrefixes(Collections.singletonList("--"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--opt"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("--"));

        writeableCommandLine.setPrefixes(Collections.singletonList("-"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("-opt"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("-"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        List arguments = new ArrayList() {{add("arg1"); add("arg2"); add("arg3");}};
        writeableCommandLine.setNormalised(arguments);

        String expected = "--opt arg1 arg2 arg3";
        String result = writeableCommandLine.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        List arguments = new ArrayList() {{add("arg1"); add("arg2"); add("arg3");}};
        writeableCommandLine.setNormalised(arguments);

        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(arguments, normalised);
    }

    @Test
    public void testGetOptionsWithEmptyList() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(0, options.size());
    }

    @Test
    public void testGetOptionTriggersWithEmptyMap() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(0, optionTriggers.size());
    }
}