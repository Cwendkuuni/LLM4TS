import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private static final String CSV_WITH_VALID_DATA = "name,age,city\nJohn,30,New York\nMary,25,Los Angeles";

 @Test
 public void testGetValidValue() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(CSV_WITH_VALID_DATA);
 CSVRecord record = parser.nextRecord();

 Assert.assertEquals("John", record.get(0));
 }

 @Test
 public void testGetValidValueByName() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(CSV_WITH_VALID_DATA);
 CSVRecord record = parser.nextRecord();

 Assert.assertEquals("New York", record.get("city"));
 }

 @Test
 public void testGetValidValueByEnum() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(CSV_WITH_VALID_DATA);
 CSVRecord record = parser.nextRecord();

 Assert.assertEquals("John", record.get(CSVRecord.Column.NAME));
 }

 @Test
 public void testGetEmptyValue() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(CSV_WITH_VALID_DATA.replace("John,30,New York", ""));
 CSVRecord record = parser.nextRecord();

 Assert.assertNull(record.get("name"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetValueWithoutHeaderMapping() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(CSV_WITH_VALID_DATA);
 CSVRecord record = parser.nextRecord();
 record.get("nonExistentColumn");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithInvalidIndex() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(CSV_WITH_VALID_DATA);
 CSVRecord record = parser.nextRecord();

 record.get(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithInvalidName() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(CSV_WITH_VALID_DATA);
 CSVRecord record = parser.nextRecord();

 record.get("nonExistentColumn");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetValueWithMismatchedSize() {
 CSVFormat format = new CSVFormat();
 String invalidCSV = "name,age,city\nJohn,30\nMary,25,New York,Los Angeles";
 CSVParser parser = format.parse(invalidCSV);
 CSVRecord record = parser.nextRecord();

 record.get("city");
 }

 @Test
 public void testIsConsistent() {
 CSVFormat format = new CSVFormat();
 String validCSV = "name,age,city\nJohn,30,New York\nMary,25,Los Angeles";
 CSVParser parser = format.parse(validCSV);
 CSVRecord record = parser.nextRecord();

 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVFormat format = new CSVFormat();
 String validCSV = "name,age,city\nJohn,30,New York\nMary,25,Los Angeles";
 CSVParser parser = format.parse(validCSV);
 CSVRecord record = parser.nextRecord();

 Assert.assertTrue(record.isMapped("name"));
 }

 @Test
 public void testIsSet() {
 CSVFormat format = new CSVFormat();
 String validCSV = "name,age,city\nJohn,30,New York\nMary,25,Los Angeles";
 CSVParser parser = format.parse(validCSV);
 CSVRecord record = parser.nextRecord();

 Assert.assertTrue(record.isSet("name"));
 }

 @Test
 public void testIterator() {
 CSVFormat format = new CSVFormat();
 String validCSV = "name,age,city\nJohn,30,New York\nMary,25,Los Angeles";
 CSVParser parser = format.parse(validCSV);
 CSVRecord record = parser.nextRecord();
 Iterator<String> it = record.iterator();
 List<String> values = Arrays.asList("name", "age", "city", "John", "30", "New York");

 Assert.assertEquals(values.size(), it.size());
 for (int i = 0; i < values.size(); i++) {
 Assert.assertEquals(values.get(i), it.next());
 }
 }

 @Test
 public void testToList() {
 CSVFormat format = new CSVFormat();
 String validCSV = "name,age,city\nJohn,30,New York\nMary,25,Los Angeles";
 CSVParser parser = format.parse(validCSV);
 CSVRecord record = parser.nextRecord();

 Assert.assertEquals(Arrays.asList("John", "30", "New York"), record.toList());
 }

 @Test
 public void testToMap() {
 CSVFormat format = new CSVFormat();
 String validCSV = "name,age,city\nJohn,30,New York\nMary,25,Los Angeles";
 CSVParser parser = format.parse(validCSV);
 CSVRecord record = parser.nextRecord();

 Map<String, String> expectedMap = new HashMap<>();
 expectedMap.put("name", "John");
 expectedMap.put("age", "30");
 expectedMap.put("city", "New York");

 Map<String, String> actualMap = record.toMap();

 Assert.assertEquals(expectedMap, actualMap);
 }
}