package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Scope;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {
    private ErrorReporter errorReporter;
    private RecordTypeBuilder recordTypeBuilder;
    private RecordType recordType;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        errorReporter = mock(ErrorReporter.class);
        recordTypeBuilder = new RecordTypeBuilder(mock(JSTypeRegistry.class));
        recordType = new RecordType(
                mock(JSTypeRegistry.class),
                Maps.newHashMapOf(
                        "property1",
                        new RecordProperty("property1", new IntType())
                ),
                true);
    }

    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType2 = new RecordType(
                mock(JSTypeRegistry.class),
                Maps.newHashMapOf(
                        "property1",
                        new RecordProperty("property1", new IntType())
                ),
                true);

        assertTrue(recordType.checkRecordEquivalenceHelper(recordType2, true));

        RecordType recordType3 = new RecordType(
                mock(JSTypeRegistry.class),
                Maps.newHashMapOf(
                        "property1",
                        new RecordProperty("property1", new StringType())
                ),
                true);

        assertFalse(recordType.checkRecordEquivalenceHelper(recordType3, true));
    }

    @Test
    public void testGetImplicitPrototype() {
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        RecordProperty recordProperty = new RecordProperty("property2", new StringType());
        Scope scope = mock(Scope.class);

        JSType jstype = recordType.defineProperty("property2", new StringType(), false, null);
        assertEquals(jstype, recordType.getPropertyType("property2"));

        verify(scope).put(eq("property2"), isA(JSType.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyWithNullRecordProperty() {
        recordType.defineProperty("property2", null, false, null);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType2 = new RecordType(
                mock(JSTypeRegistry.class),
                Maps.newHashMapOf(
                        "property1",
                        new RecordProperty("property1", new IntType())
                ),
                true);

        UnionType unionType = new UnionType(registry);
        unionType.addAlternate(recordType);

        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(unionType);
        assertEquals(unionType, greatestSubtype);

        JSType greatestSubtype2 = recordType.getGreatestSubtypeHelper(new ObjectType(registry, null, null));
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype2);
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType2 = new RecordType(
                mock(JSTypeRegistry.class),
                Maps.newHashMapOf(
                        "property1",
                        new RecordProperty("property1", new IntType())
                ),
                true);

        RecordType recordType3 = new RecordType(
                mock(JSTypeRegistry.class),
                Maps.newHashMapOf(
                        "property1",
                        new RecordProperty("property1", new IntType()),
                        "property2",
                        new RecordProperty("property2", new IntType())
                ),
                true);

        assertTrue(recordType.isSubtype(recordType2));
        assertTrue(recordType3.isSubtype(recordType2));
        assertFalse(recordType.isSubtype(new IntType()));
        assertFalse(recordType.isSubtype(new StringType()));
        assertFalse(recordType.isSubtype(new ArrayType(new IntType()[0])));
        assertFalse(recordType.isSubtype(new ObjectType(registry, null, null)));
        assertFalse(recordType.isSubtype(new NativeJavaObjectType("java.lang.String")));
        assertFalse(recordType.isSubtype(new FunctionType(new NativeContextFunctionType(),
                new IntType(), new IntType(), registry, new NodeOperations())));
    }

    @Test
    public void testResolveInternal() {
        RecordProperty recordProperty = new RecordProperty("property1", new IntType());
        Scope scope = mock(Scope.class);
        when(scope.put(eq("property1"), isA(JSType.class))).thenReturn(recordProperty);

        recordType.resolveInternal(errorReporter, scope);

        verify(scope).put(eq("property1"), isA(JSType.class));
    }
}