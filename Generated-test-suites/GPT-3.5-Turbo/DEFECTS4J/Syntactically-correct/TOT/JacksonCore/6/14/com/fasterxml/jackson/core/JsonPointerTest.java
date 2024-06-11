import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testConstructor() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertNotNull(pointer);
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        Assert.assertEquals(pointer, JsonPointer.EMPTY);

        pointer = JsonPointer.compile("/foo");
        Assert.assertNotNull(pointer);
        Assert.assertEquals("/foo", pointer.toString());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("");
        Assert.assertEquals(pointer, JsonPointer.EMPTY);

        pointer = JsonPointer.valueOf("/foo");
        Assert.assertNotNull(pointer);
        Assert.assertEquals("/foo", pointer.toString());
    }
    
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("");
        Assert.assertTrue(pointer.matches());

        pointer = JsonPointer.compile("/foo");
        Assert.assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("");
        Assert.assertEquals("", pointer.getMatchingProperty());

        pointer = JsonPointer.compile("/foo");
        Assert.assertEquals("foo", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("");
        Assert.assertEquals(-1, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/0");
        Assert.assertEquals(0, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("");
        Assert.assertTrue(pointer.mayMatchProperty());

        pointer = JsonPointer.compile("/foo");
        Assert.assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("");
        Assert.assertFalse(pointer.mayMatchElement());

        pointer = JsonPointer.compile("/0");
        Assert.assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        Assert.assertEquals(pointer, pointer.matchProperty("foo"));

        pointer = JsonPointer.compile("/foo");
        Assert.assertNull(pointer.matchProperty("bar"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        Assert.assertEquals(pointer, pointer.matchElement(0));

        pointer = JsonPointer.compile("/0");
        Assert.assertNull(pointer.matchElement(1));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        Assert.assertNotNull(pointer.tail());
        Assert.assertEquals("/bar", pointer.tail().toString());

        pointer = JsonPointer.compile("");
        Assert.assertEquals(JsonPointer.EMPTY, pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("");
        Assert.assertEquals("", pointer.toString());

        pointer = JsonPointer.compile("/foo");
        Assert.assertEquals("/foo", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("");
        JsonPointer pointer2 = JsonPointer.compile("");
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("");
        JsonPointer pointer2 = JsonPointer.compile("");
        Assert.assertTrue(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        Assert.assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void test_parseIndex() {
        Assert.assertEquals(-1, JsonPointer._parseIndex(""));
        Assert.assertEquals(-1, JsonPointer._parseIndex("abc"));
        Assert.assertEquals(0, JsonPointer._parseIndex("0"));
        Assert.assertEquals(1, JsonPointer._parseIndex("1"));
    }

    @Test
    public void test_parseTail() {
        JsonPointer pointer = JsonPointer._parseTail("");
        Assert.assertEquals("", pointer.toString());

        pointer = JsonPointer._parseTail("/foo/bar");
        Assert.assertEquals("/foo", pointer.toString());
        Assert.assertEquals("/bar", pointer.tail().toString());
    }

    @Test
    public void test_parseQuotedTail() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("~0~1");
        Assert.assertEquals("~0/", pointer.toString());
    }
    
    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        Assert.assertEquals("~0", sb.toString());

        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '1');
        Assert.assertEquals("~1", sb.toString());

        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '2');
        Assert.assertEquals("~2", sb.toString());
    }
}