
package de.beiri22.stringincrementor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        assertEquals(new RelativeString(), StringIncrementor.diff("", ""));
        assertEquals(new RelativeString(), StringIncrementor.diff("abc", "abc"));
        assertEquals(new RelativeString(), StringIncrementor.diff("abc", "def"));
    }

    @Test
    public void testDiffVerbose() {
        assertEquals(new RelativeString(), StringIncrementor.diff("", "", true));
        assertEquals(new RelativeString(), StringIncrementor.diff("abc", "abc", true));
        assertEquals(new RelativeString(), StringIncrementor.diff("abc", "def", true));
    }

    @Test
    public void testPatch() {
        assertEquals("", StringIncrementor.patch("", new RelativeString()));
        assertEquals("abc", StringIncrementor.patch("abc", new RelativeString()));
        assertEquals("def", StringIncrementor.patch("abc", new RelativeString()));
    }
    
    @Test
    public void testPatchVerbose() {
        assertEquals("", StringIncrementor.patch("", new RelativeString(), true));
        assertEquals("abc", StringIncrementor.patch("abc", new RelativeString(), true));
        assertEquals("def", StringIncrementor.patch("abc", new RelativeString(), true));
    }
}
