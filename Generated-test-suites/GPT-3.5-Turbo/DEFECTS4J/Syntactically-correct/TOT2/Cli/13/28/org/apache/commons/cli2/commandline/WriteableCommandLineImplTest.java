import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private Option option;
    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setUp() {
        // Initialize Option
        option = new Argument();
        writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList());
    }

    @Test
    public void testAddOption() {
        Option newOption = new Argument();
        writeableCommandLine.addOption(newOption);
        assertTrue(writeableCommandLine.getOptions().contains(newOption));
    }

    @Test
    public void testAddValue() {
        Object value = "value";
        writeableCommandLine.addValue(option, value);
        assertTrue(writeableCommandLine.getValues(option, new ArrayList()).contains(value));
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        assertEquals(option, writeableCommandLine.getOption("trigger"));
    }

    @Test
    public void testGetValues() {
        List valueList = new ArrayList();
        writeableCommandLine.addValue(option, "value");
        assertEquals(valueList, writeableCommandLine.getValues(option, valueList));
    }

    @Test
    public void testGetSwitch() {
        writeableCommandLine.addSwitch(option, true);
        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty("key", "value");
        assertEquals("value", writeableCommandLine.getProperty("key", ""));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("-trigger"));
    }

    @Test
    public void testToString() {
        writeableCommandLine.addValue(option, "value");
        assertEquals("value", writeableCommandLine.toString());
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = new ArrayList();
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getValues(option, defaultValues));
    }

    @Test
    public void testSetDefaultSwitch() {
        writeableCommandLine.setDefaultSwitch(option, true);
        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        List normalisedList = new ArrayList();
        assertEquals(normalisedList, writeableCommandLine.getNormalised());
    }
}