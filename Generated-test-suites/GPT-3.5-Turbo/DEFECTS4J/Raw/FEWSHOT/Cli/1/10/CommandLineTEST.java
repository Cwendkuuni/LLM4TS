import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

    private CommandLine cmd;

    @Before
    public void setup() {
        cmd = new CommandLine();
    }

    @Test
    public void testHasOption() {
        assertFalse(cmd.hasOption("f"));
        cmd.addOption(new Option("f"));
        assertTrue(cmd.hasOption("f"));
        assertFalse(cmd.hasOption("x"));
    }

    @Test
    public void testHasOptionChar() {
        assertFalse(cmd.hasOption('f'));
        cmd.addOption(new Option("f"));
        assertTrue(cmd.hasOption('f'));
        assertFalse(cmd.hasOption('x'));
    }

    @Test
    public void testGetOptionValue() {
        assertNull(cmd.getOptionValue("f"));
        cmd.addOption(new Option("f", true));
        assertNull(cmd.getOptionValue("f"));
        cmd.addOptionValue("f", "value");
        assertEquals("value", cmd.getOptionValue("f"));
    }

    @Test
    public void testGetOptionValueChar() {
        assertNull(cmd.getOptionValue('f'));
        cmd.addOption(new Option("f", true));
        assertNull(cmd.getOptionValue('f'));
        cmd.addOptionValue("f", "value");
        assertEquals("value", cmd.getOptionValue('f'));
    }

    @Test
    public void testGetOptionValues() {
        assertNull(cmd.getOptionValues("f"));
        cmd.addOption(new Option("f", true));
        assertNull(cmd.getOptionValues("f"));
        cmd.addOptionValue("f", "value1");
        cmd.addOptionValue("f", "value2");
        assertEquals("value1", cmd.getOptionValues("f")[0]);
        assertEquals("value2", cmd.getOptionValues("f")[1]);
    }

    @Test
    public void testGetOptionValuesChar() {
        assertNull(cmd.getOptionValues('f'));
        cmd.addOption(new Option("f", true));
        assertNull(cmd.getOptionValues('f'));
        cmd.addOptionValue("f", "value1");
        cmd.addOptionValue("f", "value2");
        assertEquals("value1", cmd.getOptionValues('f')[0]);
        assertEquals("value2", cmd.getOptionValues('f')[1]);
    }

    @Test
    public void testGetOptionValueDefaultValue() {
        assertEquals("default", cmd.getOptionValue("f", "default"));
        cmd.addOption(new Option("f", true));
        assertNull(cmd.getOptionValue("f", "default"));
        cmd.addOptionValue("f", "value");
        assertEquals("value", cmd.getOptionValue("f", "default"));
    }

    @Test
    public void testGetOptionValueCharDefaultValue() {
        assertEquals("default", cmd.getOptionValue('f', "default"));
        cmd.addOption(new Option("f", true));
        assertNull(cmd.getOptionValue('f', "default"));
        cmd.addOptionValue("f", "value");
        assertEquals("value", cmd.getOptionValue('f', "default"));
    }

    @Test
    public void testGetArgs() {
        assertEquals(0, cmd.getArgs().length);
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @Test
    public void testGetArgList() {
        assertEquals(0, cmd.getArgList().size());
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        assertEquals("arg1", cmd.getArgList().get(0));
        assertEquals("arg2", cmd.getArgList().get(1));
    }

    @Test
    public void testAddOption() {
        assertEquals(0, cmd.getOptions().length);
        cmd.addOption(new Option("f"));
        assertEquals(1, cmd.getOptions().length);
        assertEquals("f", cmd.getOptions()[0].getOpt());
    }

    @Test
    public void testIterator() {
        cmd.addOption(new Option("f"));
        cmd.addOption(new Option("x"));
        Iterator<Option> iterator = cmd.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Option option = iterator.next();
            assertEquals("f", option.getOpt());
            count++;
        }
        assertEquals(1, count);
    }

}