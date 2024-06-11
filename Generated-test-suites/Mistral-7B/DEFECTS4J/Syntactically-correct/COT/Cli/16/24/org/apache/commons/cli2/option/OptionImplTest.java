import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        Option option = new OptionImpl(1, false);
        Assert.assertEquals(1, option.getId());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        Option option = new OptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

        Assert.assertFalse(option.canProcess(commandLine, arguments));

        arguments.add("optionArgument");
        Assert.assertTrue(option.canProcess(commandLine, arguments));
        arguments.previous(); // remove the added argument
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcessWithInvalidArgument() {
        Option option = new OptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

        arguments.add("invalidArgument");
        option.canProcess(commandLine, arguments);
    }

    @Test
    public void testToString() {
        Option option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "This is a test option";
            }

            @Override
            public Set getPrefixes() {
                return null;
            }

            @Override
            public Set getTriggers() {
                return null;
            }
        };

        String expected = "testOption: This is a test option";
        String actual = option.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Option option1 = new OptionImpl(1, false);
        Option option2 = new OptionImpl(1, false);
        Option option3 = new OptionImpl(2, false);

        Assert.assertTrue(option1.equals(option1));
        Assert.assertTrue(option1.equals(option2));
        Assert.assertFalse(option1.equals(option3));
    }

    @Test
    public void testHashCode() {
        Option option1 = new OptionImpl(1, false);
        Option option2 = new OptionImpl(1, false);
        Option option3 = new OptionImpl(2, false);

        Assert.assertEquals(option1.hashCode(), option1.hashCode());
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
        Assert.assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testFindOption() {
        Option option1 = new OptionImpl(1, false) {
            @Override
            public Set getTriggers() {
                return null;
            }
        };
        Option option2 = new OptionImpl(2, false) {
            @Override
            public Set getTriggers() {
                Set triggers = new java.util.HashSet<>();
                triggers.add("testTrigger");
                return triggers;
            }
        };

        Assert.assertNull(option1.findOption("nonExistentTrigger"));
        Assert.assertEquals(option2, option2.findOption("testTrigger"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOptionWithInvalidTrigger() {
        Option option = new OptionImpl(1, false) {
            @Override
            public Set getTriggers() {
                Set triggers = new java.util.HashSet<>();
                triggers.add("validTrigger");
                return triggers;
            }
        };

        option.findOption("nonExistentTrigger");
    }

    @Test
    public void testIsRequired() {
        Option option1 = new OptionImpl(1, false);
        Option option2 = new OptionImpl(2, true);

        Assert.assertFalse(option1.isRequired());
        Assert.assertTrue(option2.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithEmptyPrefixes() {
        Option option = new OptionImpl(1, false) {
            @Override
            public Set getPrefixes() {
                return new java.util.HashSet<>();
            }
        };

        option.checkPrefixes(new java.util.HashSet<>());
    }

    @Test
    public void testCheckPrefixesWithPrefix() {
        Option option = new OptionImpl(1, false) {
            @Override
            public Set getPrefixes() {
                Set prefixes = new java.util.HashSet<>();
                prefixes.add("testPrefix");
                return prefixes;
            }
        };

        Set prefixes = new java.util.HashSet<>();
        prefixes.add("testPrefix");
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNoMatchingPrefix() {
        Option option = new OptionImpl(1, false) {
            @Override
            public Set getPrefixes() {
                Set prefixes = new java.util.HashSet<>();
                prefixes.add("prefix1");
                return prefixes;
            }
        };

        Set prefixes = new java.util.HashSet<>();
        prefixes.add("prefix2");
        option.checkPrefixes(prefixes);
    }
}