package org.apache.commons.csv;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for CSVRecord
 */
public class CSVRecordTest {

/**
 * Test method for {@link CSVRecord#CSVRecord(String[], Map, String, long)} .
 */
@Test
public void testCSVRecord() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals("John", record.get("Name"));
assertEquals("Doe", record.get("Age"));
assertEquals("Comment", record.getComment());
assertEquals(1, record.getRecordNumber());
assertTrue(record.isConsistent());
}

/**
 * Test method for {@link CSVRecord#get(Enum)} .
 */
@Test
public void testGetEnum() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals("John", record.get(Enum.NAME));
}

/**
 * Test method for {@link CSVRecord#get(int)} .
 */
@Test
public void testGetInt() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals("John", record.get(0));
assertEquals("Doe", record.get(1));
assertEquals("30", record.get(2));
}

/**
 * Test method for {@link CSVRecord#get(String)} .
 */
@Test
public void testGetString() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals("John", record.get("Name"));
assertEquals("Doe", record.get("Age"));
assertEquals("30", record.get("Age")); // should also work
assertEquals("Comment", record.getComment());
}

/**
 * Test method for {@link CSVRecord#getComment()} .
 */
@Test
public void testGetComment() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals("Comment", record.getComment());
assertEquals(null, record.getComment()); // should also work
}

/**
 * Test method for {@link CSVRecord#getRecordNumber()} .
 */
@Test
public void testGetRecordNumber() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals(1, record.getRecordNumber());
}

/**
 * Test method for {@link CSVRecord#isConsistent()} .
 */
@Test
public void testIsConsistent() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
put("Birthday", 2); // extra column
}}, "Comment", 1);

assertFalse(record.isConsistent());

record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertTrue(record.isConsistent());
}

/**
 * Test method for {@link CSVRecord#isMapped(String)} .
 */
@Test
public void testIsMapped() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
put("Birthday", 2); // unmap existing column
}}, "Comment", 1);

assertFalse(record.isMapped("Name"));
assertFalse(record.isMapped("Birthday"));

record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertTrue(record.isMapped("Name"));
assertTrue(record.isMapped("Age"));
}

/**
 * Test method for {@link CSVRecord#isSet(String)} .
 */
@Test
public void testIsSet() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertTrue(record.isSet("Name"));
assertTrue(record.isSet("Age"));
assertTrue(record.isSet("Age")); // same as above
assertFalse(record.isSet("Birthday"));
}

/**
 * Test method for {@link CSVRecord#iterator()} .
 */
@Test
public void testIterator() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);
List<String> values = record.toList();

assertEquals("John", record.iterator().next());
assertEquals("Doe", record.iterator().next());
assertEquals("30", record.iterator().next());
}

/**
 * Test method for {@link CSVRecord#putIn(Map)} .
 */
@Test
public void testPutIn() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);
Map<String, String> map = new HashMap<String, String>();

record.putIn(map);

assertEquals("John", map.get("Name"));
assertEquals("Doe", map.get("Age"));
assertEquals("30", map.get("Age"));
}

/**
 * Test method for {@link CSVRecord#size()} .
 */
@Test
public void testSize() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals(3, record.size());
}

/**
 * Test method for {@link CSVRecord#toList()} .
 */
@Test
public void testToList() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals(Arrays.asList("John", "Doe", "30"), record.toList());
}

/**
 * Test method for {@link CSVRecord#toMap()} .
 */
@Test
public void testToMap() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals(new HashMap<String, String>(){{
put("Name", "John");
put("Age", "30");
}}, record.toMap());
}

/**
 * Test method for {@link CSVRecord#toString()} .
 */
@Test
public void testToString() {
CSVRecord record = new CSVRecord(new String[]{"John", "Doe", "30"}, new HashMap<String, Integer>(){{
put("Name", 0);
put("Age", 1);
}}, "Comment", 1);

assertEquals("[John, Doe, 30]", record.toString());
}

/**
 * Test method for empty record.
 */
@Test
public void testEmptyRecord() {
CSVRecord record = new CSVRecord(CSVRecord.EMPTY_STRING_ARRAY, new HashMap<String, Integer>(), null, 0);

assertFalse(record.isConsistent());
}

/**
 * Test method for null values.
 */
@Test(expected = IllegalArgumentException.class)
public void testNullValues() {
CSVFormat csvFormat = new CSVFormatBuilder().setNullString("").build();
List<CSVRecord> records = csvFormat.parse(Arrays.asList("Name, Age, null", "John, 30,", "John, ,30").iterator());
CSVRecord record = records.get(1);

assertEquals(2, record.size());
assertFalse(record.isConsistent());
}
}