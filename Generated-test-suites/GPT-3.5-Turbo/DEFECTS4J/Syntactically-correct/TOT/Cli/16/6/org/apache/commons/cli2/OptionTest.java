import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class OptionTest {

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();

        List<String> args = new ArrayList<>();
        args.add("-f");
        args.add("file.txt");

        ListIterator<String> argsIterator = args.listIterator();
        try {
            option.process(commandLine, argsIterator);
        } catch (OptionException e) {
            Assert.fail("Failed to process arguments");
        }

        Assert.assertEquals("file.txt", commandLine.getOptionValue("f"));
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();

        option.defaults(commandLine);

        Assert.assertNotNull(commandLine);
    }

    @Test
    public void testCanProcessSingleArgument() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();

        boolean canProcess = option.canProcess(commandLine, "-f");

        Assert.assertTrue(canProcess);
    }

    @Test
    public void testCanProcessMultipleArguments() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();

        List<String> args = new ArrayList<>();
        args.add("-f");
        args.add("file.txt");

        ListIterator<String> argsIterator = args.listIterator();
        boolean canProcess = option.canProcess(commandLine, argsIterator);

        Assert.assertTrue(canProcess);
    }

    @Test
    public void testGetTriggers() {
        Option option = new OptionImpl();

        Set triggers = option.getTriggers();

        Assert.assertNotNull(triggers);
    }

    @Test
    public void testGetPrefixes() {
        Option option = new OptionImpl();

        Set prefixes = option.getPrefixes();

        Assert.assertNotNull(prefixes);
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl();

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            Assert.fail("Failed to validate command line");
        }

        Assert.assertTrue(true);
    }

    @Test
    public void testHelpLines() {
        Option option = new OptionImpl();

        List helpLines = option.helpLines(0, null, null);

        Assert.assertNotNull(helpLines);
    }

    @Test
    public void testAppendUsage() {
        Option option = new OptionImpl();

        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, null, null);

        Assert.assertTrue(buffer.length() > 0);
    }

    @Test
    public void testGetPreferredName() {
        Option option = new OptionImpl();

        String preferredName = option.getPreferredName();

        Assert.assertNotNull(preferredName);
    }

    @Test
    public void testGetDescription() {
        Option option = new OptionImpl();

        String description = option.getDescription();

        Assert.assertNotNull(description);
    }

    @Test
    public void testGetId() {
        Option option = new OptionImpl();

        int id = option.getId();

        Assert.assertTrue(id >= 0);
    }

    @Test
    public void testFindOption() {
        Option option = new OptionImpl();

        Option foundOption = option.findOption("f");

        Assert.assertNotNull(foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new OptionImpl();

        boolean isRequired = option.isRequired();

        Assert.assertFalse(isRequired);
    }

    @Test
    public void testGetParent() {
        Option option = new OptionImpl();

        Option parent = option.getParent();

        Assert.assertNull(parent);
    }

    @Test
    public void testSetParent() {
        Option option = new OptionImpl();
        Option parent = new OptionImpl();

        option.setParent(parent);

        Assert.assertEquals(parent, option.getParent());
    }
}