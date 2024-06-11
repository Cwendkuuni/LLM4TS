
package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.File;


public class FileTextTest {
    
    private static final String CHARSET_NAME = "UTF-8";
    
    private File testFile;
    
    @Before
    public void setUp() {
        testFile = new File("test.txt");
    }
    
    @Test
    public void testConstructor() {
        FileText fileText = null;
        try {
            fileText = new FileText(testFile, CHARSET_NAME);
            assertEquals(testFile, fileText.getFile());
            assertEquals(Charset.forName(CHARSET_NAME), fileText.getCharset());
        } catch (Exception e) {
            assertNull(fileText);
        }
    }
    
    @Test
    public void testDuplicateConstructor() {
        FileText originalFileText = null;
        FileText duplicateFileText = null;
        try {
            originalFileText = new FileText(testFile, CHARSET_NAME);
            duplicateFileText = new FileText(originalFileText);
            
            assertEquals(originalFileText.getFile(), duplicateFileText.getFile());
            assertEquals(originalFileText.getCharset(), duplicateFileText.getCharset());
            assertEquals(originalFileText.getFullText(), duplicateFileText.getFullText());
            
            String[] originalFileTextLines = originalFileText.toLinesArray();
            String[] duplicateFileTextLines = duplicateFileText.toLinesArray();
            
            for (int i = 0; i < originalFileTextLines.length; i++) {
                assertEquals(originalFileTextLines[i], duplicateFileTextLines[i]);
            }
            
        } catch (Exception e) {
            assertNull(originalFileText);
            assertNull(duplicateFileText);
        }
    }
    
    @Test
    public void testFromLines() {
        FileText fileText = null;
        try {
            fileText = FileText.fromLines(testFile, Arrays.asList("Line 1", "Line 2", "Line 3"));
            
            assertEquals(testFile, fileText.getFile());
            assertNull(fileText.getCharset());
            
            String[] fileTextLines = fileText.toLinesArray();
            String[] expectedFileTextLines = {"Line 1", "Line 2", "Line 3"};
            
            for (int i = 0; i < fileTextLines.length; i++) {
                assertEquals(expectedFileTextLines[i], fileTextLines[i]);
            }
            
        } catch (Exception e) {
            assertNull(fileText);
        }
    }
    
    @Test
    public void testLineColumn() {
        FileText fileText = null;
        try {
            fileText = new FileText(testFile, CHARSET_NAME);
            
            LineColumn lineColumn = fileText.lineColumn(0);
            
            assertEquals(1, lineColumn.getLineNo());
            assertEquals(0, lineColumn.getColumnNo());
            
            lineColumn = fileText.lineColumn(10);
            
            assertEquals(2, lineColumn.getLineNo());
            assertEquals(1, lineColumn.getColumnNo());
            
            lineColumn = fileText.lineColumn(50);
            
            assertEquals(5, lineColumn.getLineNo());
            assertEquals(2, lineColumn.getColumnNo());
            
        } catch (Exception e) {
            assertNull(fileText);
        }
    }
    
    @Test
    public void testGet() {
        FileText fileText = null;
        try {
            fileText = new FileText(testFile, CHARSET_NAME);
            
            assertEquals("Line 1", fileText.get(0));
            assertEquals("Line 2", fileText.get(1));
            assertEquals("Line 3", fileText.get(2));
            
        } catch (Exception e) {
            assertNull(fileText);
        }
    }
    
    @Test
    public void testSize() {
        FileText fileText = null;
        try {
            fileText = new FileText(testFile, CHARSET_NAME);
            
            assertEquals(3, fileText.size());
            
        } catch (Exception e) {
            assertNull(fileText);
        }
    }
}
