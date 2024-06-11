import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
    }

    @Test
    public void get_shouldReturnCorrectValue() {
        String result = attributes.get("attribute1");

        assertEquals("value1", result);
    }

    @Test
    public void get_shouldReturnEmptyStringIfKeyNotSet() {
        String result = attributes.get("nonExistingKey");

        assertEquals("", result);
    }

    @Test
    public void getIgnoreCase_shouldReturnCorrectValue() {
        String result = attributes.getIgnoreCase("ATTRIBUTE1");

        assertEquals("value1", result);
    }

    @Test
    public void getIgnoreCase_shouldReturnEmptyStringIfKeyNotSet() {
        String result = attributes.getIgnoreCase("nonExistingKey");

        assertEquals("", result);
    }

    @Test
    public void put_shouldAddNewAttribute() {
        attributes.put("attribute3", "value3");

        assertTrue(attributes.hasKey("attribute3"));
        assertEquals("value3", attributes.get("attribute3"));
    }

    @Test
    public void put_shouldReplaceExistingAttribute() {
        attributes.put("attribute1", "newValue");

        assertEquals("newValue", attributes.get("attribute1"));
    }

    @Test
    public void put_booleanValueTrue_shouldAddNewAttribute() {
        attributes.put("attribute3", true);

        assertTrue(attributes.hasKey("attribute3"));
        assertEquals("true", attributes.get("attribute3"));
    }

    @Test
    public void put_booleanValueFalse_shouldRemoveAttribute() {
        attributes.put("attribute2", false);

        assertFalse(attributes.hasKey("attribute2"));
    }

    @Test
    public void remove_shouldRemoveAttributeByKey() {
        attributes.remove("attribute1");

        assertFalse(attributes.hasKey("attribute1"));
    }

    @Test
    public void removeIgnoreCase_shouldRemoveAttributeByKeyCaseInsensitive() {
        attributes.removeIgnoreCase("ATTRIBUTE1");

        assertFalse(attributes.hasKey("attribute1"));
    }

    @Test
    public void hasKey_shouldReturnTrueIfKeyExists() {
        assertTrue(attributes.hasKey("attribute1"));
    }

    @Test
    public void hasKey_shouldReturnFalseIfKeyDoesNotExist() {
        assertFalse(attributes.hasKey("nonExistingKey"));
    }

    @Test
    public void hasKeyIgnoreCase_shouldReturnTrueIfKeyExistsCaseInsensitive() {
        assertTrue(attributes.hasKeyIgnoreCase("ATTRIBUTE1"));
    }

    @Test
    public void hasKeyIgnoreCase_shouldReturnFalseIfKeyDoesNotExistCaseInsensitive() {
        assertFalse(attributes.hasKeyIgnoreCase("nonExistingKey"));
    }

    @Test
    public void size_shouldReturnNumberOfAttributes() {
        int result = attributes.size();

        assertEquals(2, result);
    }

    @Test
    public void addAll_shouldAddAllAttributesFromIncomingSet() {
        Attributes incoming = new Attributes();
        incoming.put("attribute3", "value3");
        incoming.put("attribute4", "value4");

        attributes.addAll(incoming);

        assertTrue(attributes.hasKey("attribute3"));
        assertTrue(attributes.hasKey("attribute4"));
    }

    @Test
    public void iterator_shouldReturnIteratorOverAttributes() {
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void asList_shouldReturnAttributesAsList() {
        List<Attribute> resultList = attributes.asList();

        assertEquals(2, resultList.size());
        assertEquals("attribute1", resultList.get(0).getKey());
        assertEquals("value1", resultList.get(0).getValue());
        assertEquals("attribute2", resultList.get(1).getKey());
        assertEquals("value2", resultList.get(1).getValue());
    }

    @Test
    public void dataset_shouldReturnCustomDataAttributes() {
        attributes.put("data-attribute1", "value1");
        attributes.put("data-attribute2", "value2");
        attributes.put("nonCustomKey", "value3");

        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("attribute1"));
        assertTrue(dataset.containsKey("attribute2"));
        assertFalse(dataset.containsKey("nonCustomKey"));
        assertEquals("value1", dataset.get("attribute1"));
        assertEquals("value2", dataset.get("attribute2"));
    }

    @Test
    public void html_shouldReturnHtmlRepresentationOfAttributes() {
        String result = attributes.html();

        assertTrue(result.contains("attribute1=\"value1\""));
        assertTrue(result.contains("attribute2=\"value2\""));
    }

    @Test
    public void equals_shouldReturnTrueIfAttributesAreEqual() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("attribute1", "value1");
        otherAttributes.put("attribute2", "value2");

        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void equals_shouldReturnFalseIfAttributesAreNotEqual() {
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("attribute1", "value1");
        otherAttributes.put("attribute2", "value3");

        assertFalse(attributes.equals(otherAttributes));
    }

    @Test
    public void hashCode_shouldReturnHashCodeOfAttributes() {
        int result = attributes.hashCode();

        assertEquals(1618750641, result);
    }

    @Test
    public void clone_shouldReturnCopyOfAttributes() {
        Attributes clone = attributes.clone();

        assertEquals(attributes, clone);
    }
}