import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the WriteableCommandLine interface
 */
public class WriteableCommandLineTest {

    /**
     * Tests the addOption method
     */
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptionByName("option"));
    }

    /**
     * Tests the addValue method
     */
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator(' ')
                .required(true)
                .build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals( Arrays.asList("value1", "value2"), values);
    }

    /**
     * Tests the getUndefaultedValues method
     */
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("o").longOpt("option")
                .build();
        commandLine.addOption(option);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertTrue(values.isEmpty());
    }

    /**
     * Tests the setDefaultValues method
     */
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("o").longOpt("option")
                .build();
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(defaultValues, values);
    }

    /**
     * Tests the addSwitch method
     */
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("s").longOpt("switch")
                .build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.isSet());
        try {
            commandLine.addSwitch(option, true);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Tests the setDefaultSwitch method
     */
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("s").longOpt("switch")
                .build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.isSet());
        OptionParser parser = new DefaultParser();
        WriteableCommandLine parsedCommandLine = parser.parse(commandLine.getSpec(), new String[0]);
        Assert.assertTrue(parsedCommandLine.getOptionByName("switch").isSet());
    }

    /**
     * Tests the addProperty method
     */
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("p").longOpt("property")
                .build();
        commandLine.addProperty(option, "propertyName", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getOptionByName("property").getValue());
    }

    /**
     * Tests the addProperty method with default property
     */
    @Test
    public void testAddPropertyWithDefault() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.addProperty("propertyName", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getPropertyValues("propertyName").get(0));
    }

    /**
     * Tests the looksLikeOption method
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    /**
     * Tests the getCurrentOption method
     */
    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option = Option.builder("o").longOpt("option")
                .build();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }

    /**
     * Tests the setCurrentOption method
     */
    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new DefaultCommandLine();
        Option option1 = Option.builder("o1").longOpt("option1")
                .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                .build();
        commandLine.setCurrentOption(option1);
        commandLine.setCurrentOption(option2);
        Assert.assertEquals(option2, commandLine.getCurrentOption());
    }
}