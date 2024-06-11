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
    public void testEmptyRecord() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader(""));
        CSVRecord emptyRecord = csvParser.nextRecord();

        Assert.assertNull(emptyRecord.getComment());
        Assert.assertEquals(0, emptyRecord.size());
        Assert.assertFalse(emptyRecord.isConsistent());
    }

    @Test
    public void testSimpleRecord() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age\nJohn,25"));
        CSVRecord record = csvParser.nextRecord();

        Assert.assertEquals("Name:Age", record.getComment());
        Assert.assertEquals("Age", record.getMapping().get("Age").toString());
        Assert.assertEquals("25", record.get("Age"));
        Assert.assertEquals("John", record.get("Name"));
        Assert.assertEquals(2, record.size());
        Assert.assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingMapping() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age,City\nJohn,25"));
        CSVRecord record = csvParser.nextRecord();

        Assert.assertNull(record.getComment());
        Assert.assertNull(record.get("City"));
        Assert.assertEquals(3, record.size());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertFalse(record.isMapped("City"));
        Assert.assertFalse(record.isSet("City"));
        Assert.assertTrue(record.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInconsistentRecord() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age\nJohn,25,City"));
        CSVRecord record = csvParser.nextRecord();

        Assert.assertNull(record.getComment());
        Assert.assertEquals(2, record.size());
        Assert.assertTrue(record.isMapped("Name"));
        Assert.assertTrue(record.isMapped("Age"));
        Assert.assertFalse(record.isConsistent());
    }

    @Test
    public void testGetByEnum() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age\nJohn,25"));
        CSVRecord record = csvParser.nextRecord();

        Assert.assertEquals("Name", record.getClass().getDeclaredField("Name").getName());
        Assert.assertEquals("Age", record.getClass().getDeclaredField("Age").getName());

        Assert.assertEquals("John", record.get(CSVRecord.Name.class));
        Assert.assertEquals("25", record.get(CSVRecord.Age.class));
    }

    @Test
    public void testToString() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age\nJohn,25"));
        CSVRecord record = csvParser.nextRecord();

        Assert.assertEquals("[John, 25]", record.toString());
    }

    @Test
    public void testGetRecordNumber() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age\nJohn,25\nMary,30"));
        CSVRecord record1 = csvParser.nextRecord();
        CSVRecord record2 = csvParser.nextRecord();

        Assert.assertEquals(1, record1.getRecordNumber());
        Assert.assertEquals(2, record2.getRecordNumber());
    }

    @Test
    public void testIterable() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age\nJohn,25\nMary,30"));
        CSVRecord record1 = csvParser.nextRecord();
        CSVRecord record2 = csvParser.nextRecord();

        int i = 0;
        for (String value : record1) {
            System.out.println("Value: " + value);
            i++;
        }

        Assert.assertEquals(2, i);
        i = 0;
        for (String value : record2) {
            System.out.println("Value: " + value);
            i++;
        }

        Assert.assertEquals(2, i);
    }

    @Test
    public void testMap() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age\nJohn,25\nMary,30"));
        CSVRecord record1 = csvParser.nextRecord();
        CSVRecord record2 = csvParser.nextRecord();

        Map<String, String> map1 = record1.toMap();
        Map<String, String> map2 = record2.toMap();

        Assert.assertEquals("John", map1.get("Name"));
        Assert.assertEquals("25", map1.get("Age"));
        Assert.assertEquals("Mary", map2.get("Name"));
        Assert.assertEquals("30", map2.get("Age"));
    }

    @Test
    public void testGetByIndex() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age\nJohn,25\nMary,30"));
        CSVRecord record1 = csvParser.nextRecord();
        CSVRecord record2 = csvParser.nextRecord();

        Assert.assertEquals("John", record1.get(0));
        Assert.assertEquals("25", record1.get(1));
        Assert.assertEquals("Mary", record2.get(0));
        Assert.assertEquals("30", record2.get(1));
    }

    @Test
    public void testGetByColumnName() {
        CSVFormat csvFormat = new CSVFormat();
        CSVParser csvParser = csvFormat.newParser(new StringReader("Name,Age\nJohn,25\nMary,30"));
        CSVRecord record1 = csvParser.nextRecord();
        CSVRecord record2 = csvParser.nextRecord();

        Assert.assertEquals("John", record1.get("Name"));
        Assert.assertEquals("25", record1.get("Age"));
        Assert.assertEquals("Mary", record2.get("Name"));
        Assert.assertEquals("30", record2.get("Age"));
    }
}