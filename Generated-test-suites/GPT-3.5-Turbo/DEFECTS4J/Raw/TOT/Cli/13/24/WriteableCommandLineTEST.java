import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("a", "alpha", true, "Alpha option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption("a"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("b", "beta", true, "Beta option");
        commandLine.addOption(option);

        Integer value = Integer.valueOf(10);
        commandLine.addValue(option, value);

        List<Object> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals(value, values.get(0));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("c", "gamma", true, "Gamma option");
        commandLine.addOption(option);

        Integer value1 = Integer.valueOf(10);
        commandLine.addValue(option, value1);

        Integer value2 = Integer.valueOf(20);
        commandLine.addValue(option, value2);

        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("d", "delta", true, "Delta option");
        commandLine.addOption(option);

        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        List<Object> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals(defaultValues.get(0), values.get(0));
        assertEquals(defaultValues.get(1), values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("e", "epsilon", false, "Epsilon option");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.hasSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("f", "zeta", true, "Zeta option");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));

        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.hasSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "foo";
        String value = "bar";

        commandLine.addProperty(property, value);

        assertTrue(commandLine.hasProperty(property));
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-a"));
        assertTrue(commandLine.looksLikeOption("--alpha"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}