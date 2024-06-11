import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = new byte[] {72, 101, 108, 108, 111};
        byte[] result = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAltBase64ToByteArrayEdgeCases1() {
        String input = "aG"; // Invalid length
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArrayEdgeCases2() {
        String input = "SG==="; // Invalid padding
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testAltBase64ToByteArrayEdgeCases3() {
        String input = "@#$%"; // Invalid characters
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(input));
    }

}