import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class RecordTypeTest {

    private Map<String, RecordType.RecordProperty> properties;
    private RecordType recordType;

    @Before
    public void setUp() throws Exception {
        properties = new HashMap<>();
        properties.put("key1", new RecordType.RecordProperty(Type.TYPE1, null));
        properties.put("key2", new RecordType.RecordProperty(Type.TYPE2, null));
        properties.put("key3", new RecordType.RecordProperty(Type.TYPE3, null));
        recordType = new RecordType(properties);
    }

    @Test
    public void testProperties() {
        assertEquals(Type.TYPE1, recordType.getPropertyType("key1"));
        assertEquals(Type.TYPE2, recordType.getPropertyType("key2"));
        assertEquals(Type.TYPE3, recordType.getPropertyType("key3"));
    }

    @Test
    public void testDeclareProperty() {
        recordType.defineProperty("key4", Type.TYPE4, true, null);
        assertEquals(Type.TYPE4, recordType.getPropertyType("key4"));

        recordType.defineProperty("key5", Type.TYPE5, false, null);
        assertEquals(Type.TYPE5, recordType.getPropertyType("key5"));
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordType.RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("key1", new RecordType.RecordProperty(Type.TYPE1, null));
        otherProperties.put("key2", new RecordType.RecordProperty(Type.TYPE2, null));
        otherProperties.put("key3", new RecordType.RecordProperty(Type.TYPE3, null));
        RecordType otherRecordType = new RecordType(otherProperties);
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecordType, true));
    }

    @Test
    public void testGetImplicitPrototype() {
        assertEquals(ObjectType.class, recordType.getImplicitPrototype().getClass());
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType otherRecordType = new RecordType(properties);
        assertEquals(recordType, recordType.getGreatestSubtypeHelper(otherRecordType));
    }

    @Test
    public void testIsSubtype() {
        RecordType otherRecordType = new RecordType(properties);
        assertTrue(recordType.isSubtype(otherRecordType));
    }

    @Test
    public void testResolveInternal() {
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertEquals(recordType, resolvedType);
    }
}