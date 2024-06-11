package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertIterableEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class CSVRecordTest {

 @Test
 public void testSize() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 assertEquals(3, record.size());
 }

 @Test
 public void testGetByEnum() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 Enum<String> e = Enum.valueOf("FIRST", "FIRST");
 assertEquals("foo", record.get(e));
 }

 @Test
 public void testGetByIndex() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 assertEquals("foo", record.get(0));
 }

 @Test
 public void testGetByNameWithMapping() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>() {{
 put("FIRST", 0);
 put("SECOND", 1);
 put("THIRD", 2);
 }}.entrySet().stream().collect(Collectors.toMap(Entry::getKey, Map.Entry::getValue)),
 "some comment", 1);
 assertEquals("foo", record.get("FIRST"));
 }

 @Test
 public void testGetByNameWithoutMapping() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 assertThrows(IllegalArgumentException.class, () -> record.get("FOO"));
 }

 @Test
 public void testGetComment() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, "some comment", 1);
 assertEquals("some comment", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testIsConsistentTrue() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistentFalse() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar"}, null, null, 1);
 assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMappedTrue() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>() {{
 put("FIRST", 0);
 put("SECOND", 1);
 put("THIRD", 2);
 }}.entrySet().stream().collect(Collectors.toMap(Entry::getKey, Map.Entry::getValue)),
 null, null, 1);
 assertTrue(record.isMapped("FIRST"));
 }

 @Test
 public void testIsMappedFalse() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 assertFalse(record.isMapped("FOO"));
 }

 @Test
 public void testIsSetTrue() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, new HashMap<String, Integer>() {{
 put("FIRST", 0);
 put("SECOND", 1);
 put("THIRD", 2);
 }}.entrySet().stream().collect(Collectors.toMap(Entry::getKey, Map.Entry::getValue)),
 null, null, 1);
 assertTrue(record.isSet("FIRST"));
 }

 @Test
 public void testIsSetFalse() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 assertFalse(record.isSet("FOO"));
 }

 @Test
 public void testGetValues() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 assertArrayEquals(new String[]{"foo", "bar", "baz"}, record.values());
 }

 @Test
 public void testToString() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 assertEquals("[foo, bar, baz]", record.toString());
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 List<String> expected = new ArrayList<>(Arrays.asList("foo", "bar", "baz"));
 Iterator<String> iterator = record.iterator();
 List<String> actual = new ArrayList<>();
 while (iterator.hasNext()) {
 actual.add(iterator.next());
 }
 assertIterableEquals(expected, actual);
 }

 @Test
 public void testPutIn() {
 CSVRecord record = new CSVRecord(new String[]{"foo", "bar", "baz"}, null, null, 1);
 Map<String, String> map = new HashMap<>();
 map.putAll(record.putIn(map));
 List<String> expected = Arrays.asList("foo", "bar", "baz");
 List<String> actual = new ArrayList<>(map.values());
 assertIterableEquals(expected, actual);
 }
}