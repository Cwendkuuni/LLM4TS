
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testConstructorWithFileAndCharsetName() {
        // Test case for constructor with File and charsetName
        // Create a temporary file
        File tempFile;
        try {
            tempFile = File.createTempFile("testFile", ".txt");
            String fileContent = "Line 1\nLine 2\nLine 3";
            Files.write(fileContent, tempFile, Charset.forName("UTF-8"));

            FileText fileText = new FileText(tempFile, "UTF-8");

            // Check if the file is set correctly
            assertEquals(tempFile, fileText.getFile());

            // Check if the charset is set correctly
            assertEquals(Charset.forName("UTF-8"), fileText.getCharset());

            // Check if the full text is read correctly
            assertEquals(fileContent, fileText.getFullText().toString());

            // Check if the lines array was created correctly
            String[] expectedLines = {"Line 1", "Line 2", "Line 3"};
            assertArrayEquals(expectedLines, fileText.toLinesArray());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred during test");
        }
    }

    @Test
    public void testConstructorWithFileText() {
        // Test case for constructor with FileText parameter
        // Create a temporary file
        File tempFile;
        try {
            tempFile = File.createTempFile("testFile", ".txt");
            String fileContent = "Line 1\nLine 2\nLine 3";
            Files.write(fileContent, tempFile, Charset.forName("UTF-8"));

            FileText originalFileText = new FileText(tempFile, "UTF-8");

            FileText duplicateFileText = new FileText(originalFileText);

            // Check if the file is set correctly
            assertEquals(tempFile, duplicateFileText.getFile());

            // Check if the charset is set correctly
            assertEquals(Charset.forName("UTF-8"), duplicateFileText.getCharset());

            // Check if the full text is copied correctly
            assertEquals(originalFileText.getFullText().toString(), duplicateFileText.getFullText().toString());

            // Check if the lines array was created correctly
            assertArrayEquals(originalFileText.toLinesArray(), duplicateFileText.toLinesArray());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred during test");
        }
    }

    @Test
    public void testConstructorWithFileAndLines() {
        // Test case for constructor with File and List<String> parameters
        // Create a temporary file
        File tempFile;
        try {
            tempFile = File.createTempFile("testFile", ".txt");
            String fileContent = "Line 1\nLine 2\nLine 3";
            Files.write(fileContent, tempFile, Charset.forName("UTF-8"));

            List<String> fileLines = Arrays.asList("Line 1", "Line 2", "Line 3");

            FileText fileText = FileText.fromLines(tempFile, fileLines);

            // Check if the file is set correctly
            assertEquals(tempFile, fileText.getFile());

            // Check if the charset is null
            assertNull(fileText.getCharset());

            // Check if the full text is built correctly
            assertEquals(fileContent, fileText.getFullText().toString());

            // Check if the lines array was created correctly
            assertArrayEquals(fileLines.toArray(), fileText.toLinesArray());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred during test");
        }
    }

    @Test
    public void testGet() {
        // Test case for get() method
        // Create a temporary file
        File tempFile;
        try {
            tempFile = File.createTempFile("testFile", ".txt");
            String fileContent = "Line 1\nLine 2\nLine 3";
            Files.write(fileContent, tempFile, Charset.forName("UTF-8"));

            FileText fileText = new FileText(tempFile, "UTF-8");

            // Check if the correct line is retrieved
            assertEquals("Line 1", fileText.get(0));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred during test");
        }
    }

    @Test
    public void testSize() {
        // Test case for size() method
        // Create a temporary file
        File tempFile;
        try {
            tempFile = File.createTempFile("testFile", ".txt");
            String fileContent = "Line 1\nLine 2\nLine 3";
            Files.write(fileContent, tempFile, Charset.forName("UTF-8"));

            FileText fileText = new FileText(tempFile, "UTF-8");

            // Check if the correct size is returned
            assertEquals(3, fileText.size());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred during test");
        }
    }

    @Test
    public void testLineColumn() {
        // Test case for lineColumn() method
        // Create a temporary file
        File tempFile;
        try {
            tempFile = File.createTempFile("testFile", ".txt");
            String fileContent = "Line 1\nLine 2\nLine 3";
            Files.write(fileContent, tempFile, Charset.forName("UTF-8"));

            FileText fileText = new FileText(tempFile, "UTF-8");

            // Check if the correct LineColumn object is returned
            assertEquals(new LineColumn(1, 6), fileText.lineColumn(6));
            assertEquals(new LineColumn(2, 6), fileText.lineColumn(13));
            assertEquals(new LineColumn(3, 1), fileText.lineColumn(18));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Exception occurred during test");
        }
    }
}
