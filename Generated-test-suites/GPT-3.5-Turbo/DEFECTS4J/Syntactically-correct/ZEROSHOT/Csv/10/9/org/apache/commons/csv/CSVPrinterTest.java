import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {
	
	private CSVPrinter csvPrinter;
	private Appendable outMock;
	private CSVFormat formatMock;
	private Object valueMock;
	
	@Before
	public void setUp() throws IOException {
		outMock = mock(Appendable.class);
		formatMock = mock(CSVFormat.class);
		csvPrinter = new CSVPrinter(outMock, formatMock);
		valueMock = mock(Object.class);
	}
	
	@Test
	public void testClose() throws IOException {
		// Verify that close() calls close() on the underlying Appendable
		csvPrinter.close();
		verify(outMock).close();
	}
	
	@Test
	public void testFlush() throws IOException {
		// Verify that flush() calls flush() on the underlying Appendable
		csvPrinter.flush();
		verify(outMock).flush();
	}
	
	@Test
	public void testPrint() throws IOException {
		// Verify that the print() method calls print(Object, String, int, int)
		// with the correct parameters
		String strValue = "test";
		when(valueMock.toString()).thenReturn(strValue);
		csvPrinter.print(valueMock);
		verify(csvPrinter).print(valueMock, strValue, 0, strValue.length());
	}
	
	@Test(expected = IOException.class)
	public void testPrintWithIOException() throws IOException {
		// Verify that IOException is thrown when the print() method encounters an IOException
		String strValue = "test";
		when(valueMock.toString()).thenReturn(strValue);
		doThrow(new IOException()).when(csvPrinter).print(valueMock, strValue, 0, strValue.length());
		csvPrinter.print(valueMock);
	}
	
	@Test
	public void testPrintRecordIterable() throws IOException {
		// Verify that printRecord(Iterable) calls print() for each value in the Iterable
		Iterable<?> valuesMock = mock(Iterable.class);
		csvPrinter.printRecord(valuesMock);
		verify(csvPrinter).print(valueMock);
	}
	
	@Test
	public void testPrintRecordObjectArray() throws IOException {
		// Verify that printRecord(Object...) calls print() for each value in the Object array
		Object[] valuesMock = new Object[] { valueMock };
		csvPrinter.printRecord(valuesMock);
		verify(csvPrinter).print(valueMock);
	}
	
	@Test
	public void testPrintRecordsIterable() throws IOException {
		// Verify that printRecords(Iterable) calls printRecord(Iterable) for each value in the Iterable
		Iterable<?> valuesMock = mock(Iterable.class);
		csvPrinter.printRecords(valuesMock);
		verify(csvPrinter).printRecord(valuesMock);
	}
	
	@Test
	public void testPrintRecordsObjectArray() throws IOException {
		// Verify that printRecords(Object...) calls printRecord(Object...) for each value in the Object array
		Object[] valuesMock = new Object[] { valueMock };
		csvPrinter.printRecords(valuesMock);
		verify(csvPrinter).printRecord(valuesMock);
	}
	
	@Test
	public void testPrintRecordsResultSet() throws SQLException, IOException {
		// Verify that printRecords(ResultSet) calls print() for each value in the ResultSet
		ResultSet resultSetMock = mock(ResultSet.class);
		csvPrinter.printRecords(resultSetMock);
		verify(resultSetMock).getMetaData();
		verify(resultSetMock, times(2)).next();
		verify(resultSetMock, times(2)).getString(anyInt());
		verify(csvPrinter, times(2)).print(anyString());
	}
	
	@Test
	public void testGetOut() {
		// Verify that getOut() returns the initialized Appendable object
		assertEquals(csvPrinter.getOut(), outMock);
	}
	
}