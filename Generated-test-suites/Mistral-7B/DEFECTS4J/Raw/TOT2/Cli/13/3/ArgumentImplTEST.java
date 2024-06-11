import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.ArgumentImpl;

public class ArgumentImplTest {

    @Test
    public void testArgumentImplWithMinimumValue() throws Exception {
        ArgumentImpl argument = new ArgumentImpl("arg1", "test argument", 1, 1, ' ', ' ', null, "--", null);
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--arg1", "test");
        argument.process(commandLine, args.listIterator(), null);
        assertEquals("test", commandLine.getValues(argument).get(0));
    }

    @Test
    public void testArgumentImplWithMaximumValue() throws Exception {
        ArgumentImpl argument = new ArgumentImpl("arg1", "test argument", 1, 1, ' ', ' ', null, "--", null);
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--arg1", "test1", "test2");
        argument.process(commandLine, args.listIterator(), null);
        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("test1", commandLine.getValues(argument).get(0));
        assertEquals("test2", commandLine.getValues(argument).get(1));
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithMinimumAndMaximumValues() throws Exception {
        ArgumentImpl argument = new ArgumentImpl("arg1", "test argument", 1, 2, ' ', ' ', null, "--", null);
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--arg1", "test1", "test2", "test3");
        argument.process(commandLine, args.listIterator(), null);
        fail("Should throw OptionException for too many values");
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithInvalidValue() throws Exception {
        ArgumentImpl argument = new ArgumentImpl("arg1", "test argument", 1, 1, ' ', ' ', null, "--", null);
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--arg1", "test1", "--arg2");
        argument.process(commandLine, args.listIterator(), null);
        fail("Should throw OptionException for invalid value");
    }

    @Test
    public void testArgumentImplWithDefaultValues() throws Exception {
        ArgumentImpl argument = new ArgumentImpl("arg1", "test argument", 0, 1, ' ', ' ', null, "--", Arrays.asList("default1"));
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--arg1");
        argument.process(commandLine, args.listIterator(), null);
        assertEquals("default1", commandLine.getValues(argument).get(0));
    }

    @Test
    public void testArgumentImplWithNoValues() throws Exception {
        ArgumentImpl argument = new ArgumentImpl("arg1", "test argument", 0, 1, ' ', ' ', null, "--", null);
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList();
        argument.process(commandLine, args.listIterator(), null);
        assertTrue(commandLine.getValues(argument).isEmpty());
    }

    @Test
    public void testArgumentImplWithSeparators() throws Exception {
        ArgumentImpl argument = new ArgumentImpl("arg1", "test argument", 1, 1, ' ', ',', null, "--", null);
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--arg1", "test1,test2");
        argument.process(commandLine, args.listIterator(), null);
        assertEquals(2, commandLine.getValues(argument).size());
        assertEquals("test1", commandLine.getValues(argument).get(0));
        assertEquals("test2", commandLine.getValues(argument).get(1));
    }

    @Test(expected = OptionException.class)
    public void testArgumentImplWithInvalidSeparators() throws Exception {
        ArgumentImpl argument = new ArgumentImpl("arg1", "test argument", 1, 1, ' ', ';', null, "--", null);
        CommandLine commandLine = new CommandLine();
        List<String> args = Arrays.asList("--arg1", "test1;test2");
        argument.process(commandLine, args.listIterator(), null);
        fail("Should throw OptionException for invalid separator");
    }
}