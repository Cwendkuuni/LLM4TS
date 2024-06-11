import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNotNull(jsonPointer);
    }

    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        Assert.assertEquals(jsonPointer, JsonPointer.EMPTY);
    }

    @Test
    public void testCompileWithInvalidInput() {
        try {
            JsonPointer.compile("invalid");
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCompileWithValidInput() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        Assert.assertNotNull(jsonPointer);
        Assert.assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testValueOf() {
        String input = "/baz";
        JsonPointer jsonPointer = JsonPointer.valueOf(input);
        Assert.assertNotNull(jsonPointer);
        Assert.assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNotNull(jsonPointer.getMatchingProperty());
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "segment", null);
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer.matchProperty("name"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer.matchElement(0));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals(jsonPointer, jsonPointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "segment", null);
        Assert.assertEquals("fullString", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer();
        JsonPointer jsonPointer2 = new JsonPointer();
        Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer();
        JsonPointer jsonPointer2 = new JsonPointer();
        Assert.assertEquals(jsonPointer1, jsonPointer2);
    }

    @Test
    public void test_parseIndexWithValidIndex() {
        String str = "1234";
        int index = JsonPointer._parseIndex(str);
        Assert.assertEquals(1234, index);
    }

    @Test
    public void test_parseIndexWithInvalidIndex() {
        String str = "abc";
        int index = JsonPointer._parseIndex(str);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void test_parseTailWithNoSegments() {
        String input = "/";
        JsonPointer jsonPointer = JsonPointer._parseTail(input);
        Assert.assertEquals(JsonPointer.EMPTY, jsonPointer);
    }

    @Test
    public void test_parseTailWithSegments() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer._parseTail(input);
        Assert.assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void test_parseQuotedTail() {
        String input = "/~1~0";
        JsonPointer jsonPointer = JsonPointer._parseQuotedTail(input, 1);
        Assert.assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        Assert.assertEquals("~0", sb.toString());
    }
}