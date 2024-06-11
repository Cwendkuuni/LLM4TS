
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ObjectResultTest {

    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = mock(ObjectStatus.class);
    }

    // Test constructor
    @Test
    public void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertNotNull(objectResult);
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals("schema", objectResult.getSchema());
    }

    // Test getSummary
    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    // Test static method of
    @Test
    public void testObjectResultOf() {
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    // Edge case 1: Empty constructor call
    @Test
    public void testEmptyObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult(); // Default constructor
        assertNotNull(objectResult);
        assertEquals(null, objectResult.getSchema());
        assertEquals(null, objectResult.getName());
        assertEquals(null, objectResult.getType());
        assertEquals(null, objectResult.getStatus());
        assertEquals(null, objectResult.getCount());
        assertEquals(null, objectResult.getTotal());
    }

    // Edge case 2: Null schema, name, or type in constructor
    @Test
    public void testObjectResultConstructionWithNullValues() {
        ObjectResult objectResult1 = new ObjectResult(null, "name", "type");
        ObjectResult objectResult2 = new ObjectResult("schema", null, "type");
        ObjectResult objectResult3 = new ObjectResult("schema", "name", null);

        assertEquals(null, objectResult1.getSchema());
        assertEquals("name", objectResult1.getName());
        assertEquals("type", objectResult1.getType());

        assertEquals("schema", objectResult2.getSchema());
        assertEquals(null, objectResult2.getName());
        assertEquals("type", objectResult2.getType());

        assertEquals("schema", objectResult3.getSchema());
        assertEquals("name", objectResult3.getName());
        assertEquals(null, objectResult3.getType());
    }

    // Exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructionWithNullValuesThrowsException() {
        ObjectResult objectResult = new ObjectResult(null, null, null); // All null values
    }
}