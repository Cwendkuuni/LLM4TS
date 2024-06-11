package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.OptionImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OptionImplTest {

    @Test
    public void testOptionImplConstructor() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> arguments = Arrays.asList("arg1", "arg2", "option", "arg3")
                .listIterator();

        assertFalse(option.canProcess(commandLine, arguments));

        arguments = commandLine.getParsedArgs().listIterator();
        assertFalse(option.canProcess(commandLine, arguments));

        arguments = Arrays.asList("arg1", "arg2", "--option", "arg3")
                .listIterator();
        assertTrue(option.canProcess(commandLine, arguments));

        arguments = Arrays.asList("arg1", "arg2", "-o", "arg3")
                .listIterator();
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option";
            }

            @Override
            public String getDescription() {
                return "This is an option.";
            }

            @Override
            public Iterable<String> getPrefixes() {
                return new HashSet<>(Arrays.asList("--", "-o"));
            }

            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("option", "o");
            }
        };

        ResourceHelper helper = ResourceHelper.getResourceHelper();
        String expected = helper.getMessage(ResourceConstants.OPTION_DESCRIPTION, option.getId(),
                option.getPreferredName(), option.getDescription());

        assertEquals("This is an option.", option.toString());
        assertEquals(expected, option.toString(DisplaySetting.SHORT));
        assertEquals("Option " + option.getPreferredName() + ": " + option.getDescription(),
                option.toString(DisplaySetting.ALL));
    }

    @Test
    public void testFindOption() {
        OptionImpl option1 = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("opt1");
            }
        };
        OptionImpl option2 = new OptionImpl(2, false) {
            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("opt2");
            }
        };

        OptionImpl optionFound = option1.findOption("opt1");
        assertEquals(option1, optionFound);

        optionFound = option1.findOption("opt2");
        assertNull(optionFound);

        optionFound = option2.findOption("opt1");
        assertNull(optionFound);

        optionFound = option2.findOption("opt2");
        assertEquals(option2, optionFound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNoPrefix() {
        OptionImpl option = new OptionImpl(1, true);
        Set<String> emptySet = new HashSet<>();
        option.checkPrefixes(emptySet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithMismatchedPrefix() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("opt");
            }
        };
        Set<String> prefixes = new HashSet<>(Arrays.asList("--"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithMismatchedTrigger() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--");
            }
        };
        Set<String> prefixes = new HashSet<>(Arrays.asList("--opt"));
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithMultipleMismatchedPrefix() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--", "--opt");
            }
        };
        Set<String> prefixes = new HashSet<>(Arrays.asList("--opt"));
        option.checkPrefixes(prefixes);
    }
}