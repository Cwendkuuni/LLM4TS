import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.OptionImpl;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        OptionImpl option = new OptionImpl(1, true);
        Assert.assertEquals(1, option.getId());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        String[] arguments = new String[]{"arg1", "arg2"};
        ListIterator<String> argumentListIterator = Arrays.asList(arguments).listIterator();

        Assert.assertTrue(option.canProcess(commandLine, argumentListIterator));
        Assert.assertEquals("arg1", option.canProcess(commandLine, argumentListIterator).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testCanProcessNoArguments() {
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> argumentListIterator = Arrays.asList(new String[0]).listIterator();

        option.canProcess(commandLine, argumentListIterator);
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true);
        Assert.assertNotNull(option.toString());
    }

    @Test
    public void testFindOption() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(2, false);

        Set<String> triggers1 = new HashSet<String>(Arrays.asList("opt1", "opt1a"));
        option1.setTriggers(triggers1);

        Set<String> triggers2 = new HashSet<String>(Arrays.asList("opt2", "opt2b"));
        option2.setTriggers(triggers2);

        Assert.assertEquals(option1, option1.findOption("opt1"));
        Assert.assertEquals(option1, option1.findOption("opt1a"));
        Assert.assertEquals(null, option1.findOption("opt2"));
        Assert.assertEquals(option2, option2.findOption("opt2"));
        Assert.assertEquals(null, option2.findOption("opt1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesMissingPrefix() {
        OptionImpl option = new OptionImpl(1, true);
        Set<String> prefixes = new HashSet<String>(Arrays.asList("prefix2"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesValidPrefixes() {
        OptionImpl option = new OptionImpl(1, true);
        Set<String> prefixes = new HashSet<String>(Arrays.asList("prefix1", "prefix2"));
        option.checkPrefixes(prefixes);
    }
}