import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 @Test
 public void testCSVRecordGetValueByEnum() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertEquals("John", record.get(Enum.NAME));
 assertEquals("30", record.get(Enum.AGE));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetValueByUnknownEnum() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 record.get(Enum.UNKNOWN);
 }
 }

 @Test
 public void testCSVRecordGetValueByIndex() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertEquals("John", record.get(0));
 assertEquals("30", record.get(1));
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testCSVRecordGetValueByNegativeIndex() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 record.get(-1);
 }
 }

 @Test
 public void testCSVRecordGetValueByName() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertEquals("John", record.get("Name"));
 assertEquals("30", record.get("Age"));
 assertNull(record.get("Unknown"));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetValueByNameWithNullMapping() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 record.get("Unknown");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCSVRecordGetValueByNameWithUnmappedColumn() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 record.get("Column");
 }
 }

 @Test
 public void testCSVRecordGetValueByNameWithMappedButEmptyColumn() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertNull(record.get("Age"));
 }
 }

 @Test
 public void testCSVRecordGetValueByNameWithMappedAndFilledColumn() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertEquals("30", record.get("Age"));
 }
 }

 @Test
 public void testCSVRecordGetComment() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age,Comment\nJohn,30,\nMike,25,Test comment"))) {
 CSVRecord record = parser.nextRecord();
 assertEquals("", record.getComment());
 CSVRecord record2 = parser.nextRecord();
 assertEquals("Test comment", record2.getComment());
 }
 }

 @Test
 public void testCSVRecordGetRecordNumber() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertEquals(1, record.getRecordNumber());
 }
 }

 @Test
 public void testCSVRecordIsConsistent() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertTrue(record.isConsistent());
 }
 }

 @Test
 public void testCSVRecordIsNotConsistent() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25,Test"))) {
 CSVRecord record = parser.nextRecord();
 assertFalse(record.isConsistent());
 }
 }

 @Test
 public void testCSVRecordIsMapped() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Age"));
 assertFalse(record.isMapped("Unknown"));
 }
 }

 @Test
 public void testCSVRecordIsSet() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertFalse(record.isSet("Unknown"));
 }
 }

 @Test
 public void testCSVRecordSize() {
 CSVFormat format = new CSVFormat(',', '\n');
 try (CSVParser parser = format.newParser(new StringReader("Name,Age\nJohn,30\nMike,25"))) {
 CSVRecord record = parser.nextRecord();
 assertEquals(2, record.size());
 }
 }
}