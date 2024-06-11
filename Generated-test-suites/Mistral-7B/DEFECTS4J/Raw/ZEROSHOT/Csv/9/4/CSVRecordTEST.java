import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static final String TEST_CSV = "record1,col1,col2,col3\n" +
         "header1,value1,value2,value3\n" +
         "record2,col1,col4,col5\n" +
         "header2,value4,value5,value6";

 @Test
 public void testGetByEnum() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 Enum<String> enumCol1 = new Enum<>("COL1") {
 };
 String value = record.get(enumCol1);
 assertEquals("col1", value);
 }

 @Test
 public void testGetByIndex() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 String value = record.get(0);
 assertEquals("record1", value);
 }

 @Test
 public void testGetByName() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 String value = record.get("col1");
 assertEquals("col1", value);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameUnmapped() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 record.get("nonExistentColumn");
 }

 @Test
 public void testGetComment() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV + "\n--comment1\nrecord1,col1,col2,col3\n"), format);
 CSVRecord record = parser.getRecord();
 assertEquals("--comment1", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record1 = parser.getRecord();
 CSVRecord record2 = parser.getRecord();
 assertEquals(1L, record1.getRecordNumber());
 assertEquals(2L, record2.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 assertTrue(record.isMapped("col1"));
 }

 @Test
 public void testIsSet() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 assertTrue(record.isSet("col1"));
 }

 @Test
 public void testIterator() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 List<String> values = record.toList();
 Iterator<String> iterator = record.iterator();
 int i = 0;
 while (iterator.hasNext()) {
 String value = iterator.next();
 assertEquals(values.get(i), value);
 i++;
 }
 }

 @Test
 public void testPutIn() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 Map<String, String> map = new HashMap<>();
 map = record.putIn(map);
 assertNotNull(map);
 assertEquals(4, map.size());
 assertEquals("record1", map.get("recordNumber"));
 assertEquals("col1", map.get("col1"));
 assertEquals("col2", map.get("col2"));
 assertEquals("col3", map.get("col3"));
 }

 @Test
 public void testSize() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 assertEquals(4, record.size());
 }

 @Test
 public void testToString() {
 CSVFormat format = CSVFormat.DEFAULT;
 CSVParser parser = new CSVParser(new StringReader(TEST_CSV), format);
 CSVRecord record = parser.getRecord();
 assertEquals("[col1, col2, col3, col4]", record.toString());
 }
}