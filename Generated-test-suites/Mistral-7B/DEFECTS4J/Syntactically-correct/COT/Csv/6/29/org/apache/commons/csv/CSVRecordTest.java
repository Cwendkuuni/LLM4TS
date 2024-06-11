package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * JUnit test cases for CSVRecord class
 */
public class CSVRecordTest {

 private String[] values = new String[]{"Name", "Age", "City"};
 private Map<String, Integer> mapping = new HashMap<String, Integer>() {{
 put("Name", 0);
 put("Age", 1);
 put("City", 2);
 }};

 private CSVRecord record = new CSVRecord(values, mapping, null, 1);

 @Test
 public void testGetEnum() {
 Enum<String> testEnum = Enum.valueOf("Name", "Test");
 String result = record.get(testEnum);
 assertEquals("Test", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumInvalid() {
 Enum<String> testEnum = Enum.valueOf("Invalid", "Test");
 record.get(testEnum);
 }

 @Test
 public void testGetIndex() {
 String result = record.get(0);
 assertEquals("Name", result);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetIndexInvalid() {
 record.get(3);
 }

 @Test
 public void testGetName() {
 String result = record.get("Name");
 assertEquals("Name", result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetNameNoMapping() {
 record = new CSVRecord(values, null, null, 1);
 record.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNameInvalidMapping() {
 record.get("Invalid");
 }

 @Test
 public void testGetNameInconsistent() {
 record = new CSVRecord(new String[0], mapping, null, 1);
 record.get("Name");
 }

 @Test
 public void testGetComment() {
 String result = record.getComment();
 assertNull(result);
 }

 @Test
 public void testGetRecordNumber() {
 long result = record.getRecordNumber();
 assertEquals(1, result);
 }

 @Test
 public void testIsConsistent() {
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 assertTrue(record.isMapped("Name"));
 }

 @Test
 public void testIsSet() {
 assertTrue(record.isSet("Name"));
 }

 @Test
 public void testIterator() {
 List<String> expected = Arrays.asList(values);
 List<String> result = Arrays.asList(record.iterator().toArray());
 assertEquals(expected, result);
 }

 @Test
 public void testPutIn() {
 Map<String, String> expected = new HashMap<String, String>() {{
 put("Name", "Name");
 put("Age", "25");
 put("City", "New York");
 }};
 Map<String, String> result = record.putIn(new HashMap<String, String>());
 assertEquals(expected, result);
 }

 @Test
 public void testSize() {
 assertEquals(3, record.size());
 }

 @Test
 public void testToList() {
 List<String> expected = Arrays.asList(values);
 List<String> result = record.toList();
 assertEquals(expected, result);
 }

 @Test
 public void testToMap() {
 Map<String, String> expected = new HashMap<String, String>() {{
 put("Name", "Name");
 put("Age", "25");
 put("City", "New York");
 }};
 Map<String, String> result = record.toMap();
 assertEquals(expected, result);
 }
}