import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 @Test
 public void testCSVRecordCreation() {
 CSVParser parser = new CSVParser(getClass().getResourceAsStream("/test.csv"), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();

 assertNotNull(record);
 assertNotNull(record.values());
 assertNotNull(record.mapping());
 assertNull(record.comment());
 assertFalse(record.isConsistent());
 assertFalse(record.isMapped("NonExistingColumn"));
 assertFalse(record.isSet("NonExistingColumn"));
 assertFalse(record.iterator().hasNext());

 assertEquals(0, record.size());
 assertEquals(0, record.values().length);
 }

 @Test
 public void testCSVRecordWithValues() {
 CSVParser parser = new CSVParser(getClass().getResourceAsStream("/test.csv"), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();

 assertNotNull(record);
 assertNotNull(record.values());
 assertNotNull(record.mapping());
 assertNull(record.comment());
 assertFalse(record.isConsistent());

 assertEquals(5, record.size());
 assertEquals(5, record.values().length);

 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));
 assertFalse(record.isSet("UnknownColumn"));

 String name = record.get("Name");
 String age = record.get("Age");
 String city = record.get("City");

 assertNotNull(name);
 assertNotNull(age);
 assertNotNull(city);

 assertEquals("Alice", name);
 assertEquals("30", age);
 assertEquals("New York", city);

 assertNull(record.get("UnknownColumn"));
 }

 @Test
 public void testCSVRecordWithComment() {
 CSVParser parser = new CSVParser(getClass().getResourceAsStream("/test.csv"), CSVFormat.DEFAULT);
 CSVRecord record = parser.getRecord();

 assertNotNull(record);
 assertNotNull(record.values());
 assertNotNull(record.mapping());
 assertNull(record.comment());

 assertFalse(record.isConsistent());

 record = parser.getRecord(1);

 assertNotNull(record);
 assertNotNull(record.values());
 assertNotNull(record.mapping());
 assertNull(record.comment());

 assertEquals(5, record.size());
 assertEquals(5, record.values().length);

 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("City"));
 assertFalse(record.isSet("UnknownColumn"));

 String name = record.get("Name");
 String age = record.get("Age");
 String city = record.get("City");

 assertNotNull(name);
 assertNotNull(age);
 assertNotNull(city);

 assertEquals("Bob", name);
 assertEquals("25", age);
 assertEquals("Los Angeles", city);

 assertNotNull(record.getComment());
 assertEquals("Record with comment.\n", record.getComment());
 }

 @Test
 public void testCSVRecordWithMapping() {
 CSVParser parser = new CSVParser(getClass().getResourceAsStream("/test.csv"), CSVFormat.DEFAULT.withHeader("Name,City,Age,UnknownColumn,Price"));
 CSVRecord record = parser.getRecord();

 assertNotNull(record);
 assertNotNull(record.values());
 assertNotNull(record.mapping());
 assertNotNull(record.comment());

 assertEquals(5, record.size());
 assertEquals(5, record.values().length);

 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("City"));
 assertTrue(record.isSet("Age"));
 assertTrue(record.isSet("Price"));
 assertFalse(record.isSet("UnknownColumn"));

 String name = record.get("Name");
 String city = record.get("City");
 String age = record.get("Age");
 String price = record.get("Price");

 assertNotNull(name);
 assertNotNull(city);
 assertNotNull(age);
 assertNotNull(price);

 assertEquals("Alice", name);
 assertEquals("New York", city);
 assertEquals("30", age);
 assertEquals("2.5", price);

 assertNull(record.get("UnknownColumn"));
 }
}