import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

    @Test
    public void testResolveInternal_withValidReference_returnsResolvedType() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "com.google.javascript.rhino.jstype.ObjectType", "", 0, 0);
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> staticScope = new StaticScope<>();
        
        JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
        
        assertEquals(resolvedType, namedType.getReferencedType());
    }
    
    @Test
    public void testResolveInternal_withInvalidReference_returnsUnknownType() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "InvalidType", "", 0, 0);
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> staticScope = new StaticScope<>();
        
        JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
        
        assertEquals(resolvedType, namedType.getReferencedType());
        assertTrue(resolvedType.isUnknownType());
    }
    
    @Test
    public void testResolveViaProperties_withValidReference_returnsResolvedType() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "com.google.javascript.rhino.jstype.ObjectType", "", 0, 0);
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> staticScope = new StaticScope<>();
        
        JSType resolvedType = namedType.lookupViaProperties(errorReporter, staticScope);
        
        assertEquals(resolvedType, namedType.getReferencedType());
    }
    
    @Test
    public void testResolveViaProperties_withInvalidReference_returnsNull() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "InvalidType", "", 0, 0);
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> staticScope = new StaticScope<>();
        
        JSType resolvedType = namedType.lookupViaProperties(errorReporter, staticScope);
        
        assertNull(resolvedType);
    }
    
    @Test
    public void testSetValidator_withResolvedType_validatesType() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "com.google.javascript.rhino.jstype.ObjectType", "", 0, 0);
        namedType.resolveInternal(new ErrorReporter(), new StaticScope<>());
        Predicate<JSType> validator = new Predicate<JSType>() {
            public boolean apply(JSType type) {
                return type.isObject();
            }
        };
        
        boolean result = namedType.setValidator(validator);
        
        assertTrue(result);
    }
    
    @Test
    public void testSetValidator_withUnresolvedType_doesNotValidateType() {
        NamedType namedType = new NamedType(new JSTypeRegistry(), "InvalidType", "", 0, 0);
        Predicate<JSType> validator = new Predicate<JSType>() {
            public boolean apply(JSType type) {
                return type.isObject();
            }
        };
        
        boolean result = namedType.setValidator(validator);
        
        assertTrue(result);
    }
}