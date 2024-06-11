package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    private Option option;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLine();
        option = new Option() {
            @Override
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
            }

            @Override
            public void defaults(WriteableCommandLine commandLine) {
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            @Override
            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return false;
            }

            @Override
            public Set getTriggers() {
                return new HashSet();
            }

            @Override
            public Set getPrefixes() {
                return new HashSet();
            }

            @Override
            public void validate(WriteableCommandLine commandLine) throws OptionException {
            }

            @Override
            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                return null;
            }

            @Override
            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
            }

            @Override
            public String getPreferredName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public int getId() {
                return 0;
            }

            @Override
            public Option findOption(String trigger) {
                return null;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public Option getParent() {
                return null;
            }

            @Override
            public void setParent(Option parent) {
            }
        };
    }

    @Test
    public void testGetTriggers() {
        Set triggers = option.getTriggers();
        assertTrue(triggers != null && triggers.isEmpty());
    }

    @Test
    public void testGetPrefixes() {
        Set prefixes = option.getPrefixes();
        assertTrue(prefixes != null && prefixes.isEmpty());
    }

    @Test
    public void testCanProcessNull() {
        assertFalse(option.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessEmpty() {
        ListIterator args = Arrays.asList("").listIterator();
        assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcessNoMatch() {
        ListIterator args = Arrays.asList("--foo", "--bar").listIterator();
        assertFalse(option.canProcess(commandLine, args));
    }

    @Test
    public void testCanProcessMatch() {
        ListIterator args = Arrays.asList("--myOption", "--myOptionValue").listIterator();
        assertTrue(option.canProcess(commandLine, args));
    }

    @Test
    public void testProcess() {
        ListIterator args = Arrays.asList("--myOption", "--myOptionValue").listIterator();
        option.process(commandLine, args);
        assertNull(commandLine.getOptionValues("myOption"));
        assertNull(commandLine.getOptionValues("myOptionValue"));
    }

    @Test
    public void testProcessWithValue() {
        ListIterator args = Arrays.asList("--myOption", "--myOptionValue=foo").listIterator();
        option.process(commandLine, args);
        assertEquals("foo", commandLine.getOptionValues("myOptionValue")[0]);
    }

    @Test(expected = OptionException.class)
    public void testProcessException() throws OptionException {
        ListIterator args = Arrays.asList("--myOption", "--myOptionValue").listIterator();
        option.process(commandLine, args);
        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        option.defaults(commandLine);
        assertNull(commandLine.getOptionValues("myOption"));
        assertNull(commandLine.getOptionValues("myOptionValue"));
    }

    @Test
    public void testValidate() throws OptionException {
        option.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateException() throws OptionException {
        commandLine.putOptionValue("myOption", "invalid");
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        List helpLines = option.helpLines(0, new HashSet(), null);
        assertNull(helpLines);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        option.appendUsage(buffer, new HashSet(), null);
        assertNull(buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        String preferredName = option.getPreferredName();
        assertNull(preferredName);
    }

    @Test
    public void testGetDescription() {
        String description = option.getDescription();
        assertNull(description);
    }

    @Test
    public void testGetId() {
        int id = option.getId();
        assertEquals(0, id);
    }

    @Test
    public void testFindOptionNull() {
        assertNull(option.findOption(null));
    }

    @Test
    public void testFindOptionEmpty() {
        assertNull(option.findOption(new String[0]));
    }

    @Test
    public void testFindOptionNoMatch() {
        Option otherOption = new Option() {
            @Override
            public String getPreferredName() {
                return "otherOption";
            }

            @Override
            public int getId() {
                return 1;
            }
        };
        assertNull(option.findOption(new String[]{"--myOption"}));
        assertNull(option.findOption(new String[]{"--otherOption"}));
    }

    @Test
    public void testFindOptionMatch() {
        Option otherOption = new Option() {
            @Override
            public String getPreferredName() {
                return "myOption";
            }

            @Override
            public int getId() {
                return 1;
            }
        };
        option.setParent(otherOption);
        assertSame(otherOption, option.findOption(new String[]{"--myOption"}));
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testSetParentNull() {
        option.setParent(null);
        assertNull(option.getParent());
    }

    @Test
    public void testSetParentSelf() {
        option.setParent(option);
        assertSame(option, option.getParent());
    }

    @Test
    public void testSetParentOther() {
        Option otherOption = new Option() {};
        option.setParent(otherOption);
        assertSame(otherOption, option.getParent());
    }
}