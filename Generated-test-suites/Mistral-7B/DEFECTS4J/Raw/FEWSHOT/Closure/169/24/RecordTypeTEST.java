package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {
    private JSTypeRegistry registry;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        registry = new JSTypeRegistry() {
            @Override
            public JSType getNativeObjectType(int type) {
                return super.getNativeObjectType(type);
            }

            @Override
            public JSTypeRegistry getNativeTypeRegistry() {
                return this;
            }
        };
    }

    @Test
    public void testIsSubtype() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNativeType(1)));
        RecordType recordTypeA = new RecordType(registry, properties, true);

        properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNativeType(2)));
        RecordType recordTypeB = new RecordType(registry, properties, true);

        // Both types are equivalent
        assertTrue(recordTypeA.isSubtype(recordTypeB));
        assertTrue(recordTypeB.isSubtype(recordTypeA));

        // Record type with fewer properties is a subtype of a record type
        // with more properties, as long as the types of the properties
        // are equivalent
        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty("a", registry.getNativeType(1)));
        RecordType recordTypeC = new RecordType(registry, propertiesC, true);
        assertTrue(recordTypeA.isSubtype(recordTypeC));

        // Record type with more properties is not a subtype of a record
        // type with fewer properties, unless all the properties in the
        // larger record are subtypes of the corresponding properties in
        // the smaller record
        Map<String, RecordProperty> propertiesD = Maps.newHashMap();
        propertiesD.put("a", new RecordProperty("a", registry.getNativeType(1)));
        propertiesD.put("b", new RecordProperty("b", registry.getNativeType(3)));
        RecordType recordTypeD = new RecordType(registry, propertiesD, true);
        assertFalse(recordTypeA.isSubtype(recordTypeD));

        // An object type is a subtype of any record type
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(recordTypeA.isSubtype(objectType));
        assertTrue(objectType.isSubtype(recordTypeA));

        // A record type is not a subtype of an object type, unless the
        // record type is the empty record type
        Map<String, RecordProperty> emptyProperties = Maps.newHashMap();
        RecordType emptyRecordType = new RecordType(registry, emptyProperties, true);
        assertTrue(emptyRecordType.isSubtype(objectType));
        assertTrue(objectType.isSubtype(emptyRecordType));

        // A record type with properties is not a subtype of an object type,
        // unless the record type is a subtype of Object and all properties
        // are subtypes of their corresponding properties in Object
        properties = Maps.newHashMap();
        properties.put("a", new RecordProperty("a", registry.getNativeType(1)));
        RecordType recordTypeE = new RecordType(registry, properties, true);
        assertFalse(recordTypeE.isSubtype(objectType));
    }

    @Test
    public void testCheckRecordEquivalence() {
        Map<String, RecordProperty> propertiesA = Maps.newHashMap();
        propertiesA.put("a", new RecordProperty("a", registry.getNativeType(1)));
        RecordType recordTypeA = new RecordType(registry, propertiesA, true);

        Map<String, RecordProperty> propertiesB = Maps.newHashMap();
        propertiesB.put("a", new RecordProperty("a", registry.getNativeType(1)));
        propertiesB.put("b", new RecordProperty("b", registry.getNativeType(2)));
        RecordType recordTypeB = new RecordType(registry, propertiesB, true);

        // Record types are not equivalent if they have different properties
        assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));

        // Record types are equivalent if they have the same properties
        Map<String, RecordProperty> propertiesC = Maps.newHashMap();
        propertiesC.put("a", new RecordProperty("a", registry.getNativeType(1)));
        RecordType recordTypeC = new RecordType(registry, propertiesC, true);
        assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, false));

        // Tolerating unknown properties allows record types with different
        // numbers of properties to be considered equivalent
        assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true));
    }
}