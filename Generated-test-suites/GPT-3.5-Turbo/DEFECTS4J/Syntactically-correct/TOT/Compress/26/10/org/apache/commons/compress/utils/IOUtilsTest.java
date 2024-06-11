import org.junit.Test;
import java.io.*;

public class IOUtilsTest {

    //Testing copy() method
    @Test
    public void testCopy() {
        try {
            ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
            String inputStr = "This is a test string";
            InputStream input = new ByteArrayInputStream(inputStr.getBytes());
            IOUtils.copy(input, expectedOutput);

            // Check if the input string is correctly copied to the output stream
            assert expectedOutput.toString().equals(inputStr);
        } catch (IOException e) {
            assert false; // Test failed if an exception was thrown
        }
    }

    //Testing copy() method with custom buffer size
    @Test
    public void testCopyWithBufferSize() {
        try {
            ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
            String inputStr = "This is a test string";
            InputStream input = new ByteArrayInputStream(inputStr.getBytes());
            IOUtils.copy(input, expectedOutput, 100);

            // Check if the input string is correctly copied to the output stream
            assert expectedOutput.toString().equals(inputStr);
        } catch (IOException e) {
            assert false; // Test failed if an exception was thrown
        }
    }

    //Testing skip() method
    @Test
    public void testSkip() {
        try {
            String inputStr = "This is a test string";
            InputStream input = new ByteArrayInputStream(inputStr.getBytes());

            // Skip the first 5 characters
            long skipped = IOUtils.skip(input, 5);

            // Check if the skipped characters count is correct
            assert skipped == 5;

            // Read the remaining string
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(input, output, 100);

            // Check if the remaining string is correct
            assert output.toString().equals("is a test string");
        } catch (IOException e) {
            assert false; // Test failed if an exception was thrown
        }
    }

    //Testing readFully() method
    @Test
    public void testReadFully() {
        try {
            String inputStr = "This is a test string";
            InputStream input = new ByteArrayInputStream(inputStr.getBytes());

            byte[] buffer = new byte[10];

            // Read 10 bytes
            int bytesRead = IOUtils.readFully(input, buffer);

            // Check if the number of bytes read is correct
            assert bytesRead == 10;

            // Check if the read bytes are correct
            assert new String(buffer).equals("This is a ");

            // Read the remaining bytes
            byte[] remainingBytes = new byte[input.available()];
            bytesRead = IOUtils.readFully(input, remainingBytes);

            // Check if the number of bytes read is correct
            assert bytesRead == remainingBytes.length;

            // Check if the remaining bytes are correct
            assert new String(remainingBytes).equals("test string");
        } catch (IOException e) {
            assert false; // Test failed if an exception was thrown
        }
    }

    //Testing toByteArray() method
    @Test
    public void testToByteArray() {
        try {
            String inputStr = "This is a test string";
            InputStream input = new ByteArrayInputStream(inputStr.getBytes());

            // Convert input stream to byte array
            byte[] byteArray = IOUtils.toByteArray(input);

            // Check if the byte array is correct
            assert new String(byteArray).equals(inputStr);
        } catch (IOException e) {
            assert false; // Test failed if an exception was thrown
        }
    }

    //Testing closeQuietly() method
    @Test
    public void testCloseQuietly() {
        // Close a null Closeable
        IOUtils.closeQuietly(null);

        // Create a mock Closeable to test closing
        Closeable mockCloseable = new Closeable() {
            boolean isClosed = false;

            @Override
            public void close() throws IOException {
                isClosed = true;
            }
        };

        // Close the mock Closeable
        IOUtils.closeQuietly(mockCloseable);

        // Check if the close method of the mock Closeable was called
        assert mockCloseable.isClosed;
    }
    
}