import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test diff method with different inputs
        
        // Test case 1: a and b are empty strings
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
        
        // Test case 2: a is empty string and b is not empty
        a = "";
        b = "test";
        expected = new RelativeString();
        result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
        
        // Test case 3: a is not empty string and b is empty
        a = "test";
        b = "";
        expected = new RelativeString();
        result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
        
        // Test case 4: a and b are equal strings
        a = "test";
        b = "test";
        expected = new RelativeString();
        result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
        
        // Test case 5: a and b are different strings
        a = "test";
        b = "example";
        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 4));
        result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
        
        // Test case 6: a is substring of b
        a = "est";
        b = "test";
        expected = new RelativeString();
        expected.addLink(new StringLink(1, 0, 3));
        result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
        
        // Test case 7: b is substring of a
        a = "test";
        b = "es";
        expected = new RelativeString();
        expected.addLink(new StringLink(1, 0, 2));
        result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffVerbose() {
        // Test diff method with verbose option
        
        // Test case 1: verbose disabled
        String a = "test";
        String b = "example";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 4));
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
        
        // Test case 2: verbose enabled
        a = "test";
        b = "example";
        expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 4));
        result = StringIncrementor.diff(a, b, true);
        assertEquals(expected, result);
        // Additional verification of verbose output is not possible in unit testing
    }

    @Test
    public void testPatch() {
        // Test patch method with different inputs
        
        // Test case 1: a and r are empty
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
        
        // Test case 2: a is empty and r is not empty
        a = "";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 4));
        expected = "test";
        result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
        
        // Test case 3: a is not empty and r is empty
        a = "test";
        r = new RelativeString();
        expected = "test";
        result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
        
        // Test case 4: a is not empty and r is not empty
        a = "example";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 4));
        expected = "testmple";
        result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchVerbose() {
        // Test patch method with verbose option
        
        // Test case 1: verbose disabled
        String a = "example";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 4));
        String expected = "testmple";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
        
        // Test case 2: verbose enabled
        a = "example";
        r = new RelativeString();
        r.addLink(new StringLink(0, 0, 4));
        expected = "testmple";
        result = StringIncrementor.patch(a, r, true);
        assertEquals(expected, result);
        // Additional verification of verbose output is not possible in unit testing
    }
}