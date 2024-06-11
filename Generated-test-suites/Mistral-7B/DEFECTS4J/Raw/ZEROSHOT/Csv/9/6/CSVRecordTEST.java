import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static final String CSV_TEST_STRING = "Name,Age,City\n" +
         "Alice,30,New York\n" +
         "Bob,25,San Francisco\n" +
         "Charlie,22,Chicago";

 @Test
 public void testGetByIndex() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 String expectedValue = "Alice";
 String actualValue = record.get(0);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetByIndexOutOfBounds() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 String actualValue = record.get(3);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test
 public void testGetByEnum() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 Enum<String> nameEnum = new Enum<String>("Name") {
 };
 String expectedValue = "Alice";
 String actualValue = record.get(nameEnum);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNonExistentName() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 String actualValue = record.get("Gender");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testGetComment() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 String expectedValue = "";
 String actualValue = record.getComment();
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetRecordNumber() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 long expectedValue = 1;
 long actualValue = record.getRecordNumber();
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testIsConsistent() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 assertTrue(record.isMapped("Name"));
 }

 @Test
 public void testIsSet() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 assertTrue(record.isSet("Name"));
 }

 @Test
 public void testSize() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 int expectedValue = 3;
 int actualValue = record.size();
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testToString() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 String expectedValue = "[Alice, 30, New York]";
 String actualValue = record.toString();
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testIterable() {
 CSVParser parser = new CSVParser(new StringReader(CSV_TEST_STRING), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();
 List<String> expectedList = Arrays.asList("Alice", "30", "New York");
 List<String> actualList = new ArrayList<>();
 for (String s : record) {
 actualList.add(s);
 }
 assertEquals(expectedList, actualList);
 }
}