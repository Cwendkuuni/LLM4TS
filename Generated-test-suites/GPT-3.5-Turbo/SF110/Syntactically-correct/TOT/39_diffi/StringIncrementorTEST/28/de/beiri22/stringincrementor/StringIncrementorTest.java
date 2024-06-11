import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff1() {
        String a = "abcd";
        String b = "efgh";
        RelativeString expected = new RelativeString();
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff2() {
        String a = "abcd";
        String b = "abcd";
        RelativeString expected = new RelativeString();
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff3() {
        String a = "abcde";
        String b = "abbcd";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 3));
        expected.addLink(new StringLink(3, 3, 1));
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff4() {
        String a = "abcde";
        String b = "abcde";
        RelativeString expected = new RelativeString();
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff5() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDiff6() {
        String a = "abcdefg";
        String b = "abcdef";
        RelativeString expected = new RelativeString();
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(expected, result);
    }

    @Test
    public void testPatch1() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        String expected = "abcd";
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatch2() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 4));
        String expected = "abcdabcd";
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatch3() {
        String a = "abcdef";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(0, 0, 3));
        String expected = "abcddef";
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatch4() {
        String a = "abcdef";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 2, 2));
        String expected = "abcefg";
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatch5() {
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testPatch6() {
        String a = "abcdefghi";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 3, 3));
        String expected = "abcdefabcghi";
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals(expected, result);
    }
}