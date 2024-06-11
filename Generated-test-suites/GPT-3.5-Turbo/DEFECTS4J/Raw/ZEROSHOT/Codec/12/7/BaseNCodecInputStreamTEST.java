import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream inputStream;
    private BaseNCodec baseNCodec;
    private InputStream in;

    @Test
    public void testRead() throws Exception {
        // setup
        in = mock(InputStream.class);
        baseNCodec = mock(BaseNCodec.class);
        when(baseNCodec.hasData()).thenReturn(false);
        when(in.read()).thenReturn(65);
        inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        // execute
        int result = inputStream.read();

        // assert
        assertEquals(65, result);
        verify(in, times(1)).read();
    }

    @Test
    public void testReadWithNegativeValue() throws Exception {
        // setup
        in = mock(InputStream.class);
        baseNCodec = mock(BaseNCodec.class);
        when(baseNCodec.hasData()).thenReturn(false);
        when(in.read()).thenReturn(-1);
        inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        // execute
        int result = inputStream.read();

        // assert
        assertEquals(-1, result);
        verify(in, times(1)).read();
    }

    @Test
    public void testReadArray() throws Exception {
        // setup
        in = mock(InputStream.class);
        baseNCodec = mock(BaseNCodec.class);
        byte[] array = new byte[]{65, 66, 67};
        when(baseNCodec.hasData()).thenReturn(false);
        when(in.read(array)).thenReturn(3);
        inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        // execute
        int result = inputStream.read(array);

        // assert
        assertEquals(3, result);
        verify(in, times(1)).read(array);
    }

    @Test
    public void testReadArrayWithOffsetAndLength() throws Exception {
        // setup
        in = mock(InputStream.class);
        baseNCodec = mock(BaseNCodec.class);
        byte[] array = new byte[]{65, 66, 67};
        when(baseNCodec.hasData()).thenReturn(false);
        when(in.read(array, 1, 2)).thenReturn(2);
        inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        // execute
        int result = inputStream.read(array, 1, 2);

        // assert
        assertEquals(2, result);
        verify(in, times(1)).read(array, 1, 2);
    }

    @Test
    public void testMarkSupported() {
        // setup
        in = mock(InputStream.class);
        baseNCodec = mock(BaseNCodec.class);
        inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        // execute
        boolean result = inputStream.markSupported();

        // assert
        assertEquals(false, result);
    }

}