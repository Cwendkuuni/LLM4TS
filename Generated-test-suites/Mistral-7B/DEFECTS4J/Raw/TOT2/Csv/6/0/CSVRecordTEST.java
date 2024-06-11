import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private String[] values = new String[]{"John", "Doe", "123 Main St.", "New York", "NY", "12345"};
 private Map<String, Integer> mapping = new HashMap<String, Integer>() {{
 put("First name", 0);
 put("Last name", 1);
 put("Street", 2);
 put("City", 3);
 put("State", 4);
 put("Zip", 5);
 }};
 private CSVRecord record;

 @Test
 public void testGetByEnum() {
 record = new CSVRecord(values, mapping, null, 1);
 Assert.assertEquals("John", record.get(Enum.valueOf(CSVRecord.class, "FIRST_NAME")));
 }

 @Test
 public void testGetByIndex() {
 record = new CSVRecord(values, mapping, null, 1);
 Assert.assertEquals("John", record.get(0));
 }

 @Test
 public void testGetByName() {
 record = new CSVRecord(values, mapping, null, 1);
 Assert.assertEquals("Doe", record.get("Last name"));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoMapping() {
 record = new CSVRecord(values, null, null, 1);
 record.get("NonExistentColumn");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMapping() {
 record = new CSVRecord(values, mapping, null, 1);
 record.get("First name1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIndex() {
 record = new CSVRecord(values, mapping, null, 1);
 record.get(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInconsistentRecord() {
 record = new CSVRecord(values, null, null, 1);
 record.get(10);
 }

 @Test
 public void testGetComment() {
 record = new CSVRecord(values, mapping, "Comment for record #1", 1);
 Assert.assertEquals("Comment for record #1", record.getComment());
 }

 @Test
 public void testGetRecordNumber() {
 record = new CSVRecord(values, mapping, null, 1);
 Assert.assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testIsConsistent() {
 record = new CSVRecord(values, mapping, null, 1);
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 record = new CSVRecord(values, mapping, null, 1);
 Assert.assertTrue(record.isMapped("First name"));
 }

 @Test
 public void testIsSet() {
 record = new CSVRecord(values, mapping, null, 1);
 Assert.assertTrue(record.isSet("First name"));
 }

 @Test
 public void testIterator() {
 record = new CSVRecord(values, mapping, null, 1);
 Iterator<String> iterator = record.iterator();
 List<String> expectedValues = Arrays.asList(values);
 Assert.assertEquals(expectedValues, expectedValues.subList(0, expectedValues.size() - 1));
 }

 @Test
 public void testPutIn() {
 record = new CSVRecord(values, mapping, null, 1);
 Map<String, String> result = record.putIn(new HashMap<String, String>());
 Assert.assertEquals(mapping.size(), result.size());
 for (Entry<String, Integer> entry : mapping.entrySet()) {
 Assert.assertEquals(entry.getValue().toString(), result.get(entry.getKey()));
 }
 }

 @Test
 public void testSize() {
 record = new CSVRecord(values, mapping, null, 1);
 Assert.assertEquals(values.length, record.size());
 }

 @Test
 public void testToString() {
 record = new CSVRecord(values, mapping, null, 1);
 String expectedToString = "[John, Doe, 123 Main St., New York, NY, 12345]";
 Assert.assertEquals(expectedToString, record.toString());
 }

 @Test
 public void testValues() {
 record = new CSVRecord(values, mapping, null, 1);
 Assert.assertArrayEquals(values, record.values());
 }
}