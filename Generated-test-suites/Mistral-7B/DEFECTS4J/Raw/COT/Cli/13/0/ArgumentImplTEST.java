package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentImplTest {

    @Mock
    private HelpFormatter helpFormatter;
    @Mock
    private ResourceHelper resourceHelper;
    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        Mockito.when(commandLine.getValues(ArgumentImpl.class)).thenReturn(new ArrayList<>());
        Mockito.when(commandLine.addValue(ArgumentImpl.class, ArgumentMatchers.anyString())).thenAnswer(invocationOnMock -> {});
        argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '.', ':', null, null, Collections.singletonList("default"), 0);
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("testArg", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        argument.processValues(commandLine, commandLine.argumentList().listIterator(), new OptionImpl(0, false));
        assertEquals(1, commandLine.getValues(argument).size());
        assertEquals("default", commandLine.getValues(argument).get(0));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidNumberOfArguments() {
        argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '.', ':', null, null, Collections.emptyList(), 0);

        argument.processValues(commandLine, commandLine.argumentList().listIterator(), new OptionImpl(0, false));
        commandLine.argumentList().next();

        argument.processValues(commandLine, commandLine.argumentList().listIterator(), new OptionImpl(0, false));
        throw new OptionException(new Option("", null), "Option testArg does not accept the given value(s)", "value2");
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "default"));
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.emptySet(), argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals("", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        assertEquals(Collections.singletonList("default"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        assertEquals(null, argument.getValidator());
    }

    @Test
    public void testValidate() {
        argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '.', ':', null, null, Collections.singletonList("default"), 0);

        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            fail("Validation should not throw an exception");
        }
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValues() {
        argument = new ArgumentImpl("testArg", "Test Argument", 1, 1, '.', ':', null, null, Collections.singletonList("default"), 0);
        Mockito.when(resourceHelper.getMessage(ArgumentImpl.class, ResourceConstants.ARGUMENT_UNEXPECTED_VALUE, ArgumentMatchers.anyString())).thenThrow(new RuntimeException());

        try {
            argument.validate(commandLine);
            fail("Validation should throw an exception");
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testAppendUsage() {
        argument.appendUsage(Mockito.mock(StringBuffer.class), Mockito.mock(Set.class), null);
        String expectedUsage = "[testArg] ...";
        StringBuffer buffer = Mockito.mock(StringBuffer.class);
        Mockito.when(buffer.toString()).thenReturn(expectedUsage);
        assertEquals(expectedUsage, argument.appendUsage(buffer, Mockito.mock(Set.class), null));
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test Argument", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = argument.helpLines(0, null, null);
        String expectedHelpLine = "  [testArg]                                                                  ";
        HelpLine helpLine = helpLines.get(0);
        assertEquals(expectedHelpLine, helpLine.getShortOption());
        assertEquals("Test Argument", helpLine.getLongOption());
        assertEquals(null, helpLine.getValues());
        assertEquals(null, helpLine.getDescription());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        argument.defaults(commandLine);
        assertEquals(Collections.singletonList("default"), commandLine.getValues(argument));
    }
}