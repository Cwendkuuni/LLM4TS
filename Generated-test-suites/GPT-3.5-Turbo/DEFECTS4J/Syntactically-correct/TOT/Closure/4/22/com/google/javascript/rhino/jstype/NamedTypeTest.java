import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;

public class NamedTypeTest {

    // Test case for resolveInternal() method when type is resolved via registry
    @Test
    public void testResolveInternalViaRegistry() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();

        NamedType namedType = new NamedType(registry, "Object", "test.js", 1, 1);
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getInstanceType(), resolvedType);
    }

    // Test case for resolveInternal() method when type is resolved via properties
    @Test
    public void testResolveInternalViaProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();
        enclosing.addSlot("goog", registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));

        NamedType namedType = new NamedType(registry, "goog.dom", "test.js", 1, 1);
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE).getInstanceType(), resolvedType);
    }

    // Test case for resolveInternal() method when type is unresolved
    @Test
    public void testResolveInternalUnresolved() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> enclosing = new StaticScope<>();

        NamedType namedType = new NamedType(registry, "UnknownType", "test.js", 1, 1);
        JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

        assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), resolvedType);
    }

    // Test case for setting and validating a validator
    @Test
    public void testSetValidator() {
        JSTypeRegistry registry = new JSTypeRegistry();
        NamedType namedType = new NamedType(registry, "Object", "test.js", 1, 1);

        namedType.setValidator(type -> type.isSameType(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getInstanceType()));

        assertTrue(namedType.getValidator().test(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getInstanceType()));
        assertFalse(namedType.getValidator().test(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)));
    }
}