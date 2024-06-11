import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {
    @Mock
    private WriteableCommandLine commandLine;

    @Mock
    private Option option;

    private Argument argument;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        argument = new ArgumentImpl("", "", 0, 1, ' ', ' ', null, "--", null, 0);
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(' ', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertNull(argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, ""));
    }

    @Test
    public void testGetMaximum() {
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        assertEquals("foo", argument.stripBoundaryQuotes("\"foo\""));
        assertEquals("foo ", argument.stripBoundaryQuotes("\"foo\" "));
        assertEquals(" foo", argument.stripBoundaryQuotes(" foo\""));
        assertEquals("\"foo", argument.stripBoundaryQuotes("\"foo"));
        assertEquals("foo", argument.stripBoundaryQuotes("foo"));
    }

    @Test
    public void testIsRequired() {
        assertEquals(false, argument.isRequired());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDefaults() {
        argument.defaultValues(commandLine, option);

        assertEquals(null, commandLine.getDefaultValues(option));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentImplThrowsIllegalArgumentExceptionForInvalidMinimumAndMaximum() {
        new ArgumentImpl("", "", 2, 1, ' ', ' ', null, "--", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentImplThrowsIllegalArgumentExceptionForTooFewDefaultValues() {
        new ArgumentImpl("", "", 2, 3, ' ', ' ', null, "--", Collections.singletonList(""), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentImplThrowsIllegalArgumentExceptionForTooManyDefaultValues() {
        new ArgumentImpl("", "", 0, 2, ' ', ' ', null, "--", Collections.nCopies(3, ""), 0);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testProcessValues() {
        argument.processValues(commandLine, Collections.singletonList("value").listIterator(), option);

        assertEquals("value", commandLine.getValues(option).get(0));
    }

    @Test
    public void testProcessValuesForConsumeRemainingOption() {
        argument = new ArgumentImpl("", "", 0, Integer.MAX_VALUE, ' ', ' ', null, "--", null, 0);

        argument.processValues(commandLine, Collections.singletonList("--").listIterator(), option);

        argument.processValues(commandLine, Collections.singletonList("value").listIterator(), option);

        assertEquals("value", commandLine.getValues(option).get(0));
        assertEquals(1, commandLine.getValues(option).size());
    }

    @Test
    public void testProcessValuesForLooksLikeOption() {
        OngoingStubbing<Boolean> thenReturn = org.mockito.Mockito.when(commandLine.looksLikeOption("--")).thenReturn(true);
        OngoingStubbing thenReturn1 = org.mockito.Mockito.when(commandLine.looksLikeOption("")).thenReturn(false);

        argument.processValues(commandLine, Collections.singletonList("--").listIterator(), option);

        thenReturn1.thenReturn(false).thenThrow(new AssertionError(ResourceConstants.ARGUMENT_UNEXPECTED_VALUE));

        assertTrue(argument.canProcess(commandLine, "value"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testProcessValuesForSubsequentSplit() {
        argument = new ArgumentImpl("", "", 0, Integer.MAX_VALUE, ' ', ',', null, "--", null, 0);

        argument.processValues(commandLine, Collections.singletonList("value1,value2").listIterator(), option);

        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));
        assertEquals(2, commandLine.getValues(option).size());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = OptionException.class)
    public void testProcessValuesIfThereAreMoreValuesThanMaximum() {
        argument.processValues(commandLine, Collections.singletonList("value1,value2").listIterator(), option);

        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));
        assertEquals(2, commandLine.getValues(option).size());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = OptionException.class)
    public void testValidateThrowsOptionExceptionForMissingValues() {
        argument.validate(commandLine);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = OptionException.class)
    public void testValidateThrowsOptionExceptionForUnexpectedValue() {
        argument.processValues(commandLine, Collections.singletonList("value,unexpected").listIterator(), option);

        argument.validate(commandLine);
    }

    @Test
    public void testValidate() {
        argument.processValues(commandLine, Collections.singletonList("value").listIterator(), option);

        argument.validate(commandLine);

        assertEquals("value", commandLine.getValues(option).get(0));
    }
}