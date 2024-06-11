import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine writeableCommandLine;

    // Mock objects for testing
    @Mock
    private Option option;
    @Mock
    private List<Object> defaultValues;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        writeableCommandLine = new WriteableCommandLineImpl();
    }

    @After
    public void tearDown() {
        writeableCommandLine = null;
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        writeableCommandLine.addValue(option, value);

        Assert.assertTrue(writeableCommandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");

        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option1, "value2");

        writeableCommandLine.addValue(option2, "value3");
        writeableCommandLine.addValue(option2, "value4");

        List<String> expectedValues1 = new ArrayList<>();
        expectedValues1.add("value1");
        expectedValues1.add("value2");

        List<String> expectedValues2 = new ArrayList<>();
        expectedValues2.add("value3");
        expectedValues2.add("value4");

        Assert.assertEquals(expectedValues1, writeableCommandLine.getValues(option1));
        Assert.assertEquals(expectedValues2, writeableCommandLine.getValues(option2));
    }

    @Test
    public void testSetDefaultValues() {
        writeableCommandLine.setDefaultValues(option, defaultValues);

        Assert.assertEquals(defaultValues, writeableCommandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        writeableCommandLine.addSwitch(option, true);

        Assert.assertTrue(writeableCommandLine.getSwitches().contains(option));
        Assert.assertTrue(writeableCommandLine.getSwitchValue(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        Assert.assertEquals(defaultSwitch, writeableCommandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        writeableCommandLine.addProperty(property, value);

        Assert.assertEquals(value, writeableCommandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        Assert.assertTrue(writeableCommandLine.looksLikeOption(argument));
    }
}