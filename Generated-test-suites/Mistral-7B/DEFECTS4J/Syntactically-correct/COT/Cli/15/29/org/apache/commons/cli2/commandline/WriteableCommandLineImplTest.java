import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.exceptions.ConvertException;
import org.apache.commons.cli2.exceptions.InvalidArgumentValue;
import org.apache.commons.cli2.exceptions.InvalidOptionArgumentValue;
import org.apache.commons.cli2.exceptions.InvalidOptionDefinition;
import org.apache.commons.cli2.exceptions.InvalidOptionValue;
import org.apache.commons.cli2.exceptions.MissingArgumentException;
import org.apache.commons.cli2.exceptions.MissingOptionException;
import org.apache.commons.cli2.exceptions.NoSuchOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.commons.cli2.commandline.WriteableCommandLineImpl.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertEquals(option, writeableCommandLine.getOption("o"));
        Assert.assertEquals(option, writeableCommandLine.getOption("option"));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option[0], new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List valueList = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1"), valueList);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithAlreadySetSwitch() {
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
        Assert.assertEquals(option, writeableCommandLine.getOption("o"));
        Assert.assertEquals(option, writeableCommandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List valueList = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), valueList);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List valueList = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("value1"), valueList);
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, true);
        Boolean bool = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(bool);
    }

    @Test
    public void testgetProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");
        String property = writeableCommandLine.getProperty("property");
        Assert.assertEquals("value", property);
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("property", "value");
        Properties expectedProperties = new Properties();
        expectedProperties.putAll(properties);
        writeableCommandLine.addProperty(option, "property", "value");
        Properties actualProperties = (Properties) writeableCommandLine.getProperties(option);
        Assert.assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void testGetProperties() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("property1", "value1");
        properties.put("property2", "value2");
        Properties expectedProperties = new Properties();
        expectedProperties.putAll(properties);
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");
        Set<String> actualProperties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(expectedProperties.keySet(), actualProperties);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option[0], new ArrayList<>());
        Assert.assertFalse(writeableCommandLine.looksLikeOption(""));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("-o"));
    }

    @Test
    public void testToString() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        Assert.assertEquals("value1 option", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList(option2));
        writeableCommandLine.addOption(option1);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(Arrays.asList(option1, option2), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Arrays.asList(option2));
        writeableCommandLine.addOption(option1);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(Arrays.asList("o1", "option1"), optionTriggers);
    }

    @Test(expected = NoSuchOptionException.class)
    public void testGetOptionWithUnregisteredOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option[0], new ArrayList<>());
        writeableCommandLine.addProperty(option, "property", "value");
        writeableCommandLine.getOption("nonexistent");
    }

    @Test(expected = MissingOptionException.class)
    public void testGetOptionWithMissingOption() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option[0], new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.getOption("missing");
    }

    @Test(expected = MissingArgumentException.class)
    public void testGetValueWithMissingValue() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.getValue(option, "");
    }

    @Test(expected = MissingArgumentException.class)
    public void testGetValueWithMissingValueForProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        String property = writeableCommandLine.getProperty("property");
        writeableCommandLine.getProperty("nonexistent");
    }

    @Test(expected = InvalidArgumentValue.class)
    public void testAddValueWithInvalidValue() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        try {
            writeableCommandLine.addValue(option, new Object());
        } catch (Exception e) {
            Assert.assertEquals(InvalidArgumentValue.class, e.getClass());
        }
    }

    @Test(expected = InvalidArgumentValue.class)
    public void testAddValueWithInvalidValueForProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        try {
            writeableCommandLine.addProperty(option, "property", new Object());
        } catch (Exception e) {
            Assert.assertEquals(InvalidArgumentValue.class, e.getClass());
        }
    }

    @Test(expected = InvalidOptionArgumentValue.class)
    public void testAddValueWithInvalidOptionValue() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        try {
            writeableCommandLine.addValue(option, 5);
        } catch (Exception e) {
            Assert.assertEquals(InvalidOptionArgumentValue.class, e.getClass());
        }
    }

    @Test(expected = InvalidOptionArgumentValue.class)
    public void testAddPropertyWithInvalidOptionValue() {
        Option option = Option.builder("p").longOpt("property").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        try {
            writeableCommandLine.addProperty(option, "property", 5);
        } catch (Exception e) {
            Assert.assertEquals(InvalidOptionArgumentValue.class, e.getClass());
        }
    }

    @Test(expected = InvalidOptionDefinition.class)
    public void testAddOptionWithInvalidOption() {
        Option option = Option.builder("o").longOpt("").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        try {
            writeableCommandLine.addOption(option);
        } catch (Exception e) {
            Assert.assertEquals(InvalidOptionDefinition.class, e.getClass());
        }
    }

    @Test(expected = InvalidOptionDefinition.class)
    public void testAddPropertyWithInvalidProperty() {
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        try {
            writeableCommandLine.addProperty(option, "", "");
        } catch (Exception e) {
            Assert.assertEquals(InvalidOptionDefinition.class, e.getClass());
        }
    }
}