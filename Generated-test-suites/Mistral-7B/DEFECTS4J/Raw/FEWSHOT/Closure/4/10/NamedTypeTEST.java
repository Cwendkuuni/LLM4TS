package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StylizedName;
import com.google.javascript.rhino.jstype.*;

import java.util.List;

public class NamedTypeTest {

    @Test
    public void testIsNamedType() {
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "String", "", 0, 0);
        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "String", "", 0, 0);
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testHasReferenceName() {
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "String", "", 0, 0);
        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testToStringHelper() {
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "String", "", 0, 0);
        assertEquals("String", namedType.toStringHelper(false));
    }

    @Test
    public void testDefineProperty() {
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "Obj", "", 0, 0);
        List<PropertyContinuation> propertyContinuations = Lists.newArrayList();
        namedType.defineProperty("prop", JSType.NULL, false, null);
        assertEquals(propertyContinuations, namedType.propertyContinuations);
    }

    @Test
    public void testFinishPropertyContinuations() {
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "Obj", "", 0, 0);
        JSType referencedObjType = new ObjectType(JSTypeRegistry.instance(), "String");
        namedType.finishPropertyContinuations();
        assertEquals(referencedObjType, namedType.getReferencedObjTypeInternal());
    }

    @Test
    public void testGetReferencedType() {
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "Obj", "", 0, 0);
        JSType referencedObjType = new ObjectType(JSTypeRegistry.instance(), "String");
        namedType.setReferencedType(referencedObjType);
        assertEquals(referencedObjType, namedType.getReferencedType());
    }

    @Test
    public void testResolveViaRegistry() {
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "String", "", 0, 0);
        JSType referencedType = JSTypeRegistry.instance().getType("String");
        assertTrue(namedType.resolveViaRegistry(errorReporter, enclosingScope));
        assertEquals(referencedType, namedType.getReferencedType());
    }

    @Test
    public void testResolveViaProperties() {
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        JSType objType = new ObjectType(JSTypeRegistry.instance(), "Obj");
        JSType stringType = JSTypeRegistry.instance().getType("String");
        objType.setProperty("prop", stringType);
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "Obj.prop", "", 0, 0);
        assertTrue(namedType.resolveViaProperties(errorReporter, enclosingScope));
        assertEquals(stringType, namedType.getReferencedType());
    }

    @Test
    public void testLookupViaProperties() {
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        JSType objType = new ObjectType(JSTypeRegistry.instance(), "Obj");
        objType.setProperty("prop1", JSTypeRegistry.instance().getType("String"));
        objType.setProperty("prop2", JSTypeRegistry.instance().getType("Number"));
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "Obj.prop2", "", 0, 0);
        JSType lookedUpType = namedType.lookupViaProperties(errorReporter, enclosingScope);
        assertEquals(JSTypeRegistry.instance().getType("Number"), lookedUpType);
    }

    @Test
    public void testSetReferencedAndResolvedType() {
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        JSType objType = new ObjectType(JSTypeRegistry.instance(), "Obj");
        JSType stringType = JSTypeRegistry.instance().getType("String");
        objType.setProperty("prop", stringType);
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "Obj.prop", "", 0, 0);
        JSType referencedType = JSTypeRegistry.instance().getType("Number");
        namedType.setValidator(new Predicate<JSType>() {
            @Override
            public boolean apply(JSType input) {
                return input.equals(referencedType);
            }
        });
        namedType.resolveViaProperties(errorReporter, enclosingScope);
        assertEquals(referencedType, namedType.getReferencedType());
    }

    @Test
    public void testHandleTypeCycle() {
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        JSType cycleType = new ObjectType(JSTypeRegistry.instance(), "CycleType");
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "CycleType.cycleType", "", 0, 0);
        namedType.handleTypeCycle(errorReporter);
        assertEquals(JSTypeRegistry.instance().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    }

    @Test
    public void testCheckEnumElementCycle() {
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        JSType enumType = new EnumType(JSTypeRegistry.instance(), "EnumType", null, null, null);
        JSType enumElementType1 = new EnumElementType(
            JSTypeRegistry.instance(), "EnumType", "EnumValue1", null, null);
        JSType enumElementType2 = new EnumElementType(
            JSTypeRegistry.instance(), "EnumType", "EnumValue2", null, null);
        enumType.setProperty("EnumValue1", enumElementType1);
        enumType.setProperty("EnumValue2", enumElementType2);
        enumElementType1.setPrimitiveType(namedType);
        enumElementType2.setPrimitiveType(namedType);
        namedType.checkEnumElementCycle(errorReporter);
        assertEquals(JSTypeRegistry.instance().getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    }

    @Test
    public void testHandleUnresolvedType() {
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        JSType unresolvedType = JSTypeRegistry.instance().getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE);
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "UnresolvedType", "", 0, 0);
        namedType.handleUnresolvedType(errorReporter, false);
        assertEquals(unresolvedType, namedType.getReferencedType());
    }

    @Test
    public void testGetTypedefType() {
        ErrorReporter errorReporter = ErrorReporter.getInstance();
        StaticScope<JSType> enclosingScope = new StaticScope<>();
        JSType objType = new ObjectType(JSTypeRegistry.instance(), "Obj");
        JSType undefinedType = JSTypeRegistry.instance().getNativeObjectType(JSTypeNative.UNDEFINED);
        StaticSlot<JSType> slot = new StaticSlot<>(objType, new StylizedName("unresolved"));
        slot.setType(undefinedType);
        NamedType namedType = new NamedType(JSTypeRegistry.instance(), "Obj.unresolved", "", 0, 0);
        JSType referencedType = namedType.getTypedefType(errorReporter, slot, "unresolved");
        assertEquals(undefinedType, referencedType);
    }
}