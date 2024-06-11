import static org.junit.Assert.*;

import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        RecordType type1 = new RecordType();
        RecordType type2 = new RecordType();
        assertEquals(true, type1.isEquivalentTo(type2));
    }
    
    @Test
    public void testGetImplicitPrototype() {
        RecordType type = new RecordType();
        assertEquals(ObjectType.class, type.getImplicitPrototype().getClass());
    }
    
    @Test
    public void testDefineProperty() {
        RecordType type = new RecordType();
        assertEquals(true, type.defineProperty("property1", JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
        assertEquals(false, type.defineProperty("property1", JSTypeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType type1 = new RecordType();
        RecordType type2 = new RecordType();
        assertEquals(type1, type1.getGreatestSubtypeHelper(type2));
    }
    
    @Test
    public void testIsSubtype() {
        RecordType type1 = new RecordType();
        RecordType type2 = new RecordType();
        assertEquals(true, type1.isSubtype(type2));
    }
    
    @Test
    public void testResolveInternal() {
        RecordType type = new RecordType();
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();
        assertEquals(type, type.resolveInternal(errorReporter, scope));
    }
}