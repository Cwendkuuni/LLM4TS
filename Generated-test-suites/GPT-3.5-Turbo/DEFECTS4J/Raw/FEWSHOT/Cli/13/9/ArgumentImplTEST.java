package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class ArgumentImplTest {

    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;
    private ListIterator arguments;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("name", "description", 1, 2, ' ', ',', null, null, null, 0);
        commandLine = new WriteableCommandLineImpl();
        arguments = new ArrayList<String>().listIterator();
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        argument.processValues(commandLine, arguments, argument);
        
        assertEquals(0, commandLine.getValues(argument).size());
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "arg"));
    }

    @Test
    public void testGetPrefixes() {
        assertNotNull(argument.getPrefixes());
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertNotNull(argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertNull(argument.getConsumeRemaining());
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
    public void testValidate() throws OptionException {
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValue() throws OptionException {
        commandLine.addValue(argument, "invalidValue");
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.emptySet(), null);

        assertEquals("name", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = argument.helpLines(0, Collections.emptySet(), null);

        assertEquals(1, helpLines.size());
        assertEquals(argument, helpLines.get(0).getTarget());
        assertEquals(0, helpLines.get(0).getDepth());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = argument.stripBoundaryQuotes("\"token\"");
        assertEquals("token", token);
    }

    @Test
    public void testIsRequired() {
        assertFalse(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        argument.defaults(commandLine);

        assertTrue(commandLine.hasOption(argument));
    }

    @Test
    public void testDefaultValues() {
        argument.defaultValues(commandLine, argument);
        assertTrue(commandLine.getDefaultValues(argument).isEmpty());
    }
}