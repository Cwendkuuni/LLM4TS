import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.junit.Assert;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        OptionImpl option = new OptionImpl(1, false);
        Assert.assertEquals(1, option.getId());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testCanProcessTrue() {
        OptionImpl option = new OptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

        arguments.add("optionArgument");
        Assert.assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testCanProcessFalse() {
        OptionImpl option = new OptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

        Assert.assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesMissingPrefix() {
        OptionImpl option = new OptionImpl(1, false);
        Set prefixes = java.util.Collections.singleton("prefix");
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesMatchingPrefix() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "prefixOption";
            }

            @Override
            public Iterable<String> getTriggers() {
                return java.util.Arrays.asList("prefixOption", "longPrefix");
            }
        };
        Set prefixes = java.util.Collections.singleton("prefix");
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesMismatchingPrefix() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "prefixOption";
            }

            @Override
            public Iterable<String> getTriggers() {
                return java.util.Arrays.asList("prefixOption", "longOption");
            }
        };
        Set prefixes = java.util.Collections.singleton("prefix");
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public Iterable<String> getTriggers() {
                return java.util.Arrays.asList("prefixOption", "longOption");
            }
        };

        Assert.assertEquals(option, option.findOption("prefixOption"));
        Assert.assertNull(option.findOption("longOption"));
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);

        Assert.assertEquals(option1, option2);

        OptionImpl option3 = new OptionImpl(2, false);
        Assert.assertNotEquals(option1, option3);

        OptionImpl option4 = new OptionImpl(1, true);
        Assert.assertNotEquals(option1, option4);

        OptionImpl option5 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "newPreferredName";
            }
        };
        Assert.assertNotEquals(option1, option5);

        OptionImpl option6 = new OptionImpl(1, false) {
            @Override
            public Iterable<String> getTriggers() {
                return java.util.Arrays.asList("newTrigger");
            }
        };
        Assert.assertNotEquals(option1, option6);
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);

        Assert.assertEquals(option1.hashCode(), option2.hashCode());

        OptionImpl option3 = new OptionImpl(2, false);
        Assert.assertNotEquals(option1.hashCode(), option3.hashCode());

        OptionImpl option4 = new OptionImpl(1, true);
        Assert.assertNotEquals(option1.hashCode(), option4.hashCode());

        OptionImpl option5 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "newPreferredName";
            }
        };
        Assert.assertNotEquals(option1.hashCode(), option5.hashCode());

        OptionImpl option6 = new OptionImpl(1, false) {
            @Override
            public Iterable<String> getTriggers() {
                return java.util.Arrays.asList("newTrigger");
            }
        };
        Assert.assertNotEquals(option1.hashCode(), option6.hashCode());
    }
}