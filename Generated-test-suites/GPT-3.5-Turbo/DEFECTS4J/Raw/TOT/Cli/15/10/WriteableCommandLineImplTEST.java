import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl cmdLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionBuilder().withName("rootOption").create();
        List arguments = new ArrayList();
        cmdLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    /**
     * Test for addOption() method.
     * Test if option is added correctly to the list and map.
     */
    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withName("testOption").create();
        cmdLine.addOption(option);
        assertTrue(cmdLine.getOptions().contains(option));
        assertEquals(option, cmdLine.getOption("testOption"));
    }

    /**
     * Test for addValue() method.
     * Test if value is added correctly to the option.
     */
    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withName("testOption").create();
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        List values = cmdLine.getValues(option, null);
        assertTrue(values.contains("value1"));
    }

    /**
     * Test for addSwitch() method.
     * Test if switch is added correctly to the option.
     */
    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withName("testOption").create();
        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, false));
    }

    /**
     * Test for hasOption() method.
     * Test if the option is present or not.
     */
    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withName("testOption").create();
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }

    /**
     * Test for getProperty() method.
     * Test if the property value can be retrieved correctly.
     */
    @Test
    public void testGetProperty() {
        Option option = new OptionBuilder().withName("testOption").create();
        cmdLine.addProperty(option, "property1", "value1");
        assertEquals("value1", cmdLine.getProperty(option, "property1", null));
    }

    /**
     * Test for toString() method.
     * Test if the command line arguments are converted to string correctly.
     */
    @Test
    public void testToString() {
        Option option = new OptionBuilder().withName("testOption").create();
        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");
        cmdLine.addValue(option, "value3");
        cmdLine.addOption(new OptionBuilder().withName("option2").create());
        
        assertEquals("value1 value2 value3", cmdLine.toString());
    }

    /**
     * Test for getOptions() method.
     * Test if the list of options is returned correctly.
     */
    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withName("testOption1").create();
        Option option2 = new OptionBuilder().withName("testOption2").create();
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        assertTrue(cmdLine.getOptions().contains(option1));
        assertTrue(cmdLine.getOptions().contains(option2));
    }

    /**
     * Test for getOptionTriggers() method.
     * Test if the set of option triggers is returned correctly.
     */
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withName("testOption1").create();
        Option option2 = new OptionBuilder().withName("testOption2").create();
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        assertTrue(cmdLine.getOptionTriggers().contains("testOption1"));
        assertTrue(cmdLine.getOptionTriggers().contains("testOption2"));
    }

    /**
     * Test for setDefaultValues() method.
     * Test if the default values are set correctly for the option.
     */
    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withName("testOption").create();
        List defaults = Arrays.asList("default1", "default2");
        cmdLine.setDefaultValues(option, defaults);
        assertEquals(defaults, cmdLine.getValues(option, null));
    }

    /**
     * Test for setDefaultSwitch() method.
     * Test if the default switch is set correctly for the option.
     */
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withName("testOption").create();
        cmdLine.setDefaultSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, false));
    }

    /**
     * Test for getNormalised() method.
     * Test if the list of normalised arguments is returned correctly.
     */
    @Test
    public void testGetNormalised() {
        Option option1 = new OptionBuilder().withName("testOption1").create();
        Option option2 = new OptionBuilder().withName("testOption2").create();
        cmdLine.addValue(option1, "value1");
        cmdLine.addSwitch(option2, true);
        List normalised = cmdLine.getNormalised();
        assertTrue(normalised.contains("value1"));
        assertTrue(normalised.contains("option2"));
    }
}