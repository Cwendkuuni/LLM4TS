package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.Tests;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for OptionImpl
 */
public class OptionImplTest {

    private OptionImpl option;
    private WriteableCommandLine commandLine;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        resourceHelper = new Tests.ResourceHelper();
        commandLine = new WriteableCommandLine();
        option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "Test Option";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Collections.singletonList("--test");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "option");
            }
        };
    }

    @Test
    public void testCanProcess() {
        Set<String> arguments = new HashSet<>();
        arguments.add("--test");
        arguments.add("testValue");

        assertTrue(option.canProcess(commandLine, arguments.listIterator()));

        assertFalse(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithEmptyPrefixList() {
        Set<String> prefixes = Collections.emptySet();
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("--test");

        option.checkPrefixes(prefixes);

        Set<String> prefixes2 = new HashSet<>();
        prefixes2.add("--other");
        option.checkPrefixes(prefixes2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidPrefix() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("--testother");

        option.checkPrefixes(prefixes);
    }

    @Test
    public void testEquals() {
        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "Test Option";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Collections.singletonList("--test");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test", "option");
            }
        };

        OptionImpl option3 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption2";
            }

            @Override
            public String getDescription() {
                return "Test Option 2";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Collections.singletonList("--test2");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test2", "option2");
            }
        };

        assertTrue(option.equals(option));
        assertTrue(option.equals(option2));
        assertFalse(option.equals(option3));
    }

    @Test
    public void testFindOption() {
        Option testOption = option.findOption("test");
        assertNotNull(testOption);
        assertTrue(testOption.equals(option));

        Option testOption2 = option.findOption("option");
        assertNotNull(testOption2);
        assertTrue(testOption2.equals(option));

        Option testOption3 = option.findOption("nonexistent");
        assertNull(testOption3);
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testHashCode() {
        OptionImpl option2 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "testOption";
            }

            @Override
            public String getDescription() {
                return "Test Option";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return Collections.singletonList("--test2");
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("test2", "option2");
            }
        };

        int hashCode = option.hashCode();
        int hashCode2 = option2.hashCode();

        assertEquals(hashCode, option.hashCode());
        assertNotEquals(hashCode, option2.hashCode());
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testToString() {
        String expected = "--test=Test Option";
        String actual = option.toString();

        assertEquals(expected, actual);
    }
}