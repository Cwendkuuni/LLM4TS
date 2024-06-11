import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.ListIterator;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testProcessValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        ListIterator arguments = Mockito.mock(ListIterator.class);
        Option option = Mockito.mock(Option.class);

        Mockito.when(arguments.hasNext()).thenReturn(true, false);
        Mockito.when(arguments.next()).thenReturn("value");
        Mockito.when(commandLine.looksLikeOption("value")).thenReturn(false);

        argument.processValues(commandLine, arguments, option);

        Mockito.verify(commandLine).addValue(option, "value");
    }

    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        String arg = "value";

        Assert.assertTrue(argument.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals(0, argument.getPrefixes().size());
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals(',', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals(';', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetTriggers() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals(0, argument.getTriggers().size());
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals(Arrays.asList("default"), argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Validator validator = Mockito.mock(Validator.class);
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', validator, "--", Arrays.asList("default"), 0);
        Assert.assertEquals(validator, argument.getValidator());
    }

    @Test
    public void testValidate() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);

        Mockito.when(commandLine.getValues(option)).thenReturn(Arrays.asList("value"));

        argument.validate(commandLine, option);

        Mockito.verify(commandLine).getValues(option);
    }

    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, Integer.MAX_VALUE, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Mockito.mock(Set.class);
        Comparator comp = Mockito.mock(Comparator.class);

        argument.appendUsage(buffer, helpSettings, comp);

        Assert.assertEquals("name ...", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        int depth = 0;
        Set helpSettings = Mockito.mock(Set.class);
        Comparator comp = Mockito.mock(Comparator.class);

        Assert.assertEquals(1, argument.helpLines(depth, helpSettings, comp).size());
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 5, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals(5, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 2, 5, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        Assert.assertEquals(2, argument.getMinimum());
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);

        Assert.assertEquals("value", argument.stripBoundaryQuotes("\"value\""));
        Assert.assertEquals("value", argument.stripBoundaryQuotes("value"));
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl argument1 = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        ArgumentImpl argument2 = new ArgumentImpl("name", "description", 0, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);

        Assert.assertTrue(argument1.isRequired());
        Assert.assertFalse(argument2.isRequired());
    }

    @Test
    public void testDefaults() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);

        argument.defaults(commandLine);

        Mockito.verify(commandLine).setDefaultValues(argument, Arrays.asList("default"));
    }

    @Test
    public void testDefaultValues() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, ',', ';', Mockito.mock(Validator.class), "--", Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Mockito.mock(Option.class);

        argument.defaultValues(commandLine, option);

        Mockito.verify(commandLine).setDefaultValues(option, Arrays.asList("default"));
    }
}