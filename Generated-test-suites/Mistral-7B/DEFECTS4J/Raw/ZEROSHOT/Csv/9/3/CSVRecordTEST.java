import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private CSVRecord record;
 private CSVParser parser;
 private CSVFormat format;

 @Before
 public void setup() throws Exception {
 String csvData = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Paris";
 format = new CSVFormat();
 parser = new CSVParser(new StringReader(csvData), format);
 record = parser.nextRecord();
 }

 @Test
 public void testGet() {
 assertEquals("John Doe", record.get(0));
 assertEquals("30", record.get(1));
 assertEquals("New York", record.get(2));
 }

 @Test
 public void testGetInt() {
 assertEquals(30, Integer.parseInt(record.get(1)));
 }

 @Test
 public void testGetEnum() {
 enum TestEnum { A, B, C };
 assertEquals("New York", record.get(TestEnum.B));
 }

 @Test
 public void testGetComment() {
 assertEquals(null, record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertTrue(record.isMapped("City"));
 }

 @Test
 public void testIsSet() {
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));
 }

 @Test
 public void testIterator() {
 List<String> values = record.toList();
 Iterator<String> iterator = record.iterator();
 assertEquals(values.get(0), iterator.next());
 assertEquals(values.get(1), iterator.next());
 assertEquals(values.get(2), iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testPutIn() {
 Map<String, String> map = new HashMap<>();
 record.putIn(map);
 assertEquals("John Doe", map.get("Name"));
 assertEquals("30", map.get("Age"));
 assertEquals("New York", map.get("City"));
 }

 @Test
 public void testSize() {
 assertEquals(3, record.size());
 }

 @Test
 public void testToString() {
 assertEquals("[Name=John Doe, Age=30, City=New York]", record.toString());
 }

}