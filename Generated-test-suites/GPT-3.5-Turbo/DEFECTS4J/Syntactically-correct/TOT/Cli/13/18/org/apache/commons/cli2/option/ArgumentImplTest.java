import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setup() {
        argument = new ArgumentImpl("arg", "Description", 1, 3, '\0', '\0', null, "--", null, 0);
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = argument.getPreferredName();

        Assert.assertEquals("arg", preferredName);
    }

    @Test
    public void testProcessValues_consumeRemaining() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1", "value2", "--", "value3", "value4").listIterator();
        OptionImpl option = new OptionImpl(0, false);

        argument.processValues(commandLine, arguments, option);

        List<String> values = commandLine.getValues(option);

        Assert.assertEquals(4, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
        Assert.assertEquals("value4", values.get(3));
    }

    @Test
    public void testProcessValues_subsequentSplit() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1,value2,value3").listIterator();
        OptionImpl option = new OptionImpl(0, false);
        argument = new ArgumentImpl("arg", "Description", 1, 3, '\0', ',', null, "--", null, 0);

        argument.processValues(commandLine, arguments, option);

        List<String> values = commandLine.getValues(option);

        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
    }

    @Test
    public void testProcessValues_singleValue() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Collections.singletonList("value1").listIterator();
        OptionImpl option = new OptionImpl(0, false);

        argument.processValues(commandLine, arguments, option);

        List<String> values = commandLine.getValues(option);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String arg = "value1";

        boolean canProcess = argument.canProcess(commandLine, arg);

        Assert.assertTrue(canProcess);
    }

    @Test
    public void testGetInitialSeparator() {
        char initialSeparator = argument.getInitialSeparator();

        Assert.assertEquals('\0', initialSeparator);
    }

    @Test
    public void testGetSubsequentSeparator() {
        char subsequentSeparator = argument.getSubsequentSeparator();

        Assert.assertEquals('\0', subsequentSeparator);
    }

    @Test
    public void testGetTriggers() {
        Set triggers = argument.getTriggers();

        Assert.assertEquals(Collections.EMPTY_SET, triggers);
    }

    @Test
    public void testGetConsumeRemaining() {
        String consumeRemaining = argument.getConsumeRemaining();

        Assert.assertEquals("--", consumeRemaining);
    }

    @Test
    public void testGetDefaultValues() {
        List defaultValues = argument.getDefaultValues();

        Assert.assertEquals(null, defaultValues);
    }

    @Test
    public void testGetValidator() {
        Validator validator = argument.getValidator();

        Assert.assertEquals(null, validator);
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        OptionImpl option = new OptionImpl(0, false);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");

        argument.validate(commandLine, option);
    }
}