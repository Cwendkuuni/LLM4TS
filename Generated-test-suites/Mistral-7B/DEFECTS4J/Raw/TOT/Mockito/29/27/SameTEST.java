import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class SameTest {

 // Test for the constructor and matches method with null object
 @Test
 public void testSameWithNull() {
 Same same = new Same(null);
 assertFalse(same.matches(new Object()));
 }

 // Test for the matches method with same objects
 @Test
 public void testSameWithSameObjects() {
 Object obj1 = new Object();
 Same same = new Same(obj1);
 assertTrue(same.matches(obj1));
 }

 // Test for the matches method with different objects of the same class
 @Test
 public void testSameWithDifferentObjectsOfSameClass() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Same same = new Same(obj1);
 assertFalse(same.matches(obj2));
 }

 // Test for the matches method with different objects of different classes
 @Test
 public void testSameWithDifferentObjectsOfDifferentClasses() {
 Object obj1 = new Object();
 String str = "test";
 Same same = new Same(obj1);
 assertFalse(same.matches(str));
 }

 // Test for the describeTo method with String object
 @Test
 public void testDescribeToWithString() {
 Description description = Mockito.mock(Description.class);
 Same same = new Same("test");
 same.describeTo(description);
 String expected = "same(\"test\")";
 verify(description).appendText(expected);
 }

 // Test for the describeTo method with Character object
 @Test
 public void testDescribeToWithCharacter() {
 Description description = Mockito.mock(Description.class);
 Same same = new Same('a');
 same.describeTo(description);
 String expected = "same('a')";
 verify(description).appendText(expected);
 }
}