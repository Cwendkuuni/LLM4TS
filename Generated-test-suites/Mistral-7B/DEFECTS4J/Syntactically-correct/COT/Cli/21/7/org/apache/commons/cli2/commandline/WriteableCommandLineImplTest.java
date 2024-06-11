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

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("b", "b", false, "b-value");
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        Option actualOption = (Option) options.get(0);
        Assert.assertEquals("b", actualOption.getLongOpt());
        Assert.assertEquals("b-value", actualOption.getDescription());
        Assert.assertFalse(actualOption.isRequired());
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("b", "b", false, "b-value");
        writeableCommandLine.addOption(option);
        Option actualOption = writeableCommandLine.getOption("b");
        Assert.assertEquals(option, actualOption);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("b", false, "b-value");
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Option("b", "b", false, "b-value");
        writeableCommandLine.addOption(option);
        boolean actualResult = writeableCommandLine.hasOption(option);
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option propertyOption = new Option("p", "p", true, "property");
        writeableCommandLine.addOption(propertyOption);
        writeableCommandLine.addProperty(propertyOption, "property-value");
        String actualValue = writeableCommandLine.getProperty("p");
        Assert.assertEquals("property-value", actualValue);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option switchOption = new Option("s", false, "switch");
        writeableCommandLine.addOption(switchOption);
        Boolean actualValue = writeableCommandLine.getSwitch(switchOption, false);
        Assert.assertFalse(actualValue);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Argument("v", "value", true, "The value to set");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List actualValues = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(Collections.singletonList("value1"), actualValues);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option = new Argument("v", "value", true, "The value to set");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List actualValues = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(Collections.singletonList("value1"), actualValues);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        writeableCommandLine.addOption(new Option("b", "b", false, "b"));
        writeableCommandLine.addOption(new Option("c", "c", false, "c"));
        String actualValue = writeableCommandLine.toString();
        String expectedValue = "b b c";
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option1 = new Option("b", "b", false, "b");
        Option option2 = new Option("c", "c", false, "c");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List actualOptions = writeableCommandLine.getOptions();
        List expectedOptions = new ArrayList();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        Assert.assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a"), new ArrayList<>());
        Option option1 = new Option("b", "b", false, "b");
        Option option2 = new Option("c", "c", false, "c");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set actualOptions = writeableCommandLine.getOptionTriggers();
        Set expectedOptions = new HashSet();
        expectedOptions.add("b");
        expectedOptions.add("c");
        Assert.assertEquals(expectedOptions, actualOptions);
    }
}