import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

private static JsonPointer createJsonPointer(String input) {
try {
return JsonPointer.compile(input);
} catch (IllegalArgumentException e) {
Assert.fail("Unexpected exception: " + e.getMessage());
return null;
}
}

@Test
public void testEmptyConstructor() {
JsonPointer emptyPointer = new JsonPointer();
Assert.assertFalse(emptyPointer.matchesElement());
Assert.assertFalse(emptyPointer.mayMatchProperty());
Assert.assertNull(emptyPointer.getMatchingProperty());
Assert.assertEquals("", emptyPointer.toString());
}

@Test
public void testStringConstructor() {
JsonPointer pointer = new JsonPointer("/foo/bar/1", "bar", null);
Assert.assertFalse(pointer.matches());
Assert.assertEquals("bar", pointer.getMatchingProperty());
Assert.assertEquals(1, pointer.getMatchingIndex());
Assert.assertEquals("/foo/bar/1", pointer.toString());
}

@Test
public void testCompileMethod() {
JsonPointer pointer = createJsonPointer("/foo/bar/1");
Assert.assertFalse(pointer.matches());
Assert.assertEquals("bar", pointer.getMatchingProperty());
Assert.assertEquals(1, pointer.getMatchingIndex());
Assert.assertEquals("/foo/bar/1", pointer.toString());
}

@Test(expected = IllegalArgumentException.class)
public void testInvalidInput() {
createJsonPointer("/foo1");
}

@Test(expected = IllegalArgumentException.class)
public void testInvalidInputWithElementIndex() {
createJsonPointer("/foo/bar1");
}

@Test(expected = IllegalArgumentException.class)
public void testInvalidInputWithPropertyName() {
createJsonPointer("/foo/1");
}

@Test
public void testTailMethod() {
JsonPointer pointer = new JsonPointer("/foo/bar/1", "bar", null);
JsonPointer tail = pointer.tail();
Assert.assertFalse(pointer.matches());
Assert.assertEquals("bar", pointer.getMatchingProperty());
Assert.assertEquals(1, pointer.getMatchingIndex());
Assert.assertTrue(tail.matches());
Assert.assertNull(tail.getMatchingProperty());
Assert.assertFalse(tail.mayMatchElement());
Assert.assertEquals("/foo/bar", tail.toString());
}

@Test
public void testToStringMethod() {
JsonPointer pointer = new JsonPointer("/foo/bar/1", "bar", null);
Assert.assertEquals("/foo/bar/1", pointer.toString());
}

@Test
public void testHashCodeAndEqualsMethods() {
JsonPointer pointer1 = new JsonPointer("/foo/bar/1", "bar", null);
JsonPointer pointer2 = new JsonPointer("/foo/bar/1", "bar", null);
JsonPointer pointer3 = new JsonPointer("/foo/baz/1", "baz", null);
Assert.assertEquals(pointer1.hashCode(), pointer1.hashCode());
Assert.assertEquals(pointer1, pointer1);
Assert.assertEquals(pointer1, pointer2);
Assert.assertNotEquals(pointer1, pointer3);
}

@Test
public void testParsingLongIndex() {
JsonPointer pointer = createJsonPointer("/foo/bar/1000000001");
Assert.assertTrue(pointer.mayMatchElement());
Assert.assertEquals(1000000001, pointer.getMatchingIndex());
}

@Test(expected = IllegalArgumentException.class)
public void testParsingInvalidLongIndex() {
createJsonPointer("/foo/bar/2147483649");
}

@Test
public void testParsingElementIndexWithSingleSegment() {
JsonPointer pointer = new JsonPointer("/foo/1", "", null);
Assert.assertTrue(pointer.mayMatchElement());
Assert.assertEquals(1, pointer.getMatchingIndex());
}

@Test
public void testParsingElementIndexWithMultipleSegments() {
JsonPointer pointer = new JsonPointer("/foo/bar/1", "bar", null);
Assert.assertTrue(pointer.mayMatchElement());
Assert.assertEquals(1, pointer.getMatchingIndex());
}

@Test(expected = IllegalArgumentException.class)
public void testParsingNegativeElementIndex() {
createJsonPointer("/foo/-1");
}

@Test(expected = IllegalArgumentException.class)
public void testParsingNegativeElementIndexWithMultipleSegments() {
createJsonPointer("/foo/bar/-1");
}

@Test
public void testParsingPropertyName() {
JsonPointer pointer = new JsonPointer("/foo/bar", "", null);
Assert.assertTrue(pointer.mayMatchProperty());
Assert.assertEquals("bar", pointer.getMatchingProperty());
}

@Test
public void testParsingPropertyNameWithEmptyString() {
JsonPointer pointer = new JsonPointer("/foo/", "", null);
Assert.assertFalse(pointer.mayMatchProperty());
Assert.assertNull(pointer.getMatchingProperty());
}

@Test
public void testParsingPropertyNameWithSingleSegment() {
JsonPointer pointer = new JsonPointer("/foo", "", null);
Assert.assertFalse(pointer.mayMatchProperty());
Assert.assertNull(pointer.getMatchingProperty());
}

@Test
public void testParsingPropertyNameWithMultipleSegments() {
JsonPointer pointer = new JsonPointer("/foo/bar", "", null);
Assert.assertTrue(pointer.mayMatchProperty());
Assert.assertEquals("bar", pointer.getMatchingProperty());
}

@Test
public void testMatchPropertyMethod() {
JsonPointer pointer = new JsonPointer("/foo/bar/1", "bar", null);
JsonPointer matchingProperty = new JsonPointer("/foo/bar", "", null);
JsonPointer nonMatchingProperty = new JsonPointer("/foo/baz", "", null);
Assert.assertEquals(matchingProperty, pointer.matchProperty("bar"));
Assert.assertEquals(null, pointer.matchProperty("baz"));
Assert.assertEquals(null, pointer.matchProperty(""));
}

@Test
public void testMatchElementMethod() {
JsonPointer pointer = new JsonPointer("/foo/bar/1", "bar", null);
JsonPointer matchingElement = new JsonPointer("/foo/bar", "", null);
JsonPointer nonMatchingElement = new JsonPointer("/foo/baz", "", null);
Assert.assertEquals(matchingElement, pointer.matchElement(1));
Assert.assertEquals(null, pointer.matchElement(-1));
Assert.assertEquals(null, pointer.matchElement(0));
Assert.assertEquals(null, pointer.matchElement(2));
}
}