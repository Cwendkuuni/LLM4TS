import org.junit.Assert;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalCase() {
        // Typical case: sequence with characters less than '\u0080'
        CharSequence sequence = "hello";
        int expected = 5;
        int actual = Utf8.encodedLength(sequence);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_edgeCase() {
        // Edge case: empty sequence
        CharSequence sequence = "";
        int expected = 0;
        int actual = Utf8.encodedLength(sequence);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_errorCase() {
        // Error case: UTF-8 length does not fit in int
        CharSequence sequence = "A" + new String(new char[Integer.MAX_VALUE]);
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed_typicalCase() {
        // Typical case: well-formed byte array
        byte[] bytes = new byte[]{65, 66, 67};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_edgeCase() {
        // Edge case: empty byte array
        byte[] bytes = new byte[]{};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_errorCase() {
        // Error case: byte array with invalid bytes
        byte[] bytes = new byte[]{65, (byte) 128, 67};
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        Assert.assertEquals(expected, actual);
    }

    // Add more test cases for other methods if necessary

}