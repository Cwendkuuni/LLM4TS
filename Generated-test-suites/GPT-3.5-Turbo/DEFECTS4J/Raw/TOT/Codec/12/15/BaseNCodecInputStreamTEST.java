import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;
    private BaseNCodec baseNCodec;
    private InputStream inputStream;

    @Before
    public void setUp() {
        baseNCodec = mock(BaseNCodec.class);
        inputStream = mock(InputStream.class);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }
    
    @Test
    public void testRead_normalData_returnsCorrectValue() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        when(inputStream.read()).thenReturn(data[0], data[1], data[2], -1);
        assertEquals(1, baseNCodecInputStream.read());
        assertEquals(2, baseNCodecInputStream.read());
        assertEquals(3, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testRead_emptyData_returnsEOF() throws IOException {
        when(inputStream.read()).thenReturn(-1);
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testRead_noDataAvailable_returnsZero() throws IOException {
        when(inputStream.read()).thenReturn(0);
        assertEquals(0, baseNCodecInputStream.read());
    }

    @Test
    public void testRead_zeroLengthBuffer_returnsZero() throws IOException {
        byte[] buffer = new byte[0];
        when(inputStream.read(buffer)).thenReturn(0);
        assertEquals(0, baseNCodecInputStream.read(buffer, 0, 0));
    }

    @Test
    public void testRead_validDataIntoBuffer_returnsCorrectValue() throws IOException {
        byte[] buffer = new byte[3];
        when(inputStream.read(buffer)).thenReturn(3);
        assertEquals(3, baseNCodecInputStream.read(buffer, 0, 3));
    }

    @Test(expected = NullPointerException.class)
    public void testRead_nullBuffer_throwsNullPointerException() throws IOException {
        baseNCodecInputStream.read(null, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_negativeOffset_throwsIndexOutOfBoundsException() throws IOException {
        baseNCodecInputStream.read(new byte[3], -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_negativeLength_throwsIndexOutOfBoundsException() throws IOException {
        baseNCodecInputStream.read(new byte[3], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_offsetGreaterThanBufferLength_throwsIndexOutOfBoundsException() throws IOException {
        baseNCodecInputStream.read(new byte[3], 4, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_offsetPlusLengthGreaterThanBufferLength_throwsIndexOutOfBoundsException() throws IOException {
        baseNCodecInputStream.read(new byte[3], 0, 4);
    }

    @Test
    public void testMarkSupported_alwaysReturnsFalse() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable_beforeEndOfStream_returnsNonZero() throws IOException {
        when(baseNCodec.hasData()).thenReturn(true);
        assertTrue(baseNCodecInputStream.available() > 0);
    }

    @Test
    public void testAvailable_afterEndOfStream_returnsZero() throws IOException {
        when(baseNCodec.hasData()).thenReturn(false);
        assertTrue(baseNCodecInputStream.available() == 0);
    }
}