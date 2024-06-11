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
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "output file");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        // test addOption method
        Option option1 = new Option("i", "input", true, "input file");
        writeableCommandLine.addOption(option1);
        Assert.assertTrue(writeableCommandLine.hasOption(option1));
        Assert.assertEquals(option1, writeableCommandLine.getOption("i"));

        // test addValue method
        writeableCommandLine.addValue(option, "test.txt");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test.txt", values.get(0));

        // test addSwitch method
        writeableCommandLine.addSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));

        // test getOption method
        Option option2 = new Option("t", "type", true, "output type");
        writeableCommandLine.addOption(option2);
        Assert.assertEquals(option2, writeableCommandLine.getOption("t"));

        // test getValues method
        writeableCommandLine.addValue(option, "test1.txt");
        writeableCommandLine.addValue(option, "test2.txt");
        values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1.txt", values.get(0));
        Assert.assertEquals("test2.txt", values.get(1));

        // test getUndefaultedValues method
        values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1.txt", values.get(0));
        Assert.assertEquals("test2.txt", values.get(1));

        // test getSwitch method
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));

        // test getProperty method
        writeableCommandLine.addProperty(option, "prop", "value");
        String property = writeableCommandLine.getProperty(option, "prop");
        Assert.assertEquals("value", property);

        // test getProperty method with default value
        String propertyDefault = writeableCommandLine.getProperty(option, "propDefault");
        Assert.assertNull(propertyDefault);

        // test addProperty method
        writeableCommandLine.addProperty(option, "prop2", "value2");
        Map properties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("prop", properties.keySet().iterator().next());
        Assert.assertEquals("value", properties.get("prop"));

        // test getProperties method
        Map optionsProperties = writeableCommandLine.getProperties();
        Assert.assertEquals(1, optionsProperties.size());
        Assert.assertEquals(option, optionsProperties.keySet().iterator().next());
        Assert.assertEquals(1, optionsProperties.get(option).size());
        Assert.assertEquals("prop", optionsProperties.get(option).keySet().iterator().next());
        Assert.assertEquals("value", optionsProperties.get(option).get("prop"));

        // test looksLikeOption method
        Assert.assertTrue(writeableCommandLine.looksLikeOption("o"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("a"));

        // test toString method
        String toString = writeableCommandLine.toString();
        Assert.assertEquals("test1.txt test2.txt -o test.txt -t type", toString);

        // test getOptions method
        List options = writeableCommandline.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertTrue(options.get(0) instanceof Option);
        Assert.assertTrue(options.get(1) instanceof Option);
        Assert.assertTrue(options.get(2) instanceof Option);

        // test getOptionTriggers method
        Set optionTriggers = writeableCommandline.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("i"));
        Assert.assertTrue(optionTriggers.contains("o"));
        Assert.assertTrue(optionTriggers.contains("t"));

        // test setDefaultValues method
        writeableCommandline.setDefaultValues(option, new ArrayList());
        values = writeableCommandline.getValues(option, null);
        Assert.assertEquals(0, values.size());

        // test setDefaultSwitch method
        writeableCommandline.setDefaultSwitch(option, null);
        Assert.assertFalse(writeableCommandline.getSwitch(option, false));

        // test getNormalised method
        List normalised = writeableCommandline.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("test1.txt", normalised.get(0));
        Assert.assertEquals("test2.txt", normalised.get(1));
        Assert.assertEquals("-o test.txt -t type", normalised.get(2));
    }
}