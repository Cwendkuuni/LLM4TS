import org.junit.Test;
import java.io.*;

public class IOUtilsTest {

    @Test
    public void testCopy_InvalidInputs() {
        // Test null input stream
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(null, output);
            assert false;  // Failure if no exception is thrown
        } catch (IOException e) {
            // Expected exception
            assert true;
        }

        // Test null output stream
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            IOUtils.copy(input, null);
            assert false;  // Failure if no exception is thrown
        } catch (IOException e) {
            // Expected exception
            assert true;
        }
    }

    @Test
    public void testCopy_EmptyInput() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{});
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            long count = IOUtils.copy(input, output);
            assert count == 0;
            assert output.toByteArray().length == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testCopy_SmallInput() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            long count = IOUtils.copy(input, output);
            assert count == 3;
            assert output.toByteArray().length == 3;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testSkip_InvalidInputs() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            long skipped = IOUtils.skip(null, 5);
            assert skipped == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }

        try {
            long skipped = IOUtils.skip(input, -1);
            assert skipped == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testSkip_EmptyInput() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{});
            long skipped = IOUtils.skip(input, 5);
            assert skipped == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testSkip_SmallInput() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            long skipped = IOUtils.skip(input, 2);
            assert skipped == 2;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testReadFully_InvalidInputs() {
        // Test null input stream
        try {
            byte[] buffer = new byte[3];
            int bytesRead = IOUtils.readFully(null, buffer);
            assert bytesRead == 0;
            assert buffer[0] == 0 && buffer[1] == 0 && buffer[2] == 0;  // Buffer should not be modified
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }

        // Test negative offset
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            byte[] buffer = new byte[3];
            int bytesRead = IOUtils.readFully(input, buffer, -1, 3);
            assert bytesRead == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }

        // Test offset beyond buffer length
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            byte[] buffer = new byte[3];
            int bytesRead = IOUtils.readFully(input, buffer, 4, 2);
            assert bytesRead == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }

        // Test negative length
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            byte[] buffer = new byte[3];
            int bytesRead = IOUtils.readFully(input, buffer, 0, -1);
            assert bytesRead == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }

        // Test offset + length beyond buffer length
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            byte[] buffer = new byte[3];
            int bytesRead = IOUtils.readFully(input, buffer, 1, 3);
            assert bytesRead == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testReadFully_EmptyInput() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{});
            byte[] buffer = new byte[3];
            int bytesRead = IOUtils.readFully(input, buffer, 0, 3);
            assert bytesRead == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testReadFully_SmallInput() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            byte[] buffer = new byte[3];
            int bytesRead = IOUtils.readFully(input, buffer, 0, 3);
            assert bytesRead == 3;
            assert buffer[0] == 1 && buffer[1] == 2 && buffer[2] == 3;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testToByteArray_InvalidInput() {
        try {
            byte[] byteArray = IOUtils.toByteArray(null);
            assert false;  // Failure if no exception is thrown
        } catch (IOException e) {
            // Expected exception
            assert true;
        }
    }
    

    @Test
    public void testToByteArray_EmptyInput() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{});
            byte[] byteArray = IOUtils.toByteArray(input);
            assert byteArray.length == 0;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testToByteArray_SmallInput() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
            byte[] byteArray = IOUtils.toByteArray(input);
            assert byteArray.length == 3;
            assert byteArray[0] == 1 && byteArray[1] == 2 && byteArray[2] == 3;
        } catch (IOException e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    @Test
    public void testCloseQuietly_NullInput() {
        // Test null closeable
        try {
            IOUtils.closeQuietly(null);
            assert true;  // Success if no exception is thrown
        } catch (Exception e) {
            assert false;  // Failure if an exception is thrown
        }
    }

    // TODO: Additional test methods for closeQuietly() with various closeable implementations

}