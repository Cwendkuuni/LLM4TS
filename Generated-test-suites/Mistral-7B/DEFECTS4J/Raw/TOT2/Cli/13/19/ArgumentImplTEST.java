// Combined test file for ArgumentImpl

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.TestUtil;
import org.junit.*;
import static org.junit.Assert.*;

public class ArgumentImplTest {
    private static Argument argument;
    private static CommandLine commandLine;
    private static HelpFormatter formatter;

    @BeforeClass
    public static void setUp() {
        argument = new ArgumentImpl("arg", "description", 1, 1, 'a', 'b', null, "--", null);
        commandLine = new CommandLine();
        formatter = new HelpFormatter();
    }

    @Before
    public void beforeEachTest() {
        commandLine.clearParsedArgs();
    }

    @Test
    public void testBasicUsage() {
        argument.process(commandLine, TestUtil.args("arg1"));
        assertEquals("arg1", commandLine.getValueForOption("arg"));
    }

    @Test
    public void testEmptyArgumentList() {
        argument.process(commandLine, TestUtil.args());
        assertNull(commandLine.getValueForOption("arg"));
    }

    @Test
    public void testConsumingOption() {
        argument = new ArgumentImpl("arg", "description", 1, 1, 'a', 'b', null, "--", "--consume");
        commandLine.parse(TestUtil.args("--consume"));
        argument.process(commandLine, TestUtil.args("arg1"));
        assertEquals("arg1", commandLine.getValueForOption("arg"));
    }

    @Test
    public void testHelp() {
        String help = formatter.printHelp("Test", argument.helpLines(0, null, null));
        assertTrue(help.contains("arg [arg]"));
        assertTrue(help.contains("description"));
        assertTrue(help.contains("1"));
        assertTrue(help.contains("1"));
        assertTrue(help.contains("--"));
        assertTrue(help.contains("--consume"));
    }

    @Test
    public void testInvalidOption() {
        try {
            argument.process(commandLine, TestUtil.args("--invalidOption"));
            fail();
        } catch (OptionException e) {
            // expected
        }
        assertFalse(commandLine.hasOption("invalidOption"));
    }

    @Test
    public void testInvalidValue() {
        try {
            argument.process(commandLine, TestUtil.args("arg1", "invalid"));
            fail();
        } catch (OptionException e) {
            // expected
        }
        assertNull(commandLine.getValueForOption("arg"));
    }

    @Test
    public void testMaximumValues() {
        argument = new ArgumentImpl("arg", "description", 1, 2, 'a', 'b', null, "--", null);
        commandLine.parse(TestUtil.args("arg1", "arg2"));
        assertEquals(2, commandLine.getNumValuesForOption("arg"));
        assertEquals("arg1", commandLine.getValueForOption("arg")[0]);
        assertEquals("arg2", commandLine.getValueForOption("arg")[1]);
    }

    @Test
    public void testMinimumValues() {
        argument = new ArgumentImpl("arg", "description", 2, 2, 'a', 'b', null, "--", null);
        commandLine.parse(TestUtil.args("arg1"));
        try {
            argument.process(commandLine, TestUtil.args("arg2"));
            fail();
        } catch (OptionException e) {
            // expected
        }
        assertEquals(1, commandLine.getNumValuesForOption("arg"));
    }
}