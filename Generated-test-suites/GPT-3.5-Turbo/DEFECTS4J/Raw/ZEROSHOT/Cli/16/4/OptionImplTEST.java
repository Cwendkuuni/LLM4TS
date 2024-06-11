package org.apache.commons.cli2.option;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

public class OptionImplTest {

    private OptionImpl option = new OptionImpl(1, false) {
        public boolean canProcess(Object a, Object b) {
            return true;
        }

        public void appendUsage(Object a, Object b, Object c) {}

        public String getPreferredName() {
            return null;
        }

        public String getDescription() {
            return null;
        }

        public HashSet getPrefixes() {
            return null;
        }

        public HashSet getTriggers() {
            return null;
        }

        public boolean equals(Object a) {
            return false;
        }

        public int hashCode() {
            return 0;
        }

        public Option findOption(String a) {
            return null;
        }

        public void defaults(Object a) {}

        protected void checkPrefixes(HashSet prefixes) {}

    };

    @Test
    public void testCanProcess() {
        assertTrue(option.canProcess(null, null));
    }

    @Test
    public void testToString() {
        assertEquals("", option.toString());
    }

    @Test
    public void testGetId() {
        assertEquals(1, option.getId());
    }

    @Test
    public void testEquals() {
        assertFalse(option.equals(null));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, option.hashCode());
    }

    @Test
    public void testFindOption() {
        assertNull(option.findOption(null));
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testDefaults() {
        option.defaults(null);
    }

}