package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.apache.commons.cli2.resource.ResourceConstants.*;

/**
 * JUnit test cases for ArgumentImpl class.
 */
public class ArgumentImplTest {

    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, new SimpleValidator(), null,
                Collections.singletonList("defaultValue"));
        commandLine = new WriteableCommandLine();
    }

    

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ArgumentImpl argument = new ArgumentImpl("arg", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, new SimpleValidator(), null,
                Collections.singletonList("defaultValue"), 0);
        Assert.assertEquals("arg", argument.getName());
        Assert.assertEquals("description", argument.getDescription());
        Assert.assertEquals(1, argument.getMinimum());
        Assert.assertEquals(1, argument.getMaximum());
        Assert.assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
        Assert.assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
        Assert.assertEquals(false, argument.isSubsequentSplit());
        Assert.assertNull(argument.getValidator());
        Assert.assertEquals(null, argument.getConsumeRemaining());
        Assert.assertEquals(Collections.singletonList("defaultValue"), argument.getDefaultValues());
        Assert.assertNull(argument.getPrefixes());
    }

    /**
     * Test getPreferredName method.
     */
    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("arg", argument.getPreferredName());
    }

    /**
     * Test processValues method with valid arguments.
     */
    @Test
    public void testProcessValuesValid() {
        List<String> args = Arrays.asList("arg1", "arg2");
        argument.processValues(commandLine, args.listIterator(), this.argument);
        List<String> values = commandLine.getValues(this.argument);
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), values);
    }

    /**
     * Test processValues method with invalid arguments.
     */
    @Test(expected = OptionException.class)
    public void testProcessValuesInvalid() {
        argument.processValues(commandLine, Arrays.asList("invalidArg").listIterator(), this.argument);
    }

    /**
     * Test canProcess method with valid argument.
     */
    @Test
    public void testCanProcessValid() {
        Assert.assertTrue(argument.canProcess(commandLine, "argValue"));
    }

    /**
     * Test canProcess method with invalid argument.
     */
    @Test
    public void testCanProcessInvalid() {
        Assert.assertFalse(argument.canProcess(commandLine, "--consumeRemaining"));
    }

    /**
     * Test getPrefixes method.
     */
    @Test
    public void testGetPrefixes() {
        Assert.assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    /**
     * Test process method with valid arguments.
     */
    @Test
    public void testProcessValid() {
        List<String> args = Arrays.asList("arg1", "arg2");
        argument.process(commandLine, args.listIterator());
        List<String> values = commandLine.getValues(this.argument);
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), values);
    }

    /**
     * Test process method with invalid arguments.
     */
    @Test(expected = OptionException.class)
    public void testProcessInvalid() {
        argument.process(commandLine, Arrays.asList("--consumeRemaining").listIterator());
    }

    /**
     * Test getInitialSeparator method.
     */
    @Test
    public void testGetInitialSeparator() {
        Assert.assertEquals(ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
    }

    /**
     * Test getSubsequentSeparator method.
     */
    @Test
    public void testGetSubsequentSeparator() {
        Assert.assertEquals(ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
    }

    /**
     * Test getTriggers method.
     */
    @Test
    public void testGetTriggers() {
        Assert.assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    /**
     * Test getConsumeRemaining method.
     */
    @Test
    public void testGetConsumeRemaining() {
        Assert.assertEquals(null, argument.getConsumeRemaining());
    }

    /**
     * Test getDefaultValues method.
     */
    @Test
    public void testGetDefaultValues() {
        Assert.assertEquals(Collections.singletonList("defaultValue"), argument.getDefaultValues());
    }

    /**
     * Test getValidator method.
     */
    @Test
    public void testGetValidator() {
        Assert.assertEquals(new SimpleValidator(), argument.getValidator());
    }

    /**
     * Test validate method with valid arguments.
     */
    @Test
    public void testValidateValid() {
        argument.validate(commandLine);
    }

    /**
     * Test validate method with invalid arguments.
     */
    @Test(expected = OptionException.class)
    public void testValidateInvalid() {
        List<String> values = Arrays.asList("invalidValue");
        commandLine.setValues(this.argument, values);
        argument.validate(commandLine);
    }

    /**
     * Test appendUsage method.
     */
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.EMPTY_SET, null);
        Assert.assertEquals("arg [<arg>]", buffer.toString());
    }

    /**
     * Test getDescription method.
     */
    @Test
    public void testGetDescription() {
        Assert.assertEquals("description", argument.getDescription());
    }

    /**
     * Test helpLines method.
     */
    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = argument.helpLines(0, Collections.EMPTY_SET, null);
        Assert.assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        Assert.assertEquals("arg", helpLine.getName());
        Assert.assertEquals("description", helpLine.getShortDescription());
        Assert.assertEquals(null, helpLine.getLongDescription());
        Assert.assertEquals(0, helpLine.getOptionality());
    }

    /**
     * Test getMaximum method.
     */
    @Test
    public void testGetMaximum() {
        Assert.assertEquals(1, argument.getMaximum());
    }

    /**
     * Test getMinimum method.
     */
    @Test
    public void testGetMinimum() {
        Assert.assertEquals(1, argument.getMinimum());
    }

    /**
     * Test stripBoundaryQuotes method.
     */
    @Test
    public void testStripBoundaryQuotes() {
        Assert.assertEquals("argValue", argument.stripBoundaryQuotes("arg\"Value\""));
    }

    /**
     * Test isRequired method.
     */
    @Test
    public void testIsRequired() {
        Assert.assertTrue(argument.isRequired());
    }

    /**
     * Test defaults method.
     */
    @Test
    public void testDefaults() {
        argument.defaults(commandLine);
        Assert.assertEquals(Collections.singletonList("defaultValue"), commandLine.getValues(this.argument));
    }

    

    /**
     * Simple validator for testing.
     */
    private static class SimpleValidator implements Validator {
        @Override
        public void validate(List<?> values) throws InvalidArgumentException {
            // no validation
        }
    }
}