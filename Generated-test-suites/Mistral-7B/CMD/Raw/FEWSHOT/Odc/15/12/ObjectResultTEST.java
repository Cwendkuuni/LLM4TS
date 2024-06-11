package com.oceanbase.odc.plugin.task.api.datatransfer.model;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import java.util.ArrayList;
import java.util.List;

public class ObjectResultTest {

 @Test
 public void testObjectResultConstructor() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "TYPE_INT");
 assertEquals("schema1", objectResult.getSchema());
 assertEquals("table1", objectResult.getName());
 assertEquals("TYPE_INT", objectResult.getType());
 assertEquals(0, objectResult.getCount().get());
 assertEquals(0, objectResult.getTotal().get());
 assertEquals(ObjectStatus.Status.NONE, objectResult.getStatus());
 }

 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("table1", new AtomicLong(100), new AtomicLong(100), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals("table1", objectResult.getName());
 assertEquals(ObjectStatus.Status.NONE, objectResult.getStatus());
 assertEquals("TYPE_UNKNOWN", objectResult.getType()); // default value
 assertEquals(100, objectResult.getCount().get());
 assertEquals(100, objectResult.getTotal().get());
 }

 @Test
 public void testGetSummary() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "TYPE_INT");
 assertEquals("schema1.table1[TYPE_INT]", objectResult.getSummary());
 }

 @Test
 public void testSetExportPaths() {
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "TYPE_INT");
 List<URL> exportPaths = new ArrayList<>();
 exportPaths.add(new URL("http://localhost:8080/export/path1"));
 exportPaths.add(new URL("http://localhost:8080/export/path2"));
 objectResult.setExportPaths(exportPaths);
 assertEquals(exportPaths, objectResult.getExportPaths());
 }

 @Test
 public void testJsonSerialization() throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectResult objectResult = new ObjectResult("schema1", "table1", "TYPE_INT");
 String json = objectMapper.writeValueAsString(objectResult);
 ObjectResult deserializedObjectResult = objectMapper.readValue(json, ObjectResult.class);
 assertEquals("schema1", deserializedObjectResult.getSchema());
 assertEquals("table1", deserializedObjectResult.getName());
 assertEquals("TYPE_INT", deserializedObjectResult.getType());
 assertEquals(0, deserializedObjectResult.getCount().get());
 assertEquals(0, deserializedObjectResult.getTotal().get());
 assertEquals(ObjectStatus.Status.NONE, deserializedObjectResult.getStatus());
 assertEquals(null, deserializedObjectResult.getExportPaths());
 }
}