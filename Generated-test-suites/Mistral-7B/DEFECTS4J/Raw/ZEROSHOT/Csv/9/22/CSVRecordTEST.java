import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecord() {
        String csv = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Miami";
        CSVFormat format = CSVFormat.DEFAULT;
        try (CSVParser parser = new CSVParser(new StringReader(csv), format)) {
            CSVRecord record1 = parser.nextRecord();
            CSVRecord record2 = parser.nextRecord();

            // Test constructor
            CSVRecord testRecord1 = new CSVRecord(record1.values(), record1.mapping, record1.comment, record1.recordNumber);
            Assert.assertArrayEquals(record1.values(), testRecord1.values());
            Assert.assertEquals(record1.mapping, testRecord1.mapping);
            Assert.assertEquals(record1.comment, testRecord1.comment);
            Assert.assertEquals(record1.recordNumber, testRecord1.recordNumber);

            // Test get methods
            Assert.assertEquals("John Doe", testRecord1.get(0));
            Assert.assertEquals("30", testRecord1.get(1));
            Assert.assertEquals("New York", testRecord1.get(2));
            Assert.assertEquals(30, testRecord1.getAge());
            Assert.assertEquals("New York", testRecord1.getCity());
            Assert.assertEquals("Name", testRecord1.get(CSVRecordTest.CSVColumn.NAME.toString()));
            Assert.assertEquals("Age", testRecord1.get(CSVColumn.AGE.toString()));
            Assert.assertEquals("City", testRecord1.get(CSVColumn.CITY.toString()));
            Assert.assertNull(testRecord1.get("Foo"));

            // Test isConsistent
            Assert.assertTrue(testRecord1.isConsistent());

            // Test isMapped and isSet
            Assert.assertTrue(testRecord1.isMapped(CSVColumn.NAME.toString()));
            Assert.assertTrue(testRecord1.isMapped(CSVColumn.AGE.toString()));
            Assert.assertTrue(testRecord1.isMapped(CSVColumn.CITY.toString()));
            Assert.assertTrue(testRecord1.isSet(CSVColumn.NAME.toString()));
            Assert.assertTrue(testRecord1.isSet(CSVColumn.AGE.toString()));
            Assert.assertTrue(testRecord1.isSet(CSVColumn.CITY.toString()));
            Assert.assertFalse(testRecord1.isSet("Foo"));

            // Test iterator and size
            List<String> list = testRecord1.toList();
            Assert.assertEquals(3, list.size());
            Assert.assertEquals("John Doe", list.get(0));
            Assert.assertEquals("30", list.get(1));
            Assert.assertEquals("New York", list.get(2));

            // Test toMap
            Map<String, String> map = testRecord1.toMap();
            Assert.assertEquals(3, map.size());
            Assert.assertEquals("John Doe", map.get(CSVColumn.NAME.toString()));
            Assert.assertEquals("30", map.get(CSVColumn.AGE.toString()));
            Assert.assertEquals("New York", map.get(CSVColumn.CITY.toString()));

            // Test getComment and getRecordNumber
            Assert.assertEquals("", testRecord1.getComment());
            Assert.assertEquals(1, testRecord1.getRecordNumber());

            // Test equals and hashCode
            CSVRecord record3 = new CSVRecord(record1.values(), record1.mapping, record1.comment, record1.recordNumber);
            Assert.assertEquals(testRecord1, record3);
            Assert.assertEquals(testRecord1.hashCode(), record3.hashCode());

            // Test get method with null values
            CSVRecord testRecord2 = new CSVRecord(record2.values(), record2.mapping, record2.comment, record2.recordNumber);
            Assert.assertNull(testRecord2.get(CSVColumn.AGE.toString()));

            // Test toString
            Assert.assertEquals("[John Doe, 30, New York]", testRecord1.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private enum CSVColumn {
        NAME,
        AGE,
        CITY
    }

}