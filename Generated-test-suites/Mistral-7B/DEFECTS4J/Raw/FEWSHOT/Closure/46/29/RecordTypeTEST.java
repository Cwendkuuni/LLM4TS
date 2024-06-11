package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.SortedMap;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

public class RecordTypeTest {
    private TypeRegistry registry;
    private ErrorReporter errorReporter;
    private StaticScope<JSType> scope;

    @Before
    public void setup() {
        registry = mock(TypeRegistry.class);
        errorReporter = mock(ErrorReporter.class);
        scope = mock(StaticScope.class);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordType recordType1 = createRecordType("{ a: INT, b: INT }");
        RecordType recordType2 = createRecordType("{ a: INT, b: INT }");
        RecordType recordType3 = createRecordType("{ a: INT, c: INT }");

        assertTrue(recordType1.isEquivalentTo(recordType2));
        assertFalse(recordType1.isEquivalentTo(recordType3));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType recordType = createRecordType("{ a: INT }");
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
                recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        RecordType recordType = createRecordType("{ a: INT }");

        // Define a new property
        JSType newPropertyType = IntType.getInstance();
        Node newPropertyNode = Node.NULL();
        assertTrue(recordType.defineProperty("newProperty", newPropertyType,
                false, newPropertyNode));

        // Check if the new property was added
        assertTrue(recordType.hasProperty("newProperty"));
        assertEquals(newPropertyType, recordType.getPropertyType("newProperty"));
    }

    @Test
    public void testFreeze() {
        RecordType recordType = createRecordType("{ a: INT }");

        // Define a new property
        JSType newPropertyType = IntType.getInstance();
        Node newPropertyNode = Node.NULL();
        assertTrue(recordType.defineProperty("newProperty", newPropertyType,
                false, newPropertyNode));

        // Freeze the record type
        recordType.freeze();

        // Try to define a new property
        assertFalse(recordType.defineProperty("newProperty2", IntType.getInstance(),
                false, Node.NULL()));
    }

    @Test
    public void testGetLeastSupertype() {
        RecordType recordType1 = createRecordType("{ a: INT, b: INT }");
        RecordType recordType2 = createRecordType("{ b: INT, a: INT }");
        RecordType recordType3 = createRecordType("{ a: INT, b: INT, c: INT }");
        RecordType recordType4 = createRecordType("{ a: INT, b: INT }");

        JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
        assertTrue(recordType1.isEquivalentTo(leastSupertype));

        leastSupertype = recordType1.getLeastSupertype(recordType3);
        assertEquals(recordType4, leastSupertype);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType1 = createRecordType("{ a: INT, b: INT }");
        RecordType recordType2 = createRecordType("{ b: INT, a: INT }");
        RecordType recordType3 = createRecordType("{ a: INT, c: INT }");
        RecordType recordType4 = createRecordType("{ a: INT, b: INT, d: INT }");
        RecordType recordType5 = createRecordType("{ c: INT, a: INT }");

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertTrue(recordType1.isEquivalentTo(greatestSubtype));

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType4);
        assertEquals(recordType1, greatestSubtype);

        greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType5);
        assertEquals(recordType3, greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = createRecordType("{ a: INT, b: INT }");
        RecordType recordType2 = createRecordType("{ a: INT, b: INT, c: INT }");
        RecordType recordType3 = createRecordType("{ a: INT }");
        RecordType recordType4 = createRecordType("{ b: INT, a: INT }");

        assertTrue(recordType3.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType2));
        assertFalse(recordType1.isSubtype(recordType4));
    }

    @Test
    public void testToString() {
        RecordType recordType = createRecordType("{ a: INT, b: INT }");
        assertEquals("{ a: INT, b: INT }", recordType.toString());
    }

    @Test
    public void testToMaybeRecordType() {
        RecordType recordType = createRecordType("{ a: INT, b: INT }");
        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType = createRecordType("{ a: STRING, b: INT }");
        when(registry.getTypeByName("STRING")).thenReturn(StringType.getInstance());

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertEquals(recordType, resolvedType);
    }

    private RecordType createRecordType(String recordTypeDefinition) {
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        for (String propertyName : recordTypeDefinition.substring(1, recordTypeDefinition.length() - 1)
                .split(",")) {
            String[] propertyParts = propertyName.split(":");
            JSType propertyType = StringType.getInstance(); // Replace with actual type
            if (propertyParts.length > 1) {
                propertyType = TypeRegistry.getInstance().getTypeByQualifiedName(propertyParts[1]);
            }
            RecordProperty recordProperty = new RecordProperty(propertyName, propertyType, null);
            recordProperties.put(propertyName, recordProperty);
        }

        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        return new RecordType(registry, recordProperties);
    }
}