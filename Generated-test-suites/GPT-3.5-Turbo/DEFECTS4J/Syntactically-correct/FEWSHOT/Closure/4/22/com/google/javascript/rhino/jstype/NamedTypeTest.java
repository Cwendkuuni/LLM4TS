package com.google.javascript.rhino.jstype;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
public class NamedTypeTest {
    @Test
    public void testDefineProperty() {
        NamedType namedType = new NamedType(null, "", "", 0, 0);
        namedType.defineProperty("propertyName", null, true, null);
        assertNull(namedType.lookupViaProperties(null, null));
    }
    @Test
    public void testLookupViaProperties() {
        NamedType namedType = new NamedType(null, "", "", 0, 0);
        assertNull(namedType.lookupViaProperties(null, null));
        namedType.setReferencedType(null);
        assertNull(namedType.lookupViaProperties(null, null));
    }
    @Test
    public void testSetReferencedAndResolvedType() {
        NamedType namedType = new NamedType(null, "", "", 0, 0);
        namedType.setReferencedAndResolvedType(null, null, null);
        assertEquals(namedType.getReferencedType(), namedType.getReferencedObjTypeInternal());
    }
    @Test
    public void testHandleTypeCycle() {
        ErrorReporter reporter = null;
        NamedType namedType = new NamedType(null, "", "", 0, 0);
        namedType.handleTypeCycle(reporter);
        assertNull(namedType.getReferencedType());
    }
    @Test
    public void testCheckEnumElementCycle() {
        ErrorReporter reporter = null;
        NamedType namedType = new NamedType(null, "", "", 0, 0);
        namedType.checkEnumElementCycle(reporter);
        assertNull(namedType.getReferencedType());
    }
    @Test
    public void testHandleUnresolvedType() {
        ErrorReporter reporter = null;
        NamedType namedType = new NamedType(null, "", "", 0, 0);
        namedType.handleUnresolvedType(reporter, true);
        assertNull(namedType.getReferencedType());
    }
    @Test
    public void testGetTypedefType() {
        ErrorReporter reporter = null;
        StaticSlot<JSType> slot = null;
        String name = "";
        NamedType namedType = new NamedType(null, "", "", 0, 0);
        JSType result = namedType.getTypedefType(reporter, slot, name);
        assertNull(result);
    }
}