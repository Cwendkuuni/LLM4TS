import static org.junit.Assert.*;
import org.junit.Test;
import de.beiri22.stringincrementor.StringIncrementor;
import de.beiri22.stringincrementor.helper.RelativeString;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

  // Test cases for the diff method

  @Test
  public void testDiffWithTwoEqualStrings() {
    String a = "abcdefg";
    String b = "abcdefg";
    RelativeString expected = new RelativeString();
    assertEquals(expected, StringIncrementor.diff(a, b));
  }

  @Test
  public void testDiffWithTwoDifferentStrings() {
    String a = "abcdefg";
    String b = "hijklmn";
    RelativeString expected = new RelativeString();
    expected.addLink(new StringLink(-1, 0, 7));
    expected.setAbsolute("hijklmn");
    assertEquals(expected, StringIncrementor.diff(a, b));
  }

  @Test
  public void testDiffWithVerboseMode() {
    String a = "abcdefg";
    String b = "abxyzfg";
    RelativeString expected = new RelativeString();
    expected.addLink(new StringLink(0, 2, 2));
    expected.addLink(new StringLink(-1, 2, 3));
    expected.addLink(new StringLink(5, 5, 1));
    expected.addLink(new StringLink(6, 6, 1));
    expected.setAbsolute("xyz");
    assertEquals(expected, StringIncrementor.diff(a, b, true));
  }

  // Test cases for the patch method

  @Test
  public void testPatchWithEmptyString() {
    String a = "";
    RelativeString r = new RelativeString();
    String expected = "";
    assertEquals(expected, StringIncrementor.patch(a, r));
  }

  @Test
  public void testPatchWithNoLinks() {
    String a = "abcdefg";
    RelativeString r = new RelativeString();
    String expected = "abcdefg";
    assertEquals(expected, StringIncrementor.patch(a, r));
  }

  @Test
  public void testPatchWithLinks() {
    String a = "abcdefg";
    RelativeString r = new RelativeString();
    r.addLink(new StringLink(0, 0, 2));
    r.addLink(new StringLink(2, 2, 2));
    r.setAbsolute("xyz");
    String expected = "abxyzcd";
    assertEquals(expected, StringIncrementor.patch(a, r));
  }

  @Test
  public void testPatchWithVerboseMode() {
    String a = "abcdefg";
    RelativeString r = new RelativeString();
    r.addLink(new StringLink(0, 0, 2));
    r.addLink(new StringLink(2, 2, 2));
    r.setAbsolute("xyz");
    String expected = "abxyzcxyzd";
    assertEquals(expected, StringIncrementor.patch(a, r, true));
  }

}