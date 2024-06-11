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
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", true, "o", "op");
        writeableCommandLine.addOption(option);

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", true, "o", "op");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", true, "o", "op");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", false, "o", "op");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", true, "o", "op");
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", true, "o", "op");
        writeableCommandLine.addOption(option);

        Option resultOption = writeableCommandLine.getOption("option1");
        Assert.assertEquals(option, resultOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", true, "o", "op");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", false, "o", "op");
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        Boolean defaultSwitchValue = writeableCommandLine.getSwitch(option, null);
        Assert.assertNull(defaultSwitchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addProperty("property1", "value1");

        String propertyValue = writeableCommandLine.getProperty("property1", null);
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addProperty("property1", "value1");

        Set properties = writeableCommandLine.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("property1", properties.iterator().next());
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(new Option("option1", true, "o", "op"));
        writeableCommandLine.addOption(new Option("option2", false, "op"));

        String commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("op option1 option2", commandLineString);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        writeableCommandLine.addOption(new Option("option1", true, "o", "op"));
        writeableCommandLine.addOption(new Option("option2", false, "op"));

        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("op option1", normalised.get(0));
        Assert.assertEquals("op option2", normalised.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", true, "o", "op");
        writeableCommandLine.addOption(option);

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("option1", false, "o", "op");
        writeableCommandLine.addOption(option);

        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertEquals(defaultSwitch, switchValue);
    }
}