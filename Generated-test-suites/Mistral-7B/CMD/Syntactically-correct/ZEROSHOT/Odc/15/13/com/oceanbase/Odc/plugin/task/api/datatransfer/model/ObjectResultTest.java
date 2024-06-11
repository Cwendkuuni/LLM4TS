import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.AtomicLong;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstruction() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        ObjectResult objectResult = new ObjectResult(schema, name, type);

        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.Status.CREATED, objectResult.getStatus());
        assertEquals(new AtomicLong(0), objectResult.getCount());
        assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("testType");
        objectStatus.setStatus(ObjectStatus.Status.CREATED);
        objectStatus.setName("testName");
        objectStatus.setSchema("testSchema");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        AtomicLong count = new AtomicLong(10);
        AtomicLong total = new AtomicLong(20);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(count, objectResult.getCount());
        assertEquals(total, objectResult.getTotal());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");
        objectResult.setName("testName");
        objectResult.setType("testType");

        String expectedSummary = "testSchema.testName[testType]";
        String actualSummary = objectResult.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testJsonSerializationAndDeserialization() throws Exception {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType("testType");
        objectResult.setStatus(ObjectStatus.Status.CREATED);
        objectResult.setName("testName");
        objectResult.setSchema("testSchema");
        objectResult.setCount(new AtomicLong(10));
        objectResult.setTotal(new AtomicLong(20));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(objectResult);
        ObjectResult deserializedObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);

        assertEquals(objectResult, deserializedObjectResult);
    }

    @Test
    public void testExportPathsAreIgnoredWhenSerializingAndDeserializing() throws Exception {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType("testType");
        objectResult.setStatus(ObjectStatus.Status.CREATED);
        objectResult.setName("testName");
        objectResult.setSchema("testSchema");
        objectResult.setCount(new AtomicLong(10));
        objectResult.setTotal(new AtomicLong(20));
        List<URL> exportPaths = Arrays.asList(new URL("http://test.com/exportPath1"), new URL("http://test.com/exportPath2"));
        objectResult.setExportPaths(exportPaths);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(objectResult);

        ObjectResult deserializedObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);
        assertEquals(deserializedObjectResult, ObjectResult.of(ObjectStatus.CREATED, "testName", "testType", "testSchema", new AtomicLong(10), new AtomicLong(20)));
        assertNull(deserializedObjectResult.getExportPaths());
    }

    @Test
    public void testGetTypeIsImmutable() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType("testType");

        try {
            objectResult.setType("newType");
            fail("Should throw an exception");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSchemaIsImmutable() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");

        try {
            objectResult.setSchema("newSchema");
            fail("Should throw an exception");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetStatusIsImmutable() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setStatus(ObjectStatus.Status.CREATED);

        try {
            objectResult.setStatus(ObjectStatus.Status.PROCESSING);
            fail("Should throw an exception");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCountIsImmutable() {
        ObjectResult objectResult = new ObjectResult();
        AtomicLong count = new AtomicLong(10);
        objectResult.setCount(count);

        try {
            objectResult.setCount(new AtomicLong(20));
            fail("Should throw an exception");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }

        assertThat(objectResult.getCount().get(), is(count.get()));
    }

    @Test
    public void testGetTotalIsImmutable() {
        ObjectResult objectResult = new ObjectResult();
        AtomicLong total = new AtomicLong(10);
        objectResult.setTotal(total);

        try {
            objectResult.setTotal(new AtomicLong(20));
            fail("Should throw an exception");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }

        assertThat(objectResult.getTotal().get(), is(total.get()));
    }
}