import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

@DisplayName("ObjectResult class tests")
public class ObjectResultTest {

    @Test
    @DisplayName("Test constructor")
    void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
    }

    @Test
    @DisplayName("Test of method")
    void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(1), null);
        ObjectResult result = ObjectResult.of(objectStatus);
        ObjectResult expected = new ObjectResult("name1", null, null);
        expected.setType(objectStatus.getType());
        expected.setStatus(objectStatus.getStatus());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Test getSummary method")
    void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1.name1[type1]", objectResult.getSummary());
    }

    @Test
    @DisplayName("Test empty constructor")
    void testEmptyConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getCount());
        assertNull(objectResult.getTotal());
    }

    @Test
    @DisplayName("Test getName method with null name")
    void testGetNameWithNullName() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        assertNull(objectResult.getName());
    }

    @Test
    @DisplayName("Test setName method with null name")
    void testSetNameWithNullName() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setName(null);
        assertNull(objectResult.getName());
    }

    @Test
    @DisplayName("Test getType method with null type")
    void testGetTypeWithNullType() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", null);
        assertNull(objectResult.getType());
    }

    @Test
    @DisplayName("Test setType method with null type")
    void testSetTypeWithNullType() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setType(null);
        assertNull(objectResult.getType());
    }

    @Test
    @DisplayName("Test getSchema method with null schema")
    void testGetSchemaWithNullSchema() {
        ObjectResult objectResult = new ObjectResult(null, "name1", "type1");
        assertNull(objectResult.getSchema());
    }

    @Test
    @DisplayName("Test setSchema method with null schema")
    void testSetSchemaWithNullSchema() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setSchema(null);
        assertNull(objectResult.getSchema());
    }

    @Test
    @DisplayName("Test getCount method with null count")
    void testGetCountWithNullCount() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setCount(null);
        assertNull(objectResult.getCount());
    }

    @Test
    @DisplayName("Test getTotal method with null total")
    void testGetTotalWithNullTotal() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setTotal(null);
        assertNull(objectResult.getTotal());
    }

    @Test
    @DisplayName("Test ObjectResult with null ObjectStatus")
    void testObjectResultWithNullObjectStatus() {
        ObjectStatus objectStatus = null;
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNull(objectResult);
    }

}