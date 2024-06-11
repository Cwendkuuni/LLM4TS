package org.apache.commons.cli2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OptionTest {
    Option option = new Option() {
        @Override
        public void process(final WriteableCommandLine commandLine, final ListIterator args) throws OptionException {
            
        }

        @Override
        public void defaults(final WriteableCommandLine commandLine) {
            
        }

        @Override
        public boolean canProcess(final WriteableCommandLine commandLine, final String argument) {
            return false;
        }

        @Override
        public boolean canProcess(final WriteableCommandLine commandLine, final ListIterator arguments) {
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
        public void validate(final WriteableCommandLine commandLine) throws OptionException {
            
        }

        @Override
        public List helpLines(final int depth, final Set helpSettings, final Comparator comp) {
            return new ArrayList();
        }

        @Override
        public void appendUsage(final StringBuffer buffer, final Set helpSettings, final Comparator comp) {
            
        }

        @Override
        public String getPreferredName() {
            return "";
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public Option findOption(final String trigger) {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }
    };

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator args = new ArrayList().listIterator();
        option.process(commandLine, args);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        option.defaults(commandLine);
    }

    @Test
    public void testCanProcessString() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "";
        assertFalse(option.canProcess(commandLine, argument));
    }

    @Test
    public void testCanProcessListIterator() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList().listIterator();
        assertFalse(option.canProcess(commandLine, arguments));
    }

    @Test
    public void testGetTriggers() {
        assertNotNull(option.getTriggers());
    }

    @Test
    public void testGetPrefixes() {
        assertNotNull(option.getPrefixes());
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set helpSettings = new HashSet();
        Comparator comp = null;
        assertNotNull(option.helpLines(depth, helpSettings, comp));
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        option.appendUsage(buffer, helpSettings, comp);
    }

    @Test
    public void testGetPreferredName() {
        assertNotNull(option.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertNotNull(option.getDescription());
    }

    @Test
    public void testGetId() {
        assertTrue(option.getId() == 0);
    }

    @Test
    public void testFindOption() {
        String trigger = "";
        assertNull(option.findOption(trigger));
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }
}