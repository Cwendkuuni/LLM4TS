package org.apache.commons.cli2.option;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GroupImplTest {
    private GroupImpl group;

    @Before
    public void setUp() {
        group = new GroupImpl(null, "testGroup", "Test group", 0, 10, false);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertTrue(group.canProcess(commandLine, "test"));
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testGetPrefixes() {
        assertEquals(0, group.getPrefixes().size());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(0, group.getTriggers().size());
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList().listIterator();
        try {
            group.process(commandLine, arguments);
            fail("Expected OptionException to be thrown");
        } catch (OptionException e) {
            // Exception is expected
        }
    }

    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Unexpected OptionException");
        }
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("testGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test group", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet();
        Comparator comp = null;
        group.appendUsage(buffer, helpSettings, comp);
        assertEquals("testGroup", buffer.toString());
    }

    @Test
    public void testGetOptions() {
        assertNull(group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertNull(group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        assertNull(group.findOption("test"));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(10, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        group.defaults(commandLine);
    }
}