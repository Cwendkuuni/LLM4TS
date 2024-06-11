import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;

public class StringIncrementorTest {
    
    @Test
    public void testDiff_withSameString_shouldReturnRelativeStringWithEmptyLink() {
        String a = "abc";
        String b = "abc";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff_withDifferentString_shouldReturnCorrectRelativeString() {
        String a = "abcdefg";
        String b = "abcdexyz";
        RelativeString expected = new RelativeString();
        StringLink link = new StringLink(0, 4, 4);
        expected.addLink(link);
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff_withVerboseTrue_shouldPrintCorrectOutput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        String a = "abcdefg";
        String b = "abcdexyz";
        RelativeString expected = new RelativeString();
        StringLink link = new StringLink(0, 4, 4);
        expected.addLink(link);
        RelativeString result = StringIncrementor.diff(a, b, true);
        
        String expectedOutput = "Creating index...\nSearching links...\nStringLink [posOrig=0, posNew=4, len=4] @ 0.0%\n";
        assertEquals(expectedOutput, outputStream.toString());
        
        System.setOut(System.out);
    }
    
    @Test
    public void testPatch_withEmptyRelativeString_shouldReturnOriginalString() {
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        String expected = "abcdefg";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatch_withNonEmptyRelativeString_shouldReturnCorrectPatchedString() {
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(0, 4, 4);
        r.addLink(link);
        String expected = "abcdexyz";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatch_withVerboseTrue_shouldPrintCorrectOutput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        StringLink link = new StringLink(0, 4, 4);
        r.addLink(link);
        String expected = "abcdexyz";
        String result = StringIncrementor.patch(a, r, true);
        
        String expectedOutput = "Adding 0 absolute Bytes from pos#0\nAdding Link: " + link.toString() + "\n";
        assertEquals(expectedOutput, outputStream.toString());
        
        System.setOut(System.out);
    }
}