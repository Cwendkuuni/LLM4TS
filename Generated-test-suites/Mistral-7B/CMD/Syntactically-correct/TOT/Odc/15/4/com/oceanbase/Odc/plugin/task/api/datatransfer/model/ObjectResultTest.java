import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

@RunWith(JUnitPlatform.class)
public class ObjectResultTest {

  @Test
  public void testObjectResultConstruction() {
    ObjectResult objectResult = new ObjectResult();
    ObjectResult objectResultWithSchemaNameType = new ObjectResult("schema", "name", "type");

    // Test default constructor
    Assertions.assertNull(objectResult.getExportPaths());
    Assertions.assertEquals("", objectResult.getSchema());
    Assertions.assertEquals("", objectResult.getName());
    Assertions.assertEquals("", objectResult.getType());
    Assertions.assertEquals(Status.CREATED, objectResult.getStatus());
    Assertions.assertEquals(0, objectResult.getCount().get());
    Assertions.assertEquals(0, objectResult.getTotal().get());

    // Test constructor with schema, name, and type
    Assertions.assertEquals("schema", objectResultWithSchemaNameType.getSchema());
    Assertions.assertEquals("name", objectResultWithSchemaNameType.getName());
    Assertions.assertEquals("type", objectResultWithSchemaNameType.getType());
  }

  @Test
  public void testObjectResultStatus() {
    ObjectStatus status = new ObjectStatus("name", new AtomicLong(1), new AtomicLong(1), null);
    ObjectResult objectResult = ObjectResult.of(status);

    Assertions.assertEquals(Status.CREATED, objectResult.getStatus());

    status.setStatus(Status.SUCCESS);
    ObjectResult objectResultWithUpdatedStatus = ObjectResult.of(status);

    Assertions.assertEquals(Status.SUCCESS, objectResultWithUpdatedStatus.getStatus());
  }

  @Test
  public void testObjectResultGetSummary() {
    ObjectResult objectResult = new ObjectResult();
    String expectedSummary = "";

    objectResult.setSchema("schema");
    objectResult.setName("name");
    objectResult.setType("type");

    String actualSummary = objectResult.getSummary();

    Assertions.assertEquals(expectedSummary + ".schema.name[type]", actualSummary);
  }

  @Test
  public void testObjectResultSetAndGetType() {
    ObjectResult objectResult = new ObjectResult();
    String type = "type";

    objectResult.setType(type);

    String actualType = objectResult.getType();

    Assertions.assertEquals(type, actualType);
  }

  @Test
  public void testObjectResultSetAndGetSchema() {
    ObjectResult objectResult = new ObjectResult();
    String schema = "schema";

    objectResult.setSchema(schema);

    String actualSchema = objectResult.getSchema();

    Assertions.assertEquals(schema, actualSchema);
  }

  @Test
  public void testObjectResultSetAndGetName() {
    ObjectResult objectResult = new ObjectResult();
    String name = "name";

    objectResult.setName(name);

    String actualName = objectResult.getName();

    Assertions.assertEquals(name, actualName);
  }

  @Test
  public void testObjectResultGetCountAndTotal() {
    ObjectResult objectResult = new ObjectResult();
    AtomicLong count = new AtomicLong(1);
    AtomicLong total = new AtomicLong(2);

    objectResult.setCount(count);
    objectResult.setTotal(total);

    long actualCount = objectResult.getCount().get();
    long actualTotal = objectResult.getTotal().get();

    Assertions.assertEquals(count.get(), actualCount);
    Assertions.assertEquals(total.get(), actualTotal);
  }

  @Test
  public void testObjectResultSetAndGetExportPaths() {
    ObjectResult objectResult = new ObjectResult();
    List<URL> exportPaths = List.of(new URL("http://localhost:8080/export/path1"), new URL("http://localhost:8080/export/path2"));

    objectResult.setExportPaths(exportPaths);

    List<URL> actualExportPaths = objectResult.getExportPaths();

    Assertions.assertEquals(exportPaths, actualExportPaths);
  }

  @Test
  public void testObjectResultToString() {
    ObjectResult objectResult = new ObjectResult();
    String expectedToString = "ObjectResult(schema=, name=, type=, status=CREATED, count=0, total=0)";

    String actualToString = objectResult.toString();

    Assertions.assertEquals(expectedToString, actualToString);
  }

