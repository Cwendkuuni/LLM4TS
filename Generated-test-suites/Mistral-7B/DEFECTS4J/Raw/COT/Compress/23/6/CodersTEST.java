import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.Coder;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

public class CodersTest {

    @Test
    public void testAddDecoder() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        Coder coder = new DeflateDecoder();
        byte[] password = new byte[]{(byte) 0x04, (byte) 0x05, (byte) 0x06};

        InputStream result = Coders.addDecoder(is, coder, password);
        assertNotNull(result);
        assertEquals(DeflateDecoder.class, result.getClass());

        for (SevenZMethod method : SevenZMethod.values()) {
            if (method != SevenZMethod.DEFLATE) {
                Coder wrongCoder = new CopyDecoder();
                assertThrows(IOException.class, () -> Coders.addDecoder(is, wrongCoder, password));
            }
        }
    }

    @Test
    public void testAddEncoder() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        SevenZMethod method = SevenZMethod.DEFLATE;

        Coder coder = new DeflateEncoder();
        byte[] password = new byte[]{(byte) 0x04, (byte) 0x05, (byte) 0x06};

        OutputStream result = Coders.addEncoder(out, method, password);
        assertNotNull(result);
        assertEquals(DeflateEncoder.class, result.getClass());

        for (SevenZMethod wrongMethod : SevenZMethod.values()) {
            if (wrongMethod != method) {
                Coder wrongCoder = new DeflateEncoder();
                assertThrows(IOException.class, () -> Coders.addEncoder(out, wrongMethod, password));
            }
        }
    }

    @Test
    public void testCoderTable() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09});
        Coder coder = new DeflateDecoder();
        byte[] password = new byte[]{(byte) 0x0A, (byte) 0x0B, (byte) 0x0C};

        assertThrows(IOException.class, () -> Coders.addDecoder(is, coder, password));

        for (SevenZMethod method : SevenZMethod.values()) {
            byte[] methodId = method.getId();
            for (CoderId coderId : Coders.coderTable) {
                if (Arrays.equals(methodId, coderId.method.getId())) {
                    InputStream result = Coders.addDecoder(is, coderId.coder, password);
                    assertNotNull(result);
                    assertEquals(coderId.coder.getClass(), result.getClass());
                    break;
                }
            }
        }
    }

    @Test
    public void testLZMAInputStream() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F, (byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0x19, (byte) 0x1A, (byte) 0x1B, (byte) 0x1C, (byte) 0x1D, (byte) 0x1E, (byte) 0x1F, (byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23, (byte) 0x24, (byte) 0x25, (byte) 0x26, (byte) 0x27, (byte) 0x28, (byte) 0x29, (byte) 0x2A, (byte) 0x2B, (byte) 0x2C, (byte) 0x2D, (byte) 0x2E, (byte) 0x2F, (byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x3A, (byte) 0x3B, (byte) 0x3C, (byte) 0x3D, (byte) 0x3E, (byte) 0x3F});
        Coder coder = new LZMADecoder();
        byte[] password = new byte[0];

        InputStream result = Coders.addDecoder(is, coder, password);
        assertNotNull(result);
        assertEquals(LZMAInputStream.class, result.getClass());

        try {
            LZMAInputStream inputStream = new LZMAInputStream(new ByteArrayInputStream(new byte[0]));
            byte[] buffer = new byte[LZMAInputStream.DICT_SIZE_MAX + 1];
            inputStream.read(buffer, 0, buffer.length);
            assertThrows(IOException.class, () -> Coders.addDecoder(new ByteArrayInputStream(new byte[0]), coder, password));
        } catch (IOException e) {
            // Expected
        }
    }
}