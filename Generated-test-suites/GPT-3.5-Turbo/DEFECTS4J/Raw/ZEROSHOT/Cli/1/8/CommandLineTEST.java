import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine cmdLine;

    @Before
    public void setUp() {
        cmdLine = new CommandLine();
    }

    @Test
    public void testHasOption() {
        assertFalse(cmdLine.hasOption("opt"));
    }

    @Test
    public void testHasOptionChar() {
        assertFalse(cmdLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObject() {
        assertNull(cmdLine.getOptionObject("opt"));
    }

    @Test
    public void testGetOptionObjectChar() {
        assertNull(cmdLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(cmdLine.getOptionValue("opt"));
    }

    @Test
    public void testGetOptionValueChar() {
        assertNull(cmdLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(cmdLine.getOptionValues("opt"));
    }

    @Test
    public void testGetOptionValuesChar() {
        assertNull(cmdLine.getOptionValues('o'));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        String defaultValue = "default";
        String result = cmdLine.getOptionValue("opt", defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testGetOptionValueCharWithDefault() {
        String defaultValue = "default";
        String result = cmdLine.getOptionValue('o', defaultValue);
        assertEquals(defaultValue, result);
    }

    @Test
    public void testGetArgs() {
        String[] args = cmdLine.getArgs();
        assertEquals(0, args.length);
    }

    @Test
    public void testGetArgList() {
        List argList = cmdLine.getArgList();
        assertEquals(0, argList.size());
    }

    @Test
    public void testAddArg() {
        String arg = "arg";
        cmdLine.addArg(arg);
        String[] args = cmdLine.getArgs();
        assertEquals(1, args.length);
        assertEquals(arg, args[0]);
    }

    @Test
    public void testAddOption() {
        String key = "opt";
        String value = "value";
        Option option = new Option(key, value);
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(key));
        assertEquals(value, cmdLine.getOptionValue(key));
        assertEquals(option, cmdLine.getOptionObject(key));
        assertTrue(cmdLine.hasOption(option.getOpt()));
        assertEquals(value, cmdLine.getOptionValue(option.getOpt()));
        assertEquals(option, cmdLine.getOptionObject(option.getOpt()));
    }

    @Test
    public void testIterator() {
        Iterator iterator = cmdLine.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetOptions() {
        Option[] options = cmdLine.getOptions();
        assertEquals(0, options.length);
    }
}