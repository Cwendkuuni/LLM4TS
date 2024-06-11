package org.apache.commons.cli2.option;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OptionImplTest {

    private Option option;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        resourceHelper = ResourceHelper.getResourceHelper();
        option = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .create();
    }

    @Test
    public void testEquals() {
        Option option2 = OptionBuilder.withLongOpt("test2")
                .withDescription("Test option 2")
                .create();

        assertTrue(option.equals(option));
        assertFalse(option.equals(option2));

        Option clone = option.clone();
        assertTrue(option.equals(clone));
    }

    @Test
    public void testHashCode() {
        Option option2 = OptionBuilder.withLongOpt("test2")
                .withDescription("Test option 2")
                .create();

        assertEquals(option.hashCode(), option.hashCode());
        assertNotEquals(option.hashCode(), option2.hashCode());
    }

    @Test
    public void testFindOption() {
        Option option2 = OptionBuilder.withLongOpt("test2")
                .withDescription("Test option 2")
                .create();

        Set<String> triggers = new HashSet<>(Arrays.asList("test", "test2"));
        Set<String> prefixes = new HashSet<>();

        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public Set<String> getTriggers() {
                return triggers;
            }

            @Override
            public Set<String> getPrefixes() {
                return prefixes;
            }
        };

        Option foundOption = optionImpl.findOption("test");
        assertEquals(option, foundOption);

        assertNull(optionImpl.findOption("test3"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<>();

        OptionImpl optionImpl = new OptionImpl(1, true) {
            @Override
            public Set<String> getTriggers() {
                return Arrays.asList("test").iterator();
            }

            @Override
            public Set<String> getPrefixes() {
                return prefixes;
            }
        };

        optionImpl.checkPrefixes(prefixes);

        prefixes.add("t");
        optionImpl.checkPrefixes(prefixes);
    }

    @Test
    public void testCanProcess() throws ParseException {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();

        assertTrue(option.canProcess(commandLine, arguments));

        arguments.next();
        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        assertEquals("Test option", option.toString());
    }

    @Test
    public void testId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        option.defaults(commandLine);

        assertFalse(commandLine.hasOption("test"));
    }
}