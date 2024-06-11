import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;
    private JSTypeRegistry registry;
    private HashMap<String, RecordTypeBuilder.RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
        properties = Maps.newHashMap();
    }

    @Test
    public void testAddProperty() {
        JSType type = registry.createUnknownType();
        Node propertyNode = new Node("testPropertyNode");
        
        // Test adding property
        RecordTypeBuilder result = recordTypeBuilder.addProperty("testName", type, propertyNode);
        assertNotNull(result);
        properties.put("testName", new RecordTypeBuilder.RecordProperty(type, propertyNode));
        assertEquals(properties, recordTypeBuilder.getProperties());
        
        // Test adding duplicate property
        result = recordTypeBuilder.addProperty("testName", type, propertyNode);
        assertNull(result);
    }

    @Test
    public void testBuild() {
        JSType type = registry.createUnknownType();
        Node propertyNode = new Node("testPropertyNode");

        // Test building empty record
        JSType emptyRecord = recordTypeBuilder.build();
        JSType expectedEmptyRecord = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(expectedEmptyRecord, emptyRecord);
        
        // Test building record with properties
        recordTypBuilder.addProperty("testName1", type, propertyNode);
        recordTypBuilder.addProperty("testName2", type, propertyNode);
        JSType record = recordTypeBuilder.build();
        JSType expectedRecord = new RecordType(registry, Collections.unmodifiableMap(properties));
        assertEquals(expectedRecord, record);
    }
}