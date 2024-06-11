import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testJsonPointerMatches() {
        JsonPointer jsonPointer = new JsonPointer();

        boolean result = jsonPointer.matches();

        Assert.assertTrue(result);
    }

    @Test
    public void testJsonPointerGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._matchingPropertyName = "property";

        String result = jsonPointer.getMatchingProperty();

        Assert.assertEquals("property", result);
    }

    @Test
    public void testJsonPointerGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._matchingElementIndex = 5;

        int result = jsonPointer.getMatchingIndex();

        Assert.assertEquals(5, result);
    }

    @Test
    public void testJsonPointerMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._matchingPropertyName = "property";

        boolean result = jsonPointer.mayMatchProperty();

        Assert.assertTrue(result);
    }

    @Test
    public void testJsonPointerMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._matchingElementIndex = 5;

        boolean result = jsonPointer.mayMatchElement();

        Assert.assertTrue(result);
    }

    @Test
    public void testJsonPointerMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._nextSegment = new JsonPointer();
        jsonPointer._matchingPropertyName = "property";

        JsonPointer result = jsonPointer.matchProperty("property");

        Assert.assertEquals(jsonPointer._nextSegment, result);
    }

    @Test
    public void testJsonPointerMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._nextSegment = new JsonPointer();
        jsonPointer._matchingElementIndex = 5;

        JsonPointer result = jsonPointer.matchElement(5);

        Assert.assertEquals(jsonPointer._nextSegment, result);
    }

    @Test
    public void testJsonPointerTail() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._nextSegment = new JsonPointer();

        JsonPointer result = jsonPointer.tail();

        Assert.assertEquals(jsonPointer._nextSegment, result);
    }

    @Test
    public void testJsonPointerToString() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._asString = "/property";

        String result = jsonPointer.toString();

        Assert.assertEquals("/property", result);
    }

    @Test
    public void testJsonPointerHashCode() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._asString = "/property";

        int result = jsonPointer.hashCode();

        Assert.assertEquals("/property".hashCode(), result);
    }

    @Test
    public void testJsonPointerEquals() {
        JsonPointer jsonPointer1 = new JsonPointer();
        jsonPointer1._asString = "/property";

        JsonPointer jsonPointer2 = new JsonPointer();
        jsonPointer2._asString = "/property";

        Assert.assertEquals(jsonPointer1, jsonPointer2);
    }

    @Test
    public void testJsonPointerCompileWithEmptyString() {
        JsonPointer result = JsonPointer.compile("");

        Assert.assertEquals(JsonPointer.EMPTY, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerCompileWithInvalidInput() {
        JsonPointer.compile("property");
    }

    @Test
    public void testJsonPointerCompile() {
        JsonPointer result = JsonPointer.compile("/property");

        Assert.assertEquals("/property", result.toString());
    }

    @Test
    public void testJsonPointerValueOf() {
        JsonPointer result = JsonPointer.valueOf("/property");

        Assert.assertEquals("/property", result.toString());
    }

    @Test
    public void testJsonPointerFromSegmentWithEmptySegments() {
        JsonPointer result = JsonPointer.fromSegment();

        Assert.assertEquals(JsonPointer.EMPTY, result);
    }

    @Test
    public void testJsonPointerFromSegment() {
        JsonPointer result = JsonPointer.fromSegment("property");

        Assert.assertEquals("/property", result.toString());
    }

    private JsonPointer createJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer();
        jsonPointer._nextSegment = new JsonPointer();
        jsonPointer._matchingPropertyName = "property";
        jsonPointer._matchingElementIndex = 5;
        jsonPointer._asString = "/property";
        return jsonPointer;
    }

    @Test
    public void testJsonPointerParseIndexWithEmptyString() {
        int result = JsonPointer._parseIndex("");

        Assert.assertEquals(-1, result);
    }

    @Test
    public void testJsonPointerParseIndexWithLeadingZeroes() {
        int result = JsonPointer._parseIndex("0001");

        Assert.assertEquals(-1, result);
    }

    @Test
    public void testJsonPointerParseIndexWithExceedingLength() {
        int result = JsonPointer._parseIndex("12345678901");

        Assert.assertEquals(-1, result);
    }

    @Test
    public void testJsonPointerParseIndexWithValidIndexString() {
        int result = JsonPointer._parseIndex("5");

        Assert.assertEquals(5, result);
    }

    @Test
    public void testJsonPointerParseIndexWithInvalidIndexString() {
        int result = JsonPointer._parseIndex("invalidIndex");

        Assert.assertEquals(-1, result);
    }

    @Test
    public void testJsonPointerParseIndexWithValidIndexStringAndLengthTen() {
        int result = JsonPointer._parseIndex("1234567890");

        Assert.assertEquals(1234567890, result);
    }

    @Test
    public void testJsonPointerParseTail() {
        JsonPointer jsonPointer = JsonPointer._parseTail("/property");

        Assert.assertEquals("/property", jsonPointer._asString);
        Assert.assertEquals("property", jsonPointer._matchingPropertyName);
        Assert.assertEquals(JsonPointer.EMPTY, jsonPointer._nextSegment);
    }

    @Test
    public void testJsonPointerParseQuotedTail() {
        JsonPointer jsonPointer = JsonPointer._parseQuotedTail("~1property");

        Assert.assertEquals("/property", jsonPointer._asString);
        Assert.assertEquals("property", jsonPointer._matchingPropertyName);
        Assert.assertEquals(JsonPointer.EMPTY, jsonPointer._nextSegment);
    }

}