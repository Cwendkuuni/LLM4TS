import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.powermock.PowerMockHowMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockHowMockRunner.class)
public class ArgumentImplTest {

    private Argument argument;
    private CommandLine cmd;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("argName", "description", 1, 3, '.', ',', null, null, Arrays.asList("defaultValue1", "defaultValue2"), 0);
        cmd = new DefaultCommandLine();
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("argName", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() {
        argument.processValues(cmd, Arrays.asList("value1", "value2", "value3").listIterator(), argument);
        assertEquals(2, cmd.getValues(argument).size());
        assertEquals("value1", cmd.getValue(argument, 0));
        assertEquals("value2", cmd.getValue(argument, 1));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesInvalidValues() {
        argument = new ArgumentImpl("argName", "description", 1, 3, '.', ',', null, null, Arrays.asList("defaultValue1", "defaultValue2"), 0);
        cmd = new DefaultCommandLine();
        cmd.parseArgument(new OptionBuilder("argName").build(), "--argName", "value1", "value2");
        argument.processValues(cmd, Arrays.asList("value3").listiterator(), argument);
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(cmd, "value1"));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
    }

    @Test
    public void testProcess() {
        argument.process(cmd, Arrays.asList("value1", "value2", "value3").listIterator());
        assertEquals(2, cmd.getValues(argument).size());
        assertEquals("value1", cmd.getValue(argument, 0));
        assertEquals("value2", cmd.getValue(argument, 1));
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals('.', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(',', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(Collections.EMPTY_SET, argument.getTriggers());
    }

    @Test
    public void testGetConsumeRemaining() {
        assertNull(argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        List defaultValues = argument.getDefaultValues();
        assertEquals(2, defaultValues.size());
        assertEquals("defaultValue1", defaultValues.get(0));
        assertEquals("defaultValue2", defaultValues.get(1));
    }

    @Test
    public void testGetValidator() {
        assertNull(argument.getValidator());
    }

    @Test
    public void testValidate() {
        argument = new ArgumentImpl("argName", "description", 1, 3, '.', ',', null, null, Arrays.asList("defaultValue1", "defaultValue2"), 0);
        cmd = new DefaultCommandLine();
        cmd.parseArgument(new OptionBuilder("argName").build(), "--argName", "value1", "value2");
        argument.validate(cmd);
    }

    @Test(expected = OptionException.class)
    public void testValidateInvalidValues() {
        argument = new ArgumentImpl("argName", "description", 1, 3, '.', ',', new Validate() {}, null, Arrays.asList("defaultValue1", "defaultValue2"), 0);
        cmd = new DefaultCommandLine();
        cmd.parseArgument(new OptionBuilder("argName").build(), "--argName", "value1", "value2", "invalidValue");
        argument.validate(cmd);
    }

    @Test
    public void testAppendUsage() {
        StringBuilder sb = new StringBuilder();
        argument.appendUsage(sb, null, null);
        assertEquals("--argName <value> [<value>...]", sb.toString());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        HelpFormatter formatter = new HelpFormatter();
        List helpLines = argument.helpLines(0, null, null);
        formatter.printHelpOnSystemOut(System.out, "test", null, helpLines, 1);

        String output = System.out.getOutputStream().toString();
        assertTrue(output.contains("ARGUMENTS"));
        assertTrue(output.contains("argName"));
        assertTrue(output.contains("description"));
        assertTrue(output.contains("--argName <value> [<value>...]"));
    }

    @Test
    public void testGetMaximum() {
        assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, argument.getMinimum());
    }
}