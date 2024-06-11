import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RunWith(JUnit4.class)
public class OptionTest {

    @Test
    public void testProcess() {
        Option option = createMockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--test", "arg1", "arg2").listIterator();

        option.process(commandLine, args);

        Assert.assertEquals("--test", commandLine.getOptionNames().get(0));
        Assert.assertEquals("arg1", commandLine.getOptionValues().get(0));
        Assert.assertEquals(1, commandLine.getArgList().size());
        Assert.assertEquals("arg2", commandLine.getArgList().get(0));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithInvalidArgument() {
        Option option = createMockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--test", "arg1", "invalid").listIterator();

        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        Option option = createMockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.defaults(commandLine);

        Assert.assertNull(commandLine.getOptionValues().get(0));
    }

    @Test
    public void testCanProcessWithValidArgument() {
        Option option = createMockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--test", "arg1").listIterator();

        Assert.assertTrue(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcessWithInvalidArgument() {
        Option option = createMockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("--test", "arg1", "arg2").listIterator();

        Assert.assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testGetTriggers() {
        Option option = createMockOption();

        Set<String> triggers = option.getTriggers();

        Assert.assertNotNull(triggers);
        Assert.assertEquals(1, triggers.size());
        Assert.assertEquals("--test", triggers.iterator().next());
    }

    @Test
    public void testGetPrefixes() {
        Option option = createMockOption();

        Set<String> prefixes = option.getPrefixes();

        Assert.assertNotNull(prefixes);
        Assert.assertEquals(1, prefixes.size());
        Assert.assertEquals("--", prefixes.iterator().next());
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidCommandLine() {
        Option option = createMockOption();
        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = createMockOption();
        int depth = 1;
        Set helpSettings = new HashSet();
        Comparator comp = Collections.emptyComparator();

        List<Object> helpLines = option.helpLines(depth, helpSettings, comp);

        Assert.assertNotNull(helpLines);
        Assert.assertEquals(1, helpLines.size());
    }

    @Test
    public void testAppendUsage() {
        Option option = createMockOption();
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = Collections.emptyComparator();

        option.appendUsage(buffer, helpSettings, comp);

        Assert.assertEquals("", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = createMockOption();

        String preferredName = option.getPreferredName();

        Assert.assertEquals("--test", preferredName);
    }

    @Test
    public void testGetDescription() {
        Option option = createMockOption();

        String description = option.getDescription();

        Assert.assertEquals("This is a test option", description);
    }

    @Test
    public void testgetId() {
        Option option = createMockOption();

        int id = option.getId();

        Assert.assertEquals(1, id);
    }

    @Test
    public void testFindOption() {
        Option parentOption = createMockOptionWithChild();
        Option childOption = createMockOption();
        Option option = createMockOption();

        parentOption.addOption(childOption);

        Option foundOption = option.findOption(parentOption, "test");

        Assert.assertEquals(childOption, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = createMockOption();

        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testFindParent() {
        Option childOption = createMockOption();
        Option parentOption = createMockOption();

        childOption.setParent(parentOption);

        Option foundOption = Option.findParent(childOption, Option.class);

        Assert.assertEquals(parentOption, foundOption);
    }

    private Option createMockOption() {
        Option option = Mockito.mock(Option.class);
        when(option.getTriggers()).thenReturn(new HashSet<>(Arrays.asList("--test")));
        when(option.getPrefixes()).thenReturn(new HashSet<>(Arrays.asList("--")));
        when(option.getDescription()).thenReturn("This is a test option");
        return option;
    }

    private Option createMockOptionWithChild() {
        Option parentOption = Mockito.mock(Option.class);
        Option childOption = Mockito.mock(Option.class);
        when(childOption.getId()).thenReturn(2);

        Option option = Mockito.mock(Option.class);
        when(option.getTriggers()).thenReturn(new HashSet<>(Arrays.asList("--test")));
        when(option.getPrefixes()).thenReturn(new HashSet<>(Arrays.asList("--")));
        when(option.getDescription()).thenReturn("This is the parent option");
        when(option.getParent()).thenReturn(parentOption);
        when(parentOption.addOption(childOption)).thenReturn(option);

        return option;
    }
}