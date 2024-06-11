import org.junit.*;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType testType1 = new RecordType(registry, null);
        RecordType testType2 = new RecordType(registry, null);
        assertTrue(testType1.isEquivalentTo(testType2));
    }

    @Test
    public void testGetPropertyType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType testType = new RecordType(registry, null);
        JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType actual = testType.getPropertyType("property");
        assertEquals(expected, actual);
    }

    @Test
    public void testHasProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType testType = new RecordType(registry, null);
        assertTrue(testType.hasProperty("property"));
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType testType1 = new RecordType(registry, null);
        RecordType testType2 = new RecordType(registry, null);
        JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType actual = testType1.getLeastSupertype(testType2);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType testType1 = new RecordType(registry, null);
        JSType testType2 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType expected = registry.getNativeObjectType(JSTypeNative.NO_TYPE);
        JSType actual = testType1.getGreatestSubtypeHelper(testType2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType testType1 = new RecordType(registry, null);
        JSType testType2 = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertFalse(testType1.isSubtype(testType2));
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType testType = new RecordType(registry, null);
        ErrorReporter t = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<JSType>();
        JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType actual = testType.resolveInternal(t, scope);
        assertEquals(expected, actual);
    }
}