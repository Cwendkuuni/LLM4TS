package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType.NativeType;
import com.google.javascript.rhino.jstype.JSTypeRegistry.JSTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry.ReferenceType;
import com.google.javascript.rhino.testing.Asserts;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {
    private NamedType namedType;
    private ErrorReporter errorReporter;
    private StaticScope<JSType> enclosing;

    @Before
    public void setUp() {
        namedType = new NamedType(null, "reference", "sourceName", 10, 5);
        errorReporter = new ErrorReporter(null);
        enclosing = new StaticScope<>();
    }

    @Test
    public void testResolveInternal_NullRegistry_ReturnsNull() {
        assertNull(namedType.resolveInternal(errorReporter, enclosing));
    }

    @Test
    public void testResolveInternal_ResolvableRegistryType_CallsFinishPropertyContinuations() {
        namedType.setReferencedAndResolvedType(
            new JSTypeBuilder().setReferenceType(ReferenceType.UNKNOWN_TYPE).end(), errorReporter, enclosing);
        
        NamedType spyNamedType = Asserts.assertType(namedType, NamedType.class);
        spyNamedType.finishPropertyContinuations();

        assertNull(spyNamedType.propertyContinuations);
    }

    @Test
    public void testResolveInternal_ImplicitPrototypeCycle_AddsError() {
        namedType.setReferencedAndResolvedType(
            new JSTypeBuilder().setReferenceType(ReferenceType.UNKNOWN_TYPE).end(), errorReporter, enclosing);
            
        NamedType spyNamedType = Asserts.assertType(namedType, NamedType.class);
        spyNamedType.detectImplicitPrototypeCycle();
        spyNamedType.handleTypeCycle(errorReporter);

        assertNotNull(errorReporter.getErrors().get(0));
        assertEquals("Cycle detected in inheritance chain of type reference", errorReporter.getErrors().get(0).description);
    }

    @Test
    public void testSetValidator_TypeIsResolved_InvokesSuperMethod() {
        namedType.setResolvedTypeInternal(
            new JSTypeBuilder().setReferenceType(ReferenceType.STRING_LITERAL).setValue("value").end());
        namedType.setValidator(null);
        
        assertNotNull(namedType.getValidator());
    }

    @Test
    public void testSetValidator_TypeIsNotResolved_DoesNotInvokeSuperMethod() {
        namedType.setValidator(null);
        
        assertNull(namedType.getValidator());
    }
}