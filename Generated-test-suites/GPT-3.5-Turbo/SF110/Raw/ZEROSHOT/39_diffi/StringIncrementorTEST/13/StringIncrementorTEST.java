package de.beiri22.stringincrementor;

import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test Case 1
        String a1 = "";
        String b1 = "";
        RelativeString expected1 = new RelativeString();
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        assertEquals(expected1, result1);
        
        // Test Case 2
        String a2 = "abcdefg";
        String b2  = "abcdefg";
        RelativeString expected2 = new RelativeString();
        RelativeString result2 = StringIncrementor.diff(a2, b2);
        assertEquals(expected2, result2);
        
        // Test Case 3
        String a3 = "a";
        String b3 = "b";
        RelativeString expected3 = new RelativeString();
        RelativeString result3 = StringIncrementor.diff(a3, b3);
        assertEquals(expected3, result3);
        
        // Test Case 4
        String a4 = "abcd";
        String b4 = "efgh";
        RelativeString expected4 = new RelativeString();
        RelativeString result4 = StringIncrementor.diff(a4, b4);
        assertEquals(expected4, result4);
    }
    
    @Test
    public void testDiffVerbose() {
        // Test Case 1
        String a1 = "";
        String b1 = "";
        boolean verbose1 = true;
        RelativeString expected1 = new RelativeString();
        RelativeString result1 = StringIncrementor.diff(a1, b1, verbose1);
        assertEquals(expected1, result1);
        
        // Test Case 2
        String a2 = "abcdefg";
        String b2  = "abcdefg";
        boolean verbose2 = true;
        RelativeString expected2 = new RelativeString();
        RelativeString result2 = StringIncrementor.diff(a2, b2, verbose2);
        assertEquals(expected2, result2);
        
        // Test Case 3
        String a3 = "a";
        String b3 = "b";
        boolean verbose3 = true;
        RelativeString expected3 = new RelativeString();
        RelativeString result3 = StringIncrementor.diff(a3, b3, verbose3);
        assertEquals(expected3, result3);
        
        // Test Case 4
        String a4 = "abcd";
        String b4 = "efgh";
        boolean verbose4 = true;
        RelativeString expected4 = new RelativeString();
        RelativeString result4 = StringIncrementor.diff(a4, b4, verbose4);
        assertEquals(expected4, result4);
    }
    
    @Test
    public void testPatch() {
        // Test Case 1
        String a1 = "";
        RelativeString r1 = new RelativeString();
        String expected1 = "";
        String result1 = StringIncrementor.patch(a1, r1);
        assertEquals(expected1, result1);
        
        // Test Case 2
        String a2 = "abcdefg";
        RelativeString r2  = new RelativeString();
        String expected2 = "abcdefg";
        String result2 = StringIncrementor.patch(a2, r2);
        assertEquals(expected2, result2);
        
        // Test Case 3
        String a3 = "a";
        RelativeString r3 = new RelativeString();
        String expected3 = "a";
        String result3 = StringIncrementor.patch(a3, r3);
        assertEquals(expected3, result3);
        
        // Test Case 4
        String a4 = "abcd";
        RelativeString r4 = new RelativeString();
        String expected4 = "abcd";
        String result4 = StringIncrementor.patch(a4, r4);
        assertEquals(expected4, result4);
    }
    
    @Test
    public void testPatchVerbose() {
        // Test Case 1
        String a1 = "";
        RelativeString r1 = new RelativeString();
        boolean verbose1 = true;
        String expected1 = "";
        String result1 = StringIncrementor.patch(a1, r1, verbose1);
        assertEquals(expected1, result1);
        
        // Test Case 2
        String a2 = "abcdefg";
        RelativeString r2  = new RelativeString();
        boolean verbose2 = true;
        String expected2 = "abcdefg";
        String result2 = StringIncrementor.patch(a2, r2, verbose2);
        assertEquals(expected2, result2);
        
        // Test Case 3
        String a3 = "a";
        RelativeString r3 = new RelativeString();
        boolean verbose3 = true;
        String expected3 = "a";
        String result3 = StringIncrementor.patch(a3, r3, verbose3);
        assertEquals(expected3, result3);
        
        // Test Case 4
        String a4 = "abcd";
        RelativeString r4 = new RelativeString();
        boolean verbose4 = true;
        String expected4 = "abcd";
        String result4 = StringIncrementor.patch(a4, r4, verbose4);
        assertEquals(expected4, result4);
    }
}