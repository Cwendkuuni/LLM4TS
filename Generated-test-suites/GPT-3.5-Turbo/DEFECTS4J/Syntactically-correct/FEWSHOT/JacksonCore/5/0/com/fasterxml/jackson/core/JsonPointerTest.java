import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test
    public void testConstructorWithArguments() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals("fullString", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("segment", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertEquals("/test", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);

        pointer = JsonPointer.compile("");
        assertEquals("", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test");
        assertEquals("/test", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);

        pointer = JsonPointer.valueOf("");
        assertEquals("", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals(false, pointer.matches());

        pointer = new JsonPointer();
        assertEquals(true, pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals("segment", pointer.getMatchingProperty());

        pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = new JsonPointer("fullString", "1", null);
        assertEquals(1, pointer.getMatchingIndex());

        pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals(true, pointer.mayMatchProperty());

        pointer = new JsonPointer("fullString", "1", null);
        assertEquals(false, pointer.mayMatchProperty());

        pointer = new JsonPointer();
        assertEquals(false, pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals(false, pointer.mayMatchElement());

        pointer = new JsonPointer("fullString", "1", null);
        assertEquals(true, pointer.mayMatchElement());

        pointer = new JsonPointer();
        assertEquals(false, pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals(null, pointer.matchProperty("test"));

        pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals(new JsonPointer(), pointer.matchProperty("segment"));

        pointer = new JsonPointer();
        assertEquals(null, pointer.matchProperty("segment"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "1", new JsonPointer());
        assertEquals(null, pointer.matchElement(0));

        pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals(null, pointer.matchElement(0));

        pointer = new JsonPointer("fullString", "1", new JsonPointer());
        assertEquals(new JsonPointer(), pointer.matchElement(1));

        pointer = new JsonPointer();
        assertEquals(null, pointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals(new JsonPointer(), pointer.tail());

        pointer = new JsonPointer();
        assertEquals(null, pointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals("fullString", pointer.toString());

        pointer = new JsonPointer();
        assertEquals("", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals("fullString".hashCode(), pointer.hashCode());

        pointer = new JsonPointer();
        assertEquals("".hashCode(), pointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", new JsonPointer());
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals(true, pointer1.equals(pointer2));

        pointer1 = new JsonPointer();
        pointer2 = new JsonPointer();
        assertEquals(true, pointer1.equals(pointer2));

        pointer1 = new JsonPointer("fullString", "segment", new JsonPointer());
        pointer2 = new JsonPointer();
        assertEquals(false, pointer1.equals(pointer2));
    }

    @Test
    public void test_parseIndex() {
        int index = JsonPointer._parseIndex("");
        assertEquals(-1, index);

        index = JsonPointer._parseIndex("1");
        assertEquals(1, index);

        index = JsonPointer._parseIndex("12");
        assertEquals(12, index);
    }

    @Test
    public void test_parseTail() {
        JsonPointer pointer = JsonPointer._parseTail("/test");
        assertEquals("/test", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);

        pointer = JsonPointer._parseTail("");
        assertEquals("", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test
    public void test_parseQuotedTail() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("~0test");
        assertEquals("~0test", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("~test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);

        pointer = JsonPointer._parseQuotedTail("~0");
        assertEquals("~0", pointer._asString);
        assertNull(pointer._nextSegment);
        assertEquals("~", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        assertEquals("~0", sb.toString());

        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '1');
        assertEquals("~1", sb.toString());

        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, 'a');
        assertEquals("~a", sb.toString());
    }
}