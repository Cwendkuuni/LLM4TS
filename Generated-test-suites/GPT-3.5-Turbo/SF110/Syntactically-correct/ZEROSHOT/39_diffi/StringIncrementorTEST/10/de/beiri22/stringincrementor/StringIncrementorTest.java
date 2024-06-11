package de.beiri22.stringincrementor;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abc", "def");
        // add test assertions here
    }
    
    @Test
    public void testDiffVerbose() {
        RelativeString result = StringIncrementor.diff("abc", "def", true);
        // add test assertions here
    }
    
    @Test
    public void testPatch() {
        String result = StringIncrementor.patch("abc", new RelativeString());
        // add test assertions here
    }
    
    @Test
    public void testPatchVerbose() {
        String result = StringIncrementor.patch("abc", new RelativeString(), true);
        // add test assertions here
    }
    
}