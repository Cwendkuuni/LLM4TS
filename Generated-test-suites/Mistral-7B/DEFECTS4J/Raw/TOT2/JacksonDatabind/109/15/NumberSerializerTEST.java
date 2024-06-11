import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class NumberSerializerTest {

    // Test cases from all experts
    @Test
    public void testSerializeInt() throws Exception {
        // Expert 1
    }

    @Test
    public void testSerializeLong() throws Exception {
        // Expert 1
    }

    @Test
    public void testSerializeDouble() throws Exception {
        // Expert 1
    }

    @Test
    public void testSerializeBigDecimal() throws Exception {
        // Expert 1
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        // Expert 1
    }

    @Test
    public void testSerializePositiveNumber() throws Exception {
        // Expert 2
    }

    @Test
    public void testSerializeZero() throws Exception {
        // Expert 2
    }

    @Test
    public void testSerializeNegativeNumber() throws Exception {
        // Expert 2
    }

    @Test
    public void testSerializeNaN() throws Exception {
        // Expert 2
    }

    @Test
    public void testSerializeMinInt() throws Exception {
        // Expert 3
    }

    @Test
    public void testSerializeMaxInt() throws Exception {
        // Expert 3
    }

    @Test
    public void testSerializeMinLong() throws Exception {
        // Expert 3
    }

    @Test
    public void testSerializeMaxLong() throws Exception {
        // Expert 3
    }

    @Test
    public void testSerializeNegativeBigInteger() throws Exception {
        // Expert 3
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNull() throws Exception {
        // Expert 2
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeUnsupportedType() throws Exception {
        // Expert 1
    }
}