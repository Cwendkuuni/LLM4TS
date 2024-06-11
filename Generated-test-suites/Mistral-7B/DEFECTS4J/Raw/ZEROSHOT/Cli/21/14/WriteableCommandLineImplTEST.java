import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
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
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("t", "test", false, "Test option");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());

        Option option1 = new Option("i", "inputFile", true, "Input file");
        Argument arg1 = new Argument("1").required(true).name("file1");
        option1.addArgument(arg1);
        commandLine.addOption(option1);

        Option option2 = new Option("o", "outputFile", true, "Output file");
        Argument arg2 = new Argument("2").required(true).name("file2");
        option2.addArgument(arg2);
        commandLine.addOption(option2);

        Option option3 = new Option("h", "help", false, "Print this help message.");
        commandLine.addOption(option3);

        commandLine.addSwitch(option1, true);
        commandLine.addValue(option1, "testInput.txt");
        commandLine.addValue(option1, "testInput2.txt");
        commandLine.addValue(option2, "testOutput.txt");
        commandLine.addSwitch(option3, false);

        // Test hasOption
        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertTrue(commandLine.hasOption(option2));
        Assert.assertFalse(commandLine.hasOption(new Option("nonexistent")));

        // Test getOption
        Option retrievedOption1 = commandLine.getOption("i");
        Option retrievedOption2 = commandLine.getOption("o");
        Assert.assertEquals(option1, retrievedOption1);
        Assert.assertEquals(option2, retrievedOption2);

        // Test getValues
        List valuesOption1 = commandLine.getValues(option1, null);
        List valuesOption2 = commandLine.getValues(option2, null);
        List undefaultedValuesOption1 = commandLine.getUndefaultedValues(option1);
        Assert.assertEquals(2, valuesOption1.size());
        Assert.assertEquals("testInput.txt", valuesOption1.get(0));
        Assert.assertEquals("testInput2.txt", valuesOption1.get(1));
        Assert.assertEquals(1, valuesOption2.size());
        Assert.assertEquals("testOutput.txt", valuesOption2.get(0));
        Assert.assertEquals(0, undefaultedValuesOption1.size());

        // Test getSwitch
        Boolean switchOption1 = commandLine.getSwitch(option1, null);
        Boolean switchOption3 = commandLine.getSwitch(option3, null);
        Assert.assertTrue(switchOption1);
        Assert.assertFalse(switchOption3);

        // Test getProperty
        commandLine.addProperty(option1, "input", "inputValue");
        String propertyOption1 = commandLine.getProperty(option1, "input", "defaultValue");
        Assert.assertEquals("inputValue", propertyOption1);

        // Test getProperties
        Set propertiesOption1 = commandLine.getProperties(option1);
        Assert.assertEquals(1, propertiesOption1.size());

        // Test looksLikeOption
        Assert.assertTrue(commandLine.looksLikeOption("i"));
        Assert.assertFalse(commandLine.looksLikeOption("x"));

        // Test toString
        Assert.assertEquals("i:testInput.txt testInput2.txt o:testOutput.txt", commandLine.toString());

        // Test getOptions
        List options = commandLine.getOptions();
        Assert.assertEquals(3, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
        Assert.assertEquals(option3, options.get(2));

        // Test getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());

        // Test setDefaultValues
        List defaultValuesOption1 = new ArrayList<>();
        defaultValuesOption1.add("defaultInputFile1.txt");
        defaultValuesOption1.add("defaultInputFile2.txt");
        commandLine.setDefaultValues(option1, defaultValuesOption1);
        List retrievedDefaultValuesOption1 = commandLine.getValues(option1, null);
        Assert.assertEquals(4, retrievedDefaultValuesOption1.size());

        // Test setDefaultSwitch
        commandLine.setDefaultSwitch(option3, true);
        Boolean retrievedSwitchOption3 = commandLine.getSwitch(option3, null);
        Assert.assertTrue(retrievedSwitchOption3);

        // Test getNormalised
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("i:testInput.txt testInput2.txt o:testOutput.txt", commandLine.toString());
    }
}