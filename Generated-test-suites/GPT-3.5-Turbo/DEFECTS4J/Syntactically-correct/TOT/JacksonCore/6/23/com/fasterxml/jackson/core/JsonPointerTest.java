import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNotNull(jsonPointer);
    }

    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        Assert.assertEquals(JsonPointer.EMPTY, jsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("invalid");
    }

    @Test
    public void testCompileWithValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/valid");
        Assert.assertEquals("/valid", jsonPointer.toString());
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/value");
        Assert.assertEquals("/value", jsonPointer.toString());
    }

    @Test
    public void testMatchesEmpty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        Assert.assertTrue(jsonPointer.matches());
    }

    @Test
    public void testMatchesNonEmpty() {
        JsonPointer jsonPointer = JsonPointer.compile("/property");
        Assert.assertFalse(jsonPointer.matches());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/name");
        JsonPointer next = jsonPointer.matchProperty("name");
        Assert.assertNotNull(next);
    }

    @Test
    public void testMatchPropertyWithDifferentName() {
        JsonPointer jsonPointer = JsonPointer.compile("/name");
        JsonPointer next = jsonPointer.matchProperty("different");
        Assert.assertNull(next);
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/0");
        JsonPointer next = jsonPointer.matchElement(0);
        Assert.assertNotNull(next);
    }

    @Test
    public void testMatchElementWithDifferentIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/1");
        JsonPointer next = jsonPointer.matchElement(0);
        Assert.assertNull(next);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = JsonPointer.compile("/property");
        JsonPointer tail = jsonPointer.tail();
        Assert.assertNull(tail);
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.compile("/property");
        Assert.assertEquals("/property", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/property");
        JsonPointer pointer2 = JsonPointer.valueOf("/property");
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/property");
        JsonPointer pointer2 = JsonPointer.compile("/property");
        Assert.assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testParseIndexWithValidString() {
        int index = JsonPointer._parseIndex("1234567890");
        Assert.assertEquals(1234567890, index);
    }

    @Test
    public void testParseIndexWithInvalidString() {
        int index = JsonPointer._parseIndex("invalid");
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testParseTail() {
        String input = "/property";
        JsonPointer jsonPointer = JsonPointer._parseTail(input);
        Assert.assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testParseQuotedTail() {
        String input = "/~0~1property";
        JsonPointer jsonPointer = JsonPointer._parseQuotedTail(input, 1);
        Assert.assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testAppendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        Assert.assertEquals("~0", sb.toString());
    }

}