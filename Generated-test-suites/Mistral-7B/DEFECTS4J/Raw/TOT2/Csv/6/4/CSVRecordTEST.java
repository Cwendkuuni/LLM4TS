import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private static final String CSV_RECORD = "name1,name2,name3\nJohn Doe,Alice Smith,Bob Johnson\n";

 @Test
 public void testCSVRecord_constructor() throws IOException {
 CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new StringReader(CSV_RECORD));
 CSVRecord record = parser.nextRecord();
 CSVRecord expectedRecord = new CSVRecord(new String[]{"John Doe", "Alice Smith", "Bob Johnson"},
 new HashMap<String, Integer>() {{
 put("name1", 0);
 put("name2", 1);
 put("name3", 2);
 }}, null, parser.getHeaderMap().get(0));
 assertEquals(expectedRecord.toString(), record.toString());
 }

 @Test
 public void testGet_Enum() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 assertEquals("Alice", record.get(Enum.NAME));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_Enum_NotFound() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 record.get(Enum.AGE);
 }

 @Test
 public void testGet_Int() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 assertEquals("Alice", record.get(0));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGet_Int_OutOfBounds() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 record.get(2);
 }

 @Test
 public void testGet_String() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 assertEquals("Alice", record.get("name"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGet_String_NoMapping() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 record.get("unknownColumn");
 }

 @Test
 public void testGet_String_Inconsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 Map<String, Integer> mapping = new HashMap<>();
 mapping.put("name", 0);
 mapping.put("age", 1);
 record.mapping = mapping;
 record.values = new String[1];
 assertFalse(record.isConsistent());
 assertNull(record.get("age"));
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 assertTrue(record.isMapped("name"));
 assertFalse(record.isMapped("age"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 assertTrue(record.isSet("name"));
 assertFalse(record.isSet("age"));
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 List<String> expectedList = Arrays.asList("Alice", "25", "Smith");
 List<String> list = Arrays.asList(record.iterator().toArray(new String[0]));
 assertEquals(expectedList, list);
 }

 @Test
 public void testPutIn() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("name", "Alice");
 expectedMap.put("age", "25");
 expectedMap.put("lastName", "Smith");
 assertEquals(expectedMap, map);
 }

 @Test
 public void testSize() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 assertEquals(3, record.size());
 }

 @Test
 public void testToString() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "25", "Smith"}, null, null, 0);
 assertEquals("[Alice, 25, Smith]", record.toString());
 }

}