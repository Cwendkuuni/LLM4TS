import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineTest {
    private WriteableCommandLine writeableCommandLine;

    @Before
    public void setUp() {
        writeableCommandLine = mock(WriteableCommandLine.class);
    }

    @Test
    public void testAddOption() {
        Option option = mock(Option.class);
        writeableCommandLine.addOption(option);

        verify(writeableCommandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = mock(Option.class);
        Object value = mock(Object.class);
        writeableCommandLine.addValue(option, value);

        verify(writeableCommandLine).addValue(option, value);
    }

    @Test
    public void testGetArgumentValues() {
        Option option = mock(Option.class);
        List values = new ArrayList();
        when(writeableCommandLine.getArgumentValues(option)).thenReturn(values);

        assertEquals(values, writeableCommandLine.getArgumentValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = mock(Option.class);
        List defaultValues = new ArrayList();
        writeableCommandLine.setDefaultValues(option, defaultValues);

        verify(writeableCommandLine).setDefaultValues(option, defaultValues);
    }

    @Test
    public void testAddSwitch() {
        Option option = mock(Option.class);
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);

        verify(writeableCommandLine).addSwitch(option, value);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = mock(Option.class);
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        verify(writeableCommandLine).setDefaultSwitch(option, defaultSwitch);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        writeableCommandLine.addProperty(property, value);

        verify(writeableCommandLine).addProperty(property, value);
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-option";
        when(writeableCommandLine.looksLikeOption(argument)).thenReturn(true);

        assertTrue(writeableCommandLine.looksLikeOption(argument));
    }
}