import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.OptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgumentImplTest {
    
    private ArgumentImpl argument;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("testArg", "Description", 1, 5, ',', ':', null, "--", null, 0);
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("testArg", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        ListIterator arguments = createArgumentsList("--arg=value1,value2,value3").listIterator();
        argument.processValues(commandLine, arguments, argument);
        Assert.assertEquals(3, commandLine.getValues(argument).size());
    }

    @Test
    public void testCanProcess() {
        Assert.assertTrue(argument.canProcess(commandLine, "test"));
    }

    @Test
    public void testGetInitialSeparator() {
        Assert.assertEquals(',', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Assert.assertEquals(':', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        Assert.assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        argument = new ArgumentImpl("testArg", "Description", 1, 5, ',', ':', null, "--", defaultValues, 0);
        Assert.assertEquals(2, argument.getDefaultValues().size());
    }

    @Test
    public void testGetValidator() {
        Assert.assertEquals(null, argument.getValidator());
    }

    @Test
    public void testGetMaximum() {
        Assert.assertEquals(5, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        Assert.assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes_WithoutQuotes() {
        String token = "value";
        Assert.assertEquals("value", argument.stripBoundaryQuotes(token));
    }

    @Test
    public void testStripBoundaryQuotes_WithQuotes() {
        String token = "\"value\"";
        Assert.assertEquals("value", argument.stripBoundaryQuotes(token));
    }

    @Test
    public void testIsRequired() {
        Assert.assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        argument = new ArgumentImpl("testArg", "Description", 1, 5, ',', ':', null, "--", defaultValues, 0);
        argument.defaults(commandLine);
        Assert.assertEquals(2, commandLine.getValues(argument).size());
    }

}