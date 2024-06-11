import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("test1", "description"));
        
        assertTrue(cmd.hasOption("test1"));
        assertFalse(cmd.hasOption("test2"));
    }

    @Test
    public void testHasOptionChar() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("testA", "description"));
        
        assertTrue(cmd.hasOption('A'));
        assertFalse(cmd.hasOption('B'));
    }

    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("testObject", "description");
        opt.setType(Integer.class);
        cmd.addOption(opt);
        cmd.addOptionValue("testObject", "123");

        assertEquals(Integer.valueOf(123), cmd.getOptionObject("testObject"));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("testValue", "description");
        cmd.addOption(opt);
        cmd.addOptionValue("testValue", "value");

        assertEquals("value", cmd.getOptionValue("testValue"));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("testValues", "description");
        cmd.addOption(opt);
        cmd.addOptionValue("testValues", "value1");
        cmd.addOptionValue("testValues", "value2");

        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("testValues"));
    }

    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("testDefault", "description");
        cmd.addOption(opt);

        assertEquals("default", cmd.getOptionValue("testDefault", "default"));
    }

    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testAddOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("testAdd", "description");
        cmd.addOption(opt);

        assertEquals(opt, cmd.getOptions()[0]);
    }
}