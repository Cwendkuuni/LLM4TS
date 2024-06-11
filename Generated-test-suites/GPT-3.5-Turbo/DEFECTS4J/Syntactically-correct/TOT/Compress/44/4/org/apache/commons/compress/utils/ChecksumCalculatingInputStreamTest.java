import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static org.mockito.Mockito.*;

public class ChecksumCalculatingInputStreamTest {

    private static final byte[] DATA = "test data".getBytes();

    private ChecksumCalculatingInputStream stream;
    private InputStream mockInputStream;
    private Checksum mockChecksum;

    @Before
    public void setUp() throws Exception {
        mockInputStream = Mockito.mock(InputStream.class);
        mockChecksum = Mockito.mock(Checksum.class);
        when(mockChecksum.getValue()).thenReturn(12345L);

        stream = new ChecksumCalculatingInputStream(mockChecksum, mockInputStream);
    }

    @After
    public void tearDown() throws Exception {
        stream.close();
    }

    @Test
    public void testRead_singleByte() throws Exception {
        when(mockInputStream.read()).thenReturn(65); // ASCII value of 'A'

        int result = stream.read();

        Assert.assertEquals(65, result);
        verify(mockChecksum, times(1)).update(65);
    }

    @Test
    public void testRead_byteArray() throws Exception {
        byte[] buffer = new byte[4];
        when(mockInputStream.read(buffer)).thenReturn(buffer.length);

        int result = stream.read(buffer);

        Assert.assertEquals(buffer.length, result);
        verify(mockChecksum, times(buffer.length)).update(anyByte());
    }

    @Test
    public void testRead_byteArrayWithOffsetAndLength() throws Exception {
        byte[] buffer = new byte[4];
        int offset = 1;
        int length = 2;
        when(mockInputStream.read(buffer, offset, length)).thenReturn(length);

        int result = stream.read(buffer, offset, length);

        Assert.assertEquals(length, result);
        verify(mockChecksum, times(length)).update(anyByte());
    }

    @Test
    public void testSkip() throws Exception {
        when(mockInputStream.read()).thenReturn(65);

        long result = stream.skip(5);

        Assert.assertEquals(1, result);
        verify(mockChecksum, times(5)).update(65);
    }

    @Test
    public void testGetValue() throws Exception {
        long expectedResult = 12345L;
        long result = stream.getValue();

        Assert.assertEquals(expectedResult, result);
        verify(mockChecksum, times(1)).getValue();
    }

    @Test(expected = IOException.class)
    public void testRead_throwsIOException() throws Exception {
        when(mockInputStream.read()).thenThrow(new IOException());

        stream.read();
    }

    @Test(expected = IOException.class)
    public void testRead_byteArray_throwsIOException() throws Exception {
        when(mockInputStream.read(any(byte[].class))).thenThrow(new IOException());

        stream.read(new byte[4]);
    }

    @Test(expected = IOException.class)
    public void testRead_byteArrayWithOffsetAndLength_throwsIOException() throws Exception {
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException());

        stream.read(new byte[4], 1, 2);
    }

    @Test
    public void testSkip_returnsZero() throws Exception {
        long result = stream.skip(0);

        Assert.assertEquals(0, result);
        verify(mockChecksum, never()).update(anyByte());
    }
}