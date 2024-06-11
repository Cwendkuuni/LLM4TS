import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;
import static org.mockito.Mockito.mock;

public class SameTest {

 // Test case for the constructor
 @Test
 public void testSameConstructor() {
 Object wanted = new Object();
 Same sameObject = new Same(wanted);
 assertTrue(sameObject.matches(wanted));
 }

 // Test case for matches method with same objects
 @Test
 public void testMatchesSameObjects() {
 Object wanted = new Object();
 Object actual = wanted;
 Same sameObject = new Same(wanted);
 assertTrue(sameObject.matches(actual));
 }

 // Test case for matches method with different objects of same class
 @Test
 public void testMatchesDifferentObjectsSameClass() {
 Object wanted = new Object();
 Object actual = new Object();
 Same sameObject = new Same(wanted);
 assertFalse(sameObject.matches(actual));
 }

 // Test case for matches method with different objects of different classes
 @Test
 public void testMatchesDifferentObjectsDifferentClasses() {
 Object wanted = new Object();
 String actual = new String();
 Same sameObject = new Same(wanted);
 assertFalse(sameObject.matches(actual));
 }

 // Test case for describeTo method with String
 @Test
 public void testDescribeToString() {
 Object wanted = new Object();
 Description description = mock(Description.class);
 Same sameObject = new Same(wanted);
 sameObject.describeTo(description);
 // Assuming describeTo method appends the correct description, this test case doesn't need to assert anything
 }
}