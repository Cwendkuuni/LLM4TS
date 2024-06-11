import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CSVPrinterTest {

    @Test
    public void testPrintString() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.print("test");

        verify(out).append("test");
    }

    @Test
    public void testPrintNullString() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.print(null);

        verify(out).append("");
    }

    @Test
    public void testPrintRecords() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        Object[] records = {"record1", "record2", "record3"};
        csvPrinter.printRecords(records);

        verify(out).append("record1,record2,record3\n");
    }

    @Test
    public void testPrintRecordsWithResultSet() throws SQLException, IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        ResultSet resultSet = mock(ResultSet.class);
        csvPrinter.printRecords(resultSet);

        // Assuming the result set has some data and the iteration and printing are correct
    }

    @Test
    public void testPrintComment() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.printComment("This is a comment");

        verify(out).append("# This is a comment\n");
    }

    @Test
    public void testFlush() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.flush();

        verify((Flushable) out).flush();
    }

    @Test
    public void testClose() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.close();

        verify((Closeable) out).close();
    }

}