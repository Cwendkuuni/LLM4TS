import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String csv = "Name,Age,Country\nJohn Doe,30,USA\nJane Doe,25,Canada";

        CSVFormat format = new CSVFormat();
        try (CSVParser parser = format.parse(new StringReader(csv))) {
            CSVRecord record1 = parser.nextRecord();
            CSVRecord record2 = parser.nextRecord();

            // Test getting values by index
            Assert.assertEquals("John Doe", record1.get(0));
            Assert.assertEquals("30", record1.get(1));
            Assert.assertEquals("USA", record1.get(2));

            Assert.assertEquals("Jane Doe", record2.get(0));
            Assert.assertEquals("25", record2.get(1));
            Assert.assertEquals("Canada", record2.get(2));

            // Test getting values by name
            Assert.assertEquals("John Doe", record1.get("Name"));
            Assert.assertEquals("30", record1.get("Age"));
            Assert.assertEquals("USA", record1.get("Country"));

            Assert.assertEquals("Jane Doe", record2.get("Name"));
            Assert.assertEquals("25", record2.get("Age"));
            Assert.assertEquals("Canada", record2.get("Country"));

            // Test getting comment
            Assert.assertNull(record1.getComment());
            Assert.assertNull(record2.getComment());

            // Test record number
            Assert.assertEquals(1, record1.getRecordNumber());
            Assert.assertEquals(2, record2.getRecordNumber());

            // Test isConsistent
            Assert.assertTrue(record1.isConsistent());
            Assert.assertTrue(record2.isConsistent());

            // Test isMapped and isSet
            Assert.assertTrue(record1.isMapped("Name"));
            Assert.assertTrue(record1.isMapped("Age"));
            Assert.assertTrue(record1.isMapped("Country"));
            Assert.assertTrue(record1.isSet("Name"));
            Assert.assertTrue(record1.isSet("Age"));
            Assert.assertTrue(record1.isSet("Country"));

            Assert.assertTrue(record2.isMapped("Name"));
            Assert.assertTrue(record2.isMapped("Age"));
            Assert.assertTrue(record2.isMapped("Country"));
            Assert.assertTrue(record2.isSet("Name"));
            Assert.assertTrue(record2.isSet("Age"));
            Assert.assertTrue(record2.isSet("Country"));

            // Test iterator
            List<String> recordValues1 = record1.iterator().asList();
            List<String> recordValues2 = record2.iterator().asList();

            Assert.assertEquals(Arrays.asList("John Doe", "30", "USA"), recordValues1);
            Assert.assertEquals(Arrays.asList("Jane Doe", "25", "Canada"), recordValues2);

            // Test putIn
            Map<String, String> recordMap1 = record1.putIn(new HashMap<>());
            Map<String, String> recordMap2 = record2.putIn(new HashMap<>());

            Assert.assertEquals(Arrays.asList("Name", "Age", "Country"), recordMap1.keySet());
            Assert.assertEquals(Arrays.asList("John Doe", "30", "USA"), recordMap1.values());

            Assert.assertEquals(Arrays.asList("Name", "Age", "Country"), recordMap2.keySet());
            Assert.assertEquals(Arrays.asList("Jane Doe", "25", "Canada"), recordMap2.values());

            // Test size
            Assert.assertEquals(3, record1.size());
            Assert.assertEquals(3, record2.size());

            // Test toList
            List<String> recordList1 = record1.toList();
            List<String> recordList2 = record2.toList();

            Assert.assertEquals(Arrays.asList("John Doe", "30", "USA"), recordList1);
            Assert.assertEquals(Arrays.asList("Jane Doe", "25", "Canada"), recordList2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}