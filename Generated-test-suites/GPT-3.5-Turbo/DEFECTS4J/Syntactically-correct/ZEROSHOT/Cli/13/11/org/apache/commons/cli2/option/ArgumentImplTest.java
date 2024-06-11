import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setup() {
        argument = new ArgumentImpl(
                "arg",
                "This is an argument",
                1,
                3,
                ' ',
                ',',
                null,
                "--",
                Arrays.asList("default1", "default2"),
                1
        );
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = null;
        String arg = "";
        boolean result = argument.canProcess(commandLine, arg);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetPrefixes() {
        Assert.assertEquals(0, argument.getPrefixes().size());
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = null;
        ListIterator args = new ArrayList<String>().listIterator();
        argument.process(commandLine, args);
    }

    @Test
    public void testGetInitialSeparator() {
        Assert.assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Assert.assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        Assert.assertEquals(0, argument.getTriggers().size());
    }

    @Test
    public void testGetConsumeRemaining() {
        Assert.assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List<String> expected = Arrays.asList("default1", "default2");
        Assert.assertEquals(expected, argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Assert.assertNull(argument.getValidator());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = null;
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, Collections.EMPTY_SET, null);
        Assert.assertEquals("arg", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("This is an argument", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Assert.assertEquals(1, argument.helpLines(depth, Collections.EMPTY_SET, null).size());
    }

    @Test
    public void testGetMaximum() {
        Assert.assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        Assert.assertEquals(1, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = "\"quoted\"";
        String result = argument.stripBoundaryQuotes(token);
        Assert.assertEquals("quoted", result);
    }

    @Test
    public void testIsRequired() {
        Assert.assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        argument.defaults(commandLine);
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = null;
        argument.defaultValues(commandLine, argument);
    }
}