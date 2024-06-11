import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;
    
    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] data, int off, int length) {

            }

            @Override
            void decode(byte[] data, int off, int length) {

            }

            @Override
            int readResults(byte[] data, int off, int length) {
                return 0;
            }

            @Override
            boolean hasData() {
                return false;
            }
        };

        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        int result = baseNCodecInputStream.read();
        assertEquals(116, result);
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        ByteArrayyInputStream inputStream = new ByteArrayInputStream("".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] data, int off, int length) {

            }

            @Override
            void decode(byte[] data, int off, int length) {

            }

            @Override
            int readResults(byte[] data, int off, int length) {
                return 0;
            }

            @Override
            boolean hasData() {
                return false;
            }
        };

        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        
        int result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithNonBase32Data() throws IOException {
        ByteArrayyInputStream inputStream = new ByteArrayInputStream("ABC".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] data, int off, int length) {

            }

            @Override
            void decode(byte[] data, int off, int length) {

            }

            @Override
            int readResults(byte[] data, int off, int length) {
                return 0;
            }

            @Override
            boolean hasData() {
                return true;
            }
        };

        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.read();
        assertEquals(65, result);
    }

    @Test
    public void testReadWithEOF() throws IOException {
        ByteArrayyInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] data, int off, int length) {

            }

            @Override
            void decode(byte[] data, int off, int length) {

            }

            @Override
            int readResults(byte[] data, int off, int length) {
                return -1;
            }

            @Override
            boolean hasData() {
                return false;
            }
        };

        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.read();
        assertEquals(EOF, result);
    }

    @Test
    public void testReadWithIOException() throws IOException {
        ByteArrayyInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] data, int off, int length) {

            }

            @Override
            void decode(byte[] data, int off, int length) {

            }

            @Override
            int readResults(byte[] data, int off, int length) {
                return 0;
            }

            @Override
            boolean hasData() {
                return true;
            }
        };

        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

}