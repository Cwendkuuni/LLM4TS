package com.puppycrawl.tools.checkstyle.api;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.List;
import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {

    private FileText fileText;

    @Test
    public void testFileTextConstructor() {
        File file = new File("test.txt");
        String charsetName = "UTF-8";

        try {
            fileText = new FileText(file, charsetName);
        } catch (Exception e) {
            fail("Exception thrown during FileText constructor: " + e.getMessage());
        }

        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());
    }

    @Test
    public void testGetFullText() {
        String expectedText = "This is a test text\nWith multiple lines\nTesting 1, 2, 3";

        List<String> lines = List.of("This is a test text", "With multiple lines", "Testing 1, 2, 3");
        FileText fileText = FileText.fromLines(new File("test.txt"), lines);

        assertEquals(expectedText, fileText.getFullText().toString());
    }

    @Test
    public void testLineColumn() {
        File file = new File("test.txt");
        List<String> lines = List.of("This is a test text", "With multiple lines", "Testing 1, 2, 3");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(new LineColumn(1, 5), fileText.lineColumn(5));
        assertEquals(new LineColumn(2, 7), fileText.lineColumn(26));
        assertEquals(new LineColumn(3, 9), fileText.lineColumn(44));
    }

    // Additional test cases for edge cases and exceptions can be added here

}