import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOptionAndGetValues() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testAddOptionWithDefaultValue() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("o").longOpt("option").hasArg(false).build();
        commandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));
        commandLine.addValue(option, "userProvidedValue");

        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("userProvidedValue"), values);

        List<String> defaultValues = commandLine.getOptionDefaultValues(option);
        Assert.assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), defaultValues);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.setDefaultSwitch(option, false);
        commandLine.addSwitch(option, true);

        boolean switchValue = commandLine.getOptionValue(option, Boolean.TYPE);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("-o"));
        Assert.assertFalse(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        Option option = Option.builder("p").longOpt("property").build();

        commandLine.addProperty(option, "name", "value1");
        commandLine.addProperty(option, "name", "value2");

        String propertyValue = commandLine.getOptionValue(option, String.class, "name");
        Assert.assertEquals("value2", propertyValue);
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();

        commandLine.addProperty("name", "defaultValue1");
        String propertyValue = commandLine.getOptionValue(null, String.class, "name");
        Assert.assertEquals("defaultValue1", propertyValue);
    }

    @Test
    public void testParse() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        DefaultParser parser = new DefaultParser();

        List<Option> options = Arrays.asList(
                Option.builder("o").longOpt("option").hasArg(true).build(),
                Option.builder("s").longOpt("switch").build(),
                Option.builder("p").longOpt("property").hasArg(false).build()
        );

        parser.parse(commandLine, Arrays.asList("-o", "value1", "--switch", "--property", "value2", "--o", "value3"), options.toArray(new Option[0]));

        List<String> optionValues = commandLine.getUndefaultedValues(options.get(0));
        Assert.assertEquals(Arrays.asList("value1", "value3"), optionValues);

        boolean switchValue = commandLine.getOptionValue(options.get(1), Boolean.TYPE);
        Assert.assertTrue(switchValue);

        String propertyValue = commandLine.getOptionValue(options.get(2), String.class, null);
        Assert.assertEquals("value2", propertyValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidOption() {
        WriteableCommandLine commandLine = new DefaultWriteableCommandLine();
        DefaultParser parser = new DefaultParser();

        List<Option> options = Arrays.asList(
                Option.builder("o").longOpt("option").hasArg(true).build()
        );

        parser.parse(commandLine, Arrays.asList("-x"), options.toArray(new Option[0]));
    }
}