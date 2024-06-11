import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {

  @Test
  public void testDiff() {
    // Test case 1
    String a1 = "abcde";
    String b1 = "abcdefg";
    RelativeString result1 = StringIncrementor.diff(a1, b1);
    RelativeString expected1 = new RelativeString();
    expected1.addLink(new StringLink(0, 5, 5));
    assertEquals(expected1, result1);
    
    // Test case 2
    String a2 = "abcde";
    String b2 = "abcde";
    RelativeString result2 = StringIncrementor.diff(a2, b2);
    RelativeString expected2 = new RelativeString();
    assertEquals(expected2, result2);
  }

  @Test
  public void testDiffVerbose() {
    // Test case 1
    String a1 = "abcde";
    String b1 = "abcdefg";
    RelativeString result1 = StringIncrementor.diff(a1, b1, true);
    RelativeString expected1 = new RelativeString();
    expected1.addLink(new StringLink(0, 5, 5));
    assertEquals(expected1, result1);
    
    // Test case 2
    String a2 = "abcde";
    String b2 = "abcde";
    RelativeString result2 = StringIncrementor.diff(a2, b2, true);
    RelativeString expected2 = new RelativeString();
    assertEquals(expected2, result2);
  }

  @Test
  public void testPatch() {
    // Test case 1
    String a1 = "abcde";
    RelativeString r1 = new RelativeString();
    r1.addLink(new StringLink(0, 5, 5));
    String result1 = StringIncrementor.patch(a1, r1);
    String expected1 = "abcdef";
    assertEquals(expected1, result1);
    
    // Test case 2
    String a2 = "";
    RelativeString r2 = new RelativeString();
    String result2 = StringIncrementor.patch(a2, r2);
    String expected2 = "";
    assertEquals(expected2, result2);
  }

  @Test
  public void testPatchVerbose() {
    // Test case 1
    String a1 = "abcde";
    RelativeString r1 = new RelativeString();
    r1.addLink(new StringLink(0, 5, 5));
    String result1 = StringIncrementor.patch(a1, r1, true);
    String expected1 = "abcdef";
    assertEquals(expected1, result1);
    
    // Test case 2
    String a2 = "";
    RelativeString r2 = new RelativeString();
    String result2 = StringIncrementor.patch(a2, r2, true);
    String expected2 = "";
    assertEquals(expected2, result2);
  }

}