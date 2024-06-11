package org.apache.commons.csv; // import necessary packages here

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.junit.Test;

/**
 * Test class for CSVParser
 */
public class CSVParserTest {

    

    @Test
    public void testParseFile() throws Exception {
        CSVParser parser = CSVParser.parse(new File("src/test/resources/test.csv"), CSVFormat.EXCEL);
        CSVRecordIterator it = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (it.hasNext()) {
            records.add(it.next());
        }
        parser.close();

        assertEquals(4, records.size());
        assertEquals("Record1", records.get(0).get(0));
        assertEquals("Record1", records.get(0).get(1));
        assertEquals(1, records.get(0).size());

        assertEquals("Record2,Record3", records.get(1).get(0));
        assertEquals("Record2", records.get(1).get(1));
        assertEquals(2, records.get(1).size());

        assertNull(records.get(2).get(0));
        assertNull(records.get(2).get(1));
        assertEquals(0, records.get(2).size());

        assertEquals("Record4,", records.get(3).get(0));
        assertEquals("Record4", records.get(3).get(1));
        assertEquals("", records.get(3).get(2));
        assertEquals(2, records.get(3).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileWithNullFormat() throws Exception {
        CSVParser.parse(new File("src/test/resources/test.csv"), null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testParseFileWithNonExistentFile() throws Exception {
        CSVParser.parse(new File("src/test/resources/nonexistentfile.csv"), CSVFormat.EXCEL);
    }

    @Test
    public void testParseString() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d"), CSVFormat.EXCEL);
        CSVRecordIterator it = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (it.hasNext()) {
            records.add(it.next());
        }
        parser.close();

        assertEquals(2, records.size());
        assertEquals("a", records.get(0).get(0));
        assertEquals("b", records.get(0).get(1));
        assertEquals(1, records.get(0).size());

        assertEquals("c", records.get(1).get(0));
        assertEquals("d", records.get(1).get(1));
        assertEquals(1, records.get(1).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringWithNullFormat() throws Exception {
        CSVParser.parse(new StringReader("a,b\nc,d"), null);
    }

    @Test(expected = IOException.class)
    public void testParseStringWithInvalidFormat() throws Exception {
        CSVParser.parse(new StringReader("a,b\nc,d"), new CSVFormat().withHeader("InvalidHeader"));
    }

    @Test
    public void testParseUrl() throws Exception {
        CSVParser parser = CSVParser.parse(new URL("http://example.com/test.csv"), CSVFormat.EXCEL);
        CSVRecordIterator it = parser.iterator();
        List<CSVRecord> records = new ArrayList<>();
        while (it.hasNext()) {
            records.add(it.next());
        }
        parser.close();

        // Add your test cases for parsing from URL here.

    }

    @Test(expected = IOException.class)
    public void testParseUrlWithInvalidFormat() throws Exception {
        CSVParser.parse(new URL("http://example.com/test.csv"), new CSVFormat().withHeader("InvalidHeader"));
    }

    @Test
    public void testParseUrlWithInvalidCharset() throws Exception {
        CSVParser.parse(new URL("http://example.com/test.csv"), CSVFormat.EXCEL, null);
    }

    @Test
    public void testGetRecords() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d"), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());
        assertEquals("a", records.get(0).get(0));
        assertEquals("b", records.get(0).get(1));
        assertEquals(1, records.get(0).size());

        assertEquals("c", records.get(1).get(0));
        assertEquals("d", records.get(1).get(1));
        assertEquals(1, records.get(1).size());
    }

    @Test
    public void testGetRecordsEmpty() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertTrue(records.isEmpty());
    }

    @Test
    public void testGetRecordsNull() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader(""), null);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertNull(records);
    }

    @Test
    public void testGetRecordsWithComment() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader("a#comment1,b#comment2\nc,d"), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(1, records.size());
        CSVRecord record = records.get(0);
        assertEquals(2, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("comment1\ncomment2", record.getComments());
    }

    @Test
    public void testGetRecordsWithEmptyLine() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\n\nc,d"), CSVFormat.EXCEL);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(2, records.size());
        assertEquals("a", records.get(0).get(0));
        assertEquals("b", records.get(0).get(1));
        assertEquals(1, records.get(0).size());

        assertEquals("c", records.get(1).get(0));
        assertEquals("d", records.get(1).get(1));
        assertEquals(1, records.get(1).size());
    }

    @Test(expected = IOException.class)
    public void testGetRecordsWithInvalidFormat() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d"), new CSVFormat().withHeader("InvalidHeader"));
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Add your test cases for getting records with invalid format here.
    }

    @Test
    public void testIterateRecords() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d"), CSVFormat.EXCEL);
        Iterator<CSVRecord> it = parser.iterator();

        assertEquals("a", it.next().get(0));
        assertEquals("b", it.next().get(0));
        assertEquals("c", it.next().get(0));
        assertEquals("d", it.next().get(0));
        assertFalse(it.hasNext());

        parser.close();
    }

    @Test
    public void testIterateRecordsEmpty() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader(""), CSVFormat.EXCEL);
        Iterator<CSVRecord> it = parser.iterator();

        assertFalse(it.hasNext());
        parser.close();
    }

    @Test
    public void testIterateRecordsNull() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader(""), null);
        Iterator<CSVRecord> it = parser.iterator();

        assertFalse(it.hasNext());
        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIterateRecordsAfterEnd() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d"), CSVFormat.EXCEL);
        Iterator<CSVRecord> it = parser.iterator();

        it.next();
        it.next();
        it.next();
        it.next();
        it.next(); // Should throw NoSuchElementException

        parser.close();
    }

    @Test
    public void testClose() throws Exception {
        CSVParser parser = CSVParser.parse(new StringReader("a,b\nc,d"), CSVFormat.EXCEL);
        parser.close();

        try {
            parser.nextRecord();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    
}