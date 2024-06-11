import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {
  
  @Test
  public void testDiff() {
    RelativeString result = StringIncrementor.diff("abc", "def");
    assertNull(result);
    
    result = StringIncrementor.diff("abc", "abc");
    assertNotNull(result);
    assertEquals(0, result.linksCount());
    assertEquals("abc", result.getAbsolute());
    
    result = StringIncrementor.diff("abc", "abdefc");
    assertNotNull(result);
    assertEquals(1, result.linksCount());
    assertEquals("abc", result.getAbsolute());
    StringLink link = result.getLink(0);
    assertEquals(0, link.getPosOrig());
    assertEquals(2, link.getPosNew());
    assertEquals(3, link.getLen());
    
    result = StringIncrementor.diff("abc", "dabc");
    assertNotNull(result);
    assertEquals(1, result.linksCount());
    assertEquals("abc", result.getAbsolute());
    link = result.getLink(0);
    assertEquals(1, link.getPosOrig());
    assertEquals(0, link.getPosNew());
    assertEquals(3, link.getLen());
  }
  
  @Test
  public void testDiffVerbose() {
    RelativeString result = StringIncrementor.diff("abc", "def", true);
    assertNull(result);
    
    result = StringIncrementor.diff("abc", "abc", true);
    assertNotNull(result);
    assertEquals(0, result.linksCount());
    assertEquals("abc", result.getAbsolute());
    
    result = StringIncrementor.diff("abc", "abdefc", true);
    assertNotNull(result);
    assertEquals(1, result.linksCount());
    assertEquals("abc", result.getAbsolute());
    StringLink link = result.getLink(0);
    assertEquals(0, link.getPosOrig());
    assertEquals(2, link.getPosNew());
    assertEquals(3, link.getLen());
    
    result = StringIncrementor.diff("abc", "dabc", true);
    assertNotNull(result);
    assertEquals(1, result.linksCount());
    assertEquals("abc", result.getAbsolute());
    link = result.getLink(0);
    assertEquals(1, link.getPosOrig());
    assertEquals(0, link.getPosNew());
    assertEquals(3, link.getLen());
  }
  
  @Test
  public void testPatch() {
    String result = StringIncrementor.patch("abc", new RelativeString());
    assertEquals("abc", result);
    
    RelativeString r = new RelativeString();
    r.addLink(new StringLink(0, 0, 2));
    r.setAbsolute("def");
    result = StringIncrementor.patch("abc", r);
    assertEquals("defc", result);
    
    r = new RelativeString();
    r.addLink(new StringLink(1, 0, 2));
    r.setAbsolute("def");
    result = StringIncrementor.patch("abc", r);
    assertEquals("adefc", result);
    
    r = new RelativeString();
    r.addLink(new StringLink(1, 0, 3));
    r.setAbsolute("def");
    result = StringIncrementor.patch("abc", r);
    assertEquals("adef", result);
  }
  
  @Test
  public void testPatchVerbose() {
    String result = StringIncrementor.patch("abc", new RelativeString(), true);
    assertEquals("abc", result);
    
    RelativeString r = new RelativeString();
    r.addLink(new StringLink(0, 0, 2));
    r.setAbsolute("def");
    result = StringIncrementor.patch("abc", r, true);
    assertEquals("defc", result);
    
    r = new RelativeString();
    r.addLink(new StringLink(1, 0, 2));
    r.setAbsolute("def");
    result = StringIncrementor.patch("abc", r, true);
    assertEquals("adefc", result);
    
    r = new RelativeString();
    r.addLink(new StringLink(1, 0, 3));
    r.setAbsolute("def");
    result = StringIncrementor.patch("abc", r, true);
    assertEquals("adef", result);
  }
}