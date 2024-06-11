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
    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidPrefixes() {
        final OptionImpl option = new OptionImpl(1, false);
        final Set prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithValidPrefixes() {
        final OptionImpl option = new OptionImpl(1, false);
        option.setPrefixes(Arrays.asList("--prefix"));
        final Set prefixes = new HashSet<>();
        prefixes.add("--prefix");
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithInvalidTrigger() {
        final OptionImpl option = new OptionImpl(1, false);
        final Set prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);
        option.setPrefixes(Arrays.asList("--prefix"));
        option.setTriggers(Arrays.asList("trigger"));

        final ResourceHelper helper = ResourceHelper.getResourceHelper();
        final String message = helper.getMessage(ResourceConstants.OPTION_TRIGGER_NEEDS_PREFIX, "trigger", prefixes.toString());
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixesWithValidPrefixesAndTriggers() {
        final OptionImpl option = new OptionImpl(1, false);
        option.setPrefixes(Arrays.asList("--prefix"));
        final Set prefixes = new HashSet<>();
        prefixes.add("--prefix");
        option.checkPrefixes(prefixes);
        option.setTriggers(Arrays.asList("trigger1", "trigger2"));

        for (final String trigger : option.getTriggers()) {
            option.checkPrefixes(prefixes);
        }
    }

    @Test
    public void testCanProcess() {
        final OptionImpl option = new OptionImpl(1, false);
        final WriteableCommandLine commandLine = new WriteableCommandLine();
        final ListIterator<String> arguments = Arrays.asList("arg1", "arg2").listIterator();

        assertFalse(option.canProcess(commandLine, arguments));
        assertFalse(option.canProcess(commandLine, arguments));
        assertFalse(option.canProcess(commandLine, arguments));
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testFindOption() {
        final OptionImpl option1 = new OptionImpl(1, false);
        final OptionImpl option2 = new OptionImpl(2, false);
        final OptionImpl option3 = new OptionImpl(1, false);

        final Option foundOption = option1.findOption("trigger1");
        assertNull(foundOption);

        final Option foundOption2 = option1.findOption("trigger2");
        assertNull(foundOption2);

        final Option foundOption3 = option2.findOption("trigger1");
        assertNull(foundOption3);

        final Option foundOption4 = option3.findOption("trigger1");
        assertSame(option1, foundOption4);
    }

    @Test
    public void testHashCode() {
        final OptionImpl option1 = new OptionImpl(1, false);
        option1.setPreferredName("--option1");
        option1.setDescription("description1");
        option1.setPrefixes(Arrays.asList("prefix1", "prefix2"));
        option1.setTriggers(Arrays.asList("trigger1", "trigger2"));

        final OptionImpl option2 = new OptionImpl(1, false);
        option2.setPreferredName("--option2");
        option2.setDescription("description2");
        option2.setPrefixes(Arrays.asList("prefix3", "prefix4"));
        option2.setTriggers(Arrays.asList("trigger1", "trigger2"));

        final int option1HashCode = option1.hashCode();
        final int option2HashCode = option2.hashCode();

        assertTrue(option1HashCode != option2HashCode);
    }

    @Test
    public void testEquals() {
        final OptionImpl option1 = new OptionImpl(1, false);
        option1.setPreferredName("--option1");
        option1.setDescription("description1");
        option1.setPrefixes(Arrays.asList("prefix1", "prefix2"));
        option1.setTriggers(Arrays.asList("trigger1", "trigger2"));

        final OptionImpl option2 = new OptionImpl(1, false);
        option2.setPreferredName("--option1");
        option2.setDescription("description1");
        option2.setPrefixes(Arrays.asList("prefix1", "prefix2"));
        option2.setTriggers(Arrays.asList("trigger1", "trigger2"));

        assertTrue(option1.equals(option2));

        final OptionImpl option3 = new OptionImpl(1, false);
        option3.setPreferredName("--option1");
        option3.setDescription("description2");
        option3.setPrefixes(Arrays.asList("prefix1", "prefix2"));
        option3.setTriggers(Arrays.asList("trigger1", "trigger2"));

        assertFalse(option1.equals(option3));

        final OptionImpl option4 = new OptionImpl(2, false);
        option4.setPreferredName("--option1");
        option4.setDescription("description1");
        option4.setPrefixes(Arrays.asList("prefix1", "prefix2"));
        option4.setTriggers(Arrays.asList("trigger1", "trigger2"));

        assertFalse(option1.equals(option4));
    }

    @Test
    public void testToString() {
        final OptionImpl option = new OptionImpl(1, false);
        option.setPreferredName("--option");
        option.setDescription("description");
        option.setPrefixes(Arrays.asList("prefix"));
        option.setTriggers(Arrays.asList("trigger1", "trigger2"));

        final String expected = "--option\n" +
                "\tdescription\n" +
                "\t--prefix <prefix>\n" +
                "\t\ttrigger1\n" +
                "\t\ttrigger2\n";

        assertEquals(expected, option.toString());
    }
}