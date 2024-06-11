import org.junit.Test;
import static org.junit.Assert.*;

public class XMLEncoderTest {

    @Test
    public void testGetEncoding() {
        XMLEncoder encoder = new XMLEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() {
        // Write test cases for declaration method
    }

    @Test
    public void testText() {
        // Write test cases for text method
    }

    @Test
    public void testWhitespace() {
        // Write test cases for whitespace method
    }

    @Test
    public void testAttribute() {
        // Write test cases for attribute method
    }
}