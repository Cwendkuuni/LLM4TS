package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {

    private OptionImpl option;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        option = new OptionImpl(1, "option", "o", "op", "Option description") {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setValue(this, "value");
            }
        };
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testId() {
        Assert.assertEquals(1, option.getId());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, "option1", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option2 = new OptionImpl(1, "option2", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option2";
            }
        };

        OptionImpl option3 = new OptionImpl(1, "option1", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option4 = new OptionImpl(2, "option", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option";
            }
        };

        Assert.assertTrue(option1.equals(option1));
        Assert.assertTrue(option1.equals(option2));
        Assert.assertTrue(option1.equals(option3));
        Assert.assertFalse(option1.equals(option4));
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, "option1", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option2 = new OptionImpl(1, "option2", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option2";
            }
        };

        OptionImpl option3 = new OptionImpl(1, "option1", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option4 = new OptionImpl(2, "option", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option";
            }
        };

        Assert.assertEquals(option1.hashCode(), option1.hashCode());
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
        Assert.assertEquals(option1.hashCode(), option3.hashCode());
        Assert.neq(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option1 = new OptionImpl(1, "option1", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("op1"));
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("o1", "option1");
            }
        };

        OptionImpl option2 = new OptionImpl(2, "option2", null, null, null) {
            @Override
            public String getPreferredName() {
                return "option2";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<String>(Arrays.asList("op2"));
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("o2", "option2");
            }
        };

        Set<String> prefixes = new HashSet<String>(Arrays.asList("op", "o"));

        Assert.assertEquals(option1, option1.findOption("o1"));
        Assert.assertEquals(option1, option1.findOption("option1"));
        Assert.assertEquals(option1, option1.findOption("op1 option1"));
        Assert.assertEquals(option1, option1.findOption("--option1"));
        Assert.assertNull(option1.findOption("o2"));
        Assert.assertNull(option1.findOption("--option2"));

        Assert.assertEquals(option2, option2.findOption("o2"));
        Assert.assertEquals(option2, option2.findOption("option2"));
        Assert.assertEquals(option2, option2.findOption("op2 option2"));
        Assert.assertEquals(option2, option2.findOption("--option2"));
        Assert.assertNull(option2.findOption("o1"));
        Assert.assertNull(option2.findOption("--option1"));
    }

    @Test
    public void testCanProcess() {
        Set<String> arguments = new HashSet<String>(Arrays.asList("arg1", "arg2", "option", "arg3"));
        ListIterator<String> iterator = arguments.listIterator();

        Assert.assertFalse(option.canProcess(commandLine, iterator));

        iterator.next();
        Assert.assertFalse(option.canProcess(commandLine, iterator));

        iterator.next();
        Assert.assertTrue(option.canProcess(commandLine, iterator));

        commandLine.setValue(option, null);
        Assert.assertFalse(option.canProcess(commandLine, iterator));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Option description\n--o\n--op\n--option\n", option.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNoPrefixes() {
        option.checkPrefixes(new HashSet<String>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerWithoutPrefix() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("op"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerWithoutPrefixAndPrefixes() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("op1"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerWithoutPrefixAndDuplicatedPrefixes() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("op1", "op1"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerWithPrefix() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("op"));
        option.checkPrefixes(prefixes);

        Assert.assertEquals("Option description\n--o\n--op\n--option\n", option.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerWithDuplicatedPrefixes() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("o1", "o1"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerWithPrefixedTrigger() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("oo"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerWithPrefixedTriggerAndDuplicatedPrefixes() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("oo", "oo"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithTriggerWithPrefixedTriggerAndDuplicatedTriggers() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("oo", "oo"));
        List<String> triggers = Arrays.asList("o1", "o2", "o1");
        option = new OptionImpl(1, "option", null, null, null) {
            @Override
            public String getPreferredName() {
                return "";
            }

            @Override
            public Set<String> getPrefixes() {
                return prefixes;
            }

            @Override
            public List<String> getTriggers() {
                return triggers;
            }
        };

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<String>(Arrays.asList("o"));
        option.checkPrefixes(prefixes);

        Assert.assertEquals("Option description\n--o\n--op\n--option\n", option.toString());
    }
}