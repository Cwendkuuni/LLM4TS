package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OptionImplTest {

    private Option option;
    private WriteableCommandLine commandLine;
    private ResourceHelper resourceHelper;

    @Before
    public void setUp() {
        option = new OptionImpl(1, "option1") {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setOptionValue(getId(), "defaultValue1");
            }
        };
        commandLine = TestUtils.createCommandLine();
        resourceHelper = ResourceHelper.getResourceHelper();
    }

    @Test
    public void testOptionCanProcess() {
        List<String> arguments = new ArrayList<>();
        arguments.add("option1");
        assertTrue(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionCannotProcess() {
        List<String> arguments = new ArrayList<>();
        assertFalse(option.canProcess(commandLine, arguments.listIterator()));
        arguments.next();
    }

    @Test
    public void testOptionToString() {
        String expected = resourceHelper.getMessage(ResourceConstants.OPTION_FORMAT,
                option.getId(), option.getPreferredName());
        assertEquals(expected, option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option2 = new OptionImpl(1, "option1") {
            @Override
            public void defaults(WriteableCommandLine commandLine) {
                commandLine.setOptionValue(getId(), "defaultValue2");
            }
        };

        assertNotEquals(option, option2);

        Option option3 = new OptionImpl(1, "option1");
        assertEquals(option, option3);
    }

    @Test
    public void testOptionHashCode() {
        Option option2 = new OptionImpl(1, "option2");
        assertNotEquals(option.hashCode(), option2.hashCode());

        Option option3 = new OptionImpl(1, "option1");
        assertEquals(option.hashCode(), option3.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionFindOptionNullTrigger() {
        assertNull(option.findOption(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionFindOptionInvalidTrigger() {
        assertNull(option.findOption("invalidTrigger"));
    }

    @Test
    public void testOptionFindOptionValidTrigger() {
        Option expected = option;
        Option found = option.findOption("option1");
        assertEquals(expected, found);
    }

    @Test
    public void testOptionIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionDefaults() {
        option.defaults(commandLine);
        assertEquals("defaultValue1", commandLine.getOptionValue(option.getId()));
    }

    @Test(expected = IllegalStateException.class)
    public void testOptionCheckPrefixesEmptyPrefixSet() {
        Set<String> prefixes = Set.of();
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testOptionCheckPrefixesValidPrefix() {
        Set<String> prefixes = Set.of("--option");
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCheckPrefixesInvalidPrefix() {
        Set<String> prefixes = Set.of("--invalid");
        option.checkPrefixes(prefixes);
    }
}