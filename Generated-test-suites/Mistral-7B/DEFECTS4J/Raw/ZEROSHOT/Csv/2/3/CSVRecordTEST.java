import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private String[] values = new String[]{"John", "Doe", "john.doe@example.com", "45"};
 private Map<String, Integer> mapping = Map.of("Name", 0, "Surname", 1, "Email", 2, "Age", 3);
 private CSVRecord record = new CSVRecord(values, mapping, null, 1);

 @Test
 public void testGetByIndex() {
 String expected = "John";
 String actual = record.get(0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetByName() {
 String expected = "John";
 String actual = record.get("Name");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameWhenNoMapping() {
 record.mapping = null;
 String name = "Name";
 String actual = record.get(name);
 fail("Expected IllegalStateException but got " + actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameWhenInconsistent() {
 String[] values2 = new String[]{"John", "Doe"};
 record = new CSVRecord(values2, mapping, null, 1);
 String name = "Email";
 String actual = record.get(name);
 fail("Expected IllegalArgumentException but got " + actual);
 }

 @Test
 public void testIsConsistent() {
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 assertTrue(record.isMapped("Name"));
 }

 @Test
 public void testIsSet() {
 assertTrue(record.isSet("Name"));
 }

 @Test
 public void testIterator() {
 Iterator<String> iterator = record.iterator();
 String expected = "John";
 String actual = iterator.next();
 assertEquals(expected, actual);
 }

 @Test
 public void testValues() {
 String[] expected = values;
 String[] actual = record.values();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetComment() {
 String expected = null;
 String actual = record.getComment();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRecordNumber() {
 long expected = 1;
 long actual = record.getRecordNumber();
 assertEquals(expected, actual);
 }

 @Test
 public void testSize() {
 int expected = 4;
 int actual = record.size();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 String expected = "[Name=John, Surname=Doe, Email=john.doe@example.com, Age=45]";
 String actual = record.toString();
 assertEquals(expected, actual);
 }

}