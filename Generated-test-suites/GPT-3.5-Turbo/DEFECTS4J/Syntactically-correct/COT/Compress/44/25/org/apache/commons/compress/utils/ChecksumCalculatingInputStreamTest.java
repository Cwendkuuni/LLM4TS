import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream stream;
    private Checksum checksum;
    private InputStream in;

    @Before
    public void setUp() {
        checksum = mock(Checksum.class);
        in = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        stream = new ChecksumCalculatingInputStream(checksum, in);
    }

    @Test
    public void testRead() throws IOException {
        when(in.read()).thenReturn(1);
        assertEquals(1, stream.read());
        verify(checksum).update(1);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        when(in.read(buffer)).thenReturn(3);
        assertEquals(3, stream.read(buffer));
        verify(checksum).update(buffer, 0, 3);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        when(in.read(buffer, 1, 4)).thenReturn(4);
        assertEquals(4, stream.read(buffer, 1, 4));
        verify(checksum).update(buffer, 1, 4);
    }

    @Test
    public void testSkip() throws IOException {
        when(in.read()).thenReturn(-1);
        assertEquals(0, stream.skip(10));
    }

    @Test
    public void testGetValue() {
        when(checksum.getValue()).thenReturn(1234L);
        assertEquals(1234L, stream.getValue());
    }
}