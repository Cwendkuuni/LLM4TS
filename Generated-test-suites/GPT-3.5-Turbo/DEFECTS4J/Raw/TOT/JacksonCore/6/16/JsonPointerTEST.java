import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        Assert.assertTrue(emptyPointer.matches());
        Assert.assertEquals("", emptyPointer.getMatchingProperty());
        Assert.assertEquals(-1, emptyPointer.getMatchingIndex());
        Assert.assertNull(emptyPointer.tail());
    }

    @Test
    public void testJsonPointerMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        Assert.assertFalse(pointer.matches());
        Assert.assertEquals("foo", pointer.getMatchingProperty());
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        Assert.assertNull(pointer.tail());
    }

    @Test
    public void testJsonPointerMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/2");
        Assert.assertFalse(pointer.matches());
        Assert.assertNull(pointer.getMatchingProperty());
        Assert.assertEquals(2, pointer.getMatchingIndex());
        Assert.assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer.compile("foo");
    }

    @Test
    public void testJsonPointerWithNestedSegments() {
        JsonPointer pointer = JsonPointer.compile("/parent/2/child");
        Assert.assertFalse(pointer.matches());
        Assert.assertEquals("parent", pointer.getMatchingProperty());
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        Assert.assertNotNull(pointer.tail());

        JsonPointer nextSegment = pointer.tail();
        Assert.assertFalse(nextSegment.matches());
        Assert.assertEquals(2, nextSegment.getMatchingIndex());
        Assert.assertNull(nextSegment.getMatchingProperty());
        Assert.assertNotNull(nextSegment.tail());

        JsonPointer lastSegment = nextSegment.tail();
        Assert.assertFalse(lastSegment.matches());
        Assert.assertNull(lastSegment.getMatchingProperty());
        Assert.assertEquals("child", lastSegment.getMatchingProperty());
        Assert.assertNull(lastSegment.tail());
    }

}