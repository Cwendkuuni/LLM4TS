import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.cli2.CommandLineParser.parse;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithSwitchAlreadySet() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option retrievedOption = writeableCommandLine.getOption("o");
        Assert.assertSame(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addProperty(option, "property", "value");
        String propertyValue = writeableCommandLine.getProperty(option, "property", null);
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");
        Set properties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Assert.assertFalse(writeableCommandLine.looksLikeOption("option"));
        writeableCommandLine.addOption(Option.builder("o").longOpt("option").build());
        Assert.assertTrue(writeableCommandLine.looksLikeOption("o"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("option"));
    }

    @Test
    public void testToString() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        String toString = writeableCommandLine.toString();
        String expected = "option value1 value2";
        Assert.assertEquals(expected, toString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("o1"));
        Assert.assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testParseCommandLineWithValidOptions() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        CommandLine parsedCommandLine = parse(option.getParser(),
                new String[]{"o", "option1", "value1", "value2"});
        WriteableCommandLine parsedWriteableCommandLine = (WriteableCommandLine) parsedCommandLine;
        Assert.assertEquals(writeableCommandLine.toString(), parsedWriteableCommandLine.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCommandLineWithUnknownOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        CommandLine parsedCommandLine = parse(option.getParser(),
                new String[]{"u", "unknownOption"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCommandLineWithInvalidOptionValue() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        CommandLine parsedCommandLine = parse(option.getParser(),
                new String[]{"o", "option1", "invalidValue"});
    }

    @Test
    public void testParseCommandLineWithOptionSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        CommandLine parsedCommandLine = parse(option.getParser(),
                new String[]{"s"});
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Boolean parsedSwitchValue = parsedCommandLine.getValue(option);
        Assert.assertTrue(switchValue.equals(parsedSwitchValue));
    }

    @Test
    public void testParseCommandLineWithOptionValue() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        CommandLine parsedCommandLine = parse(option.getParser(),
                new String[]{"o", "option1", "value1"});
        String value1 = writeableCommandLine.getValues(option, null).get(0);
        String parsedValue1 = parsedCommandLine.getValue(option);
        Assert.assertEquals(value1, parsedValue1);
    }

    @Test
    public void testParseCommandLineWithOptionAndSwitch() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        CommandLine parsedCommandLine = parse(option.getParser(),
                new String[]{"o", "option1", "value1", "--switch"});
        String value1 = writeableCommandLine.getValues(option, null).get(0);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        String parsedValue1 = parsedCommandLine.getValue(option);
        Boolean parsedSwitchValue = parsedCommandLine.getValue(option, true);
        Assert.assertEquals(value1, parsedValue1);
        Assert.assertEquals(switchValue, parsedSwitchValue);
    }

    @Test
    public void testParseCommandLineWithMultipleOptions() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option2);
        CommandLine parsedCommandLine = parse(option1.getParser(),
                new String[]{"o1", "option11", "o2", "option21"});
        WriteableCommandLine parsedWriteableCommandLine = (WriteableCommandLine) parsedCommandLine;
        Assert.assertEquals(writeableCommandLine.toString(), parsedWriteableCommandLine.toString());
    }
}