import org.junit.Test;
import org.junit.Assert.*;
import org.mockito. ArgumentMatchers;
import org.mockito.Mockito;

public class SameTest {

 // Test for the constructor of the Same class with a valid object
 @Test
 public void testSameConstructorWithValidObject() {
 Object wanted = new Object();
 Same same = new Same(wanted);
 assertTrue(same.matches(wanted));
 }

 // Test for the matches method with two identical objects
 @Test
 public void testMatchesWithIdenticalObjects() {
 Object wanted = new Object();
 Object actual = wanted;
 Same same = new Same(wanted);
 assertTrue(same.matches(actual));
 }

 // Test for the matches method with two different objects
 @Test
 public void testMatchesWithDifferentObjects() {
 Object wanted = new Object();
 Object actual = new Object();
 Same same = new Same(wanted);
 assertFalse(same.matches(actual));
 }

 // Test for the describeTo method with a String object
 @Test
 public void testDescribeToWithStringObject() {
 Object wanted = "Mockito";
 Description description = Mockito.mock(Description.class);
 Same same = new Same(wanted);
 same.describeTo(description);
 String expectedDescription = "same(\"Mockito\")";
 assertEquals(expectedDescription, description.toString());
 }

 // Test for the describeTo method with a Character object
 @Test
 public void testDescribeToWithCharacterObject() {
 char wanted = 'A';
 Description description = Mockito.mock(Description.class);
 Same same = new Same(wanted);
 same.describeTo(description);
 String expectedDescription = "same('A')";
 assertEquals(expectedDescription, description.toString());
 }

 // Test for the describeTo method with a complex object (nested objects, lists, etc.)
 @Test
 public void testDescribeToWithComplexObject() {
 Object complexObject = new ComplexObject();
 Description description = Mockito.mock(Description.class);
 Same same = new Same(complexObject);
 same.describeTo(description);
 String expectedDescription = "same(" + complexObject.toString() + ")";
 assertEquals(expectedDescription, description.toString());
 }

 // Test for the Serializable interface implementation
 @Test
 public void testSerializability() {
 Object wanted = new Object();
 Same same = new Same(wanted);
 byte[] byteArray = Mockito.serialization.serialize(same);
 Same deserializedSame = (Same) Mockito.serialization.deserialize(byteArray, Same.class);
 assertTrue(deserializedSame.matches(wanted));
 }
}

// ComplexObject class for the test with a complex object
class ComplexObject implements Serializable {
 private static final long serialVersionUID = -1226959355938572597L;
 private int id;
 private String name;
 private List<Integer> numbers;

 public ComplexObject() {
 id = 1;
 name = "ComplexObject";
 numbers = new ArrayList<>();
 numbers.add(1);
 numbers.add(2);
 }

 @Override
 public String toString() {
 return "ComplexObject{id=" + id + ", name='" + name + "', numbers=" + numbers + "}";
 }
}