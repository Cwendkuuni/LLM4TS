import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private static final String CSV_DATA = "name,age,comment\nJohn Doe,30,some comment\nJane Doe,25,another comment";

 @Test
 public void testCSVRecord_DefaultConstructor() {
 CSVRecord record = new CSVRecord(new String[0], null, null, 0L);
 Assert.assertArrayEquals(new String[0], record.values());
 Assert.assertNull(record.mapping);
 Assert.assertNull(record.comment);
 Assert.assertEquals(0L, record.getRecordNumber());
 }

 @Test
 public void testCSVRecord_Constructor() {
 try (CSVParser parser = new CSVParser(CSV_DATA, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();

 Assert.assertArrayEquals(new String[]{"name", "age", "comment"}, record.mapping.keySet().toArray(new String[0]));
 Assert.assertArrayEquals(new String[]{"John Doe", "30", "some comment"}, record.values());
 Assert.assertEquals("some comment", record.getComment());
 Assert.assertEquals(1L, record.getRecordNumber());
 Assert.assertTrue(record.isConsistent());
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGet_Enum() {
 try (CSVParser parser = new CSVParser(CSV_DATA, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 Enum<String> header = Enum.valueOf(String.class, "name");
 String value = record.get(header);
 Assert.assertEquals("John Doe", value);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_String_NoHeaderMapping() {
 CSVRecord record = new CSVRecord(new String[]{"name", "age", "comment"}, null, null, 1L);
 record.mapping = null;
 String value = record.get("name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_String_InvalidName() {
 try (CSVParser parser = new CSVParser(CSV_DATA, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 String value = record.get("invalidName");
 } catch (Exception e) {
 // expected
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGet_String_NoHeaderMapping_AccessValue() {
 CSVRecord record = new CSVRecord(new String[]{"name", "age", "comment"}, null, null, 1L);
 String value = record.get("name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGet_String_InvalidIndex() {
 try (CSVParser parser = new CSVParser(CSV_DATA, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 String value = record.get(10);
 } catch (Exception e) {
 // expected
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGet_String_InvalidIndex_AccessValue() {
 try (CSVParser parser = new CSVParser(CSV_DATA, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 String value = record.values[10];
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testGetComment() {
 try (CSVParser parser = new CSVParser(CSV_DATA, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 Assert.assertEquals("some comment", record.getComment());
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetRecordNumber() {
 try (CSVParser parser = new CSVParser(CSV_DATA, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 Assert.assertEquals(1L, record.getRecordNumber());
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testIsConsistent() {
 try (CSVParser parser = new CSVParser(CSV_DATA, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 Assert.assertTrue(record.isConsistent());
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testIsMapped() {
 try (CSVParser parser = new CSVParser(CSV_DATA, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 Assert.assertTrue(record.isMapped("name"));
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testIsSet() {
 try (CSVParser parser = new CSVParser(CSV_Data, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 Assert.assertTrue(record.isSet("name"));
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testIterator() {
 try (CSVParser parser = new CSVParser(CSV_Data, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 Iterator<String> iterator = record.iterator();
 List<String> values = Arrays.asList("name0", "age0", "comment0");
 for (int i = 0; i < values.size(); i++) {
 String expected = values.get(i);
 String actual = iterator.next();
 Assert.assertEquals(expected, actual);
 }
 Assert.assertFalse(iterator.hasNext());
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testPutIn() {
 Map<String, String> map = new HashMap<>();
 CSVRecord record = new CSVRecord(new String[]{"name", "age", "comment"}, null, null, 1L);
 record.putIn(map);
 Assert.assertEquals("name", map.keySet().iterator().next());
 Assert.assertEquals("name", map.get("name"));
 Assert.assertEquals("age", map.get("age"));
 Assert.assertEquals("comment", map.get("comment"));
 }

 @Test
 public void testSize() {
 try (CSVParser parser = new CSVParser(CSV_Data, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 Assert.assertEquals(3, record.size());
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testToString() {
 try (CSVParser parser = new CSVParser(CSV_Data, CSVFormat.DEFAULT)) {
 CSVRecord record = parser.nextRecord();
 Assert.assertEquals("[name, age, comment]", record.toString());
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

}