import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {
    private ArgumentImpl argument;
    private CommandLine cmd;

    @Before
    public void setup() {
        argument = new ArgumentImpl("argumentName", "Description", 1, 2, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, "--values", null, 0);
        cmd = new CommandLine();
    }

    @Test
    public void testArgumentWithOneValue() throws Exception {
        argument.process(cmd, "argumentName value1".split(" "));
        assertEquals("value1", cmd.getValue(argument.getIndex()));
    }

    @Test
    public void testArgumentWithTwoValues() throws Exception {
        argument.process(cmd, "argumentName value1 value2".split(" "));
        assertEquals("value1", cmd.getValue(argument.getIndex()));
        assertEquals("value2", cmd.getValue(argument.getIndex() + 1));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithThreeValues() throws Exception {
        argument.process(cmd, "argumentName value1 value2 value3".split(" "));
        fail("Should have thrown OptionException when trying to add more than 2 values.");
    }

    @Test
    public void testArgumentWithNoValues() throws Exception {
        argument.process(cmd, new String[0]);
        assertNull(cmd.getValue(argument.getIndex()));
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithConsumingOption() throws Exception {
        argument = new ArgumentImpl("argumentName", "Description", 0, Integer.MAX_VALUE, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, "--values", null, 0);
        cmd.parse("--values arg1 arg2 arg3 --consume-remaining");
        fail("Should have thrown OptionException when trying to add more than 2 values to a consuming option.");
    }

    @Test
    public void testArgumentWithDefaultValues() throws Exception {
        argument = new ArgumentImpl("argumentName", "Description", 0, Integer.MAX_VALUE, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR, ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, "--values", Arrays.asList("defaultValue1", "defaultValue2"), 0);
        cmd.parse("--values");
        assertEquals("defaultValue1", cmd.getValue(argument.getIndex()));
        assertEquals("defaultValue2", cmd.getValue(argument.getIndex() + 1));
    }
}