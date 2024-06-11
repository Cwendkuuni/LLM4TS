package de.beiri22.stringincrementor;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

  @Test
  public void testDiff() {
    // Testing typical use case
    // a = "abcde", b = "abcdqwer", expected result = {1, 0, 5}
    RelativeString expected = new RelativeString();
    StringLink link = new StringLink(1, 0, 5);
    expected.addLink(link);
    RelativeString result = StringIncrementor.diff("abcde", "abcdqwer");
    assertEquals(expected, result);

    // Testing edge case with empty strings
    // a = "", b = "", expected result = {}
    expected = new RelativeString();
    result = StringIncrementor.diff("", "");
    assertEquals(expected, result);
  }

  @Test
  public void testPatch() {
    // Testing typical use case
    // a = "abcde", relative = {1, 0, 5}, expected result = "adecbcde"
    RelativeString relative = new RelativeString();
    relative.addLink(new StringLink(1, 0, 5));
    String expected = "adecbcde";
    String result = StringIncrementor.patch("abcde", relative);
    assertEquals(expected, result);

    // Testing edge case with empty string
    // a = "", relative = {}, expected result = ""
    relative = new RelativeString();
    expected = "";
    result = StringIncrementor.patch("", relative);
    assertEquals(expected, result);
  }
}