  @Test
  public void testObjectResultJsonSerializationAndDeserialization() throws Exception {
    ObjectResult objectResult = new ObjectResult();
    ObjectResult expectedObjectResult = new ObjectResult();
    expectedObjectResult.setSchema("schema");
    expectedObjectResult.setName("name");
    expectedObjectResult.setType("type");
    expectedObjectResult.setStatus(Status.CREATED);
    expectedObjectResult.setCount(new AtomicLong(1));
    expectedObjectResult.setTotal(new AtomicLong(2));

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(expectedObjectResult);
    ObjectResult actualObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);

    Assertions.assertEquals(expectedObjectResult, actualObjectResult);
  }

  @Test
  public void testObjectResultJsonIgnoreGetSummary() {
    ObjectResult objectResult = new ObjectResult();
    String expectedSummary = "";

    objectResult.setSchema("schema");
    objectResult.setName("name");
    objectResult.setType("type");

    String actualSummary = objectResult.getSummary();

    Assertions.assertEquals(expectedSummary, actualSummary);

    // Verify getSummary is ignored during JSON serialization and deserialization
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(objectResult);
    ObjectResult deserializedObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);
    String deserializedSummary = deserializedObjectResult.getSummary();

    Assertions.assertEquals(expectedSummary, deserializedSummary);
  }

  @Test
  public void testObjectResultOf() {
    ObjectStatus objectStatus = new ObjectStatus();
    ObjectResult expectedObjectResult = new ObjectResult();
    expectedObjectResult.setSchema("schema");
    expectedObjectResult.setName("name");
    expectedObjectResult.setType("type");
    expectedObjectResult.setStatus(Status.CREATED);
    expectedObjectResult.setCount(new AtomicLong(1));
    expectedObjectResult.setTotal(new AtomicLong(2));

    ObjectResult actualObjectResult = ObjectResult.of(objectStatus);

    Assertions.assertEquals(expectedObjectResult, actualObjectResult);
  }

  @Test
  public void testObjectResultJsonSerializationAndDeserializationWithExportPaths() throws Exception {
    ObjectResult objectResult = new ObjectResult();
    ObjectResult expectedObjectResult = new ObjectResult();
    expectedObjectResult.setSchema("schema");
    expectedObjectResult.setName("name");
    expectedObjectResult.setType("type");
    expectedObjectResult.setStatus(Status.CREATED);
    expectedObjectResult.setCount(new AtomicLong(1));
    expectedObjectResult.setTotal(new AtomicLong(2));
    List<URL> exportPaths = List.of(new URL("http://localhost:8080/export/path1"), new URL("http://localhost:8080/export/path2"));
    expectedObjectResult.setExportPaths(exportPaths);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(expectedObjectResult);
    ObjectResult actualObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);

    Assertions.assertEquals(expectedObjectResult, actualObjectResult);
  }

  @Test
  public void testObjectResultJsonIgnoreGetSummaryWithExportPaths() throws Exception {
    ObjectResult objectResult = new ObjectResult();
    ObjectResult expectedObjectResult = new ObjectResult();
    expectedObjectResult.setSchema("schema");
    expectedObjectResult.setName("name");
    expectedObjectResult.setType("type");
    expectedObjectResult.setStatus(Status.CREATED);
    expectedObjectResult.setCount(new AtomicLong(1));
    expectedObjectResult.setTotal(new AtomicLong(2));
    List<URL> exportPaths = List.of(new URL("http://localhost:8080/export/path1"), new URL("http://localhost:8080/export/path2"));
    expectedObjectResult.setExportPaths(exportPaths);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(expectedObjectResult);
    ObjectResult deserializedObjectResult = objectMapper.readValue(jsonString, ObjectResult.class);

    Assertions.assertEquals(expectedObjectResult, deserializedObjectResult);

    String deserializedSummary = deserializedObjectResult.getSummary();
    String expectedSummary = expectedObjectResult.getSummary();

    Assertions.assertEquals(expectedSummary, deserializedSummary);
  }

  @Test
  public void testObjectResultGettersAndSetters() {
    ObjectResult objectResult = new ObjectResult();

    objectResult.setSchema("schema");
    String actualSchema = objectResult.getSchema();

    Assertions.assertEquals("schema", actualSchema);

    objectResult.setName("name");
    String actualName = objectResult.getName();

    Assertions.assertEquals("name", actualName);

    objectResult.setType("type");
    String actualType = objectResult.getType();

    Assertions.assertEquals("type", actualType);

    objectResult.setStatus(Status.CREATED);
    Status actualStatus = objectResult.getStatus();

    Assertions.assertEquals(Status.CREATED, actualStatus);

    objectResult.setCount(new AtomicLong(1));
    long actualCount = objectResult.getCount().get();

    Assertions.assertEquals(1, actualCount);

    objectResult.setTotal(new AtomicLong(2));
    long actualTotal = objectResult.getTotal().get();

    Assertions.assertEquals(2, actualTotal);

    List<URL> exportPaths = List.of(new URL("http://localhost:8080/export/path1"), new URL("http://localhost:8080/export/path2"));
    objectResult.setExportPaths(exportPaths);
    List<URL> actualExportPaths = objectResult.getExportPaths();

    Assertions.assertEquals(exportPaths, actualExportPaths);
  }

  @Test
  public void testObjectResultEqualsAndHashCode() {
    ObjectResult objectResult1 = new ObjectResult();
    ObjectResult objectResult2 = new ObjectResult();

    ObjectResult objectResult3 = new ObjectResult();
    objectResult3.setSchema("schema");

    ObjectResult objectResult4 = new ObjectResult();
    objectResult4.setSchema("schema").setName("name");

    ObjectResult objectResult5 = new ObjectResult();
    objectResult5.setSchema("schema").setName("name").setType("type");

    ObjectResult objectResult6 = new ObjectResult();
    objectResult6.setSchema("schema").setName("name").setType("type").setStatus(Status.CREATED);

    ObjectResult objectResult7 = new ObjectResult();
    objectResult7.setSchema("schema")
        .setName("name")
        .setType("type")
        .setStatus(Status.CREATED)
        .setCount(new AtomicLong(1))
        .setTotal(new AtomicLong(2));

    ObjectResult objectResult8 = new ObjectResult();
    objectResult8.setSchema("schema")
        .setName("name")
        .setType("type")
        .setStatus(Status.CREATED)
        .setCount(new AtomicLong(1))
        .setTotal(new AtomicLong(2))
        .setExportPaths(List.of(new URL("http://localhost:8080/export/path1"), new URL("http://localhost:8080/export/path2")));

    ObjectResult objectResult9 = new ObjectResult();
    objectResult9.setSchema("schema")
        .setName("name")
        .setType("type")
        .setStatus(Status.CREATED)
        .setCount(new AtomicLong(1))
        .setTotal(new AtomicLong(2))
        .setExportPaths(List.of(new URL("http://localhost:8080/export/path2"), new URL("http://localhost:8080/export/path1")));

    // Test equals and hashcode for equal objects
    Assertions.assertTrue(objectResult1.equals(objectResult1));
    Assertions.assertEquals(System.identityHashCode(objectResult1), objectResult1.hashCode());

    Assertions.assertTrue(objectResult1.equals(objectResult2));
    Assertions.assertEquals(System.identityHashCode(objectResult1), objectResult2.hashCode());

    Assertions.assertTrue(objectResult1.equals(objectResult3));
    Assertions.assertEquals(System.identityHashCode(objectResult1), objectResult3.hashCode());

    Assertions.assertFalse(objectResult1.equals(objectResult4));

    Assertions.assertFalse(objectResult1.equals(objectResult5));

    Assertions.assertFalse(objectResult1.equals(objectResult6));

    Assertions.assertFalse(objectResult1.equals(objectResult7));

    Assertions.assertFalse(objectResult1.equals(objectResult8));

    Assertions.assertFalse(objectResult1.equals(objectResult9));

    // Test equals and hashcode for non-equal objects
    Assertions.assertFalse(objectResult1.equals(null));
    Assertions.assertFalse(objectResult1.equals(""));
    Assertions.assertFalse(objectResult1.equals("name"));

    ObjectResult objectResult10 = new ObjectResult();
    objectResult10.setSchema("schema2");

    Assertions.assertFalse(objectResult1.equals(objectResult10));

    ObjectStatus objectStatus = new ObjectStatus();
    ObjectResult objectResult11 = ObjectResult.of(objectStatus);

    Assertions.assertFalse(objectResult1.equals(objectResult11));
  }

  @Test
  public void testObjectResultToStringWithExportPaths() {
    ObjectResult objectResult = new ObjectResult();
    List<URL> exportPaths = List.of(new URL("http://localhost:8080/export/path1"), new URL("http://localhost:8080/export/path2"));
    objectResult.setExportPaths(exportPaths);

    String expectedToString =
        "ObjectResult(schema=, name=, type=, status=CREATED, count=0, total=0, exportPaths=[http://localhost:8080/export/path1, http://localhost:8080/export/path2])";

    String actualToString = objectResult.toString();

    Assertions.assertEquals(expectedToString, actualToString);
  }
}