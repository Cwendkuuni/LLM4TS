package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.TestHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OptionImplTest {

    @org.junit.Test
    public void testOptionImplConstructor() {
        OptionImpl option = new OptionImpl(1, false);
        assertEquals(1, option.getId());
        assertFalse(option.isRequired());
    }

    @org.junit.Test
    public void testCanProcessWithArguments() {
        OptionImpl option = new OptionImpl(1, false);

        WriteableCommandLine commandLine = TestHelper.getForTest();
        ListIterator<String> arguments = TestHelper.getArgumentList(new String[]{"--option1", "arg1"}).listIterator();

        assertTrue(option.canProcess(commandLine, arguments));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testCanProcessWithoutArguments() {
        OptionImpl option = new OptionImpl(1, false);

        WriteableCommandLine commandLine = TestHelper.getForTest();
        ListIterator<String> arguments = TestHelper.getEmptyList().listIterator();

        option.canProcess(commandLine, arguments);
    }

    @org.junit.Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt1"));
            }
        };

        StringBuilder expected = new StringBuilder();
        expected.append("--o,option1, This is an option");

        assertEquals(expected.toString(), option.toString());
    }

    @org.junit.Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt1"));
            }
        };

        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt1"));
            }
        };

        OptionImpl option3 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "option2";
            }

            @Override
            public String getDescription() {
                return "This is another option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option2", "opt2"));
            }
        };

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }

    @org.junit.Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt1"));
            }
        };

        OptionImpl option2 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option1", "opt1"));
            }
        };

        OptionImpl option3 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "option2";
            }

            @Override
            public String getDescription() {
                return "This is another option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option2", "opt2"));
            }
        };

        int option1HashCode = option1.hashCode();
        int option2HashCode = option2.hashCode();
        int option3HashCode = option3.hashCode();

        assertEquals(option1HashCode, option2.hashCode());
        assertNotEquals(option1HashCode, option3.hashCode());
    }

    @org.junit.Test
    public void testFindOptionWithMatchingTrigger() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option", "opt"));
            }
        };

        Option foundOption = option.findOption("option");
        assertEquals(option, foundOption);
    }

    @org.junit.Test
    public void testFindOptionWithoutMatchingTrigger() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option", "opt"));
            }
        };

        Option foundOption = option.findOption("other");
        assertNull(foundOption);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithMismatchedPrefixes() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option";
            }

            @Override
            public Set<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--o1"));
            }

            @Override
            public Set<String> getTriggers() {
                return new HashSet<>(Arrays.asList("option", "opt"));
            }
        };

        Set<String> prefixes = new HashSet<>(Arrays.asList("--o"));
        option.checkPrefixes(prefixes);
    }
}