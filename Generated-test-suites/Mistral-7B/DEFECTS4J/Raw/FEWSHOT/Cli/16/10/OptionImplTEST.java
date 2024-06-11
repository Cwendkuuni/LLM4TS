package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.OptionImpl;
import org.apache.commons.cli2.option.OptionType;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class OptionImplTest {

    @Test
    public void testConstructor() {
        final OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }

    @Test
    public void testEquals() {
        final OptionImpl option1 = new OptionImpl(1, true);
        final OptionImpl option2 = new OptionImpl(1, true);
        final OptionImpl option3 = new OptionImpl(2, true);

        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
    }

    @Test
    public void testCanProcess() {
        final OptionImpl option = new OptionImpl(1, true);

        assertFalse(option.canProcess(null, null));
        assertFalse(option.canProcess(null, ""));
        assertFalse(option.canProcess(null, "foo"));

        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final String[] arguments = {"foo"};
        final ListIterator iterator = Arrays.asList(arguments).listIterator();

        assertFalse(option.canProcess(commandLine, iterator));

        final WriteableCommandLine commandLineWithArgument = new WriteableCommandLine();
        commandLineWithArgument.parse(arguments);

        assertTrue(option.canProcess(commandLineWithArgument, iterator));
    }

    @Test
    public void testToString() {
        final OptionImpl option = new OptionImpl(1, true);
        final StringBuilder buffer = new StringBuilder();

        option.appendUsage(buffer, DisplaySetting.ALL, null);

        assertEquals("--1\n" +
                "optional: true\n", buffer.toString());
    }

    @Test
    public void testFindOption() {
        final OptionImpl option1 = new OptionImpl(1, true);
        final OptionImpl option2 = new OptionImpl(2, false);
        final OptionImpl option3 = new OptionImpl(3, true);

        Set<String> triggers1 = new HashSet<>(Arrays.asList("t1", "t2"));
        Set<String> triggers2 = new HashSet<>(Arrays.asList("t3"));
        Set<String> triggers3 = new HashSet<>(Arrays.asList("t4", "t5"));

        option1.setTriggers(triggers1);
        option2.setTriggers(triggers2);
        option3.setTriggers(triggers3);

        assertEquals(option1, option1.findOption("t1"));
        assertEquals(option1, option1.findOption("t2"));
        assertEquals(null, option1.findOption("t3"));
        assertEquals(option2, option2.findOption("t3"));
        assertEquals(null, option2.findOption("t1"));
        assertEquals(option3, option3.findOption("t4"));
        assertEquals(option3, option3.findOption("t5"));
    }

    @Test
    public void testIsRequired() {
        final OptionImpl option1 = new OptionImpl(1, true);
        final OptionImpl option2 = new OptionImpl(2, false);

        assertTrue(option1.isRequired());
        assertFalse(option2.isRequired());
    }

    @Test
    public void testCheckPrefixes() {
        final OptionImpl option = new OptionImpl(1, true);

        final Set prefixes = new HashSet<>(Arrays.asList("p"));

        option.setPrefixes(prefixes);

        option.checkPrefixes(prefixes);

        final ResourceHelper helper = ResourceHelper.getResourceHelper();
        final String message = helper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, "t",
                prefixes.toString());
        try {
            option.checkPrefixes(prefixes);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(message, e.getMessage());
        }

        option.checkPrefixes(new HashSet<>());
    }

    @Test
    public void testDefaults() {
        final OptionImpl option = new OptionImpl(1, true);

        option.defaults(new WriteableCommandLine());
    }
}