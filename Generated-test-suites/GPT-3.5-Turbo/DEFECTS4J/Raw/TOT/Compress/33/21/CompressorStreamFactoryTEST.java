import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamWithBzip2() {
        try {
            // Create test file
            String testFile = "test.txt";
            String compressedFile = "test.txt.bz2";
            String content = "This is a test file.";

            // Write content to test file
            FileWriter writer = new FileWriter(testFile);
            writer.write(content);
            writer.close();

            // Compress test file using bzip2
            ProcessBuilder pb = new ProcessBuilder("bzip2", testFile);
            pb.start().waitFor();

            // Create CompressorStreamFactory instance
            CompressorStreamFactory factory = new CompressorStreamFactory();

            // Create CompressorInputStream with bzip2
            FileInputStream fileIn = new FileInputStream(compressedFile);
            CompressorInputStream cis = factory.createCompressorInputStream("bzip2", fileIn);

            // Read content from CompressorInputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = cis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.close();

            // Verify content
            assertEquals(content, baos.toString());

            // Cleanup created files
            File file = new File(testFile);
            file.delete();
            file = new File(compressedFile);
            file.delete();

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStreamWithInvalidName() {
        try {
            // Create CompressorStreamFactory instance
            CompressorStreamFactory factory = new CompressorStreamFactory();

            // Create CompressorInputStream with invalid name
            FileInputStream fileIn = new FileInputStream("test.txt");
            CompressorInputStream cis = factory.createCompressorInputStream("invalid", fileIn);

            // Verify that an exception is thrown
            fail("Expected CompressorException to be thrown");
        } catch (Exception e) {
            // Verify that CompressorException is thrown
            assertTrue(e instanceof CompressorException);
        }
    }

    @Test
    public void testCreateCompressorOutputStreamWithGzip() {
        try {
            // Create test file
            String testFile = "test.txt";
            String compressedFile = "test.txt.gz";
            String content = "This is a test file.";

            // Write content to test file
            FileWriter writer = new FileWriter(testFile);
            writer.write(content);
            writer.close();

            // Create CompressorStreamFactory instance
            CompressorStreamFactory factory = new CompressorStreamFactory();

            // Create CompressorOutputStream with gzip
            FileOutputStream fileOut = new FileOutputStream(compressedFile);
            CompressorOutputStream cos = factory.createCompressorOutputStream("gzip", fileOut);

            // Write content to CompressorOutputStream
            cos.write(content.getBytes());
            cos.close();

            // Verify that compressed file is created
            File file = new File(compressedFile);
            assertTrue(file.exists());

            // Cleanup created files
            file = new File(testFile);
            file.delete();
            file = new File(compressedFile);
            file.delete();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStreamWithInvalidName() {
        try {
            // Create CompressorStreamFactory instance
            CompressorStreamFactory factory = new CompressorStreamFactory();

            // Create CompressorOutputStream with invalid name
            FileOutputStream fileOut = new FileOutputStream("test.txt");
            CompressorOutputStream cos = factory.createCompressorOutputStream("invalid", fileOut);

            // Verify that an exception is thrown
            fail("Expected CompressorException to be thrown");
        } catch (Exception e) {
            // Verify that CompressorException is thrown
            assertTrue(e instanceof CompressorException);
        }
    }

}