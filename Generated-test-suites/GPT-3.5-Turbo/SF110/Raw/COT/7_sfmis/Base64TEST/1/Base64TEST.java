import static org.junit.Assert.*;
import org.junit.Test;
import com.hf.sfm.crypt.Base64;

public class Base64Test {

@Test
public void testAltBase64ToByteArray() {
    String s = "SGVsbG8gd29ybGQh";
    byte[] expectedOutput = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
    byte[] output = Base64.altBase64ToByteArray(s);
    assertArrayEquals(expectedOutput, output);
}

@Test
public void testBase64ToByteArray() {
    String s = "SGVsbG8gd29ybGQh";
    byte[] expectedOutput = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
    byte[] output = Base64.base64ToByteArray(s);
    assertArrayEquals(expectedOutput, output);
}

@Test
public void testByteArrayToAltBase64() {
    byte[] b = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
    String expectedOutput = "SGVsbG8gd29ybGQh";
    String output = Base64.byteArrayToAltBase64(b);
    assertEquals(expectedOutput, output);
}

@Test
public void testByteArrayToBase64() {
    byte[] b = {72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33};
    String expectedOutput = "SGVsbG8gd29ybGQh";
    String output = Base64.byteArrayToBase64(b);
    assertEquals(expectedOutput, output);
}

@Test(expected = IllegalArgumentException.class)
public void testAltBase64ToByteArrayWithInvalidInput() {
    String s = "SGVsbG8gd29ybGQ";
    Base64.altBase64ToByteArray(s);
}

@Test(expected = IllegalArgumentException.class)
public void testBase64ToByteArrayWithInvalidInput() {
    String s = "SGVsbG8gd29ybGQ";
    Base64.base64ToByteArray(s);
}

@Test
public void testMain() {
    // Need to test main method separately by checking outputs manually
}

}