import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class OptionTest {

    @Test
    public void testProcess() {
        Option option = Mockito.spy(new MockOption());
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        ListIterator args = Mockito.mock(ListIterator.class);

        Mockito.doAnswer(invocation -> {
            option.process(commandLine, args);
            return true;
        }).when(option).canProcess(ArgumentMatchers.eq(commandLine), ArgumentMatchers.eq(args));

        option.process(commandLine, args);

        Mockito.verify(option, Mockito.times(1)).process(commandLine, args);
    }

    @Test(expected = OptionException.class)
    public void testProcessWithException() {
        Option option = Mockito.spy(new MockOption());
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        ListIterator args = Mockito.mock(ListIterator.class);

        Mockito.doThrow(new OptionException("Test Exception")).when(option).process(commandLine, args);

        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        Option option = new MockOption();
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);

        option.defaults(commandLine);

        // Verify that no assertions were thrown
        Mockito.verifyNoMoreInteractions(commandLine);
    }

    @Test
    public void testCanProcessWithTrueArgument() {
        Option option = new MockOption();
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String argument = "testArgument";

        Mockito.when(option.canProcess(commandLine, ArgumentMatchers.isA(ListIterator.class)))
                .thenReturn(true);

        boolean result = option.canProcess(commandLine, Arrays.asList(argument).listIterator());

        Assert.assertTrue(result);
    }

    @Test
    public void testCanProcessWithFalseArgument() {
        Option option = new MockOption();
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String argument = "testArgument";

        Mockito.when(option.canProcess(commandLine, ArgumentMatchers.isA(ListIterator.class)))
                .thenReturn(false);

        boolean result = option.canProcess(commandLine, Arrays.asList(argument).listIterator());

        Assert.assertFalse(result);
    }

    @Test
    public void testGetTriggers() {
        Option option = new MockOption();
        Set<String> expectedTriggers = new HashSet<String>() {{
            add("testTrigger");
        }};

        Set<String> actualTriggers = option.getTriggers();

        Assert.assertEquals(expectedTriggers, actualTriggers);
    }

    @Test
    public void testGetPrefixes() {
        Option option = new MockOption();
        Set<String> expectedPrefixes = new LinkedHashSet<String>() {{
            add("--");
        }};

        Set<String> actualPrefixes = option.getPrefixes();

        Assert.assertEquals(expectedPrefixes, actualPrefixes);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidCommandLine() {
        Option option = new MockOption();
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);

        Mockito.doThrow(new OptionException("Test Exception")).when(option).validate(commandLine);

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new MockOption();
        int depth = 0;
        Set helpSettings = new LinkedHashSet<>();
        Comparator comp = null;

        List<Option.HelpLine> expectedHelpLines = Arrays.asList(
                new Option.HelpLine("Test Option", "Test Option description", null, null, null),
                new Option.HelpLine("", "", null, null, null)
        );

        List<Option.HelpLine> actualHelpLines = option.helpLines(depth, helpSettings, comp);

        Assert.assertEquals(expectedHelpLines, actualHelpLines);
    }

    @Test
    public void testAppendUsage() {
        Option option = new MockOption();
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new LinkedHashSet<>();
        Comparator comp = null;

        option.appendUsage(buffer, helpSettings, comp);

        String expectedUsage = "Test Option\n" +
                "Test Option description\n";

        String actualUsage = buffer.toString();

        Assert.assertEquals(expectedUsage, actualUsage);
    }

    @Test
    public void testGetPreferredName() {
        Option option = new MockOption();

        String expectedPreferredName = "Test Option";

        String actualPreferredName = option.getPreferredName();

        Assert.assertEquals(expectedPreferredName, actualPreferredName);
    }

    @Test
    public void testGetDescription() {
        Option option = new MockOption();

        String expectedDescription = "Test Option description";

        String actualDescription = option.getDescription();

        Assert.assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testgetId() {
        Option option = new MockOption();

        int expectedId = 1;

        int actualId = option.getId();

        Assert.assertEquals(expectedId, actualId);
    }

    @Test(expected = OptionNotFoundException.class)
    public void testFindOptionWithNullTrigger() {
        Option option = new MockOption();

        Option result = option.findOption(null);

        Assert.assertNull(result);
    }

    @Test
    public void testFindOptionWithValidTrigger() {
        Option parentOption = Mockito.spy(new MockOption());
        Option childOption = new MockOption();
        parentOption.setParent(childOption);

        Option result = parentOption.findOption("testTrigger");

        Assert.assertEquals(childOption, result);
    }

    @Test
    public void testIsRequired() {
        Option option = new MockOption();

        boolean expectedIsRequired = false;

        boolean actualIsRequired = option.isRequired();

        Assert.assertEquals(expectedIsRequired, actualIsRequired);
    }

    @Test
    public void testGetParent() {
        Option parentOption = new MockOption();
        Option childOption = new MockOption();
        childOption.setParent(parentOption);

        Option result = childOption.getParent();

        Assert.assertEquals(parentOption, result);
    }

    @Test
    public void testSetParent() {
        Option option = new MockOption();
        Option parentOption = new MockOption();

        option.setParent(parentOption);

        Option expectedParent = parentOption;

        Option actualParent = option.getParent();

        Assert.assertEquals(expectedParent, actualParent);
    }

    private static class MockOption implements Option {
        // Empty implementation
    }
}