import org.junit.Assert;
import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testResolveViaRegistry() {
        JSTypeRegistry registry = new JSTypeRegistry();
        String reference = "com.google.javascript.rhino.jstype.JSTypeNative.STRING_TYPE";
        NamedType namedType = new NamedType(registry, reference, "source", 1, 1);
        
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();
        
        namedType.resolveInternal(errorReporter, enclosing);
        
        JSType referencedType = namedType.getReferencedType();
        Assert.assertNotNull(referencedType);
    }
    
    @Test
    public void testResolveViaProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        String reference = "com.google.javascript.rhino.jstype.JSTypeNative.NUMBER_TYPE";
        NamedType namedType = new NamedType(registry, reference, "source", 1, 1);
        
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();
        
        namedType.resolveInternal(errorReporter, enclosing);
        
        JSType referencedType = namedType.getReferencedType();
        Assert.assertNotNull(referencedType);
    }
    
    @Test
    public void testHandleTypeCycle() {
        JSTypeRegistry registry = new JSTypeRegistry();
        String reference = "com.google.javascript.rhino.jstype.JSTypeNative.FUNCTION_TYPE";
        NamedType namedType = new NamedType(registry, reference, "source", 1, 1);
        
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();
        
        namedType.resolveInternal(errorReporter, enclosing);
        
        JSType referencedType = namedType.getReferencedType();
        Assert.assertNotNull(referencedType);
        
        namedType.resolveInternal(errorReporter, enclosing);
        
        JSType resolvedType = namedType.getResolvedType();
        Assert.assertNotNull(resolvedType);
        Assert.assertNotEquals(resolvedType, referencedType);
    }

}