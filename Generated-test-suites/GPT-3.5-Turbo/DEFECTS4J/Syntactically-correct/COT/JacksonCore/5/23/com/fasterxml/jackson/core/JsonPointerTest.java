
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

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
        Assert.assertTrue(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer.matchProperty(""));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer.matchElement(-1));
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
        JsonPointer jsonPointer1 = new JsonPointer();
        JsonPointer jsonPointer2 = new JsonPointer();
        Assert.assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer();
        JsonPointer jsonPointer2 = new JsonPointer();
        Assert.assertTrue(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        Assert.assertTrue(jsonPointer.matches());
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("");
        Assert.assertTrue(jsonPointer.matches());
    }

    @Test
    public void test_parseIndex() {
        int index = JsonPointer._parseIndex("0");
        Assert.assertEquals(0, index);
    }

    @Test
    public void test_parseTail() {
        JsonPointer jsonPointer = JsonPointer._parseTail("/test");
        Assert.assertNotNull(jsonPointer);
    }

    @Test
    public void test_parseQuotedTail() {
        JsonPointer jsonPointer = JsonPointer._parseQuotedTail("~0", 0);
        Assert.assertNotNull(jsonPointer);
    }
}
