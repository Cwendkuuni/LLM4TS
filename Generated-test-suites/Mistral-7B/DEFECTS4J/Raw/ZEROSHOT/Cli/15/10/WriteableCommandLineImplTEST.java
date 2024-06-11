package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Option optionFromList = (Option) options.get(0);
        Assert.assertEquals(option, optionFromList);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("d", "debug", false, "Run in debug mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        Option option = new Option("d", "debug", false, "Run in debug mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        Option optionFromMap = writeableCommandLine.getOption("o");
        Assert.assertEquals(option, optionFromMap);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        writeableCommandLine.addValue(option, "results.csv");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test.txt", values.get(0));
        Assert.assertEquals("results.csv", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");

        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = new Option("d", "debug", false, "Run in debug mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("p", "property", true, "Property name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "color", "red");

        Assert.assertEquals("red", writeableCommandLine.getProperty(option, ""));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new Option("p", "property", true, "Property name");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("color", "red");
        properties.put("size", "large");
        writeableCommandLine.addProperties(option, properties);

        Set propertiesSet = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, propertiesSet.size());
        Assert.assertTrue(propertiesSet.containsAll(Arrays.asList("color", "size")));
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("d", "debug", false, "Run in debug mode"), Arrays.asList());

        Assert.assertTrue(writeableCommandLine.looksLikeOption("d"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("debug"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("test"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");

        String toString = writeableCommandLine.toString();
        Assert.assertEquals("test.txt output", toString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option1 = new Option("o1", "option1", false, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("o1", "o2")));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        List defaultValues = Arrays.asList("test.txt", "results.csv");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test.txt", values.get(0));
        Assert.assertEquals("results.csv", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        Option option = new Option("d", "debug", false, "Run in debug mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch_notSet() {
        Option option = new Option("d", "debug", false, "Run in debug mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        Assert.assertFalse(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues_notSet() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        List defaultValues = Arrays.asList("test.txt", "results.csv");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(0, values.size());
    }
}