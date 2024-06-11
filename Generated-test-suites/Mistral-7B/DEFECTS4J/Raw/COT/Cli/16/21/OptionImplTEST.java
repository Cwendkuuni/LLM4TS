import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.cli2.resource.ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        Option option = new OptionImpl(1, true);
        Assert.assertEquals(1, option.getId());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess_NoArguments() {
        Option option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Assert.assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_WrongArgument() {
        Option option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      String argument) {
                return argument.equals("arg");
            }
        };
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addArgument("arg1");
        Assert.assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_CorrectArgument() {
        Option option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      String argument) {
                return argument.equals("arg");
            }
        };
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addArgument("arg");
        Assert.assertTrue(option.canProcess(commandLine, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOption_NoMatch() {
        Option option = new OptionImpl(1, true);
        Option notMatchingOption = option.findOption("notmatch");
        Assert.assertNull(notMatchingOption);
    }

    @Test
    public void testFindOption_Match() {
        Option option = new OptionImpl(1, true) {
            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("trigger", "anotherTrigger"));
            }
        };
        Option matchingOption = option.findOption("trigger");
        Assert.assertSame(option, matchingOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_NoPrefixes() {
        Option option = new OptionImpl(1, true);
        Set prefixes = new HashSet();
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixes_NoMatch() {
        Option option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "preferred";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }
        };
        Set prefixes = new HashSet<>(Arrays.asList("prefix3"));
        Assert.assertDoesNotThrow(option::checkPrefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_Mismatch() {
        Option option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "preferred";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            };

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("trigger", "anotherTrigger"));
            }
        };

        Set prefixes = new HashSet<>(Arrays.asList("prefix3"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixes_Match() {
        Option option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "prefix1-option";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option"));
            }
        };

        Set prefixes = new HashSet<>(Arrays.asList("prefix1"));
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testToString() {
        Option option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "prefix1-option";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option"));
            }
        };

        String expected = "prefix1-option: description\n";
        String actual = option.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquals_DifferentClass() {
        Option option = new OptionImpl(1, true);
        Object notOption = new Object();
        Assert.assertFalse(option.equals(notOption));
    }

    @Test
    public void testEquals_SameOption() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "prefix1-option";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option"));
            }
        };

        Option option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "prefix1-option";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option"));
            }
        };
        Assert.assertTrue(option1.equals(option2));
    }

    @Test
    public void testHashCode_SameOption() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "prefix1-option";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option"));
            }
        };

        Option option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "prefix1-option";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option"));
            }
        };
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testHashCode_DifferentOptions() {
        Option option1 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "prefix1-option1";
            }

            @Override
            public String getDescription() {
                return "description1";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1"));
            }
        };

        Option option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "prefix1-option2";
            }

            @Override
            public String getDescription() {
                return "description2";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("prefix1", "prefix2"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option2"));
            }
        };
        Assert.assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testFindOption_EmptyTriggerList() {
        Option option = new OptionImpl(1, true) {
            @Override
            public Set<String> getTriggers() {
                return new HashSet<>();
            }
        };
        Assert.assertNull(option.findOption(null));
    }
}