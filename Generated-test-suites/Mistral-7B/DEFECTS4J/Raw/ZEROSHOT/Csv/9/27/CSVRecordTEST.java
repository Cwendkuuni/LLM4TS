import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private CSVFormat format;
 private CSVParser parser;
 private List<CSVRecord> records;

 @Before
 public void setUp() throws IOException {
 format = CSVFormat.DEFAULT.withHeader("name", "age");
 parser = new CSVParser(new ByteArrayInputStream(format.getHeaderLine().getBytes()), format);
 records = parser.getRecords();
 }

 @Test
 public void testGetValues() {
 CSVRecord record = records.get(0);
 String[] values = record.values();
 assertEquals(2, values.length);
 assertEquals("Alice", values[0]);
 assertEquals("30", values[1]);
 }

 @Test
 public void testGetValueByEnum() {
 CSVRecord record = records.get(0);
 assertEquals("Alice", record.get(CSVRecord.Name.NAME));
 assertEquals("30", record.get(CSVRecord.Name.AGE));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueByEnumUnknown() {
 CSVRecord record = records.get(0);
 record.get(new Enum<?>(){});
 }

 @Test
 public void testGetValueByIndex() {
 CSVRecord record = records.get(0);
 assertEquals("Alice", record.get(0));
 assertEquals("30", record.get(1));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetValueByIndexOutOfBounds() {
 CSVRecord record = records.get(0);
 record.get(2);
 }

 @Test
 public void testGetValueByName() {
 CSVRecord record = records.get(0);
 assertEquals("Alice", record.get("name"));
 assertEquals("30", record.get("age"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetValueByNameNoMapping() {
 CSVRecord record = records.get(1); // no header mapping
 record.get("name");
 }

 @Test
 public void testGetValueByNameMapped() {
 CSVRecord record = records.get(0);
 assertEquals("Alice", record.get("name"));
 assertEquals("30", record.get("age"));
 }

 @Test
 public void testGetComment() {
 CSVRecord record = records.get(0);
 assertNull(record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVRecord record = records.get(0);
 assertEquals(0, record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = records.get(0);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = records.get(0);
 assertTrue(record.isMapped("name"));
 assertTrue(record.isMapped("age"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = records.get(0);
 assertTrue(record.isSet("name"));
 assertTrue(record.isSet("age"));
 }

 @Test
 public void testSize() {
 CSVRecord record = records.get(0);
 assertEquals(2, record.size());
 }

 @Test
 public void testToString() {
 CSVRecord record = records.get(0);
 String expected = "[name=Alice, age=30]";
 assertEquals(expected, record.toString());
 }

 @Test
 public void testPutInMap() {
 Map<String, String> map = new HashMap<>();
 CSVRecord record = records.get(0);
 record.putIn(map);
 assertEquals(2, map.size());
 assertEquals("Alice", map.get("name"));
 assertEquals("30", map.get("age"));
 }

 @Test
 public void testMapValues() {
 Map<String, String> map = records.get(0).toMap();
 assertEquals(2, map.size());
 assertEquals("Alice", map.get("name"));
 assertEquals("30", map.get("age"));
 }

}