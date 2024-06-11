import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.StringWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import org.junit.Before;
import org.junit.After;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter writer;

    @Before
    public void setup() {
        writer = new StringWriter();
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("value1", "value2", "value3");
        assertEquals("value1,value2,value3\n", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecordsWithArrayValues() throws IOException {
        csvPrinter.printRecords(new Object[]{"a", "b", "c"}, new Object[]{"1", "2", "3"});
        assertEquals("a,b,c\n1,2,3\n", writer.toString());
    }

    @Test
    public void testPrintRecordsWithIterableValues() throws IOException {
        csvPrinter.printRecords(Arrays.asList("x", "y", "z"), Arrays.asList("4", "5", "6"));
        assertEquals("x,y,z\n4,5,6\n", writer.toString());
    }

    @Test
    public void testPrintRecordsWithJDBCResultSet() throws SQLException, IOException {
        Connection conn = DriverManager.getConnection("url", "user", "password");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM table");
        csvPrinter.printRecords(rs);
        // Will need to assert the result based on the actual ResultSet data
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnClose() throws IOException {
        writer.close(); // Simulate IOException
        csvPrinter.close();
    }
}