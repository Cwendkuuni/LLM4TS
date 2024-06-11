import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testCompile_InvalidStartingChar_ExceptionThrown() {
        try {
            JsonPointer.compile("test");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid input: JSON Pointer expression must start with '/': \"test\"", e.getMessage());
        }
    }

    @Test
    public void testCompile_EmptyInput_ReturnsEmptyPointer() {
        JsonPointer pointer = JsonPointer.compile("");
        Assert.assertTrue(pointer.matches());
    }

    @Test
    public void testCompile_ValidInput_ReturnsValidPointer() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        Assert.assertFalse(pointer.matches());
        Assert.assertEquals("property", pointer.getMatchingProperty());
        Assert.assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testValueOf_AliasForCompile() {
        JsonPointer pointer = JsonPointer.valueOf("/property/0");
        Assert.assertFalse(pointer.matches());
    }

    @Test
    public void testMatchProperty_PropertyMatches_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        JsonPointer nextSegment = pointer.matchProperty("property");
        Assert.assertNotNull(nextSegment);
        Assert.assertEquals(0, nextSegment.getMatchingIndex());
    }

    @Test
    public void testMatchProperty_PropertyDoesNotMatch_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        JsonPointer nextSegment = pointer.matchProperty("otherProperty");
        Assert.assertNull(nextSegment);
    }

    @Test
    public void testMatchElement_IndexMatches_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        JsonPointer nextSegment = pointer.matchElement(0);
        Assert.assertNotNull(nextSegment);
        Assert.assertEquals("property", nextSegment.getMatchingProperty());
    }

    @Test
    public void testMatchElement_IndexDoesNotMatch_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        JsonPointer nextSegment = pointer.matchElement(1);
        Assert.assertNull(nextSegment);
    }

    @Test
    public void testTail_RetrieveSubPointer_ReturnsSubPointer() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        JsonPointer subPointer = pointer.tail();
        Assert.assertEquals("0", subPointer.getMatchingProperty());
        Assert.assertTrue(subPointer.matches());
    }

    @Test
    public void testToString_ReturnsStringRepresentation() {
        JsonPointer pointer = JsonPointer.compile("/property/0");
        Assert.assertEquals("/property/0", pointer.toString());
    }

    @Test
    public void testEquals_CompareWithEqualInstance_ReturnsTrue() {
        JsonPointer pointer1 = JsonPointer.compile("/property/0");
        JsonPointer pointer2 = JsonPointer.compile("/property/0");
        Assert.assertEquals(pointer1, pointer2);
    }

    @Test
    public void testEquals_CompareWithDifferentInstance_ReturnsFalse() {
        JsonPointer pointer1 = JsonPointer.compile("/property/0");
        JsonPointer pointer2 = JsonPointer.compile("/otherProperty/1");
        Assert.assertNotEquals(pointer1, pointer2);
    }

    @Test
    public void testHashCode_CompareHashCodesOfEqualInstances_ReturnsEqualValues() {
        JsonPointer pointer1 = JsonPointer.compile("/property/0");
        JsonPointer pointer2 = JsonPointer.compile("/property/0");
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testHashCode_CompareHashCodesOfDifferentInstances_ReturnsDifferentValues() {
        JsonPointer pointer1 = JsonPointer.compile("/property/0");
        JsonPointer pointer2 = JsonPointer.compile("/otherProperty/1");
        Assert.assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testChildPointer_ValidInput_ReturnsChildPointer() {
        JsonPointer parent = JsonPointer.compile("/property");
        JsonPointer child = parent.childPointer("0");
        Assert.assertEquals("/property/0", child.toString());
    }

    @Test
    public void testChildPointer_InvalidInput_ReturnsNull() {
        JsonPointer parent = JsonPointer.compile("/property");
        JsonPointer child = parent.childPointer("invalidIndex");
        Assert.assertNull(child);
    }

    @Test
    public void testChildPointer_ChainingChildPointers_ReturnsChildPointer() {
        JsonPointer parent = JsonPointer.compile("/property/0");
        JsonPointer child = parent.childPointer("1").childPointer("2");
        Assert.assertEquals("/property/0/1/2", child.toString());
    }

    @Test
    public void testChildPointer_MatchingPropertyWithSlash_EscapesSlash() {
        JsonPointer parent = JsonPointer.compile("/property");
        JsonPointer child = parent.childPointer("my/property");
        Assert.assertEquals("/property/my%2Fproperty", child.toString());
    }
}