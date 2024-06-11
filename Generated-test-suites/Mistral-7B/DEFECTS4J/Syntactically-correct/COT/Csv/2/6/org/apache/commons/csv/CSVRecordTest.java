import static org.junit.Assert.*;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

 private CSVRecord record;

 @Before
 public void setUp() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = new java.util.HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("City", 2);
 record = new CSVRecord(values, mapping, null, 1);
 }

 @Test
 public void testGetByIndex() {
 String expected = "Name";
 String actual = record.get(0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetByInvalidIndex() {
 try {
 record.get(-1);
 fail();
 } catch (ArrayIndexOutOfBoundsException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetByName() {
 String expected = "Name";
 String actual = record.get("Name");
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameWhenNoMappingProvided() {
 record = new CSVRecord(new String[0], null, null, 1);
 record.get("Name");
 }

 @Test
 public void testGetByInvalidName() {
 try {
 record.get("InvalidName");
 fail();
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testIsConsistent() {
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsInconsistent() {
 String[] values = new String[]{"Name", "Age", "City"};
 Map<String, Integer> mapping = new java.util.HashMap<>();
 mapping.put("Name", 0);
 mapping.put("Age", 1);
 mapping.put("City", 2);
 record = new CSVRecord(values, mapping, null, 1);
 record.values = new String[2];
 assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 assertTrue(record.isMapped("Name"));
 }

 @Test
 public void testIsNotMapped() {
 assertFalse(record.isMapped("InvalidName"));
 }

 @Test
 public void testIsSet() {
 assertTrue(record.isSet("Name"));
 }

 @Test
 public void testIsNotSet() {
 assertFalse(record.isSet("InvalidName"));
 }

 @Test
 public void testIterator() {
 String[] expected = new String[]{"Name", "Age", "City"};
 Iterator<String> iterator = record.iterator();
 for (int i = 0; i < expected.length; i++) {
 String expectedValue = expected[i];
 String actualValue = iterator.next();
 assertEquals(expectedValue, actualValue);
 }
 }

 @Test
 public void testValues() {
 String[] expected = new String[]{"Name", "Age", "City"};
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
 int expected = 3;
 int actual = record.size();
 assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
 String expected = "[Name, Age, City]";
 String actual = record.toString();
 assertEquals(expected, actual);
 }
}