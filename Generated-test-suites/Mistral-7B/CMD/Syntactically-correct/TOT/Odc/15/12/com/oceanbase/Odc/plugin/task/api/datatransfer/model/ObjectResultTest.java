package com.oceanbase.odc.plugin.task.api.datatransfer.model.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.AtomicLong;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

public class ObjectResultTest {

 // Test constructor with valid arguments
 @Test
 public void testObjectResultConstructor() {
 String schema = "testSchema";
 String name = "testName";
 String type = "testType";
 ObjectResult objectResult = new ObjectResult(schema, name, type);
 assertEquals(schema, objectResult.getSchema());
 assertEquals(name, objectResult.getName());
 assertEquals(type, objectResult.getType());
 assertEquals(ObjectStatus.STATUS_NORMAL, objectResult.getStatus());
 assertEquals(0, objectResult.getCount().get());
 assertEquals(0, objectResult.getTotal().get());
 }

 // Test of method: getSummary
 @Test
 public void testGetSummary() {
 String schema = "testSchema";
 String name = "testName";
 String type = "testType";
 ObjectResult objectResult = new ObjectResult(schema, name, type);
 assertEquals("testSchema.testName[testType]", objectResult.getSummary());
 }

 // Test constructor with ObjectStatus argument
 @Test
 public void testObjectResultOf() {
 ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(10), new AtomicLong(100), null);
 ObjectResult objectResult = ObjectResult.of(objectStatus);
 assertEquals(objectStatus.getName(), objectResult.getName());
 assertEquals(objectStatus.getType(), objectResult.getType());
 assertEquals(objectStatus.getStatus(), objectResult.getStatus());
 assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
 assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
 }

 // Test method: setType
 @Test
 public void testSetType() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setType("testType");
 assertEquals("testType", objectResult.getType());
 }

 // Test method: setStatus
 @Test
 public void testSetStatus() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setStatus(ObjectStatus.STATUS_ERROR);
 assertEquals(ObjectStatus.STATUS_ERROR, objectResult.getStatus());
 }

 // Test method: setName
 @Test
 public void testSetName() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setName("testName");
 assertEquals("testName", objectResult.getName());
 }

 // Test method: setSchema
 @Test
 public void testSetSchema() {
 ObjectResult objectResult = new ObjectResult();
 objectResult.setSchema("testSchema");
 assertEquals("testSchema", objectResult.getSchema());
 }

 // Test method: setCount
 @Test
 public void testSetCount() {
 ObjectResult objectResult = new ObjectResult();
 AtomicLong count = new AtomicLong(10);
 objectResult.setCount(count);
 assertEquals(count, objectResult.getCount());
 }

 // Test method: setTotal
 @Test
 public void testSetTotal() {
 ObjectResult objectResult = new ObjectResult();
 AtomicLong total = new AtomicLong(100);
 objectResult.setTotal(total);
 assertEquals(total, objectResult.getTotal());
 }

 // Test equals and hashCode methods
 @Test
 public void testEqualsAndHashCode() {
 ObjectResult objectResult1 = new ObjectResult();
 objectResult1.setSchema("testSchema1");
 objectResult1.setName("testName1");
 objectResult1.setType("testType1");
 objectResult1.setStatus(ObjectStatus.STATUS_NORMAL);
 objectResult1.setCount(new AtomicLong(1));
 objectResult1.setTotal(new AtomicLong(1));

 ObjectResult objectResult2 = new ObjectResult();
 objectResult2.setSchema("testSchema2");
 objectResult2.setName("testName2");
 objectResult2.setType("testType2");
 objectResult2.setStatus(ObjectStatus.STATUS_NORMAL);
 objectResult2.setCount(new AtomicLong(2));
 objectResult2.setTotal(new AtomicLong(2));

 ObjectResult objectResult3 = new ObjectResult();
 objectResult3.setSchema("testSchema1");
 objectResult3.setName("testName1");
 objectResult3.setType("testType1");
 objectResult3.setStatus(ObjectStatus.STATUS_NORMAL);
 objectResult3.setCount(new AtomicLong(1));
 objectResult3.setTotal(new AtomicLong(1));

 assertEquals(objectResult1, objectResult3);
 assertNotEquals(objectResult1, objectResult2);
 }

 // Test JSON serialization and deserialization
 @Test
 public void testSerialization() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"schema\":\"testSchema\",\"name\":\"testName\",\"type\":\"testType\",\"status\":\"NORMAL\"," +
         "\"count\":{\"value\":1},\"total\":{\"value\":10}}";
 ObjectResult objectResult = mapper.readValue(json, ObjectResult.class);
 assertEquals("testSchema", objectResult.getSchema());
 assertEquals("testName", objectResult.getName());
 assertEquals("testType", objectResult.getType());
 assertEquals(ObjectStatus.STATUS_NORMAL, objectResult.getStatus());
 assertEquals(1, objectResult.getCount().get());
 assertEquals(10, objectResult.getTotal().get());
 }
}