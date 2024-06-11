import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CSVParserTest {

    private CSVParser parser;

    @Before
    public void setUp() {
        Reader reader = new StringReader("1,John,Doe\n2,Jane,Smith");
        this.parser = new CSVParser(reader, CSVFormat.DEFAULT);
    }

    @Test
    public void testHeaderMap() {
        assertEquals("1", parser.getHeaderMap().get("1"));
        assertEquals("2", parser.getHeaderMap().get("2"));
        assertEquals("3", parser.getHeaderMap().get("3"));
    }

    @Test
    public void testGetRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        assertEquals("1", records.get(0).get(0));
        assertEquals("John", records.get(0).get(1));
        assertEquals("Doe", records.get(0).get(2));

        assertEquals("2", records.get(1).get(0));
        assertEquals("Jane", records.get(1).get(1));
        assertEquals("Smith", records.get(1).get(2));
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() throws IOException {
        CSVParser.parse(new File("invalid.csv"), CSVFormat.DEFAULT);
    }

}