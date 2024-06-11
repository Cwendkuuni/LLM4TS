import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class FileTextTest {

    @Test
    // Test typical use case
    public void testFileTextTypical() throws IOException {
        File file = new File("test.txt");
        // Creating the file
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
        } else {
            System.out.println("File already exists.");
        }
        // Writing content to the file
        FileWriter writer = new FileWriter(file);
        writer.write("This is a typical test case.");
        writer.close();

        FileText fileText = new FileText(file, "UTF-8");
        // Asserting file properties
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertEquals("This is a typical test case.", fileText.getFullText().toString());
        assertArrayEquals(new String[] { "This is a typical test case." }, fileText.toLinesArray());

        // Asserting line and column number
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));
        assertEquals(new LineColumn(1, 4), fileText.lineColumn(4));
        assertEquals(new LineColumn(1, 11), fileText.lineColumn(11));

        // Asserting line content
        assertEquals("This is a typical test case.", fileText.get(0));

        // Asserting file size
        assertEquals(1, fileText.size());

        // Deleting the file
        if (file.delete()) {
            System.out.println("File deleted: " + file.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    @Test
    // Test edge case - empty file
    public void testFileTextEmptyFile() throws IOException {
        File file = new File("empty.txt");
        // Creating the file
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
        } else {
            System.out.println("File already exists.");
        }

        FileText fileText = new FileText(file, "UTF-8");
        // Asserting file properties
        assertEquals(file, fileText.getFile());
        assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
        assertEquals("", fileText.getFullText().toString());
        assertArrayEquals(new String[] { "" }, fileText.toLinesArray());

        // Asserting line and column number
        assertEquals(new LineColumn(1, 0), fileText.lineColumn(0));

        // Asserting line content
        assertEquals("", fileText.get(0));

        // Asserting file size
        assertEquals(1, fileText.size());

        // Deleting the file
        if (file.delete()) {
            System.out.println("File deleted: " + file.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    @Test(expected = FileNotFoundException.class)
    // Test error scenario - file not found
    public void testFileTextFileNotFound() throws IOException {
        File file = new File("missing.txt");
        // Asserting that the file does not exist
        assertFalse(file.exists());

        FileText fileText = new FileText(file, "UTF-8");
    }
}