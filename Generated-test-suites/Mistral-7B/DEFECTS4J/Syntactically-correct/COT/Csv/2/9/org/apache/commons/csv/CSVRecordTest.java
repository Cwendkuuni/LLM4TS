import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CSVRecordTest {

 private String[] testValues = new String[]{"John", "Doe", "30", "M", "New York"};
 private Map<String, Integer> testMapping = Map.of("Name", 0, "Age", 1, "Gender", 2, "City", 4);

 @Test
 public void testCSVRecordConstruction() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 assertArrayEquals(testValues, record.values());
 assertSame(testMapping, record.mapping);
 assertNull(record.getComment());
 assertSame(0, record.getRecordNumber());
 }

 @Test
 public void testGetMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 for (int i = 0; i < record.size(); i++) {
 String actual = record.get(i);
 String expected = testValues[i];
 assertSame(expected, actual);
 }
 }

 @Test
 public void testGetByNameMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 for (Map.Entry<String, Integer> entry : testMapping.entrySet()) {
 String actual = record.get(entry.getKey());
 String expected = testValues[entry.getValue().intValue()];
 assertSame(expected, actual);
 }
 }

 @Test
 public void testIsConsistentMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 assertTrue(record.isConsistent());

 String[] wrongSizeValues = new String[testValues.length + 1];
 System.arraycopy(testValues, 0, wrongSizeValues, 0, testValues.length);
 CSVRecord inconsistentRecord = new CSVRecord(wrongSizeValues, testMapping, null, 0);
 assertFalse(inconsistentRecord.isConsistent());
 }

 @Test
 public void testIsMappedMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 for (Map.Entry<String, Integer> entry : testMapping.entrySet()) {
 assertTrue(record.isMapped(entry.getKey()));
 }

 String unMappedName = "UnmappedName";
 assertFalse(record.isMapped(unMappedName));
 }

 @Test
 public void testIsSetMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 for (Map.Entry<String, Integer> entry : testMapping.entrySet()) {
 assertTrue(record.isSet(entry.getKey()));
 }

 String nullValue = null;
 String[] testValuesWithNull = new String[]{testValues};
 testValuesWithNull[2] = nullValue;
 CSVRecord recordWithNullValue = new CSVRecord(testValuesWithNull, testMapping, null, 0);
 for (Map.Entry<String, Integer> entry : testMapping.entrySet()) {
 if (entry.getKey().equals("Age")) {
 assertFalse(recordWithNullValue.isSet("Age"));
 break;
 }
 assertTrue(recordWithNullValue.isSet(entry.getKey()));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameMethodWithNullMapping() {
 CSVRecord record = new CSVRecord(testValues, null, null, 0);
 record.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetByNameMethodWithInvalidIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 record.get(values.length + 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetByNameMethodWithNoMapping() {
 CSVRecord record = new CSVRecord(testValues, null, null, 0);
 record.get("UnmappedName");
 }

}