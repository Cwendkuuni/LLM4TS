import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

    private RecordType recordType;
    private Map<String, RecordTypeBuilder.RecordProperty> properties;

    @Before
    public void setUp() {
        properties = new HashMap<>();
        properties.put("name", new RecordTypeBuilder.RecordProperty(new JSTypeRegistry(), "String", null));
        properties.put("age", new RecordTypeBuilder.RecordProperty(new JSTypeRegistry(), "int", null));
        recordType = new RecordType(new JSTypeRegistry(), properties);
    }

    @Test
    public void testDefineDeclaredProperty() {
        assertTrue(recordType.defineProperty("gender", new JSTypeRegistry().createStringType(), true, null));
    }

    @Test
    public void testDefineDeclaredPropertyWithInferred() {
        assertFalse(recordType.defineProperty("gender", new JSTypeRegistry().createStringType(), false, null));
    }

    // Add more test cases here for other methods

}