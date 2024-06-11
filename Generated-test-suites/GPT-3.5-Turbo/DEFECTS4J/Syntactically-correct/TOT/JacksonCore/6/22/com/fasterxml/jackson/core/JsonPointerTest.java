import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches_EmptyPointer_ReturnsTrue() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty_EmptyPointer_ReturnsEmptyString() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex_EmptyPointer_ReturnsNegativeOne() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty_EmptyPointer_ReturnsTrue() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement_EmptyPointer_ReturnsTrue() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty_NextSegmentNull_ReturnsNull() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertNull(jsonPointer.matchProperty("name"));
    }

    @Test
    public void testMatchProperty_NextSegmentNotNullButPropertyNameDoesNotMatch_ReturnsNull() {
        JsonPointer nextSegment = new JsonPointer();
        JsonPointer jsonPointer = new JsonPointer("fullString", "matchingPropertyName", nextSegment);
        Assert.assertNull(jsonPointer.matchProperty("name"));
    }

    @Test
    public void testMatchProperty_NextSegmentNotNullAndPropertyNameMatches_ReturnsNextSegment() {
        JsonPointer nextSegment = new JsonPointer();
        JsonPointer jsonPointer = new JsonPointer("fullString", "matchingPropertyName", nextSegment);
        Assert.assertEquals(nextSegment, jsonPointer.matchProperty("matchingPropertyName"));
    }

    @Test
    public void testMatchElement_IndexDoesNotMatch_ReturnsNull() {
        JsonPointer nextSegment = new JsonPointer();
        JsonPointer jsonPointer = new JsonPointer("fullString", "matchingPropertyName", nextSegment);
        Assert.assertNull(jsonPointer.matchElement(0));
    }

    @Test
    public void testMatchElement_IndexMatches_ReturnsNextSegment() {
        JsonPointer nextSegment = new JsonPointer();
        JsonPointer jsonPointer = new JsonPointer("fullString", "matchingPropertyName", nextSegment);
        Assert.assertEquals(nextSegment, jsonPointer.matchElement(-1));
    }

    @Test
    public void testTail_ReturnsNextSegment() {
        JsonPointer nextSegment = new JsonPointer();
        JsonPointer jsonPointer = new JsonPointer("fullString", "matchingPropertyName", nextSegment);
        Assert.assertEquals(nextSegment, jsonPointer.tail());
    }

}