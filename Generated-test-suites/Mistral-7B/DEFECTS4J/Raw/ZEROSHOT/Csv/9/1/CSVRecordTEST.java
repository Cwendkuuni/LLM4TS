import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class CSVRecordTest {

 @Test
 public void testCSVRecordWithValidValues() {
 String csvRecord = "name,age,gender John Doe,25,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 String name = record.get("name");
 int age = record.get(0, Integer.class);
 String gender = record.get("gender");

 assertEquals("John Doe", name);
 assertEquals(25, age);
 assertEquals("M", gender);
 }

 @Test
 public void testCSVRecordWithEmptyValues() {
 String csvRecord = "name,age,gender John Doe,,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 String name = record.get("name");
 int age = record.get(1, Integer.class);
 String gender = record.get("gender");

 assertEquals("John Doe", name);
 assertNull(age);
 assertEquals("M", gender);
 }

 @Test
 public void testCSVRecordWithInvalidIndex() {
 String csvRecord = "name,age,gender John Doe,25,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 String result = record.get(-1);
 assertNull(result);
 }

 @Test
 public void testCSVRecordWithInvalidName() {
 String csvRecord = "name,age,gender John Doe,25,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 String result = record.get("invalidName");
 assertNull(result);
 }

 @Test
 public void testCSVRecordWithNullValues() {
 String csvRecord = "name,age,gender John Doe,NULL,M";
 CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 String name = record.get("name");
 int age = record.get(1, Integer.class);
 String gender = record.get("gender");

 assertEquals("John Doe", name);
 assertNull(age);
 assertEquals("M", gender);
 }

 @Test
 public void testCSVRecordWithValidMapPut() {
 String csvRecord = "name,age,gender John Doe,25,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 Map<String, String> map = new HashMap<>();
 record.putIn(map);

 assertEquals("name", map.keySet().iterator().next());
 assertEquals("John Doe", map.get("name"));
 assertEquals("age", map.keySet().iterator().next());
 assertEquals("25", map.get("age"));
 assertEquals("gender", map.keySet().iterator().next());
 assertEquals("M", map.get("gender"));
 }

 @Test
 public void testCSVRecordWithConsistentValues() {
 String csvRecord = "name,age,gender John Doe,25,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 assertTrue(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithInconsistentValues() {
 String csvRecord = "name,age,gender John Doe,25,M,abcd";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithNullComment() {
 String csvRecord = "name,age,gender John Doe,25,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 assertNull(record.getComment());
 }

 @Test
 public void testCSVRecordWithValidComment() {
 String csvRecord = "name,age,gender John Doe,25,M #test comment";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 assertEquals("#test comment", record.getComment());
 }

 @Test
 public void testCSVRecordWithValidRecordNumber() {
 String csvRecord = "name,age,gender John Doe,25,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testCSVRecordWithValidIterable() {
 String csvRecord = "name,age,gender John Doe,25,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 List<String> values = Arrays.asList("name", "John Doe", "age", "25", "gender", "M");

 assertSame(values, record.iterator());
 }

 @Test
 public void testCSVRecordWithValidToString() {
 String csvRecord = "name,age,gender John Doe,25,M";
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(csvRecord), format);
 CSVRecord record = parser.nextRecord();

 assertEquals("[name, John Doe, age, 25, gender, M]", record.toString());
 }

}