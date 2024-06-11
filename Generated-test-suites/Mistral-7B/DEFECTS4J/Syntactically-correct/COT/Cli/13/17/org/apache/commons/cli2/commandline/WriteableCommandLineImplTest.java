import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("b", "bool", false));

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Option optionA = (Option) options.get(0);
        Assert.assertEquals("a", optionA.getLongOpt());
        Assert.assertEquals("arg", optionA.getOpt());
        Assert.assertTrue(optionA.isRequired());
        Assert.assertFalse(optionA.isHidden());
        Option optionB = (Option) options.get(1);
        Assert.assertEquals("b", optionB.getLongOpt());
        Assert.assertEquals("bool", optionB.getOpt());
        Assert.assertFalse(optionB.isRequired());
        Assert.assertFalse(optionB.isHidden());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));
        writeableCommandLine.addValue(new Option("a", "arg"), "value1");
        writeableCommandLine.addValue(new Option("a", "arg"), "value2");

        List values = writeableCommandLine.getValues(new Option("a", "arg"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));
        writeableCommandLine.addSwitch(new Option("a", "arg"), true);
        writeableCommandLine.addSwitch(new Option("a", "arg"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));

        boolean hasOption = writeableCommandLine.hasOption(new Option("a", "arg"));
        Assert.assertTrue(hasOption);

        hasOption = writeableCommandLine.hasOption(new Option("b", "bool"));
        Assert.assertFalse(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));

        Option option = writeableCommandLine.getOption(new Option("a", "arg").getPreferredName());
        Assert.assertNotNull(option);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));
        writeableCommandLine.addValue(new Option("a", "arg"), "value1");
        writeableCommandLine.addValue(new Option("a", "arg"), "value2");

        List values = writeableCommandLine.getValues(new Option("a", "arg"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        values = writeableCommandLine.getValues(new Option("b", "bool"), null);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));
        writeableCommandLine.addSwitch(new Option("a", "arg"), false);

        boolean switchValue = writeableCommandLine.getSwitch(new Option("a", "arg"), null);
        Assert.assertFalse(switchValue);

        writeableCommandLine.addSwitch(new Option("a", "arg"), true);
        switchValue = writeableCommandLine.getSwitch(new Option("a", "arg"), null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addProperty("property", "value");

        String propertyValue = writeableCommandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addProperty("property", "value");

        String propertyValue = writeableCommandLine.getProperty("property", null);
        Assert.assertEquals("value", propertyValue);

        propertyValue = writeableCommandLine.getProperty("anotherProperty", null);
        Assert.assertNull(propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        Set properties = writeableCommandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));
        writeableCommandLine.addValue(new Option("a", "arg"), "value1");
        writeableCommandLine.addValue(new Option("a", "arg"), "value2");

        String commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("value1 value2", commandLineString);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("--arg", "arg", true), new ArrayList());
        List normalised = writeableCommandLine.getNormalised();
        String commandLineArgument = (String) normalised.get(0);

        boolean looksLikeOption = writeableCommandLine.looksLikeOption(commandLineArgument);
        Assert.assertTrue(looksLikeOption);
    }

    @Test
    public void testGetListOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));
        writeableCommandLine.addOption(new Option("b", "bool", false));

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(new Option("a", "arg")));
        Assert.assertTrue(options.contains(new Option("b", "bool")));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));

        List defaultValues = new ArrayList();
        defaultValues.add("value1");
        defaultValues.add("value2");
        writeableCommandLine.setDefaultValues(new Option("a", "arg"), defaultValues);

        List values = writeableCommandLine.getValues(new Option("a", "arg"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));

        Boolean defaultSwitch = Boolean.TRUE;
        writeableCommandLine.setDefaultSwitch(new Option("a", "arg"), defaultSwitch);

        Boolean switchValue = writeableCommandLine.getSwitch(new Option("a", "arg"), null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("a", "arg", true), new ArrayList());
        writeableCommandLine.addOption(new Option("a", "arg", true));
        writeableCommandLine.addValue(new Option("a", "arg"), "value1");
        writeableCommandLine.addValue(new Option("a", "arg"), "value2");

        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("value1", normalised.get(1));
        Assert.assertEquals("value2", normalised.get(2));
    }
}