import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {
    private RecordTypeBuilder recordTypeBuilder;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        // Test adding a property with a new name
        String propertyName = "testProperty";
        JSType propertyType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node("propertyNode");
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(propertyType, propertyNode);

        RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

        // Verify the result is not null
        assertNotNull(result);
        // Verify the properties map contains the added property
        assertTrue(recordTypeBuilder.properties.containsKey(propertyName));
        // Verify the added property matches the expected record property
        assertEquals(recordTypeBuilder.properties.get(propertyName), recordProperty);

        // Test adding a property with an existing name
        String duplicatePropertyName = "testProperty";
        JSType duplicatePropertyType = registry.getNativeType(JSTypeNative.STRING_TYPE);

        RecordTypeBuilder duplicateResult = recordTypeBuilder.addProperty(duplicatePropertyName, duplicatePropertyType, null);

        // Verify the result is null
        assertNull(duplicateResult);
        // Verify the properties map still contains the original property
        assertTrue(recordTypeBuilder.properties.containsKey(propertyName));
        // Verify the original property is unchanged
        assertEquals(recordTypeBuilder.properties.get(propertyName), recordProperty);
    }

    @Test
    public void testBuild() {
        // Test building an empty record
        JSType expectedEmptyRecord = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType emptyRecord = recordTypeBuilder.build();

        // Verify the built empty record matches the expected empty record
        assertEquals(emptyRecord, expectedEmptyRecord);

        // Test building a non-empty record
        String propertyName = "testProperty";
        JSType propertyType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node("propertyNode");
        recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

        Map<String, RecordTypeBuilder.RecordProperty> expectedProperties = Collections.unmodifiableMap(Collections.singletonMap(propertyName, new RecordTypeBuilder.RecordProperty(propertyType, propertyNode)));
        JSType expectedRecord = new RecordType(registry, expectedProperties);

        JSType record = recordTypeBuilder.build();

        // Verify the built record matches the expected record
        assertEquals(record, expectedRecord);
    }
}