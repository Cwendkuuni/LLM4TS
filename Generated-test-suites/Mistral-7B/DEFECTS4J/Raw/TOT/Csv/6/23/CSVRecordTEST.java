import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private static final String CSV_DATA = "name,age,comment\nJohn Doe,30,\"This is a comment for John Doe\"\nJane Smith,25\n";

 @Test
 public void testCSVRecordWithValidData() {
 CSVFormat format = new CSVFormat(',').withHeader();
 Iterable<CSVRecord> records = format.parse(CSV_DATA);
 CSVRecord record = records.iterator().next();

 assertEquals("John Doe", record.get("name"));
 assertEquals("30", record.get("age"));
 assertEquals("This is a comment for John Doe", record.get("comment"));
 }

 @Test
 public void testCSVRecordWithMissingHeader() {
 CSVFormat format = new CSVFormat(',').withHeader(new String[]{"age", "name"});
 Iterable<CSVRecord> records = format.parse(CSV_DATA);
 CSVRecord record = records.iterator().next();

 assertNull(record.get("name"));
 assertEquals("30", record.get("age"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithMissingColumn() {
 CSVFormat format = new CSVFormat(',').withHeader(new String[]{"name", "comment"});
 Iterable<CSVRecord> records = format.parse(CSV_DATA);
 CSVRecord record = records.iterator().next();

 assertNull(record.get("age"));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCSVRecordWithInvalidIndex() {
 CSVFormat format = new CSVFormat(',').withHeader();
 Iterable<CSVRecord> records = format.parse(CSV_DATA);
 CSVRecord record = records.iterator().next();

 record.get(record.size() + 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordWithInvalidEnum() {
 CSVRecord record = new CSVRecord(new String[0], null, null, 0);

 record.get((Enum<?>) String.class);
 }

 @Test
 public void testCSVRecordWithEmptyData() {
 CSVFormat format = new CSVFormat(',').withHeader();
 Iterable<CSVRecord> records = format.parse(new String[0]);
 CSVRecord record = records.iterator().next();

 assertEquals(0, record.size());
 }

 @Test
 public void testCSVRecordWithEmptyMapping() {
 CSVFormat format = new CSVFormat(',').withHeader();
 Iterable<CSVRecord> records = format.parse(CSV_DATA);
 CSVRecord record = records.iterator().next();

 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithExtraColumns() {
 CSVFormat format = new CSVFormat(',').withHeader(new String[]{"name", "age"});
 Iterable<CSVRecord> records = format.parse(CSV_DATA);
 CSVRecord record = records.iterator().next();

 assertFalse(record.isConsistent());
 }

 @Test
 public void testCSVRecordWithNullString() {
 CSVFormat format = new CSVFormat(',').withNullString("NULL");
 Iterable<CSVRecord> records = format.parse(CSV_DATA);
 CSVRecord record = records.iterator().next();

 assertEquals("", record.get("age"));
 }
}