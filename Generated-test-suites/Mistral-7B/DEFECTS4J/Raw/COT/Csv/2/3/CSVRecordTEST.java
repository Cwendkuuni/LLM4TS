import static org.junit.Assert.*;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

 private CSVRecord record;

 @Before
 public void setup() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = new java.util.HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("City", 2);
 record = new CSVRecord(values, mapping, null, 1);
 }

 @Test
 public void testGet() {
 String expectedValue = "John Doe";
 String actualValue = record.get(0);
 assertEquals("Expected 'John Doe' but was '" + actualValue + "'", expectedValue, actualValue);
 }

 @Test
 public void testGetNull() {
 assertNull("Expected null but was '" + record.get("NonExistentColumn") + "'", record.get("NonExistentColumn"));
 }

 @Test
 public void testIsConsistent() {
 assertTrue("Expected record to be consistent but was not", record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 assertTrue("Expected 'Name' to be mapped but was not", record.isMapped("Name"));
 }

 @Test
 public void testIsSet() {
 assertTrue("Expected 'Name' to be set but was not", record.isSet("Name"));
 }

 @Test
 public void testIterator() {
 Iterator<String> iterator = record.iterator();
 String[] expectedValues = new String[]{"Name", "Age", "City"};
 int i = 0;
 while (iterator.hasNext()) {
 String actualValue = iterator.next();
 assertEquals("Expected '" + expectedValues[i] + "' but was '" + actualValue + "'", expectedValues[i], actualValue);
 i++;
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 record = new CSVRecord(new String[0], null, null, 1);
 record.get(0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNoMapping() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.get("City");
 }

 @Test(expected = IllegalStateException.class)
 public void testGetWithNullMapping() {
 record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 record.get("City");
 }

 @Test
 public void testSize() {
 assertEquals("Expected size to be 3 but was " + record.size(), 3, record.size());
 }

 @Test
 public void testToString() {
 String expectedToString = "[Name, Age, City]";
 String actualToString = record.toString();
 assertEquals("Expected ['Name', 'Age', 'City'] but was '" + actualToString + "'", expectedToString, actualToString);
 }

}