import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private CSVFormat format;
 private CSVParser parser;
 private List<CSVRecord> records;

 @Before
 public void setUp() throws IOException {
 format = CSVFormat.DEFAULT.withHeader("Name", "Age", "City");
 parser = new CSVParser(new StringReader("Name,Age,City\nAlice,30,New York\nBob,25,Los Angeles\n"));
 records = parser.getRecords();
 }

 @Test
 public void testGet() throws Exception {
 CSVRecord record = records.get(0);

 assertEquals("Alice", record.get(0));
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));

 assertEquals("Alice", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertEquals("New York", record.get("City"));

 record = records.get(1);
 assertEquals("Bob", record.get(0));
 assertEquals("25", record.get(1));
 assertEquals("Los Angeles", record.get(2));

 assertEquals("Bob", record.get("Name"));
 assertEquals("25", record.get("Age"));
 assertEquals("Los Angeles", record.get("City"));
 }

 @Test
 public void testGetNull() throws Exception {
 CSVRecord record = records.get(0);

 assertNull(record.get("Unknown"));

 record = records.get(1);
 assertNull(record.get("Unknown"));
 }

 @Test
 public void testIsMapped() throws Exception {
 CSVRecord record = records.get(0);

 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("City"));

 assertFalse(record.isMapped("Unknown"));
 }

 @Test
 public void testIsSet() throws Exception {
 CSVRecord record = records.get(0);

 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));

 assertFalse(record.isSet("Unknown"));
 }

 @Test
 public void testSize() throws Exception {
 CSVRecord record = records.get(0);

 assertEquals(3, record.size());
 }

 @Test
 public void testToString() throws Exception {
 CSVRecord record = records.get(0);

 assertEquals("[Alice, 30, New York]", record.toString());
 }

 @Test
 public void testToMap() throws Exception {
 CSVRecord record = records.get(0);

 Map<String, String> map = record.toMap();

 assertEquals(3, map.size());
 assertSame("Alice", map.get("Name"));
 assertSame("30", map.get("Age"));
 assertSame("New York", map.get("City"));
 }
}