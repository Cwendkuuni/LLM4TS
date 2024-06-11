import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static final String TEST_CSV = "name,age,comment\nJohn Doe,30,This is a test record\nJane Doe,25,\n";

 @Test
 public void testCSVRecordCreation() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(TEST_CSV);
 CSVRecord record = parser.nextRecord();
 CSVRecord expectedRecord = new CSVRecord(new String[]{"John Doe", "30", "This is a test record"},
 new HashMap<>(), "This is a test record", 1);
 assertEquals(expectedRecord.get(0), record.get(0));
 assertEquals(expectedRecord.get(1), record.get(1));
 assertEquals(expectedRecord.getComment(), record.getComment());
 }

 @Test
 public void testGetMethod() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(TEST_CSV);
 CSVRecord record = parser.nextRecord();

 // Test get(Enum<>) method
 Enum<String> testEnum = Enum.valueOf("String", "name");
 String expectedValue = "John Doe";
 String actualValue = record.get(testEnum);
 assertEquals(expectedValue, actualValue);

 // Test get(int) method
 int columnIndex = 0;
 String expectedIntValue = "John Doe";
 String actualIntValue = record.get(columnIndex);
 assertEquals(expectedIntValue, actualIntValue);

 // Test get(String) method
 String expectedStringValue = "John Doe";
 String actualStringValue = record.get("name");
 assertEquals(expectedStringValue, actualStringValue);

 // Test getComment() method
 String expectedComment = "This is a test record";
 String actualComment = record.getComment();
 assertEquals(expectedComment, actualComment);

 // Test getRecordNumber() method
 long expectedRecordNumber = 1;
 long actualRecordNumber = record.getRecordNumber();
 assertEquals(expectedRecordNumber, actualRecordNumber);
 }

 @Test
 public void testIsConsistent() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(TEST_CSV);
 CSVRecord record = parser.nextRecord();
 CSVRecord recordWithMissingColumn = new CSVRecord(new String[]{"John Doe", "30"}, null, null, 1);
 assertTrue(record.isConsistent());
 assertFalse(recordWithMissingColumn.isConsistent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithMissingColumn() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(TEST_CSV);
 CSVRecord record = parser.nextRecord();
 String missingColumnName = "nonexistentColumn";
 record.get(missingColumnName);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetWithInvalidIndex() {
 CSVFormat format = new CSVFormat();
 CSVParser parser = format.parse(TEST_CSV);
 CSVRecord record = parser.nextRecord();
 int invalidIndex = 3;
 record.get(invalidIndex);
 }
}