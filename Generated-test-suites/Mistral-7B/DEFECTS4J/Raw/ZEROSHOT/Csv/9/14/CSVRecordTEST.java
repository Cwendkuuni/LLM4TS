import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String csv = "name,age,country\nJohn Doe,30,USA\nJane Doe,25,Canada";
        CSVFormat format = new CSVFormat();
        try (CSVParser parser = format.newParser(new StringReader(csv))) {
            CSVRecord record1 = parser.nextRecord();
            CSVRecord record2 = parser.nextRecord();

            // Test constructor
            CSVRecord testRecord1 = new CSVRecord(record1.values(), record1.mapping, record1.comment, record1.recordNumber);
            CSVRecord testRecord2 = new CSVRecord(record2.values(), record2.mapping, record2.comment, record2.recordNumber);

            // Test get methods
            Assert.assertEquals("John Doe", testRecord1.get(0));
            Assert.assertEquals("30", testRecord1.get(1));
            Assert.assertEquals("USA", testRecord1.get(2));
            Assert.assertEquals("name", testRecord1.mapping.get(0));
            Assert.assertEquals(1, testRecord1.mapping.get(0).intValue());
            Assert.assertEquals("age", testRecord1.mapping.get(1));
            Assert.assertEquals(2, testRecord1.mapping.get(1).intValue());
            Assert.assertEquals("country", testRecord1.mapping.get(2));
            Assert.assertEquals(3, testRecord1.mapping.get(2).intValue());
            Assert.assertEquals(null, testRecord1.getComment());
            Assert.assertEquals(0, testRecord1.getRecordNumber());

            Assert.assertEquals("Jane Doe", testRecord2.get(0));
            Assert.assertEquals("25", testRecord2.get(1));
            Assert.assertEquals("Canada", testRecord2.get(2));
            Assert.assertNull(testRecord2.getComment());
            Assert.assertEquals(1, testRecord2.getRecordNumber());

            // Test get(Enum) and get(int) methods
            Assert.assertEquals("John Doe", testRecord1.get(CSVRecord.Name.NAME.toString()));
            Assert.assertEquals("30", testRecord1.get(CSVRecord.Name.AGE.toString()));
            Assert.assertEquals("USA", testRecord1.get(CSVRecord.Name.COUNTRY.toString()));
            Assert.assertEquals("John Doe", testRecord1.get(0));
            Assert.assertEquals("30", testRecord1.get(1));
            Assert.assertEquals("USA", testRecord1.get(2));

            Assert.assertEquals("Jane Doe", testRecord2.get(CSVRecord.Name.NAME.toString()));
            Assert.assertEquals("25", testRecord2.get(CSVRecord.Name.AGE.toString()));
            Assert.assertEquals("Canada", testRecord2.get(CSVRecord.Name.COUNTRY.toString()));
            Assert.assertEquals("Jane Doe", testRecord2.get(0));
            Assert.assertEquals("25", testRecord2.get(1));
            Assert.assertEquals("Canada", testRecord2.get(2));

            // Test get(String) method with valid name
            Assert.assertEquals("USA", testRecord1.get("country"));
            Assert.assertEquals("Canada", testRecord2.get("country"));

            // Test get(String) method with invalid name
            try {
                testRecord1.get("invalidName");
                Assert.fail();
            } catch (IllegalArgumentException e) {
                // Expected
            }

            try {
                testRecord2.get("invalidName");
                Assert.fail();
            } catch (IllegalArgumentException e) {
                // Expected
            }

            // Test isConsistent() method
            Assert.assertTrue(testRecord1.isConsistent());
            Assert.assertTrue(testRecord2.isConsistent());

            // Test isMapped() and isSet() methods
            Assert.assertTrue(testRecord1.isMapped("name"));
            Assert.assertTrue(testRecord1.isMapped("age"));
            Assert.assertTrue(testRecord1.isMapped("country"));
            Assert.assertTrue(testRecord1.isSet("name"));
            Assert.assertTrue(testRecord1.isSet("age"));
            Assert.assertTrue(testRecord1.isSet("country"));
            Assert.assertFalse(testRecord1.isMapped("invalidName"));
            Assert.assertFalse(testRecord1.isSet("invalidName"));

            Assert.assertTrue(testRecord2.isMapped("name"));
            Assert.assertTrue(testRecord2.isMapped("age"));
            Assert.assertTrue(testRecord2.isMapped("country"));
            Assert.assertTrue(testRecord2.isSet("name"));
            Assert.assertTrue(testRecord2.isSet("age"));
            Assert.assertTrue(testRecord2.isSet("country"));
            Assert.assertFalse(testRecord2.isMapped("invalidName"));
            Assert.assertFalse(testRecord2.isSet("invalidName"));

            // Test size() method
            Assert.assertEquals(3, testRecord1.size());
            Assert.assertEquals(3, testRecord2.size());

            // Test iterator() method
            List<String> list1 = testRecord1.iterator().asList();
            List<String> list2 = testRecord2.iterator().asList();
            Assert.assertEquals("John Doe", list1.get(0));
            Assert.assertEquals("30", list1.get(1));
            Assert.assertEquals("USA", list1.get(2));
            Assert.assertEquals("Jane Doe", list2.get(0));
            Assert.assertEquals("25", list2.get(1));
            Assert.assertEquals("Canada", list2.get(2));

            // Test putIn() method
            Map<String, String> map1 = new HashMap<>();
            map1.putAll(testRecord1.putIn(map1));
            Map<String, String> map2 = new HashMap<>();
            map2.putAll(testRecord2.putIn(map2));
            Assert.assertEquals("John Doe", map1.get("name"));
            Assert.assertEquals("30", map1.get("age"));
            Assert.assertEquals("USA", map1.get("country"));
            Assert.assertEquals("Jane Doe", map2.get("name"));
            Assert.assertEquals("25", map2.get("age"));
            Assert.assertEquals("Canada", map2.get("country"));

            // Test toString() method
            Assert.assertEquals("[John Doe, 30, USA]", testRecord1.toString());
            Assert.assertEquals("[Jane Doe, 25, Canada]", testRecord2.toString());

            // Test values() method
            String[] values1 = testRecord1.values();
            String[] values2 = testRecord2.values();
            Assert.assertArrayEquals(new String[]{"John Doe", "30", "USA"}, values1);
            Assert.assertArrayEquals(new String[]{"Jane Doe", "25", "Canada"}, values2);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    enum Name {
        NAME,
        AGE,
        COUNTRY
    }

}