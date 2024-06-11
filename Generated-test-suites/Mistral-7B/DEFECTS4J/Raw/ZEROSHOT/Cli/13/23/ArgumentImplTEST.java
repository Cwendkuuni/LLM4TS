import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_MISSING_VALUES;
import static org.apache.commons.cli2.resource.ResourceConstants.ARGUMENT_UNEXPECTED_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ArgumentImpl.class})
public class ArgumentImplTest {

    private Argument argument;
    private WriteableCommandLine commandLine;
    private List defaultValues;
    private Option option;

    @Before
    public void setUp() {
        option = PowerMockito.mock(Option.class);
        commandLine = PowerMockito.mock(WriteableCommandLine.class);
        defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        argument = new ArgumentImpl("test", "Test Argument", 1, 3, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", defaultValues, 0);
    }

    @Test
    public void getPreferredName() {
        assertEquals("test", argument.getPreferredName());
    }

    @Test
    public void processValues() throws OptionException {
        PowerMockito.when(commandLine.addValue(option, (Object) "value1"))
                .thenReturn(null);
        PowerMockito.when(commandLine.addValue(option, (Object) "value2"))
                .thenReturn(null);
        PowerMockito.when(commandLine.addValue(option, (Object) "value3"))
                .thenThrow(new OptionException(option, ARGUMENT_MISSING_VALUES, "value3"));

        argument.processValues(commandLine, commandLine.listIterator(), option);

        assertEquals(2, commandLine.getValues(option).size());
        assertSame("value1", commandLine.getValues(option).get(0));
        assertSame("value2", commandLine.getValues(option).get(1));
    }

    @Test(expected = OptionException.class)
    public void processValuesUnsupportedNumberOfValues() throws OptionException {
        PowerMockito.when(commandLine.addValue(option, (Object) "value1"))
                .thenReturn(null);
        PowerMockito.when(commandLine.addValue(option, (Object) "value2"))
                .thenReturn(null);
        PowerMockito.when(commandLine.addValue(option, (Object) "value3"))
                .thenReturn(null);
        PowerMockito.when(commandLine.addValue(option, (Object) "value4"))
                .thenThrow(new OptionException(option, ARGUMENT_UNEXPECTED_VALUE, "value4"));

        argument.processValues(commandLine, commandLine.listIterator(), option);
    }

    @Test
    public void canProcess() {
        assertTrue(argument.canProcess(commandLine, "value"));
    }

    @Test
    public void getPrefixes() {
        Set prefixes = argument.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void process() throws OptionException {
        PowerMockito.when(commandLine.addValue(option, (Object) "value1"))
                .thenReturn(null);
        PowerMockito.when(commandLine.addValue(option, (Object) "value2"))
                .thenReturn(null);
        PowerMockito.when(commandLine.addValue(option, (Object) "value3"))
                .thenThrow(new OptionException(option, ARGUMENT_MISSING_VALUES, "value3"));

        argument.process(commandLine, commandLine.listIterator());

        assertEquals(2, commandLine.getValues(option).size());
        assertSame("value1", commandLine.getValues(option).get(0));
        assertSame("value2", commandLine.getValues(option).get(1));
    }

    @Test
    public void getInitialSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    @Test
    public void getSubsequentSeparator() {
        assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    @Test
    public void getTriggers() {
        Set triggers = argument.getTriggers();
        assertTrue(triggers.isEmpty());
    }

    @Test
    public void getConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void getDefaultValues() {
        assertEquals(defaultValues, argument.getDefaultValues());
    }

    @Test
    public void getValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void validate() throws OptionException {
        PowerMockito.when(commandLine.getValues(option)).thenReturn(defaultValues);

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void validateInvalidValues() throws OptionException {
        PowerMockito.when(commandLine.getValues(option)).thenReturn(List.of("value1", "value3"));

        argument.validate(commandLine);
    }

    @Test
    public void appendUsage() {
        StringBuffer buffer = new StringBuffer();

        argument.appendUsage(buffer, null, null);

        assertEquals("test [--] [value]", buffer.toString());
    }

    @Test
    public void getDescription() {
        assertEquals("Test Argument", argument.getDescription());
    }

    @Test
    public void helpLines() {
        HelpLine helpLine = argument.helpLines(1, null, null).get(0);

        assertEquals("test [--]\n\t[value]", helpLine.getOpt());
        assertEquals("Test Argument", helpLine.getLongOpt());
        assertEquals("", helpLine.getDescription());
    }

    @Test
    public void getMaximum() {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void getMinimum() {
        assertEquals(1, argument.getMinimum());
    }
}