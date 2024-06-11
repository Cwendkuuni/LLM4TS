import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private CSVRecord record;
 private String[] values = {"John Doe", "25", "male", "New York", "IT"};
 private Map<String, Integer> mapping = new HashMap<>();

 @Before
 public void setUp() {
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("Gender", 2);
 mapping.put("City", 3);
 mapping.put("Profession", 4);
 record = new CSVRecord(values, mapping, null, 0);
 }

 @Test
 public void testGetEnum() {
 Enum<String> gender = Enum.valueOf("male", "MALE");
 String value = record.get(gender);
 assertEquals("male", value);
 }

 @Test
 public void testGetByIndex() {
 String value = record.get(0);
 assertEquals("John Doe", value);
 }

 @Test
 public void testGetByName() {
 String name = "Name";
 String value = record.get(name);
 assertEquals("John Doe", value);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameWithNoMapping() {
 String name = "Unknown";
 record = new CSVRecord(values, null, null, 0);
 try {
 record.get(name);
 } catch (IllegalStateException e) {
 assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
 }
 }

 @Test
 public void testGetByInvalidName() {
 String name = "Invalid";
 try {
 record.get(name);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Mapping for Invalid not found", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByInvalidIndex() {
 try {
 record.get(-1);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Index for Invalid is -1 but CSVRecord only has 5 values!", e.getMessage());
 }
 }

 @Test
 public void testGetComment() {
 String comment = "This is a comment for record number 0";
 record = new CSVRecord(values, mapping, comment, 0);
 assertEquals("This is a comment for record number 0", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 assertEquals(0, record.getRecordNumber());
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

 @Test(expected = IllegalStateException.class)
 public void testIsSetWithNoMapping() {
 record = new CSVRecord(values, null, null, 0);
 try {
 record.isSet("Name");
 fail();
 } catch (IllegalStateException e) {
 assertEquals("No header mapping was specified, the record values can't be accessed by name", e.getMessage());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testIsSetWithInvalidName() {
 record = new CSVRecord(values, mapping, null, 0);
 try {
 record.isSet("Invalid");
 fail();
 } catch (IllegalStateException e) {
 assertEquals("Mapping for Invalid not found, expected one of [Name, Age, Gender, City, Profession]", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsSetWithInvalidIndex() {
 try {
 record.isSet(-1);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Index for Invalid is -1 but CSVRecord only has 5 values!", e.getMessage());
 }
 }

 @Test
 public void testGetterMethods() {
 assertEquals(mapping, record.mapping);
 assertEquals(Arrays.asList(values), record.toList());
 assertEquals(values, record.values());
 assertEquals(record.toString(), "[John Doe, 25, male, New York, IT]");
 }
}