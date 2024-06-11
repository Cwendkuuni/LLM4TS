import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() throws Exception {
        Option rootOption = new OptionBuilder().withPreferredName("root").create();
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @After
    public void tearDown() throws Exception {
        commandLine = null;
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        assertEquals(expected, commandLine.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        assertEquals(expected, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property, "defaultValue"));
    }

    @Test
    public void testGetPropertyWithoutDefault() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        String property = "test";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, ""));
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        String property = "test";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Set<String> expected = new HashSet<>();
        expected.add(property);
        assertEquals(expected, commandLine.getProperties(option));
    }

    @Test
    public void testGetPropertiesWithoutOption() {
        String property = "test";
        String value = "value";
        commandLine.addProperty(property, value);
        Set<String> expected = new HashSet<>();
        expected.add(property);
        assertEquals(expected, commandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
    }

    @Test
    public void testToString() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        String expected = "value1 value2";
        assertEquals(expected, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);
        List<Option> expected = new ArrayList<>();
        expected.add(option);
        assertEquals(expected, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);
        Set<String> expected = new HashSet<>();
        expected.add("test");
        assertEquals(expected, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        List<String> defaults = new ArrayList<>();
        defaults.add("value1");
        defaults.add("value2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.setDefaultSwitch(option, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");
        assertEquals(expected, commandLine.getNormalised());
    }
}