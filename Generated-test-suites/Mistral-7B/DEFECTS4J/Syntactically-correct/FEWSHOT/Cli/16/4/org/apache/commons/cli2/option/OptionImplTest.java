package org.apache.commons.cli2.option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

public class OptionImplTest {

    private ResourceHelper helper;
    private OptionImpl option;

    @Before
    public void setup() {
        helper = ResourceHelper.getResourceHelper();
        option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "foo";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-p", "--prefix");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("-f", "--foo");
            }
        };
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Set<String> arguments = new HashSet<>();

        assertTrue(option.canProcess(commandLine, arguments.iterator()));

        arguments.add("--prefix");
        assertFalse(option.canProcess(commandLine, arguments.iterator()));

        arguments.add("--foo");
        assertTrue(option.canProcess(commandLine, arguments.iterator()));

        arguments.add("--prefix foo");
        assertTrue(option.canProcess(commandLine, arguments.iterator()));

        arguments.add("--prefix bar");
        assertFalse(option.canProcess(commandLine, arguments.iterator()));

        arguments.add("-f");
        assertTrue(option.canProcess(commandLine, arguments.iterator()));

        arguments.add("-p");
        assertTrue(option.canProcess(commandLine, arguments.iterator()));
    }

    @Test
    public void testEquals() {
        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "foo";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-p", "--prefix");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("-f", "--foo");
            }
        };

        OptionImpl option3 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "foo";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-p", "--prefix");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("-f", "--foo");
            }
        };

        OptionImpl option4 = new OptionImpl(2, true) {
            @Override
            public String getPreferredName() {
                return "foo";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-p", "--prefix");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("-f", "--foo");
            }
        };

        assertTrue(option.equals(option));
        assertTrue(option.equals(option2));
        assertFalse(option.equals(option3));
        assertFalse(option.equals(option4));
    }

    @Test
    public void testHashCode() {
        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "foo";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-p", "--prefix");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("-f", "--foo");
            }
        };

        OptionImpl option3 = new OptionImpl(1, false) {
            @Override
            public String getPreferredName() {
                return "foo";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-p", "--prefix");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("-f", "--foo");
            }
        };

        OptionImpl option4 = new OptionImpl(2, true) {
            @Override
            public String getPreferredName() {
                return "foo";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-p", "--prefix");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("-f", "--foo");
            }
        };

        assertEquals(option.hashCode(), option.hashCode());
        assertEquals(option.hashCode(), option2.hashCode());
        assertNotEquals(option.hashCode(), option3.hashCode());
        assertNotEquals(option.hashCode(), option4.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option2 = new OptionImpl(2, true) {
            @Override
            public String getPreferredName() {
                return "foo";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public List<String> getPrefixes() {
                return Arrays.asList("-p", "--prefix");
            }

            @Override
            public List<String> getTriggers() {
                return Arrays.asList("-f", "--foo");
            }
        };

        assertTrue(option.findOption("--foo") == option);
        assertTrue(option.findOption("-f") == option);
        assertNull(option.findOption("--bar"));
        assertNull(option.findOption("-b"));
        assertNull(option.findOption(null));
        assertNull(option.findOption(new Object()));
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        option.defaults(new WriteableCommandLine());
    }

    @Test
    public void testCheckPrefixes() {
        List<String> prefixes = Arrays.asList("-p1", "--prefix1", "prefix1");

        option.checkPrefixes(prefixes);

        prefixes = Arrays.asList("-p", "--prefix", "prefix");
        try {
            option.checkPrefixes(prefixes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = helper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, "-f", prefixes.toString());
            assertEquals(expectedMessage, e.getMessage());
        }

        prefixes = Arrays.asList("prefix1", "prefix2");
        try {
            option.checkPrefixes(prefixes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String expectedMessage = helper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, "-f", prefixes.toString());
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}