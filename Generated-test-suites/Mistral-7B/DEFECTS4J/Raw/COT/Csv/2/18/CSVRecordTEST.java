import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private String[] testValues = new String[]{"John", "Doe", "john.doe@example.com", "123 Main St"};
 private Map<String, Integer> testMapping;

 @Before
 public void setup() {
 testMapping = Map.of("First name", 0, "Last name", 1, "Email", 2, "Address", 3);
 }

 @Test
 public void testGetByIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0L);
 for (int i = 0; i < testValues.length; i++) {
 String value = record.get(i);
 Assert.assertEquals(testValues[i], value);
 }
 }

 @Test
 public void testGetByInvalidIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0L);
 Assert.assertNull(record.get(-1));
 }

 @Test
 public void testGetByName() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0L);
 for (Map.Entry<String, Integer> entry : testMapping.entrySet()) {
 String name = entry.getKey();
 Integer index = entry.getValue();
 String value = record.get(name);
 Assert.assertEquals(testValues[index], value);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameWithNoMapping() {
 CSVRecord record = new CSVRecord(testValues, null, null, 0L);
 record.get("InvalidName");
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0L);
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistentWithInvalidSize() {
 CSVRecord record = new CSVRecord(new String[]{"John", "Doe"}, testMapping, null, 0L);
 Assert.assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0L);
 for (Map.Entry<String, Integer> entry : testMapping.entrySet()) {
 String name = entry.getKey();
 Assert.assertTrue(record.isMapped(name));
 }
 }

 @Test
 public void testIsSet() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0L);
 for (Map.Entry<String, Integer> entry : testMapping.entrySet()) {
 String name = entry.getKey();
 Assert.assertTrue(record.isSet(name));
 }
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0L);
 Iterator<String> iterator = record.iterator();
 for (int i = 0; i < testValues.length; i++) {
 String value = iterator.next();
 Assert.assertEquals(testValues[i], value);
 }
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testValues() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0L);
 Assert.assertArrayEquals(testValues, record.values());
 }

 @Test
 public void testGetComment() {
 CSVRecord record = new CSVRecord(testValues, testMapping, "Test comment", 0L);
 Assert.assertEquals("Test comment", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 123L);
 Assert.assertEquals(123L, record.getRecordNumber());
 }
}