import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        Option option = new OptionImpl(1, true);
        Assert.assertEquals(1, option.getId());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess_WhenArgumentPresent_ReturnsTrue() {
        Option option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = Arrays.asList("arg1", "option", "arg3")
                .listIterator();

        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testCanProcess_WhenNoArgumentPresent_ReturnsFalse() {
        Option option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getArgs().listIterator();

        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_WhenPrefixesContainInvalidPrefix_ThrowsException() {
        Option option = new OptionImpl(1, true);
        Set<String> prefixes = new HashSet<>(Arrays.asList("invalid", "prefix"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testToString() {
        Option option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "optionName";
            }

            @Override
            public String getDescription() {
                return "option description";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("prefix1", "prefix2");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("trigger1", "trigger2");
            }
        };

        String expected = "optionName" + System.lineSeparator() +
                "  option description" + System.lineSeparator() +
                "  [prefix1, prefix2]" + System.lineSeparator() +
                "  {trigger1, trigger2}" + System.lineSeparator();

        Assert.assertEquals(expected, option.toString());
    }

    @Test
    public void testFindOption_WhenTriggerMatches_ReturnsOption() {
        Option option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("trigger");
            }
        };

        Option foundOption = option.findOption("trigger");
        Assert.assertEquals(option, foundOption);
    }

    @Test
    public void testFindOption_WhenTriggerDoesNotMatch_ReturnsNull() {
        Option option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("trigger1", "trigger2");
            }
        };

        Option foundOption = option.findOption("trigger3");
        Assert.assertNull(foundOption);
    }
}