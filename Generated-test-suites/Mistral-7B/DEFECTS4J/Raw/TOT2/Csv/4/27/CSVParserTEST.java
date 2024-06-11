import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVParserTest {

    @Test
    public void testParseSimpleCSVFile() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new File("src/test/resources/simple.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name1", "Value1"}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name2", "Value2"}, record2.getValues());

        CSVRecord record3 = records.get(2);
        Assert.assertArrayEquals(new String[]{"Name3", "Value3"}, record3.getValues());
    }

    @Test
    public void testParseCSVWithHeader() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{"Name", "Value"});
        CSVParser parser = CSVParser.parse(new File("src/test/resources/header.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(3, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name1", "Value1"}, record1.toArray());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name2", "Value2"}, record2.toArray());

        CSVRecord record3 = records.get(2);
        Assert.assertArrayEquals(new String[]{"Name3", "Value3"}, record3.toArray());
    }

    @Test
    public void testParseCSVWithNullValues() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new File("src/test/resources/null_values.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        Assert.assertArrayEquals(new String[]{"Name1", "Value1", null}, record1.getValues());

        CSVRecord record2 = records.get(1);
        Assert.assertArrayEquals(new String[]{"Name2", null, "Value3"}, record2.getValues());
    }

    @Test(expected = IOException.class)
    public void testParseCSVWithInvalidFormat() throws IOException {
        CSVFormat format = new CSVFormat(new String[]{"Name", "Value"});
        CSVParser parser = new CSVParser(new FileReader("src/test/resources/invalid.csv"), format);
        parser.nextRecord();
        parser.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseCSVWithNullFile() {
        CSVFormat format = new CSVFormat();
        try {
            CSVParser parser = CSVParser.parse(null, format);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("file", e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testParseCSVWithNullFormat() throws IOException {
        File file = new File("src/test/resources/simple.csv");
        try {
            CSVParser parser = new CSVParser(new FileReader(file), null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("format", e.getMessage());
        }
    }

    @Test
    public void testParseCSVWithEmptyFile() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new File("src/test/resources/empty.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
        parser.close();
    }

    @Test
    public void testParseCSVWithMultiLineValues() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new File("src/test/resources/multiline.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        String[] values1 = record1.getValues();
        Assert.assertEquals(2, values1.length);
        Assert.assertEquals("Value1", values1[0]);
        Assert.assertEquals("Value1\nValue2", values1[1]);

        CSVRecord record2 = records.get(1);
        String[] values2 = record2.getValues();
        Assert.assertEquals(2, values2.length);
        Assert.assertEquals("Name2", values2[0]);
        Assert.assertEquals("Value3\nValue4", values2[1]);

        parser.close();
    }

    @Test
    public void testParseCSVWithComments() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new File("src/test/resources/comments.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        String[] values1 = record1.getValues();
        Assert.assertEquals(2, values1.length);
        Assert.assertEquals("Name1", values1[0]);
        Assert.assertEquals("Value1", values1[1]);
        Assert.assertTrue(values1[2].startsWith("Comment1"));

        CSVRecord record2 = records.get(1);
        String[] values2 = record2.getValues();
        Assert.assertEquals(2, values2.length);
        Assert.assertEquals("Name2", values2[0]);
        Assert.assertEquals("Value2", values2[1]);
        Assert.assertTrue(values2[2].startsWith("Comment2"));

        parser.close();
    }

    @Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String formatDefinition = "Name, value1, value2\n" +
                "Name, {String: Name}, {String: Value1: int}, {String: Value2: float}";
        CSVFormat format = CSVFormat.parse(formatDefinition, Charset.forName("UTF-8"));
        CSVParser parser = new CSVParser(new FileReader("src/test/resources/custom.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        CSVRecord record1 = records.get(0);
        String name1 = record1.get("Name");
        int value1 = record1.getInt("Value1");
        float value2 = record1.getFloat("Value2");

        Assert.assertEquals("Name1", name1);
        Assert.assertEquals(1, value1);
        Assert.assertEquals(2.5f, value2, 0.01f);

        CSVRecord record2 = records.get(1);
        String name2 = record2.get("Name");
        int value3 = record2.getInt("Value1");
        float value4 = record2.getFloat("Value2");

        Assert.assertEquals("Name2", name2);
        Assert.assertEquals(3, value3);
        Assert.assertEquals(3.5f, value4, 0.01f);
    }

    @Test
    public void testParseCSVWithEmptyValues() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = CSVParser.parse(new File("src/test/resources/empty_values.csv"), format);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        String[] values1 = record1.getValues();
        Assert.assertEquals(2, values1.length);
        Assert.assertArrayEquals(new String[]{"Name1", ""}, values1);

        CSVRecord record2 = records.get(1);
        String[] values2 = record2.getValues();
        Assert.assertEquals(2, values2.length);
        Assert.assertArrayEquals(new String[]{"Name2", ""}, values2);

        parser.close();
    }
}