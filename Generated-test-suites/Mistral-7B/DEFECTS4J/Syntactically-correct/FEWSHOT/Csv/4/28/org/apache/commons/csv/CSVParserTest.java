package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws Exception {
        File csvData = new File("src/test/resources/testCSVFile.csv");
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(5, records.size());

        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"Col1", "Col2", "Col3"}, record1.getValues());
        assertEquals("Col1Header", record1.getHeaderNameMap().get("Col1"));

        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"Col2,Col3", "Value2,Value3"}, record2.getValues());
        assertNull(record2.getComment());

        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"Col3", "Col1"}, record3.getValues());
        assertSame(record1.getHeaderNameMap().get("Col3"), record3.getHeaderNameMap().get("Col3"));
        assertEquals("Col3Comment", record3.getComment());

        CSVRecord record4 = records.get(3);
        assertArrayEquals(new String[]{"", "", ""}, record4.getValues());
        assertNull(record4.getComment());

        CSVRecord record5 = records.get(4);
        assertArrayEquals(new String[]{"", ""}, record5.getValues());
        assertEquals("", record5.getComment());

        parser.close();
    }

    @Test
    public void testParseString() throws Exception {
        String csvData = "Col1,Col2,Col3\n" +
                "Col1Header,Col2Header,Col3Header\n" +
                "Value1,Value2,Value3\n" +
                "Value2,Value3,Value4\n" +
                "   \n" +
                "Col3,Col1\n" +
                "Col3Comment,Col1\n" +
                "\n" +
                "";

        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.close();

        List<CSVRecord> expectedRecords = Arrays.asList(
                new CSVRecord(new String[]{"Col1Header", "Col2Header", "Col3Header"}, Collections.emptyMap(), null, 1),
                new CSVRecord(new String[]{"Value1", "Value2", "Value3"}, Collections.emptyMap(), null, 2),
                new CSVRecord(new String[]{"Value2", "Value3", "Value4"}, Collections.emptyMap(), null, 3),
                new CSVRecord(new String[]{}, Collections.emptyMap(), null, 4),
                new CSVRecord(new String[]{"Col3Comment", "Col1"}, Collections.emptyMap(), "Col3Comment", 5)
        );

        assertEquals(expectedRecords, records);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFormat() throws Exception {
        String csvData = "Col1,Col2\n" +
                "Value1,Value2\n" +
                "Value1,Value2,Value3";

        CSVParser parser = CSVParser.parse(csvData, new CSVFormatBuilder().setHeaderNames(Collections.singletonList("Col1")).build());
        parser.getRecords();
    }

    @Test
    public void testParseEmptyCSV() throws Exception {
        String csvData = "";

        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        assertEquals(Collections.emptyList(), records);
    }
}