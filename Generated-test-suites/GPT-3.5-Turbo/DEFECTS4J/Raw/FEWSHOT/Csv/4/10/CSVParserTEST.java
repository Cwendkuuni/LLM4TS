import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CSVParserTest {

    @Test
    public void testParse() throws IOException {
        File file = new File("test.csv");
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = CSVParser.parse(file, format);
        assertNotNull(parser);

        for (CSVRecord record : parser) {
            assertEquals(3, record.size());
            assertNotNull(record.get(0));
            assertNotNull(record.get(1));
            assertNotNull(record.get(2));
        }

        parser.close();
        parser = CSVParser.parse(file, format);

        int count = 0;
        for (CSVRecord record : parser) {
            count++;
        }
        assertEquals(3, count);

        parser.close();
    }

}