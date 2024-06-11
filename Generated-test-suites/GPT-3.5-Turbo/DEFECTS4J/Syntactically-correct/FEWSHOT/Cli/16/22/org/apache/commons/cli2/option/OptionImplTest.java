package org.apache.commons.cli2.option;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OptionImplTest {

    @Mock
    private WriteableCommandLine mockCommandLine;

    @Mock
    private ListIterator<String> mockArguments;

    private OptionImpl option;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        option = new TestOptionImpl(1, false);
    }

    @Test
    public void testCanProcess() {
        when(mockArguments.hasNext()).thenReturn(true);
        when(mockArguments.next()).thenReturn("-f");
        assertTrue(option.canProcess(mockCommandLine, mockArguments));
        verify(mockArguments).previous();
        verifyNoMoreInteractions(mockArguments, mockCommandLine);
    }

    @Test
    public void testCanProcess_NoArguments() {
        when(mockArguments.hasNext()).thenReturn(false);
        assertFalse(option.canProcess(mockCommandLine, mockArguments));
        verifyNoMoreInteractions(mockArguments, mockCommandLine);
    }

    @Test
    public void testToString() {
        String expected = "TestOptionImpl";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testGetId() {
        int expected = 1;
        assertEquals(expected, option.getId());
    }

    @Test
    public void testEquals_True() {
        OptionImpl otherOption = new TestOptionImpl(1, false);
        assertTrue(option.equals(otherOption));
    }

    @Test
    public void testEquals_False() {
        OptionImpl otherOption = new TestOptionImpl(2, true);
        assertFalse(option.equals(otherOption));
    }

    @Test
    public void testEquals_OtherObject() {
        assertFalse(option.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        int expected = 1;
        assertEquals(expected, option.hashCode());
    }

    @Test
    public void testFindOption() {
        Option foundOption = option.findOption("-f");
        assertEquals(option, foundOption);
    }

    @Test
    public void testFindOption_NotFound() {
        Option foundOption = option.findOption("-x");
        assertNull(foundOption);
    }

    @Test
    public void testIsRequired_True() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequired_False() {
        OptionImpl optionalOption = new TestOptionImpl(1, false);
        assertFalse(optionalOption.isRequired());
    }

    @Test
    public void testDefaults() {
        option.defaults(mockCommandLine);
        verifyZeroInteractions(mockCommandLine);
    }

    @Test
    public void testCheckPrefixes_EmptyPrefixList() {
        Set<String> prefixes = new HashSet<>();
        option.checkPrefixes(prefixes);
    }

    @Test
    public void testCheckPrefixes_ValidPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("-");
        prefixes.add("--");
        option.checkPrefixes(prefixes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixes_InvalidPrefix() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("+");
        option.checkPrefixes(prefixes);
    }

    private class TestOptionImpl extends OptionImpl {

        public TestOptionImpl(int id, boolean required) {
            super(id, required);
        }

        @Override
        public boolean canProcess(WriteableCommandLine commandLine, String argument) {
            return argument.startsWith("-");
        }

        @Override
        public void appendUsage(StringBuffer buffer, int displaySetting, Set< ? > examinedElements) {
            buffer.append("TestOptionImpl");
        }
    }
}