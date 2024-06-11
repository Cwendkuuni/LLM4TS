import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

public class CSVParserTest {

 @Test
 public void testParseFile() throws IOException {
 File file = new File("testData.csv");
 CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Check size of records
 assertEquals(5, records.size());

 // Check values of records
 for (int i = 0; i < records.size(); i++) {
 String[] values = records.get(i).toArray(new String[records.get(i).size()]);
 switch (i) {
 case 0:
 Assert.assertArrayEquals(new String[]{"Name", "Age"}, records.get(i).getHeaderMap().keySet().toArray(new String[0]));
 Assert.assertArrayEquals(new Object[]{"Alice", "25"}, values);
 break;
 case 1:
 Assert.assertArrayEquals(new String[]{"City", "Country"}, records.get(i).getHeaderMap().keySet().toArray(new String[0]));
 Assert.assertArrayEquals(new Object[]{"Paris", "France"}, values);
 break;
 case 2:
 Assert.assertArrayEquals(new String[]{"Salary"}, records.get(i).getHeaderMap().keySet().toArray(new String[0]));
 Assert.assertArrayEquals(new Object[]{null}, values);
 break;
 case 3:
 Assert.assertArrayEquals(new String[]{"Name", "Age"}, records.get(i).getHeaderMap().keySet().toArray(new String[0]));
 Assert.assertArrayEquals(new Object[]{"Bob", "30"}, values);
 break;
 case 4:
 Assert.assertArrayEquals(new String[0], records.get(i).toArray());
 break;
 }
 }

 // Check that header map is consistent with all records
 Map<String, Integer> headerMap = parser.getHeaderMap();
 for (CSVRecord record : records) {
 assertTrue(headerMap.keySet().containsAll(record.getHeaderMap().keySet()));
 }

 parser.close();
 }

 @Test(expected = IOException.class)
 public void testParseFileWithInvalidFormat() throws IOException {
 File file = new File("testDataInvalidFormat.csv");
 CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
 parser.getRecords();
 fail("Should throw an IOException");
 parser.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullFile() {
 try {
 CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);
 fail("Should throw an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseString() throws IOException {
 String csvData = "Name,Age\nAlice,25\nBob,30";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 List<CSVRecord> records = parser.getRecords();

 // Check size of records
 assertEquals(2, records.size());

 // Check values of records
 for (int i = 0; i < records.size(); i++) {
 String[] values = records.get(i).toArray(new String[records.get(i).size()]);
 switch (i) {
 case 0:
 Assert.assertArrayEquals(new String[]{"Name", "Age"}, records.get(i).getHeaderMap().keySet().toArray(new String[0]));
 Assert.assertArrayEquals(new Object[]{"Alice", "25"}, values);
 break;
 case 1:
 Assert.assertArrayEquals(new Object[]{"Bob", "30"}, values);
 break;
 }
 }

 parser.close();
 }

 @Test(expected = IOException.class)
 public void testParseStringWithEmptyInput() throws IOException {
 String csvData = "";
 CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
 parser.getRecords();
 fail("Should throw an IOException");
 parser.close();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseStringWithNullInput() {
 try {
 CSVParser parser = new CSVParser(null, CSVFormat.DEFAULT);
 fail("Should throw an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}