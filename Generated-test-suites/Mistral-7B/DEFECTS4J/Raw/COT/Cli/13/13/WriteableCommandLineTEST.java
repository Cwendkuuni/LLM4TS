import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);

        commandLine.addOption(option);

        // Verify the call to addOption was made
        Mockito.verify(commandLine).addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        String value = "testValue";

        commandLine.addValue(option, value);

        // Verify the call to addValue was made with the correct arguments
        Mockito.verify(commandLine).addValue(option, eq(value));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        List values = Mockito.mock(List.class);

        when(commandLine.findOption(Mockito.anyString())).thenReturn(option);
        when(option.getValues()).thenReturn(values);

        List result = commandLine.getValues(option);

        // Verify the call to getValues was made and returned the expected value
        assertEquals(values, result);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        List defaultValues = Arrays.asList("default1", "default2");

        commandLine.setDefaultValues(option, defaultValues);

        // Verify the call to setDefaultValues was made with the correct arguments
        Mockito.verify(commandLine).setDefaultValues(option, eq(defaultValues));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchDuplicate() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);
        commandLine.addSwitch(option, value);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        boolean value = true;

        commandLine.addSwitch(option, value);

        // Verify the call to addSwitch was made with the correct arguments
        Mockito.verify(commandLine).addSwitch(option, eq(value));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        // Verify the call to setDefaultSwitch was made with the correct arguments
        Mockito.verify(commandLine).setDefaultSwitch(option, eq(defaultSwitch));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);

        commandLine.addProperty("property", "value");

        // Verify the call to addProperty was made with the correct arguments
        Mockito.verify(commandLine).addProperty(eq("property"), eq("value"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String argument = "argument";

        assertFalse(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String argument = "--option";

        assertTrue(commandLine.looksLikeOption(argument));
    }
}