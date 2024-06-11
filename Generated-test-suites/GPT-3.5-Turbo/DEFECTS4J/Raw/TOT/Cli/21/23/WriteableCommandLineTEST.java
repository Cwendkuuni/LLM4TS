import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    WriteableCommandLine writeableCommandLine;

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("testOption");
        writeableCommandLine.addValue(option, "testValue");
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("testOption");
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        writeableCommandLine.addSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.hasOption("testOption"));
        Assert.assertTrue(writeableCommandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");
        writeableCommandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.hasOption("testOption"));
        Assert.assertTrue(writeableCommandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("testOption");
        writeableCommandLine.addProperty(option, "property1", "value1");
        Assert.assertEquals("value1", writeableCommandLine.getProperty(option, "property1"));
    }

    @Test
    public void testAddDefaultProperty() {
        writeableCommandLine.addProperty("defaultProperty", "defaultValue");
        Assert.assertEquals("defaultValue", writeableCommandLine.getDefaultProperty("defaultProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(writeableCommandLine.looksLikeOption("-testOption"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("testValue"));
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new Option("testOption");
        writeableCommandLine.setCurrentOption(option);
        Assert.assertEquals(option, writeableCommandLine.getCurrentOption());
    }
}