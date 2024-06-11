package org.apache.commons.cli2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Comparator;

public class OptionTest {

    private WriteableCommandLine commandLine;
    private ListIterator iterator;
    private Set triggers;
    private Set prefixes;
    private int depth;
    private Set helpSettings;
    private Comparator comparator;
    private Option option;

    @Before
    public void setup() {
        commandLine = mock(WriteableCommandLine.class);
        iterator = mock(ListIterator.class);
        triggers = mock(Set.class);
        prefixes = mock(Set.class);
        depth = 0;
        helpSettings = mock(Set.class);
        comparator = mock(Comparator.class);
        option = new Option() {
            public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
            }

            public void defaults(WriteableCommandLine commandLine) {
            }

            public boolean canProcess(WriteableCommandLine commandLine, String argument) {
                return false;
            }

            public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
                return false;
            }

            public Set getTriggers() {
                return triggers;
            }

            public Set getPrefixes() {
                return prefixes;
            }

            public void validate(WriteableCommandLine commandLine) throws OptionException {

            }

            public List helpLines(int depth, Set helpSettings, Comparator comp) {
                return null;
            }

            public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {

            }

            public String getPreferredName() {
                return null;
            }

            public String getDescription() {
                return null;
            }

            public int getId() {
                return 0;
            }

            public Option findOption(String trigger) {
                return null;
            }

            public boolean isRequired() {
                return false;
            }
        };
    }

    @Test
    public void testProcess() throws OptionException {
        //TODO: Implement test process
    }

    @Test
    public void testDefaults() {
        //TODO: Implement test defaults
    }

    @Test
    public void testCanProcessWithArgument() {
        //TODO: Implement test canProcess with argument
    }

    @Test
    public void testCanProcessWithListIterator() {
        //TODO: Implement test canProcess with ListIterator
    }

    @Test
    public void testGetTriggers() {
        assertSame(triggers, option.getTriggers());
    }

    @Test
    public void testGetPrefixes() {
        assertSame(prefixes, option.getPrefixes());
    }

    @Test
    public void testValidate() throws OptionException {
        //TODO: Implement test validate
    }

    @Test
    public void testHelpLines() {
        //TODO: Implement test helpLines
    }

    @Test
    public void testAppendUsage() {
        //TODO: Implement test appendUsage
    }

    @Test
    public void testGetPreferredName() {
        //TODO: Implement test getPreferredName
    }

    @Test
    public void testGetDescription() {
        //TODO: Implement test getDescription
    }

    @Test
    public void testGetId() {
        //TODO: Implement test getId
    }

    @Test
    public void testFindOption() {
        //TODO: Implement test findOption
    }

    @Test
    public void testIsRequired() {
        //TODO: Implement test isRequired
    }
}