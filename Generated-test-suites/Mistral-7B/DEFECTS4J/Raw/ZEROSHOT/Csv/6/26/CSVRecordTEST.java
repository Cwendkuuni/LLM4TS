import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private CSVFormat format;
 private CSVParser parser;
 private CSVRecord record;

 @Before
 public void setUp() throws IOException {
 format = CSVFormat.DEFAULT.withHeader("Name", "Age", "Comment");
 parser = format.newParser();
 String csv = "Name,Age,Comment\nAlice,30,Comment1\nBob,25,Comment2\nCharlie,35,Comment3";
 record = parser.parseNextRecord();
 }

 @Test
 public void testGetByEnum() {
 assertEquals("Alice", record.get(Enum.NAME));
 assertEquals("30", record.get(Enum.AGE));
 }

 @Test
 public void testGetByIndex() {
 assertEquals("Alice", record.get(0));
 assertEquals("30", record.get(1));
 }

 @Test
 public void testGetByName() {
 assertEquals("Alice", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertNull(record.get("Unknown"));
 }

 @Test
 public void testGetComment() {
 assertEquals("Comment1", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 assertEquals(1L, record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 assertTrue(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoHeaderMapping() {
 CSVRecord recordWithoutMapping = new CSVRecord(null, null, null, 0L);
 recordWithoutMapping.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownName() {
 CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"Alice", "30"}, null, null, 0L);
 recordWithoutMapping.get("Unknown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIndex() {
 CSVRecord recordWithoutMapping = new CSVRecord(new String[]{"Alice", "30"}, null, null, 0L);
 recordWithoutMapping.get(-1);
 }

 @Test
 public void testIsMapped() {
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertFalse(record.isMapped("Unknown"));
 }

 @Test
 public void testIsSet() {
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertFalse(record.isSet("Unknown"));
 }

 @Test
 public void testPutInMap() {
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 assertEquals("Alice", map.get("Name"));
 assertEquals("30", map.get("Age"));
 assertNull(map.get("Unknown"));
 }

 @Test
 public void testSize() {
 assertEquals(3, record.size());
 }

 @Test
 public void testToString() {
 List<String> values = record.toList();
 assertEquals("[Alice, 30, Comment1]", values.toString());
 }

 @Test
 public void testToMap() {
 Map<String, String> map = record.toMap();
 assertEquals("Alice", map.get("Name"));
 assertEquals("30", map.get("Age"));
 assertNull(map.get("Unknown"));
 }

}