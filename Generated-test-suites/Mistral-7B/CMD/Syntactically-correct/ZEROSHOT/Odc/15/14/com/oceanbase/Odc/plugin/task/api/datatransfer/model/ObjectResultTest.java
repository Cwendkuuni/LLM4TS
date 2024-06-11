import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    ObjectResult objectResult;
    ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus("testSchema", "testName", ObjectStatus.Status.SUCCESS, 1L, 1L);
        objectResult = new ObjectResult("testSchema", "testName", "testType");
    }

    @Test
    public void testConstructor() {
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectResult result = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getSchema(), result.getSchema());
        assertEquals(objectStatus.getName(), result.getName());
        assertEquals(objectStatus.getType(), result.getType());
        assertEquals(objectStatus.getStatus(), result.getStatus());
        assertEquals(objectStatus.getCount().get(), result.getCount().get());
        assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }

    @Test
    public void testJsonSerializationAndDeserialization() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(objectResult);
            ObjectResult deserialized = mapper.readValue(json, ObjectResult.class);
            assertEquals(objectResult.getSchema(), deserialized.getSchema());
            assertEquals(objectResult.getName(), deserialized.getName());
            assertEquals(objectResult.getType(), deserialized.getType());
            assertEquals(objectResult.getStatus(), deserialized.getStatus());
            assertEquals(objectResult.getCount().get(), deserialized.getCount().get());
            assertEquals(objectResult.getTotal().get(), deserialized.getTotal().get());
        } catch (Exception e) {
            fail("Failed to serialize and deserialize ObjectResult: " + e.getMessage());
        }
    }
}