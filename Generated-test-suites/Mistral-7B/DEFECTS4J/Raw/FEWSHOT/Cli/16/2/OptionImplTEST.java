package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {
    @Test
    public void testOptionImplConstraint() {
        OptionImpl option = new OptionImpl(1, false);
        assertFalse(option.isRequired());

        OptionImpl requiredOption = new OptionImpl(2, true);
        assertTrue(requiredOption.isRequired());
    }

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return argument.equals("test");
            }
        };

        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("test", "arg1", "arg2");
        Iterator<String> iterator = arguments.listIterator();

        assertTrue(option.canProcess(commandLine, iterator));
    }

    @Test
    public void testFindOption() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("o1", "opt1");
            }
        };

        OptionImpl option2 = new OptionImpl(2, true) {
            @Override
            public String getPreferredName() {
                return "option2";
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("o2", "opt2", "option2");
            }
        };

        OptionImpl option3 = new OptionImpl(3, false) {
            @Override
            public String getPreferredName() {
                return "option3";
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("o3", "opt3", "option3", "o3_long");
            }
        };

        Set<String> prefixes = new HashSet<>();
        prefixes.add("--longopt");

        Option found = option1.findOption("o1");
        assertNotNull(found);
        assertEquals(option1, found);

        found = option1.findOption("opt1");
        assertNotNull(found);
        assertEquals(option1, found);

        found = option2.findOption("o2");
        assertNotNull(found);
        assertEquals(option2, found);

        found = option2.findOption("opt2");
        assertNotNull(found);
        assertEquals(option2, found);

        found = option2.findOption("option2");
        assertNotNull(found);
        assertEquals(option2, found);

        found = option3.findOption("o3");
        assertNotNull(found);
        assertEquals(option3, found);

        found = option3.findOption("opt3");
        assertNotNull(found);
        assertEquals(option3, found);

        found = option3.findOption("option3");
        assertNotNull(found);
        assertEquals(option3, found);

        found = option3.findOption("o3_long");
        assertNotNull(found);
        assertEquals(option3, found);

        found = option1.findOption("o4");
        assertNull(found);
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "option2";
            }
        };

        OptionImpl option3 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return "option2";
            }
        };

        OptionImpl option3 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option1";
            }
        };

        assertEquals(option1, option1);
        assertEquals(option1, option3);
        assertNotEquals(option1, option2);
    }

    @Test
    public void testToString() {
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
            public List<String> getPrefixes() {
                return Arrays.asList("--longopt", "--shortopt");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("o", "opt");
            }
        };

        ResourceHelper helper = ResourceHelper.getResourceHelper();

        String expected = helper.getMessage(ResourceConstants.OPTION_TEMPLATE, "option",
                "--longopt, --shortopt", "This is an option");
        assertEquals(expected, option.toString());
    }

    @Test
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("o", "opt");
            }
        };

        Set<String> prefixes = new HashSet<>();
        prefixes.add("--longopt");

        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNoPrefixMatch() {
        OptionImpl option = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("o", "opt");
            }
        };

        Set<String> prefixes = new HashSet<>();
        prefixes.add("--another-prefix");

        option.checkPrefixes(prefixes);
    }
}