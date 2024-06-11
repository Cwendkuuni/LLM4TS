import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private List<String> defaultValues;

    @Before
    public void setUp() {
        commandLine = mock(WriteableCommandLine.class);
        option = mock(Option.class);
        defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        verify(commandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value");
        verify(commandLine).addValue(option, "value");
    }

    @Test
    public void testRetrieveArgumentValues() {
        List<String> values = commandLine.retrieveArgumentValues(option);
        // assert that the returned values are not null
        assert values != null;
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option, defaultValues);
        verify(commandLine).setDefaultValues(option, defaultValues);
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        verify(commandLine).addSwitch(option, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() throws IllegalStateException {
        // simulate adding the switch twice
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        verify(commandLine).setDefaultSwitch(option, true);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        verify(commandLine).addProperty("property", "value");
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("argument"));
    }
}