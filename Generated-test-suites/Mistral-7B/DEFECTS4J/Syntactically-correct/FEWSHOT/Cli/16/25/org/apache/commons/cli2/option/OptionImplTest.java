package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.parser.DefaultParser;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Test;

public class OptionImplTest {
    @Test
    public void testOptionImplHashCode() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(1, true);

        assertEquals(option1.hashCode(), option2.hashCode());

        OptionImpl option3 = new OptionImpl(2, false);
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getTriggers() {
                return Arrays.asList("a", "b");
            }
        };

        assertTrue(option.findOption("a") instanceof OptionImpl);
        assertNull(option.findOption("c"));
    }

    @Test
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public Iterable<String> getPrefixes() {
                return Arrays.asList("--", "-p-");
            }
        };

        Set<String> prefixes = new HashSet<>();
        prefixes.add("--");
        prefixes.add("--prefix");

        option.checkPrefixes(prefixes);

        Set<String> invalidPrefixes = new HashSet<>();
        invalidPrefixes.add("--invalid");

        try {
            option.checkPrefixes(invalidPrefixes);
            fail();
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Option " + OptionImpl.class.getName() +
                    " with id 1 needs prefixes: [--, -p-], but found [--invalid]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return argument.equals("value");
            }
        };

        WriteableCommandLine commandLine = new DefaultCommandLine();
        String[] args = {"--option1", "value"};

        boolean result = option.canProcess(commandLine, Arrays.asList(args).listIterator());
        assertTrue(result);

        String[] invalidArgs = {"--option1", "invalid"};

        result = option.canProcess(commandLine, Arrays.asList(invalidArgs).listIterator());
        assertFalse(result);

        String[] noOptionArgs = {"--no-option"};

        result = option.canProcess(commandLine, Arrays.asList(noOptionArgs).listIterator());
        assertFalse(result);
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
                return "This is an option";
            }
        };

        ResourceHelper resourceHelper = ResourceHelper.getResourceHelper();

        String expected = resourceHelper.getMessage(ResourceConstants.OPTION_PREFIX, "--",
                option.getId(), option.getPreferredName()) + "\n" +
                option.getDescription() + "\n";

        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true);
        OptionImpl option2 = new OptionImpl(1, true);

        assertEquals(option1, option2);

        OptionImpl option3 = new OptionImpl(2, false);
        assertNotEquals(option1, option3);

        OptionImpl option4 = new OptionImpl(1, true) {
            @Override
            public String getPreferredName() {
                return "option2";
            }
        };
        assertNotEquals(option1, option4);
    }

    @Test
    public void testId() {
        OptionImpl option = new OptionImpl(1, true);
        assertEquals(1, option.getId());

        OptionImpl option2 = new OptionImpl(2, true);
        assertEquals(2, option2.getId());
    }
}