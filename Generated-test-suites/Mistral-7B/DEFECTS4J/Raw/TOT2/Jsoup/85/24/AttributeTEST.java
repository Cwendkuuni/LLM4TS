import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeCreationWithValidKeyAndValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    // ... other test cases for individual methods ...

    @Test
    public void testAttributeCreationWithNullParent() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);
        assertNull(attribute.getParent());
    }

    // ... other test cases for edge cases and exception handling scenarios ...

    @Test
    public void testAttributeEquals() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);
        Attribute attribute3 = new Attribute(key1, value1);

        assertTrue(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute2));
    }

    @Test
    public void testAttributeHashCode() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testAttributeClone() throws CloneNotSupportedException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

    // ... other test cases for edge cases and exception handling scenarios ...

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setValue(null);
    }

    // ... other test cases for edge cases and exception handling scenarios with specific exceptions ...
}