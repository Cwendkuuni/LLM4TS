import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
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
        Assert.assertNull(jsonPointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals(0, jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer();
        JsonPointer jsonPointer2 = new JsonPointer();
        Assert.assertEquals(jsonPointer1, jsonPointer2);
    }

    @Test
    public void testCompile() {
        Assert.assertNotNull(JsonPointer.compile(""));
    }

    @Test
    public void test_valueOf() {
        Assert.assertNotNull(JsonPointer.valueOf(""));
    }

    @Test
    public void test_parseIndex() {
        Assert.assertEquals(-1, JsonPointer._parseIndex(""));
    }

    @Test
    public void test_parseTail() {
        Assert.assertNotNull(JsonPointer._parseTail(""));
    }

    @Test
    public void test_parseQuotedTail() {
        Assert.assertNotNull(JsonPointer._parseQuotedTail("", 0));
    }

    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, 'c');
        Assert.assertEquals("~c", sb.toString());
    }
}