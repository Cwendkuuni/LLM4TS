package de.beiri22.stringincrementor;

import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    
    @Test
    public void diffTest() {
        // Test case 1
        String a1 = "abcde";
        String b1 = "abcxyz";
        RelativeString expected1 = new RelativeString();
        expected1.addLink(new StringLink(0, 0, 3));
        expected1.addLink(new StringLink(3, 3, 5));
        RelativeString actual1 = StringIncrementor.diff(a1, b1);
        assertEquals(expected1, actual1);
        
        // Test case 2
        String a2 = "abcde";
        String b2 = "abcde";
        RelativeString expected2 = new RelativeString();
        RelativeString actual2 = StringIncrementor.diff(a2, b2);
        assertEquals(expected2, actual2);
    }
    
    @Test
    public void diffVerboseTest() {
        // Test case 1
        String a1 = "abcde";
        String b1 = "abcxyz";
        RelativeString expected1 = new RelativeString();
        expected1.addLink(new StringLink(0, 0, 3));
        expected1.addLink(new StringLink(3, 3, 5));
        RelativeString actual1 = StringIncrementor.diff(a1, b1, true);
        assertEquals(expected1, actual1);
        
        // Test case 2
        String a2 = "abcde";
        String b2 = "abcde";
        RelativeString expected2 = new RelativeString();
        RelativeString actual2 = StringIncrementor.diff(a2, b2, true);
        assertEquals(expected2, actual2);
    }
    
    @Test
    public void patchTest() {
        // Test case 1
        String a1 = "abcde";
        RelativeString r1 = new RelativeString();
        r1.addLink(new StringLink(0, 0, 3));
        r1.addLink(new StringLink(3, 3, 5));
        String expected1 = "abcxyz";
        String actual1 = StringIncrementor.patch(a1, r1);
        assertEquals(expected1, actual1);
        
        // Test case 2
        String a2 = "abcde";
        RelativeString r2 = new RelativeString();
        String expected2 = "abcde";
        String actual2 = StringIncrementor.patch(a2, r2);
        assertEquals(expected2, actual2);
    }
    
    @Test
    public void patchVerboseTest() {
        // Test case 1
        String a1 = "abcde";
        RelativeString r1 = new RelativeString();
        r1.addLink(new StringLink(0, 0, 3));
        r1.addLink(new StringLink(3, 3, 5));
        String expected1 = "abcxyz";
        String actual1 = StringIncrementor.patch(a1, r1, true);
        assertEquals(expected1, actual1);
        
        // Test case 2
        String a2 = "abcde";
        RelativeString r2 = new RelativeString();
        String expected2 = "abcde";
        String actual2 = StringIncrementor.patch(a2, r2, true);
        assertEquals(expected2, actual2);
    }
}