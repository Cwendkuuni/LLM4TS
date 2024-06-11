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
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", true, "a");
        writeableCommandLine.addOption(optionA);

        Assert.assertTrue(writeableCommandLine.hasOption(optionA));
        Assert.assertEquals(optionA, writeableCommandLine.getOption("optionA"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", true, "a");
        writeableCommandLine.addOption(optionA);

        writeableCommandLine.addValue(optionA, "value1");
        List values = writeableCommandLine.getValues(optionA, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(optionA, "value2");
        values = writeableCommandLine.getValues(optionA, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", false, "a");
        writeableCommandLine.addOption(optionA);

        Assert.assertFalse(writeableCommandLine.getSwitch(optionA, false));
        writeableCommandLine.addSwitch(optionA, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(optionA, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", false, "a");
        writeableCommandLine.addOption(optionA);
        writeableCommandLine.addSwitch(optionA, true);
        writeableCommandLine.addSwitch(optionA, true);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", true, "a");
        writeableCommandLine.addOption(optionA);

        writeableCommandLine.addValue(optionA, "value1");
        List values = writeableCommandLine.getValues(optionA, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(optionA, "value2");
        values = writeableCommandLine.getValues(optionA, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        values = writeableCommandLine.getValues(optionA, defaultValues);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals(1, defaultValues.size());
        Assert.assertEquals("defaultValue1", defaultValues.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", true, "a");
        writeableCommandLine.addOption(optionA);

        writeableCommandLine.addValue(optionA, "value1");
        List values = writeableCommandLine.getUndefaultedValues(optionA);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", false, "a");
        writeableCommandLine.addOption(optionA);

        Assert.assertFalse(writeableCommandLine.getSwitch(optionA, false));
        writeableCommandLine.addSwitch(optionA, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(optionA, false));
    }

    @Test
    public void testGetSwitchWithDefault() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", false, "a");
        writeableCommandLine.addOption(optionA);

        Assert.assertFalse(writeableCommandLine.getSwitch(optionA, false));
        writeableCommandLine.addSwitch(optionA, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(optionA, false));

        Assert.assertNull(writeableCommandLine.getSwitch(optionA, true));
        writeableCommandLine.setDefaultSwitch(optionA, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(optionA, true));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", false, "a");
        writeableCommandLine.addOption(optionA);

        Map<String, String> properties = new HashMap<>();
        properties.put("property1", "value1");
        writeableCommandLine.addProperty(optionA, "property1", "value1");

        Map<String, String> propertiesResult = writeableCommandLine.getProperties(optionA);
        Assert.assertEquals(1, propertiesResult.size());
        Assert.assertEquals("value1", propertiesResult.get("property1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", false, "a");
        writeableCommandLine.addOption(optionA);
        writeableCommandLine.addSwitch(optionA, true);

        Option optionB = new Option("optionB", false, "b");
        writeableCommandLine.addOption(optionB);
        writeableCommandLine.addValue(optionB, "value1");

        Assert.assertEquals("optionA a optionB value1", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", false, "a");
        Option optionB = new Option("optionB", false, "b");
        writeableCommandLine.addOption(optionA);
        writeableCommandLine.addOption(optionB);

        List<Option> options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(optionA, options.get(0));
        Assert.assertEquals(optionB, options.get(1));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA"), new ArrayList());
        Option optionA = new Option("optionA", false, "a");
        Option optionB = new Option("optionB", false, "b");
        Option optionC = new Option("optionC", false, "c");
        writeableCommandLine.addOption(optionA);
        writeableCommandLine.addOption(optionB);
        writeableCommandLine.addOption(optionC);

        Set<String> optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("optionA", "optionB", "optionC")));
    }
}