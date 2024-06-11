package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class RecordTypeTest {
    private ErrorReporter errorReporter;
    private JSTypeRegistry registry;
    private RecordType recordTypeA;
    private RecordType recordTypeB;
    private RecordType recordTypeC;

    @Before
    public void setUp() {
        errorReporter = mock(ErrorReporter.class);
        registry = new JSTypeRegistry() {
            @Override
            public JSType getNativeObjectType(int type) {
                switch (type) {
                    case JSTypeNative.OBJECT_TYPE:
                        return new PrototypeObjectType(this, null, null);
                    default:
                        return super.getNativeType(type);
                }
            }
        };

        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeStringType()));
        propertiesA.put("b", new RecordProperty("b", registry.getNativeNumberType()));
        recordTypeA = new RecordType(registry, propertiesA);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getNativeNumberType()));
        propertiesB.put("b", new RecordProperty("b", registry.getNativeStringType()));
        recordTypeB = new RecordType(registry, propertiesB);

        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        propertiesC.put("b", new RecordProperty("b", registry.getNativeStringType()));
        recordTypeC = new RecordType(registry, propertiesC);
    }

    @Test
    public void testIsEquivalentTo_sameRecordType() {
        assertThat(recordTypeA.isEquivalentTo(recordTypeA)).isTrue();
    }

    @Test
    public void testIsEquivalentTo_differentRecordType() {
        assertThat(recordTypeA.isEquivalentTo(recordTypeB)).isFalse();
    }

    @Test
    public void testIsEquivalentTo_differentRecordTypeWithSameProperties() {
        RecordType recordTypeD = new RecordType(registry, propertiesA);
        assertThat(recordTypeA.isEquivalentTo(recordTypeD)).isTrue();
    }

    @Test
    public void testIsEquivalentTo_differentRecordTypeWithDifferentProperties() {
        Map<String, RecordProperty> propertiesD = Maps.newHashMap();
        propertiesD.put("c", new RecordProperty("c", registry.getNativeNumberType()));
        propertiesD.put("a", new RecordProperty("a", registry.getNativeStringType()));
        RecordType recordTypeD = new RecordType(registry, propertiesD);
        assertThat(recordTypeA.isEquivalentTo(recordTypeD)).isFalse();
    }

    @Test
    public void testIsEquivalentTo_objectType() {
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertThat(recordTypeA.isEquivalentTo(objectType)).isFalse();
    }

    @Test
    public void testIsEquivalentTo_null() {
        assertThat(recordTypeA.isEquivalentTo(null)).isFalse();
    }

    @Test
    public void testGetImplicitPrototype() {
        JSType implicitPrototype = recordTypeA.getImplicitPrototype();
        assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
    }

    @Test
    public void testDefineProperty() {
        RecordProperty property = new RecordProperty("c", registry.getNativeNumberType());
        boolean success = recordTypeA.defineProperty("c", property.getType(), false, null);
        assertThat(success).isTrue();
        JSType propertyType = recordTypeA.getPropertyType("c");
        assertSame(property.getType(), propertyType);
    }

    @Test(expected = IllegalStateException.class)
    public void testDefineProperty_frozen() {
        recordTypeA.isFrozen = true;
        RecordProperty property = new RecordProperty("c", registry.getNativeNumberType());
        recordTypeA.defineProperty("c", property.getType(), false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefineProperty_invalidPropertyNode() {
        RecordProperty property = new RecordProperty("c", registry.getNativeNumberType());
        recordTypeA.defineProperty("c", property.getType(), false, null);
        recordTypeA.defineProperty("c", property.getType(), false, null);
        recordTypeA.defineProperty("c", property.getType(), false, new Node());
    }

    @Test
    public void testGetLeastSupertype() {
        RecordType recordTypeE = new RecordType(registry, Maps.newHashMap());
        JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeE);
        JSType recordTypeF = new RecordType(registry, propertiesA);
        JSType greatestSupertype = recordTypeA.getGreatestSubtypeHelper(recordTypeF);
        JSType expected = new RecordTypeBuilder(registry)
            .addProperty("a", registry.getNativeStringType(), null)
            .addProperty("b", registry.getNativeNumberType(), null)
            .build();
        assertSame(expected, greatestSupertype);
        assertThat(leastSupertype.isEquivalentTo(expected)).isTrue();
    }

    @Test
    public void testGetLeastSupertype_sameRecordType() {
        JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeA);
        assertSame(recordTypeA, leastSupertype);
    }

    @Test
    public void testGetLeastSupertype_objectType() {
        JSType leastSupertype = recordTypeA.getLeastSupertype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
        JSType expected = new RecordTypeBuilder(registry)
            .addProperty("a", registry.getNativeStringType(), null)
            .addProperty("b", registry.getNativeNumberType(), null)
            .build();
        assertSame(expected, leastSupertype);
    }

    @Test
    public void testGetLeastSupertype_recordTypeWithLessProperties() {
        Map<String, RecordProperty> propertiesE = Maps.newHashMap();
        propertiesE.put("a", new RecordProperty("a", registry.getNativeStringType()));
        RecordType recordTypeE = new RecordType(registry, propertiesE);
        JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeE);
        JSType expected = new RecordTypeBuilder(registry)
            .addProperty("a", registry.getNativeStringType(), null)
            .build();
        assertSame(expected, leastSupertype);
    }

    @Test
    public void testGetLeastSupertype_recordTypeWithMoreProperties() {
        Map<String, RecordProperty> propertiesD = Maps.newHashMap();
        propertiesD.put("a", new RecordProperty("a", registry.getNativeStringType()));
        propertiesD.put("c", new RecordProperty("c", registry.getNativeNumberType()));
        RecordType recordTypeD = new RecordType(registry, propertiesD);
        JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeD);
        JSType expected = new RecordTypeBuilder(registry)
            .addProperty("a", registry.getNativeStringType(), null)
            .build();
        assertSame(expected, leastSupertype);
    }

    @Test
    public void testGetLeastSupertype_recordTypeWithConflictingPropertyType() {
        Map<String, RecordProperty> propertiesE = Maps.newHashMap();
        propertiesE.put("a", new RecordProperty("a", registry.getNativeBooleanType()));
        propertiesE.put("a", new RecordProperty("a", registry.getNativeStringType()));
        RecordType recordTypeE = new RecordType(registry, propertiesE);
        JSType leastSupertype = recordTypeA.getLeastSupertype(recordTypeE);
        JSType expected = registry.getNativeObjectType(JSTypeNative.NO_TYPE);
        assertSame(expected, leastSupertype);
    }

    @Test
    public void testGetGreatestSubtype() {
        RecordType recordTypeF = new RecordType(registry, propertiesA);
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeF);
        JSType expected = new RecordTypeBuilder(registry)
            .addProperty("a", registry.getNativeStringType(), null)
            .addProperty("b", registry.getNativeNumberType(), null)
            .build();
        assertSame(expected, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtype_sameRecordType() {
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeA);
        assertSame(recordTypeA, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtype_null() {
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(null);
        assertSame(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtype_recordTypeWithLessProperties() {
        Map<String, RecordProperty> propertiesE = Maps.newHashMap();
        propertiesE.put("a", new RecordProperty("a", registry.getNativeStringType()));
        RecordType recordTypeE = new RecordType(registry, propertiesE);
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeE);
        JSType expected = new RecordTypeBuilder(registry)
            .addProperty("a", registry.getNativeStringType(), null)
            .build();
        assertSame(expected, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtype_recordTypeWithMoreProperties() {
        Map<String, RecordProperty> propertiesD = Maps.newHashMap();
        propertiesD.put("a", new RecordProperty("a", registry.getNativeStringType()));
        propertiesD.put("c", new RecordProperty("c", registry.getNativeNumberType()));
        RecordType recordTypeD = new RecordType(registry, propertiesD);
        JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeD);
        JSType expected = new RecordTypeBuilder(registry)
            .addProperty("a", registry.getNativeStringType(), null)
            .build();
        assertSame(expected, greatestSubtype);
    }

    @Test
    public void testIsSubtype() {
        assertThat(recordTypeA.isSubtype(recordTypeA)).isTrue();
        assertThat(recordTypeA.isSubtype(recordTypeB)).isFalse();
        RecordType recordTypeF = new RecordType(registry, propertiesA);
        assertThat(recordTypeA.isSubtype(recordTypeF)).isTrue();
        RecordType recordTypeG = new RecordType(registry, Maps.newHashMap());
        assertThat(recordTypeA.isSubtype(recordTypeG)).isFalse();
        RecordType recordTypeH = new RecordType(registry, propertiesC);
        assertThat(recordTypeA.isSubtype(recordTypeH)).isFalse();
    }

    @Test
    public void testToString() {
        String expected = "{a: string, b: number}";
        String stringRepresentation = recordTypeA.toString();
        assertThat(stringRepresentation).isEqualTo(expected);
    }

    @Test
    public void testResolveInternal() {
        RecordType recordTypeD = new RecordType(registry, propertiesA);
        JSType resolvedType = recordTypeA.resolveInternal(errorReporter, new StaticScope<>(registry));
        JSType expected = recordTypeD;
        assertSame(expected, resolvedType);
    }
}