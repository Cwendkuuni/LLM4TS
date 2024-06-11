package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class CSVRecordTest {

 @Test
 public void testCSVRecordCreation() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);

 assertEquals(values.length, record.size());
 assertArrayEquals(values, record.values());
 assertSame(mapping, record.mapping);
 assertNull(record.getComment());
 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordGetValue() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);

 assertEquals("Alice", record.get(0));
 assertEquals("25", record.get(1));
 assertEquals("Smith", record.get(2));
 assertEquals("New York", record.get(3));
 assertEquals("Alice", record.get("Name"));
 assertEquals("25", record.get("Age"));
 assertEquals("Smith", record.get("LastName"));
 assertEquals("New York", record.get("City"));
 }

 @Test
 public void testCSVRecordGetValueNull() {
 String[] values = new String[0];
 Map<String, Integer> mapping = null;
 CSVRecord record = new CSVRecord(values, mapping, null, 0);

 assertNull(record.get(0));
 assertNull(record.get("Name"));
 }

 @Test
 public void testCSVRecordGetComment() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, "Comment", 0);

 assertEquals("Comment", record.getComment());
 }

 @Test
 public void testCSVRecordGetRecordNumber() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 1);

 assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testCSVRecordIsConsistent() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);

 assertTrue(record.isConsistent());

 String[] values2 = new String[]{"Alice", "25", "Smith"};
 CSVRecord record2 = new CSVRecord(values2, mapping, null, 0);

 assertFalse(record2.isConsistent());
 }

 @Test
 public void testCSVRecordIsMapped() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);

 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("LastName"));
 assertTrue(record.isMapped("City"));
 }

 @Test
 public void testCSVRecordIsSet() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);

 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("LastName"));
 assertTrue(record.isSet("City"));
 }

 @Test
 public void testCSVRecordIterator() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);
 List<String> list = record.toList();

 Iterator<String> iterator = record.iterator();

 assertEquals(values[0], iterator.next());
 assertEquals(values[1], iterator.next());
 assertEquals(values[2], iterator.next());
 assertEquals(values[3], iterator.next());
 assertEquals(values[0], list.get(0));
 assertEquals(values[1], list.get(1));
 assertEquals(values[2], list.get(2));
 assertEquals(values[3], list.get(3));
 }

 @Test
 public void testCSVRecordPutIn() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, String> map = new HashMap<>();
 CSVRecord record = new CSVRecord(values, null, null, 0);

 map.putAll(record.putIn(map));

 assertEquals(values[0], map.get("Name"));
 assertEquals(values[1], map.get("Age"));
 assertEquals(values[2], map.get("LastName"));
 assertEquals(values[3], map.get("City"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetValueByNonExistentColumn() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);

 assertThrows(IllegalArgumentException.class, () -> record.get("UnknownColumn"));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCSVRecordGetValueByNegativeIndex() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);

 assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
 }

 @Test
 public void testCSVRecordToMapEmpty() {
 Map<String, String> map = new HashMap<>();
 CSVRecord record = new CSVRecord(new String[0], null, null, 0);

 map.putAll(record.toMap());

 assertTrue(map.isEmpty());
 }

 @Test
 public void testCSVRecordToMapMapped() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);
 Map<String, String> map = record.toMap();

 assertEquals(values[0], map.get("Name"));
 assertEquals(values[1], map.get("Age"));
 assertEquals(values[2], map.get("LastName"));
 assertEquals(values[3], map.get("City"));
 }

 @Test
 public void testCSVRecordToString() {
 String[] values = new String[]{"Alice", "25", "Smith", "New York"};
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("LastName", 2);
 mapping.put("City", 3);
 CSVRecord record = new CSVRecord(values, mapping, null, 0);

 assertEquals("[Alice, 25, Smith, New York]", record.toString());
 }
}