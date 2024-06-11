import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private static final String CSV_TEXT = "name1,age1,comment1\nname2,age2,comment2\nname3,age3,comment3\n";

 @Test
 public void testCSVRecordConstructor() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 String[] expectedValues = new String[]{"name1", "age1", "comment1"};
 Map<String, Integer> expectedMapping = new HashMap<String, Integer>() {{
 put("name1", 0);
 put("age1", 1);
 put("comment1", 2);
 }};
 String expectedComment = "comment1";
 long expectedRecordNumber = 1;
 CSVRecord actualRecord = new CSVRecord(expectedValues, expectedMapping, expectedComment, expectedRecordNumber);

 Assert.assertArrayEquals(expectedValues, actualRecord.values());
 Assert.assertEquals(expectedMapping, actualRecord.mapping);
 Assert.assertEquals(expectedComment, actualRecord.getComment());
 Assert.assertEquals(expectedRecordNumber, actualRecord.getRecordNumber());
 }

 @Test
 public void testGetMethod() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 // Test get(Enum)
 Enum nameEnum = Enum.valueOf("Name", 0);
 String name = record.get(nameEnum);
 String expectedName = "name1";
 Assert.assertEquals(expectedName, name);

 // Test get(int)
 int index = 0;
 String expectedValue = "name1";
 String value = record.get(index);
 Assert.assertEquals(expectedValue, value);

 // Test get(String)
 String columnName = "Name";
 String expectedColumnValue = "name1";
 String columnValue = record.get(columnName);
 Assert.assertEquals(expectedColumnValue, columnValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetWithoutMapping() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 record.mapping = null;
 String columnName = "Name";
 record.get(columnName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidIndex() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 int index = record.values.length;
 record.get(index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidName() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 String invalidName = "nonExistingColumn";
 record.get(invalidName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInconsistentRecord() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader("name1,age1\nname2,age2,comment2\nname3,age3"));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 record.mapping = new HashMap<String, Integer>() {{
 put("name1", 0);
 put("age1", 1);
 put("comment", 2); // different from the actual value in the CSV file
 }};
 String columnName = "comment";
 record.get(columnName);
 }

 @Test
 public void testIsConsistent() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record1 = records.get(0);
 CSVRecord record2 = records.get(1);

 Assert.assertTrue(record1.isConsistent());
 Assert.assertTrue(record2.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 Assert.assertTrue(record.isMapped("name1"));
 Assert.assertTrue(record.isMapped("age1"));
 Assert.assertFalse(record.isMapped("nonExistingColumn"));
 }

 @Test
 public void testIsSet() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);

 Assert.assertTrue(record.isSet("name1"));
 Assert.assertTrue(record.isSet("age1"));
 Assert.assertFalse(record.isSet("comment1")); // comment1 exists but is Null in the record
 }

 @Test
 public void testIterator() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 List<String> expectedList = Arrays.asList("name1", "age1", "comment1");
 List<String> actualList = new ArrayList<>();
 for (String value : record) {
 actualList.add(value);
 }
 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testPutIn() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 Map<String, String> expectedMap = new HashMap<String, String>() {{
 put("name1", "name1");
 put("age1", "age1");
 put("comment1", "comment1");
 }};
 Map<String, String> actualMap = new HashMap<String, String>();
 actualMap.putAll(record.putIn(actualMap));
 Assert.assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testSize() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 int expectedSize = 3;
 int actualSize = record.size();
 Assert.assertEquals(expectedSize, actualSize);
 }

 @Test
 public void testToString() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.newParser(new StringReader(CSV_TEXT));
 List<CSVRecord> records = parser.getRecords();
 CSVRecord record = records.get(0);
 String expectedToString = "[name1, age1, comment1]";
 String actualToString = record.toString();
 Assert.assertEquals(expectedToString, actualToString);
 }

}