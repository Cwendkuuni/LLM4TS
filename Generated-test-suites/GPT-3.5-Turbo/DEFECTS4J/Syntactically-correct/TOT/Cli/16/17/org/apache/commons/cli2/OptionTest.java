package org.apache.commons.cli2;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class OptionTest {

    Option option;

    @Before
    public void setUp() {
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
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
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
    public void process_WhenValidArguments_ShouldNotThrowException() {
        WriteableCommandLine commandLine = null;

        try {
            ListIterator args = new ArrayList<String>().listIterator();
            option.process(commandLine, args);
        } catch (OptionException e) {
            fail("Should not throw OptionException");
        }
    }

    @Test(expected = OptionException.class)
    public void process_WhenEmptyArguments_ShouldThrowOptionException() throws OptionException {
        WriteableCommandLine commandLine = null;

        ListIterator args = new ArrayList<String>().listIterator();
        option.process(commandLine, args);
    }

    @Test
    public void defaults_Always_ShouldNotThrowException() {
        WriteableCommandLine commandLine = null;

        try {
            option.defaults(commandLine);
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
    }

    @Test
    public void canProcess_WhenValidArgument_ShouldReturnTrue() {
        WriteableCommandLine commandLine = null;
        String argument = "test";

        boolean result = option.canProcess(commandLine, argument);

        assertTrue(result);
    }

    @Test
    public void canProcess_WhenInvalidArgument_ShouldReturnFalse() {
        WriteableCommandLine commandLine = null;
        String argument = "invalid";

        boolean result = option.canProcess(commandLine, argument);

        assertFalse(result);
    }

    @Test
    public void canProcess_WhenValidArgumentIterator_ShouldReturnTrue() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = new ArrayList<String>().listIterator();
        arguments.add("test");

        boolean result = option.canProcess(commandLine, arguments);

        assertTrue(result);
    }

    @Test
    public void canProcess_WhenInvalidArgumentIterator_ShouldReturnFalse() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = new ArrayList<String>().listIterator();
        arguments.add("invalid");

        boolean result = option.canProcess(commandLine, arguments);

        assertFalse(result);
    }

    @Test
    public void getTriggers_WhenTriggersExist_ShouldReturnSetOfTriggers() {
        Set triggers = option.getTriggers();

        assertNotNull(triggers);
    }

    @Test
    public void getPrefixes_WhenPrefixesExist_ShouldReturnSetOfPrefixes() {
        Set prefixes = option.getPrefixes();

        assertNotNull(prefixes);
    }

    @Test
    public void validate_WhenValidCommandLine_ShouldNotThrowException() {
        WriteableCommandLine commandLine = null;

        try {
            option.validate(commandLine);
        } catch (OptionException e) {
            fail("Should not throw OptionException");
        }
    }

    @Test(expected = OptionException.class)
    public void validate_WhenInvalidCommandLine_ShouldThrowOptionException() throws OptionException {
        WriteableCommandLine commandLine = null;

        option.validate(commandLine);
    }

    @Test
    public void helpLines_Always_ShouldReturnListOfHelpLineObjects() {
        int depth = 0;
        Set helpSettings = null;
        Comparator comp = null;

        List helpLines = option.helpLines(depth, helpSettings, comp);

        assertNotNull(helpLines);
    }

    @Test
    public void appendUsage_Always_ShouldNotThrowException() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = null;
        Comparator comp = null;

        try {
            option.appendUsage(buffer, helpSettings, comp);
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
    }

    @Test
    public void getPreferredName_Always_ShouldReturnPreferredName() {
        String preferredName = option.getPreferredName();

        assertNotNull(preferredName);
    }

    @Test
    public void getDescription_Always_ShouldReturnDescription() {
        String description = option.getDescription();

        assertNotNull(description);
    }

    @Test
    public void getId_Always_ShouldReturnId() {
        int id = option.getId();

        assertNotNull(id);
    }

    @Test
    public void findOption_WhenValidTrigger_ShouldReturnMatchingOption() {
        String trigger = "test";

        Option matchingOption = option.findOption(trigger);

        assertNotNull(matchingOption);
    }

    @Test
    public void findOption_WhenInvalidTrigger_ShouldReturnNull() {
        String trigger = "invalid";

        Option matchingOption = option.findOption(trigger);

        assertNull(matchingOption);
    }

    @Test
    public void isRequired_Always_ShouldReturnBoolean() {
        boolean result = option.isRequired();

        assertNotNull(result);
    }

    @Test
    public void getParent_Always_ShouldReturnParentOption() {
        Option parentOption = option.getParent();

        assertNotNull(parentOption);
    }

    @Test
    public void setParent_WhenValidParentOption_ShouldSetParentOption() {
        Option parentOption = new Option() {
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
                return null;
            }

            @Override
            public Set getPrefixes() {
                return null;
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

        option.setParent(parentOption);

        assertNotNull(option.getParent());
    }
}