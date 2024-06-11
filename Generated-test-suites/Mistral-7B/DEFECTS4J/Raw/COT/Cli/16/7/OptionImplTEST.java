import static org.junit.Assert.*;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        Option option = new OptionImpl(1, false);
        assertEquals(1, option.getId());
        assertFalse(option.isRequired());
    }

    @Test
    public void testCanProcessTrue() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        WriteableCommandLine commandLine = new CommandLine();
        commandLine.parse(Arrays.asList(new String[]{"test", "testOption", "arg"}));
        assertTrue(option.canProcess(commandLine, commandLine.getArgs().listIterator()));
    }

    @Test
    public void testCanProcessFalse() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        WriteableCommandLine commandLine = new CommandLine();
        commandLine.parse(Arrays.asList(new String[]{"test"}));
        assertFalse(option.canProcess(commandLine, commandLine.getArgs().listIterator()));
    }

    @Test(expected = ParseException.class)
    public void testCanProcessWithEmptyArgumentList() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        WriteableCommandLine commandLine = new CommandLine();
        commandLine.parse(Arrays.asList(new String[]{}));
        option.canProcess(commandLine, commandLine.getArgs().listIterator());
    }

    @Test
    public void testToString() {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        String result = option.toString();
        assertEquals("--testOption <arg>", result);
    }

    @Test
    public void testEqualsTrue() {
        Option option1 = new OptionBuilder()
                .withLongOpt("testOption1")
                .withArgName("arg1")
                .withDescription("Test Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withLongOpt("testOption2")
                .withArgName("arg2")
                .withDescription("Test Option 2")
                .create();
        Option option3 = new OptionBuilder()
                .withLongOpt("testOption1")
                .withArgName("arg1")
                .withDescription("Test Option 1")
                .create();
        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option3));
        assertFalse(option1.equals(option2));
        assertFalse(option1.equals(null));
        assertFalse(option1.equals(""));
    }

    @Test
    public void testHashCode() {
        Option option1 = new OptionBuilder()
                .withLongOpt("testOption1")
                .withArgName("arg1")
                .withDescription("Test Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withLongOpt("testOption2")
                .withArgName("arg2")
                .withDescription("Test Option 2")
                .create();
        Option option3 = new OptionBuilder()
                .withLongOpt("testOption1")
                .withArgName("arg1")
                .withDescription("Test Option 1")
                .create();
        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testFindOptionTrue() {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        Option foundOption = option.findOption("testOption");
        assertEquals(option, foundOption);
    }

    @Test
    public void testFindOptionFalse() {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        Option foundOption = option.findOption("testOtherOption");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequiredTrue() {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .withRequired(true)
                .create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequiredFalse() {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        assertFalse(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        Set<String> prefixes = new HashSet<>(Arrays.asList("testPrefix"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesEmptyPrefixList() {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        Set<String> prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithTriggerMatchesPrefix() {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        Set<String> prefixes = new HashSet<>(Arrays.asList("test"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerDoesNotMatchPrefix() {
        Option option = new OptionBuilder()
                .withLongOpt("testOption")
                .withArgName("arg")
                .withDescription("Test Option")
                .create();
        Set<String> prefixes = new HashSet<>(Arrays.asList("test2"));
        option.checkPrefixes(prefixes);
    }
}