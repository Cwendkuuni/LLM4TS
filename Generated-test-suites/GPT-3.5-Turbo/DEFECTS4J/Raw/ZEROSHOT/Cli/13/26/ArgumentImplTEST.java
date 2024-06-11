import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 1, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals("test", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 1, ',', '\0', null, "--", null, 0);
        Assert.assertEquals(',', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 1, '\0', ';', null, "--", null, 0);
        Assert.assertEquals(';', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 1, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 1, '\0', '\0', null, "--", defaultValues, 0);
        Assert.assertEquals(defaultValues, argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        // Create a mock validator for test
        Validator validator = new Validator() {
            @Override
            public void validate(List values) throws InvalidArgumentException {
                // Mock implementation
            }
        };
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 1, '\0', '\0', validator, "--", null, 0);
        Assert.assertEquals(validator, argument.getValidator());
    }

    @Test
    public void testValidate() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 2, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            Assert.fail("Validation failed: " + e.getMessage());
        }
    }

    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 3, '\0', '\0', null, "--", null, 0);
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<>();
        argument.appendUsage(buffer, helpSettings, null);
        Assert.assertEquals("test test2 test3", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 1, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals("Test argument", argument.getDescription());
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 3, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 2, 3, '\0', '\0', null, "--", null, 0);
        Assert.assertEquals(2, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 1, '\0', '\0', null, "--", null, 0);
        String quotedToken = "\"test\"";
        Assert.assertEquals("test", argument.stripBoundaryQuotes(quotedToken));
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl argument1 = new ArgumentImpl("test", "Test argument", 1, 1, '\0', '\0', null, "--", null, 0);
        ArgumentImpl argument2 = new ArgumentImpl("test", "Test argument", 0, 1, '\0', '\0', null, "--", null, 0);
        Assert.assertTrue(argument1.isRequired());
        Assert.assertFalse(argument2.isRequired());
    }

    @Test
    public void testDefaults() {
        ArgumentImpl argument = new ArgumentImpl("test", "Test argument", 1, 1, '\0', '\0', null, "--", null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        argument.defaults(commandLine);
        Assert.assertNull(commandLine.getValue(argument));
    }
}