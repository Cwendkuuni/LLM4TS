
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expected = "Hello world".getBytes();
        byte[] actual = Base64.base64ToByteArray(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = "Hello world".getBytes();
        String expected = "SGVsbG8gd29ybGQ=";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain() {
        // Test main method by capturing the console output
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        PrintStream savedOut = System.out;
        System.setOut(new PrintStream(outStream));

        String[] args = {};
        Base64.main(args);
        String output = outStream.toString();

        assertEquals("SGVsbG8gd29ybGQ=\nHello world\n", output);

        System.setOut(savedOut);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        String input = "SGVsbG8gd29ybGQ";
        Base64.altBase64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        String input = "SGVsbG8gd29ybGQ";
        Base64.base64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayIllegalCharacter() {
        String input = "SGVsbG8gd29ybG@=";
        Base64.altBase64ToByteArray(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayIllegalCharacter() {
        String input = "SGVsbG8gd29ybG@=";
        Base64.base64ToByteArray(input);
    }

    @Test
    public void testByteArrayToAltBase64EmptyArray() {
        byte[] input = new byte[0];
        String expected = "";
        String actual = Base64.byteArrayToAltBase64(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testByteArrayToBase64EmptyArray() {
        byte[] input = new byte[0];
        String expected = "";
        String actual = Base64.byteArrayToBase64(input);
        assertEquals(expected, actual);
    }

}
