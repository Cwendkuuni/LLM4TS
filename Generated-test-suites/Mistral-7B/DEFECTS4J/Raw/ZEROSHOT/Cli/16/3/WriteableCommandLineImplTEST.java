import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Option optionFromMap = writeableCommandLine.getOption("o");
        Assert.assertEquals(option, optionFromMap);
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List<Object> values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_HasOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        boolean hasOption = writeableCommandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testWriteableCommandLineImpl_GetOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Option optionFromMap = writeableCommandLine.getOption("o");
        Assert.assertEquals(option, optionFromMap);
    }

    @Test
    public void testWriteableCommandLineImpl_GetValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List<Object> values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        List<Object> values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_GetSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperty() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value1");

        String propertyValue = writeableCommandLine.getProperty(option, "property", "defaultValue");
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_AddProperty() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        writeableCommandLine.addProperty(option, "property", properties);

        Map<String, String> propertiesMap = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, propertiesMap.size());
        Assert.assertEquals("value1", propertiesMap.get("property1"));
        Assert.assertEquals("value2", propertiesMap.get("property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperties() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        writeableCommandLine.addProperty(option, "property", properties);

        Set<String> propertiesNames = writeableCommandLine.getProperties(option).keySet();
        Assert.assertEquals(2, propertiesNames.size());
        Assert.assertTrue(propertiesNames.containsAll(Arrays.asList("property1", "property2")));
    }

    @Test
    public void testWriteableCommandLineImpl_LooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Assert.assertFalse(writeableCommandLine.looksLikeOption("option"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("--option"));

        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.looksLikeOption("o"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("switch"));
    }

    @Test
    public void testWriteableCommandLineImpl_ToString() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        String commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("-o value1", commandLineString);

        option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("-o value1 value2", commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptions() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();

        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        List<Option> options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();

        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set<String> optionsTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionsTriggers.size());
        Assert.assertTrue(optionsTriggers.containsAll(Arrays.asList("o1", "o2")));
    }

    @Test
    public void testWriteableCommandLineImpl_SetDefaultValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        List<String> defaultValues = Arrays.asList("value1", "value2");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        List<Object> values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testWriteableCommandLineImpl_SetDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        writeableCommandLine.setDefaultSwitch(option, false);
        switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_GetNormalised() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List<String> normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("value1", normalised.get(0));
        Assert.assertEquals("value2", normalised.get(1));
    }
}