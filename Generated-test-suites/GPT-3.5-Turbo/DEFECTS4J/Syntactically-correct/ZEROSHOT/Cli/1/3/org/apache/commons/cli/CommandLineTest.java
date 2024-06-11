import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CommandLineTest {

    private CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOption() {
        Assert.assertFalse(commandLine.hasOption("opt"));
        Assert.assertFalse(commandLine.hasOption('o'));
    }

    @Test
    public void testGetOptionObject() {
        Assert.assertNull(commandLine.getOptionObject("opt"));
        Assert.assertNull(commandLine.getOptionObject('o'));
    }

    @Test
    public void testGetOptionValue() {
        Assert.assertNull(commandLine.getOptionValue("opt"));
        Assert.assertNull(commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues() {
        Assert.assertNull(commandLine.getOptionValues("opt"));
        Assert.assertNull(commandLine.getOptionValues('o'));
    }

    @Test
    public void testGetArgs() {
        Assert.assertArrayEquals(new String[0], commandLine.getArgs());
    }

    @Test
    public void testGetArgList() {
        Assert.assertEquals(Arrays.asList(), commandLine.getArgList());
    }

    @Test
    public void testAddArg() {
        commandLine.addArg("arg");
        Assert.assertArrayEquals(new String[]{"arg"}, commandLine.getArgs());
        Assert.assertEquals(Arrays.asList("arg"), commandLine.getArgList());
    }

    @Test
    public void testAddOption() {
        Option option = new Option("opt", "option", true, "description");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption("opt"));
        Assert.assertTrue(commandLine.hasOption('o'));
        Assert.assertEquals(option, commandLine.getOptionObject("opt"));
        Assert.assertEquals(option, commandLine.getOptionObject('o'));
        Assert.assertEquals("option", commandLine.getOptionValue("opt"));
        Assert.assertEquals("option", commandLine.getOptionValue('o'));
        Assert.assertArrayEquals(new String[]{"option"}, commandLine.getOptionValues("opt"));
        Assert.assertArrayEquals(new String[]{"option"}, commandLine.getOptionValues('o'));
        Assert.assertArrayEquals(new Option[]{option}, commandLine.getOptions());
    }

}