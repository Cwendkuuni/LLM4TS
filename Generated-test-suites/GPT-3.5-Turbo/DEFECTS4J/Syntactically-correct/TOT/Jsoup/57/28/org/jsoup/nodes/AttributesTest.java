import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet_ValidKey_ReturnsValue() {
        attributes.put("name", "John");
        String value = attributes.get("name");
        assertEquals("John", value);
    }

    @Test
    public void testGet_EmptyKey_ReturnsEmptyString() {
        String value = attributes.get("");
        assertEquals("", value);
    }

    @Test
    public void testGet_KeyNotFound_ReturnsEmptyString() {
        String value = attributes.get("age");
        assertEquals("", value);
    }

    @Test
    public void testGetIgnoreCase_ValidKey_ReturnsValue() {
        attributes.put("Name", "John");
        String value = attributes.getIgnoreCase("name");
        assertEquals("John", value);
    }

    @Test
    public void testGetIgnoreCase_KeyNotFound_ReturnsEmptyString() {
        attributes.put("name", "John");
        String value = attributes.getIgnoreCase("age");
        assertEquals("", value);
    }

    @Test
    public void testPut_NewAttribute_AddsAttribute() {
        attributes.put("name", "John");
        assertTrue(attributes.hasKey("name"));
        assertEquals("John", attributes.get("name"));
    }

    @Test
    public void testPut_ExistingAttribute_ReplacesAttribute() {
        attributes.put("name", "John");
        attributes.put("name", "Jane");
        assertEquals("Jane", attributes.get("name"));
    }

    @Test
    public void testPut_BoolValueTrue_AddsAttribute() {
        attributes.put("enabled", true);
        assertTrue(attributes.hasKey("enabled"));
        assertEquals("", attributes.get("enabled"));
    }

    @Test
    public void testPut_BoolValueFalse_RemovesAttribute() {
        attributes.put("enabled", true);
        attributes.put("enabled", false);
        assertFalse(attributes.hasKey("enabled"));
    }

    @Test
    public void testRemove_ExistingAttribute_RemovesAttribute() {
        attributes.put("name", "John");
        attributes.remove("name");
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testRemove_NonExistingAttribute_NoEffect() {
        attributes.remove("name");
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testRemoveIgnoreCase_ExistingAttribute_RemovesAttribute() {
        attributes.put("name", "John");
        attributes.removeIgnoreCase("NAME");
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testRemoveIgnoreCase_NonExistingAttribute_NoEffect() {
        attributes.removeIgnoreCase("name");
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testHasKey_ExistingKey_ReturnsTrue() {
        attributes.put("name", "John");
        assertTrue(attributes.hasKey("name"));
    }

    @Test
    public void testHasKey_NonExistingKey_ReturnsFalse() {
        assertFalse(attributes.hasKey("name"));
    }

    @Test
    public void testHasKeyIgnoreCase_ExistingKey_ReturnsTrue() {
        attributes.put("name", "John");
        assertTrue(attributes.hasKeyIgnoreCase("NAME"));
    }

    @Test
    public void testHasKeyIgnoreCase_NonExistingKey_ReturnsFalse() {
        assertFalse(attributes.hasKeyIgnoreCase("name"));
    }

    @Test
    public void testSize_EmptyAttributes_ReturnsZero() {
        int size = attributes.size();
        assertEquals(0, size);
    }

    @Test
    public void testSize_AttributesAdded_ReturnsCorrectSize() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        int size = attributes.size();
        assertEquals(2, size);
    }

    @Test
    public void testAddAll_EmptyAttributes_NoEffect() {
        Attributes incoming = new Attributes();
        attributes.addAll(incoming);
        int size = attributes.size();
        assertEquals(0, size);
    }

    @Test
    public void testAddAll_AttributesAdded_MergesAttributes() {
        Attributes incoming = new Attributes();
        incoming.put("name", "John");
        incoming.put("age", "25");
        attributes.addAll(incoming);
        assertTrue(attributes.hasKey("name"));
        assertEquals("John", attributes.get("name"));
        assertTrue(attributes.hasKey("age"));
        assertEquals("25", attributes.get("age"));
    }

    @Test
    public void testIterator_EmptyAttributes_ReturnsEmptyIterator() {
        assertFalse(attributes.iterator().hasNext());
    }

    @Test
    public void testIterator_AttributesAdded_ReturnsIteratorWithAttributes() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        assertTrue(attributes.iterator().hasNext());
        assertEquals("John", attributes.iterator().next().getValue());
    }

    @Test
    public void testAsList_EmptyAttributes_ReturnsEmptyList() {
        assertEquals(0, attributes.asList().size());
    }

    @Test
    public void testAsList_AttributesAdded_ReturnsListWithAttributes() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        assertEquals(2, attributes.asList().size());
        assertEquals("John", attributes.asList().get(0).getValue());
    }

    @Test
    public void testDataset_EmptyAttributes_ReturnsEmptyMap() {
        assertEquals(0, attributes.dataset().size());
    }

    @Test
    public void testDataset_AttributesWithCustomData_ReturnsMapWithCustomData() {
        attributes.put("data-name", "John");
        attributes.put("data-age", "25");
        assertEquals(2, attributes.dataset().size());
        assertEquals("John", attributes.dataset().get("name"));
    }

    @Test
    public void testHtml_EmptyAttributes_ReturnsEmptyString() {
        String html = attributes.html();
        assertEquals("", html);
    }

    @Test
    public void testHtml_AttributesAdded_ReturnsHtmlString() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        String html = attributes.html();
        assertEquals(" name=\"John\" age=\"25\"", html);
    }

    @Test
    public void testEquals_SameAttributes_ReturnsTrue() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        Attributes other = new Attributes();
        other.put("name", "John");
        other.put("age", "25");
        assertTrue(attributes.equals(other));
    }

    @Test
    public void testEquals_DifferentAttributes_ReturnsFalse() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        Attributes other = new Attributes();
        other.put("name", "Jane");
        other.put("age", "25");
        assertFalse(attributes.equals(other));
    }

    @Test
    public void testHashCode_SameAttributes_ReturnsSameHashCode() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        Attributes other = new Attributes();
        other.put("name", "John");
        other.put("age", "25");
        assertEquals(attributes.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCode_DifferentAttributes_ReturnsDifferentHashCode() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        Attributes other = new Attributes();
        other.put("name", "Jane");
        other.put("age", "25");
        assertNotEquals(attributes.hashCode(), other.hashCode());
    }

    @Test
    public void testClone_EmptyAttributes_ReturnsClonedAttributes() {
        Attributes clone = attributes.clone();
        assertNotSame(attributes, clone);
        assertEquals(attributes.size(), clone.size());
    }

    @Test
    public void testClone_AttributesAdded_ReturnsClonedAttributes() {
        attributes.put("name", "John");
        attributes.put("age", "25");
        Attributes clone = attributes.clone();
        assertNotSame(attributes, clone);
        assertEquals(attributes.get("name"), clone.get("name"));
        assertEquals(attributes.get("age"), clone.get("age"));
    }
}