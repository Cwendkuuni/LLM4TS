package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {
    private static final String OPTION_NAME = "testOption";
    private static final String OPTION_ALIAS = "t";
    private static final String OPTION_DESCRIPTION = "This is a test option";
    private static final List<String> OPTION_PREFIXES = Arrays.asList("-to", "--test");
    private static final List<String> OPTION_TRIGGERS = Arrays.asList("test", "t");

    private WriteableCommandLine commandLine;
    private OptionImpl option;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        commandLine = new TestUtils.WriteableCommandLine("-test");
        option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return OPTION_NAME;
            }

            @Override
            public String getDescription() {
                return OPTION_DESCRIPTION;
            }

            @Override
            public List<String> getPrefixes() {
                return OPTION_PREFIXES;
            }

            @Override
            public List<String> getTriggers() {
                return OPTION_TRIGGERS;
            }
        };
        resourceHelper = ResourceHelper.getResourceHelper();
    }

    @Test
    public void testCanProcess() {
        assertFalse(option.canProcess(commandLine, commandLine.getArgs().listIterator()));

        List<String> args = new ArrayList<>(Arrays.asList("-t", "arg1", "arg2"));
        commandLine.setArgs(args);

        assertFalse(option.canProcess(commandLine, commandLine.getArgs().listIterator()));

        args.remove(0);
        assertTrue(option.canProcess(commandLine, commandLine.getArgs().listIterator()));
    }

    @Test
    public void testEquals() {
        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return OPTION_NAME;
            }

            @Override
            public String getDescription() {
                return OPTION_DESCRIPTION;
            }

            @Override
            public List<String> getPrefixes() {
                return OPTION_PREFIXES;
            }

            @Override
            public List<String> getTriggers() {
                return OPTION_TRIGGERS;
            }
        };

        OptionImpl option3 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "otherOption";
            }

            @Override
            public String getDescription() {
                return "This is another test option";
            }

            @Override
            public List<String> getPrefixes() {
                return new ArrayList<>();
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("other");
            }
        };

        Set<OptionImpl> options = new HashSet<>();
        options.add(option);
        options.add(option2);
        options.add(option3);

        assertTrue(option.equals(option));
        assertTrue(option2.equals(option2));
        assertTrue(option.equals(option2) == false);
        assertTrue(option3.equals(option3) == true);
        assertTrue(option.equals(option3) == false);
    }

    @Test
    public void testFindOption() {
        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return OPTION_NAME;
            }

            @Override
            public String getDescription() {
                return OPTION_DESCRIPTION;
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-to1", "--test1");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("test1");
            }
        };

        OptionImpl option3 = new OptionImpl(3, false) {
            @Override
            public String getPreferredName() {
                return OPTION_NAME;
            }

            @Override
            public String getDescription() {
                return OPTION_DESCRIPTION;
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-to2", "--test2");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("test2");
            }
        };

        Set<OptionImpl> options = new HashSet<>();
        options.add(option);
        options.add(option2);
        options.add(option3);

        OptionImpl foundOption = option.findOption("test");
        assertNull(foundOption);

        foundOption = option.findOption("test1");
        assertEquals(option2, foundOption);

        foundOption = option.findOption("test2");
        assertEquals(option3, foundOption);
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testHashCode() {
        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return OPTION_NAME;
            }

            @Override
            public String getDescription() {
                return OPTION_DESCRIPTION;
            }

            @Override
            public List<String> getPrefixes() {
                return OPTION_PREFIXES;
            }

            @Override
            public List<String> getTriggers() {
                return OPTION_TRIGGERS;
            }
        };

        OptionImpl option3 = new OptionImpl(3, true) {
            @Override
            public String getPreferredName() {
                return OPTION_NAME;
            }

            @Override
            public String getDescription() {
                return OPTION_DESCRIPTION;
            }

            @Override
            public List<String> getPrefixes() {
                return new ArrayList<>();
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("other");
            }
        };

        Set<OptionImpl> options = new HashSet<>();
        options.add(option);
        options.add(option2);
        options.add(option3);

        int hashCode = option.hashCode();
        int expectedHashCode = 1 * 37 + OPTION_NAME.hashCode() + OPTION_DESCRIPTION.hashCode()
                + OPTION_PREFIXES.hashCode() + OPTION_TRIGGERS.hashCode();
        assertEquals(expectedHashCode, hashCode);

        hashCode = option2.hashCode();
        expectedHashCode = 2 * 37 + OPTION_NAME.hashCode() + OPTION_DESCRIPTION.hashCode()
                + OPTION_PREFIXES.hashCode() + OPTION_TRIGGERS.hashCode();
        assertEquals(expectedHashCode, hashCode);

        hashCode = option3.hashCode();
        expectedHashCode = 3 * 37 + OPTION_NAME.hashCode() + OPTION_DESCRIPTION.hashCode()
                + new ArrayList<>().hashCode() + Arrays.asList("other").hashCode();
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testToString() {
        String expected = "OPTION: " + OPTION_NAME + ", ARGUMENT: ";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testFindOptionWithPrefixes() {
        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public String getPreferredName() {
                return OPTION_NAME;
            }

            @Override
            public String getDescription() {
                return OPTION_DESCRIPTION;
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-to1", "--test1");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("test1");
            }
        };

        OptionImpl option3 = new OptionImpl(3, false) {
            @Override
            public String getPreferredName() {
                return OPTION_NAME;
            }

            @Override
            public String getDescription() {
                return OPTION_DESCRIPTION;
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-to2", "--test2");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("test2");
            }
        };

        Set<OptionImpl> options = new HashSet<>();
        options.add(option);
        options.add(option2);
        options.add(option3);

        OptionImpl foundOption = null;
        for (OptionImpl option : options) {
            if (option.getPrefixes().contains("--test")) {
                foundOption = option;
                break;
            }
        }

        assertNotNull(foundOption);
        assertEquals(option2, foundOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidPrefix() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("--test");

        option.checkPrefixes(prefixes);

        // this should not be reached
    }

    @Test
    public void testCheckPrefixesWithValidPrefixes() {
        Set<String> prefixes = new HashSet<>(OPTION_PREFIXES);
        prefixes.add("--test");

        option.checkPrefixes(prefixes);
    }
}