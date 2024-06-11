import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

    @Test
    public void testConstructor() {
        Map<String, RecordType.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordType.RecordProperty(new JSType(), new Node()));
        properties.put("b", new RecordType.RecordProperty(new JSType(), new Node()));

        RecordType recordType = new RecordType(new JSTypeRegistry(), properties, true);

        Assert.assertNotNull(recordType);
        Assert.assertEquals(2, recordType.getProperties().size());
    }

    @Test
    public void testConstructorWithNullProperty() {
        Map<String, RecordType.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordType.RecordProperty(new JSType(), new Node()));
        properties.put("b", null);

        try {
            new RecordType(new JSTypeRegistry(), properties, true);
            Assert.fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
        }
    }

    @Test
    public void testIsSynthetic() {
        Map<String, RecordType.RecordProperty> properties = new HashMap<>();

        RecordType recordDeclaredType = new RecordType(new JSTypeRegistry(), properties, true);
        RecordType recordSyntheticType = new RecordType(new JSTypeRegistry(), properties, false);

        Assert.assertFalse(recordDeclaredType.isSynthetic());
        Assert.assertTrue(recordSyntheticType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordType.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordType.RecordProperty(new JSType(), new Node()));
        properties1.put("b", new RecordType.RecordProperty(new JSType(), new Node()));

        Map<String, RecordType.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordType.RecordProperty(new JSType(), new Node()));
        properties2.put("b", new RecordType.RecordProperty(new JSType(), new Node()));

        RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

        Assert.assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    }

    @Test
    public void testDefineProperty() {
        Map<String, RecordType.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordType.RecordProperty(new JSType(), new Node()));

        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);

        boolean result = recordType.defineProperty("b", new JSType(), true, new Node());

        Assert.assertTrue(result);
        Assert.assertEquals(2, recordType.getProperties().size());
    }

    @Test
    public void testDefinePropertyWhenFrozen() {
        Map<String, RecordType.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordType.RecordProperty(new JSType(), new Node()));

        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
        recordType.setFrozen(true);

        boolean result = recordType.defineProperty("b", new JSType(), true, new Node());

        Assert.assertFalse(result);
        Assert.assertEquals(1, recordType.getProperties().size());
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        Map<String, RecordType.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordType.RecordProperty(new JSType(), new Node()));
        properties.put("b", new RecordType.RecordProperty(new JSType(), new Node()));

        Map<String, RecordType.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordType.RecordProperty(new JSType(), new Node()));

        RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties);
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

        Assert.assertEquals(1, greatestSubtype.getProperties().size());
        Assert.assertTrue(greatestSubtype.hasProperty("a"));
        Assert.assertFalse(greatestSubtype.hasProperty("b"));
    }

    @Test
    public void testIsSubtype() {
        Map<String, RecordType.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", new RecordType.RecordProperty(new JSType(), new Node()));
        properties1.put("b", new RecordType.RecordProperty(new JSType(), new Node()));

        Map<String, RecordType.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", new RecordType.RecordProperty(new JSType(), new Node()));

        RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

        Assert.assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testResolveInternal() {
        Map<String, RecordType.RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordType.RecordProperty(new JSType(), new Node()));

        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
        StaticScope<JSType> scope = new StaticScope<>();

        JSType resolvedType = recordType.resolveInternal(new ErrorReporter(), scope);

        Assert.assertNotNull(resolvedType);
    }

}