package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private Appendable outMock;
    private CSVFormat formatMock;

    @Before
    public void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(outMock, formatMock);
    }
    
    @Test
    public void testPrint_NullValue_PrintsNullString() throws IOException {
        // Arrange
        String nullString = "NULL";
        Object value = null;
        csvPrinter = new CSVPrinter(outMock, formatMock);
        csvPrinter.print(value);
        // Act
        verify(outMock).append(nullString);
    }
    
    @Test
    public void testPrint_NonNullValue_PrintsValueToString() throws IOException {
        // Arrange
        String valueString = "foo";
        Object value = valueString;
        csvPrinter.print(value);
        // Act
        verify(outMock).append(valueString);
    }
    
    //...
    
}