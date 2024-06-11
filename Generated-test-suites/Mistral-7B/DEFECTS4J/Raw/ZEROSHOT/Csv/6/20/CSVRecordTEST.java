import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static final String CSV_DATA = "name,age,comment\nAlice,30,this is Alice\nBob,25,this is Bob\n";

 @Test
 public void testCSVRecord() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(CSV_DATA);
 CSVRecord record = parser.nextRecord();

 // Test get methods
 assertEquals("Alice", record.get(0));
 assertEquals("name", record.get(0).getClass().getName());
 assertEquals(30, Integer.parseInt(record.get(1)));
 assertEquals("age", record.get(1).getClass().getName());
 assertEquals("this is Alice", record.get(2));
 assertEquals("comment", record.get(2).getClass().getName());
 assertEquals(null, record.get(-1));

 // Test get by name
 assertEquals("Alice", record.get("name"));
 assertEquals(30, Integer.parseInt(record.get("age")));
 assertEquals("this is Alice", record.get("comment"));

 // Test getComment
 assertEquals("this is Alice", record.getComment());

 // Test getRecordNumber
 assertEquals(1, record.getRecordNumber());

 // Test isConsistent
 assertTrue(record.isConsistent());

 // Test isMapped
 assertTrue(record.isMapped("name"));
 assertTrue(record.isMapped("age"));
 assertFalse(record.isMapped("nonExistent"));

 // Test isSet
 assertTrue(record.isSet("name"));
 assertTrue(record.isSet("age"));
 assertFalse(record.isSet("nonExistent"));

 // Test iterator
 List<String> values = record.iterator().asList();
 assertEquals(3, values.size());
 assertEquals("Alice", values.get(0));
 assertEquals("30", values.get(1));
 assertEquals("this is Alice", values.get(2));

 // Test putIn
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 assertEquals(3, map.size());
 assertEquals("Alice", map.get("name"));
 assertEquals("30", map.get("age"));
 assertEquals("this is Alice", map.get("comment"));

 // Test size
 assertEquals(3, record.size());

 // Test toMap
 Map<String, String> map2 = record.toMap();
 assertEquals(3, map2.size());
 assertEquals("Alice", map2.get("name"));
 assertEquals("30", map2.get("age"));
 assertEquals("this is Alice", map2.get("comment"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNotFound() {
 CSVRecord record = new CSVRecord(null, null, null, 1L);
 record.get("name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidIndex() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "30"}, null, null, 1L);
 record.get(3);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetNoMapping() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "30"}, null, null, 1L);
 record.get("nonExistent");
 }

}