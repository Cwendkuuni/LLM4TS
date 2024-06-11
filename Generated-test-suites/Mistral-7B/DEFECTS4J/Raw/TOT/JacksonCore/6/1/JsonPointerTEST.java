import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        Assert.assertTrue(emptyPointer.matches());
        Assert.assertEquals("", emptyPointer.getMatchingProperty());
        Assert.assertEquals(-1, emptyPointer.getMatchingIndex());
        Assert.assertNull(emptyPointer.tail());
        Assert.assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        Assert.assertFalse(pointer.matches());
        Assert.assertEquals("foo", pointer.getMatchingProperty());
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        Assert.assertNotNull(pointer.tail());
        Assert.assertEquals("/foo", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("bar");
    }

    @Test
    public void testComplexPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/bar");
        Assert.assertFalse(pointer.matches());
        Assert.assertEquals("foo", pointer.getMatchingProperty());
        Assert.assertEquals(0, pointer.getMatchingIndex());
        Assert.assertNotNull(pointer.tail());
        Assert.assertEquals("/foo/0/bar", pointer.toString());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/bar");
        JsonPointer matchingProperty = pointer.matchProperty("/foo");
        Assert.assertEquals("/foo", matchingProperty.toString());
        Assert.assertNull(matchingProperty.tail());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/bar");
        JsonPointer matchingElement = pointer.matchElement(0);
        Assert.assertEquals("/bar", matchingElement.toString());
        Assert.assertNull(matchingElement.tail());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/bar");
        JsonPointer tail = pointer.tail();
        Assert.assertTrue(tail.matches());
        Assert.assertEquals("/foo/0", tail.getMatchingProperty());
        Assert.assertEquals(-1, tail.getMatchingIndex());
        Assert.assertNotNull(tail.tail());
        Assert.assertEquals("/foo/0", tail.toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/bar");
        Assert.assertEquals("/foo/0/bar", pointer.toString());
    }
}