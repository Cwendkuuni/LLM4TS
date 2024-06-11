package de.beiri22.stringincrementor;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test case 1
        String a1 = "abc";
        String b1 = "def";
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        // Assert statements
        // ...

        // Test case 2
        String a2 = "abc";
        String b2 = "abc";
        RelativeString result2 = StringIncrementor.diff(a2, b2);
        // Assert statements
        // ...
    }

    @Test
    public void testDiffVerbose() {
        // Test case 1
        String a1 = "abc";
        String b1 = "def";
        RelativeString result1 = StringIncrementor.diff(a1, b1, true);
        // Assert statements
        // ...

        // Test case 2
        String a2 = "abc";
        String b2 = "abc";
        RelativeString result2 = StringIncrementor.diff(a2, b2, true);
        // Assert statements
        // ...
    }

    @Test
    public void testPatch() {
        // Test case 1
        String a1 = "abc";
        RelativeString r1 = new RelativeString();
        String result1 = StringIncrementor.patch(a1, r1);
        // Assert statements
        // ...

        // Test case 2
        String a2 = "abc";
        RelativeString r2 = new RelativeString();
        String result2 = StringIncrementor.patch(a2, r2);
        // Assert statements
        // ...
    }

    @Test
    public void testPatchVerbose() {
        // Test case 1
        String a1 = "abc";
        RelativeString r1 = new RelativeString();
        String result1 = StringIncrementor.patch(a1, r1, true);
        // Assert statements
        // ...

        // Test case 2
        String a2 = "abc";
        RelativeString r2 = new RelativeString();
        String result2 = StringIncrementor.patch(a2, r2, true);
        // Assert statements
        // ...
    }

}