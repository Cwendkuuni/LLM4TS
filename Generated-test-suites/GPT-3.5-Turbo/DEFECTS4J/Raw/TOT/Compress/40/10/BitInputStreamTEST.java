import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] data = {0x12, 0x34, 0x56, 0x78};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_typicalCase() throws IOException {
        long result = bitInputStream.readBits(16);
        assertEquals(0x1234, result);
    }

    @Test
    public void testReadBits_boundaryCase() throws IOException {
        long result = bitInputStream.readBits(31);
        assertEquals(0x123456, result);
    }

    @Test
    public void testReadBits_errorCase() throws IOException {
        long result = bitInputStream.readBits(64);
        assertEquals(-1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_exceptionCase() throws IOException {
        bitInputStream.readBits(-10);
    }

}