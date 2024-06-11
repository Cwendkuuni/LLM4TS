import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testConstructorEmptyInstance() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals("", pointer.toString());
    }

    @Test
    public void testConstructorNonEmptyInstance() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        Assert.assertEquals("pointer", pointer.toString());
    }

    @Test
    public void testCompileValidInput() {
        String input = "/segment";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertEquals(input, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        String input = "segment";
        JsonPointer pointer = JsonPointer.compile(input);
    }

    @Test
    public void testValueOfValidInput() {
        String input = "/segment";
        JsonPointer pointer = JsonPointer.valueOf(input);
        Assert.assertEquals(input, pointer.toString());
    }

    @Test
    public void testValueOfEmptyInput() {
        String input = "";
        JsonPointer pointer = JsonPointer.valueOf(input);
        Assert.assertEquals("", pointer.toString());
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        Assert.assertEquals("segment", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        Assert.assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        Assert.assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        Assert.assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        JsonPointer result = pointer.matchProperty("segment");
        Assert.assertNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        JsonPointer result = pointer.matchElement(1);
        Assert.assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        JsonPointer result = pointer.tail();
        Assert.assertNull(result);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        Assert.assertEquals("pointer", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("pointer", "segment", null);
        JsonPointer pointer2 = new JsonPointer("pointer", "segment", null);
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEqualsSameObject() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        Assert.assertEquals(pointer, pointer);
    }

    @Test
    public void testEqualsDifferentClass() {
        JsonPointer pointer = new JsonPointer("pointer", "segment", null);
        String other = "pointer";
        Assert.assertNotEquals(pointer, other);
    }

    @Test
    public void testEqualsEqualObjects() {
        JsonPointer pointer1 = new JsonPointer("pointer", "segment", null);
        JsonPointer pointer2 = new JsonPointer("pointer", "segment", null);
        Assert.assertEquals(pointer1, pointer2);
    }

    @Test
    public void testEqualsUnequalObjects() {
        JsonPointer pointer1 = new JsonPointer("pointer1", "segment1", null);
        JsonPointer pointer2 = new JsonPointer("pointer2", "segment2", null);
        Assert.assertNotEquals(pointer1, pointer2);
    }

    @Test
    public void testAppendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        Assert.assertEquals("~0", sb.toString());
    }

    @Test
    public void testAppendEscapeWithSpecialCharacter() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '1');
        Assert.assertEquals("~1", sb.toString());
    }

}