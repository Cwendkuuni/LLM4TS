import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine cmd;

    @Before
    public void setUp() {
        cmd = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("test");
        cmd.addOption(option);
        List<Option> options = cmd.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("test");
        cmd.addOption(option);

        Object value = "testValue";
        cmd.addValue(option, value);

        List<Object> values = cmd.getValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("test");
        cmd.addOption(option);

        Object value1 = "testValue1";
        cmd.addValue(option, value1);

        Object value2 = "testValue2";
        cmd.addValue(option, value2);

        List<Object> undefaultedValues = cmd.getUndefaultedValues(option);
        assertTrue(undefaultedValues.contains(value1));
        assertTrue(undefaultedValues.contains(value2));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("test");
        cmd.addOption(option);

        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue");
        cmd.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, cmd.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("test");
        cmd.addOption(option);

        boolean value = true;
        cmd.addSwitch(option, value);

        assertTrue(cmd.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("test");
        cmd.addOption(option);

        Boolean defaultSwitch = true;
        cmd.setDefaultSwitch(option, defaultSwitch);

        assertEquals(defaultSwitch, cmd.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("test");
        cmd.addOption(option);

        String property = "testProperty";
        String value = "testValue";
        cmd.addProperty(option, property, value);

        assertEquals(value, cmd.getProperty(option, property));
    }

    @Test
    public void testAddDefaultProperty() {
        String property = "testProperty";
        String value = "testValue";
        cmd.addProperty(property, value);

        assertEquals(value, cmd.getDefaultProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-test";
        assertTrue(cmd.looksLikeOption(argument));
    }

    @Test
    public void testGetCurrentOption() {
        Option option = new OptionImpl("test");
        cmd.setCurrentOption(option);

        assertEquals(option, cmd.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new OptionImpl("test");
        cmd.setCurrentOption(option);

        assertEquals(option, cmd.getCurrentOption());
    }

}