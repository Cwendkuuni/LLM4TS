
package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {
    private DataUtil dataUtil;
    private File file;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
        file = mock(File.class);
    }

    @Test
    public void testLoad_withFileAndCharsetNameAndBaseUri_shouldReturnDocument() throws IOException {
        // Arrange
        FileInputStream fileInputStream = mock(FileInputStream.class);
        when(fileInputStream.read(any(byte[].class))).thenReturn(-1);
        when(fileInputStream.read()).thenReturn(-1);
        when(fileInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);
        whenNew(FileInputStream.class).withArguments(file).thenReturn(fileInputStream);

        // Act
        Document result = dataUtil.load(file, "UTF-8", "http://example.com");

        // Assert
        verifyNew(FileInputStream.class).withArguments(file);
        assertEquals(null, result);
    }
    
    // Add more test cases for the remaining methods in the DataUtil class
    
}
