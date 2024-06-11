import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void diff_shouldReturnEmptyRelativeString_whenBothStringsAreEmpty() {
        String a = "";
        String b = "";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(0, result.linksCount());
        assertEquals("", result.getAbsolute());
    }
    
    @Test
    public void diff_shouldReturnEmptyRelativeString_whenTheStringsAreEqual() {
        String a = "abc";
        String b = "abc";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(0, result.linksCount());
        assertEquals("", result.getAbsolute());
    }
    
    @Test
    public void diff_shouldReturnRelativeStringWithOneLink_whenTheStringsDifferByOneCharacter() {
        String a = "abcd";
        String b = "abce";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(1, result.linksCount());
        assertEquals("", result.getAbsolute());
        StringLink link = result.getLink(0);
        assertEquals(3, link.getPosOrig());
        assertEquals(3, link.getPosNew());
        assertEquals(1, link.getLen());
    }
    
    @Test
    public void diff_shouldReturnRelativeStringWithTwoLinks_whenTheStringsDifferByTwoCharacters() {
        String a = "abcd";
        String b = "abef";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(2, result.linksCount());
        assertEquals("", result.getAbsolute());
        StringLink link1 = result.getLink(0);
        assertEquals(2, link1.getPosOrig());
        assertEquals(2, link1.getPosNew());
        assertEquals(2, link1.getLen());
        StringLink link2 = result.getLink(1);
        assertEquals(4, link2.getPosOrig());
        assertEquals(4, link2.getPosNew());
        assertEquals(1, link2.getLen());
    }
    
    @Test
    public void diff_shouldReturnRelativeStringWithOneLink_whenTheStringsDifferByMultipleCharacters() {
        String a = "abcdefg";
        String b = "axcdeyg";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(1, result.linksCount());
        assertEquals("y", result.getAbsolute());
        StringLink link = result.getLink(0);
        assertEquals(0, link.getPosOrig());
        assertEquals(0, link.getPosNew());
        assertEquals(1, link.getLen());
    }
    
    @Test
    public void diff_shouldReturnRelativeStringWithNoLinks_whenNoCommonSubstringIsFound() {
        String a = "abcdefg";
        String b = "hijklmn";
        
        RelativeString result = StringIncrementor.diff(a, b);
        
        assertEquals(0, result.linksCount());
        assertEquals("hijklmn", result.getAbsolute());
    }
    
    @Test
    public void patch_shouldReturnOriginalString_whenRelativeStringIsEmpty() {
        String a = "abc";
        RelativeString r = new RelativeString();
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("abc", result);
    }
    
    @Test
    public void patch_shouldReturnPatchedString_whenApplyingOneLink() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(1, 2, 2));
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("accd", result);
    }
    
    @Test
    public void patch_shouldReturnPatchedString_whenApplyingMultipleLinks() {
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(1, 2, 2));
        r.addLink(new StringLink(4, 6, 2));
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("accefg", result);
    }
    
    @Test
    public void patch_shouldReturnPatchedString_whenApplyingLinksAndAddingAbsoluteString() {
        String a = "abcdefg";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(1, 2, 2));
        r.addLink(new StringLink(4, 6, 2));
        r.setAbsolute("xyz");
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("accefgxyz", result);
    }
    
    @Test
    public void patch_shouldReturnPatchedString_whenAddingAbsoluteStringOnly() {
        String a = "";
        RelativeString r = new RelativeString();
        r.setAbsolute("abcdefg");
        
        String result = StringIncrementor.patch(a, r);
        
        assertEquals("abcdefg", result);
    }
}