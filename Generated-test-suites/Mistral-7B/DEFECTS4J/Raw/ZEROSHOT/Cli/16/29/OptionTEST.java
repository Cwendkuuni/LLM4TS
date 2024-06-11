import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Option.class)
public class OptionTest {

    private WriteableCommandLine commandLine;
    private Option option;
    private Options options;

    @Before
    public void setup() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        option = PowerMockito.spy(new MockOption());
        options = new Options();
        options.addOption(option);
    }

    @Test
    public void testProcess() {
        String[] args = {"-t", "test"};
        List<String> argList = Arrays.asList(args);
        ListIterator<String> argIterator = argList.listIterator();

        PowerMockito.doAnswer(invocation -> option.process(commandLine, argIterator)).when(option);

        option.process(commandLine, argIterator);

        Assert.assertEquals("test", commandLine.getOptionValue("t"));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithException() {
        String[] args = {"--invalid"};
        List<String> argList = Arrays.asList(args);
        ListIterator<String> argIterator = argList.listIterator();

        PowerMockito.doThrow(new OptionException("Invalid option")).when(option).process(commandLine, argIterator);

        option.process(commandLine, argIterator);
    }

    @Test
    public void testCanProcess() {
        String[] args = {"-t", "test"};
        List<String> argList = Arrays.asList(args);
        ListIterator<String> argIterator = argList.listIterator();

        PowerMockito.doReturn(true).when(option).canProcess(commandLine, argIterator);

        Assert.assertTrue(option.canProcess(commandLine, argIterator));
    }

    @Test
    public void testCanProcessFalse() {
        String[] args = {"--invalid"};
        List<String> argList = Arrays.asList(args);
        ListIterator<String> argIterator = argList.listIterator();

        PowerMockito.doReturn(false).when(option).canProcess(commandLine, argIterator);

        Assert.assertFalse(option.canProcess(commandLine, argIterator));
    }

    @Test
    public void testGetTriggers() {
        Set<String> triggers = option.getTriggers();
        Assert.assertNotNull(triggers);
        Assert.assertEquals(Arrays.asList("--trigger1", "--trigger2"), new ArrayList(triggers));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = option.getPrefixes();
        Assert.assertNotNull(prefixes);
        Assert.assertEquals(Arrays.asList("-p", "--prefix1", "--prefix2"), new ArrayList(prefixes));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithException() {
        PowerMockito.doThrow(new OptionException("Invalid command line")).when(option).validate(commandLine);

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        List<Object> helpLines = option.helpLines(1, null, null);
        Assert.assertNotNull(helpLines);
        Assert.assertEquals(1, helpLines.size());
        Assert.assertTrue(helpLines.get(0) instanceof HelpLineImpl);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, null, null);
        Assert.assertEquals("Option test\n", buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("test", option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("This is a test option", option.getDescription());
    }

    @Test
    public void testgetId() {
        Assert.assertEquals(1, option.getId());
    }

    @Test
    public void testFindOption() {
        Option option2 = PowerMockito.spy(new MockOption());
        options.addOption(option2);

        Option foundOption = option.findOption("--trigger2");
        Assert.assertEquals(option2, foundOption);
    }

    @Test
    public void testIsRequired() {
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testHasParent() {
        OptionGroup group = new OptionGroup();
        group.setName("group");
        group.addOption(option);

        option.setParent(group);
        Assert.assertEquals(group, option.getParent());
    }
}

private static class MockOption extends AbstractOption {
    @Override
    public void process(final WriteableCommandLine commandLine, final ListIterator<String> args) {
        super.process(commandLine, args);
    }

    @Override
    public void defaults(final WriteableCommandLine commandLine) {
        super.defaults(commandLine);
    }

    @Override
    public boolean canProcess(final WriteableCommandLine commandLine, final String argument) {
        return super.canProcess(commandLine, argument);
    }

    @Override
    public boolean canProcess(final WriteableCommandLine commandLine, final ListIterator<String> arguments) {
        return super.canProcess(commandLine, arguments);
    }

    @Override
    public Set getTriggers() {
        return super.getTriggers();
    }

    @Override
    public Set getPrefixes() {
        return super.getPrefixes();
    }

    @Override
    public void validate(final WriteableCommandLine commandLine) throws OptionException {
        super.validate(commandLine);
    }

    @Override
    public List helpLines(final int depth, final Set helpSettings, final Comparator comp) {
        return super.helpLines(depth, helpSettings, comp);
    }

    @Override
    public void appendUsage(final StringBuffer buffer, final Set helpSettings, final Comparator comp) {
        super.appendUsage(buffer, helpSettings, comp);
    }

    @Override
    public String getPreferredName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "This is a test option";
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public Option findOption(final String trigger) {
        return super.findOption(trigger);
    }

    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    @Override
    public Object getParent() {
        return super.getParent();
    }

    @Override
    public void setParent(final Object parent) {
        super.setParent(parent);
    }
}