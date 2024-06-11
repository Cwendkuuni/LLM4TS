import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WriteableCommandLineImplTest {

    @Mock
    private Option mockOption;

    private WriteableCommandLineImpl writeableCommandLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        writeableCommandLine = new WriteableCommandLineImpl(mockOption, Arrays.asList("argument1", "argument2"));
    }

    @Test
    public void testAddOption() {
        writeableCommandLine.addOption(mockOption);

        assertEquals(1, writeableCommandLine.getOptions().size());
        assertEquals(mockOption, writeableCommandLine.getOptions().get(0));
    }

    @Test
    public void testGetOption() {
        writeableCommandLine.addOption(mockOption);

        Option result = writeableCommandLine.getOption("trigger");

        assertNull(result);

        writeableCommandLine.addOption(mockOption);
        writeableCommandLine.addOption(mockOption);
        result = writeableCommandLine.getOption("trigger");

        assertSame(mockOption, result);
    }

    @Test
    public void testHasOption() {
        writeableCommandLine.addOption(mockOption);

        assertTrue(writeableCommandLine.hasOption(mockOption));
    }

    @Test
    public void testGetValues() {
        writeableCommandLine.addValue(mockOption, "value1");
        writeableCommandLine.addValue(mockOption, "value2");

        List result = writeableCommandLine.getValues(mockOption, null);

        assertEquals(2, result.size());
        assertEquals("value1", result.get(0));
        assertEquals("value2", result.get(1));
    }

    @Test
    public void testGetSwitch() {
        writeableCommandLine.addSwitch(mockOption, true);

        assertTrue(writeableCommandLine.getSwitch(mockOption, false));
    }

    @Test
    public void testAddProperty() {
        writeableCommandLine.addProperty("property", "value");

        Properties result = writeableCommandLine.getProperties();

        assertEquals("value", result.getProperty("property"));
    }

    @Test
    public void testgetProperty() {
        writeableCommandLine.addProperty("property", "value");

        String result = writeableCommandLine.getProperty("property", "defaultValue");

        assertEquals("value", result);
    }

    @Test
    public void testGetProperties() {
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        Set result = writeableCommandLine.getProperties();

        assertEquals(2, result.size());
        assertTrue(result.contains("property1"));
        assertTrue(result.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(writeableCommandLine.looksLikeOption("option"));
        assertFalse(writeableCommandLine.looksLikeOption("argument"));
    }

    @Test
    public void testToString() {
        String result = writeableCommandLine.toString();

        assertEquals("argument1 argument2", result);
    }

    @Test
    public void testGetOptionTriggers() {
        writeableCommandLine.addOption(mockOption);
        writeableCommandLine.addOption(mockOption);

        Set result = writeableCommandLine.getOptionTriggers();

        assertEquals(2, result.size());
        assertTrue(result.contains("trigger"));
        assertTrue(result.contains("trigger2"));
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        writeableCommandLine.setDefaultValues(mockOption, defaultValues);

        List result = writeableCommandLine.getValues(mockOption, null);

        assertEquals(2, result.size());
        assertEquals("defaultValue1", result.get(0));
        assertEquals("defaultValue2", result.get(1));

        verify(mockOption).getDefaultValues(writeableCommandLine, null);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(mockOption, defaultSwitch);

        assertTrue(writeableCommandLine.getSwitch(mockOption, false));

        verify(mockOption).getDefaultSwitch(writeableCommandLine, false);
    }
}