import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws Exception {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream gzipInputStream = new CompressorStreamFactory().createCompressorInputStream(GZIP, input);
        byte[] expected = new byte[]{(byte) 0x1f, (byte) 0x8b, (byte) 0x08, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] result = new byte[expected.length];
        int length = IOUtils.readFully(gzipInputStream, result);
        assertArrayEquals(expected, result);
        gzipInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws Exception {
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream bzip2InputStream = new CompressorStreamFactory().createCompressorInputStream(BZIP2, input);
        byte[] expected = new byte[15];
        int length = IOUtils.readFully(bzip2InputStream, expected, 0, expected.length);
        assertArrayEquals(BZip2CompressorInputStream.BZIP2_SIGNATURE, expected);
        bzip2InputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_XZ() throws Exception {
        InputStream input = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        CompressorInputStream xzInputStream = new CompressorStreamFactory().createCompressorInputStream(XZ, input);
        byte[] expected = new byte[8];
        int length = IOUtils.readFully(xzInputStream, expected, 0, expected.length);
        assertArrayEquals(XZCompressorInputStream.XZ_SIGNATURE, expected);
        xzInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidFormat() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        new CompressorStreamFactory().createCompressorInputStream("invalidFormat", input);
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutputStream = new CompressorStreamFactory().createCompressorOutputStream(GZIP, output);
        byte[] input = new byte[5];
        gzipOutputStream.write(input, 0, input.length);
        gzipOutputStream.close();
        byte[] expected = new byte[1 + input.length + 4];
        System.arraycopy(GzipCompressorOutputStream.GZIP_SIGNATURE, 0, expected, 0, GzipCompressorOutputStream.GZIP_SIGNATURE.length);
        System.arraycopy(input, 0, expected, 1 + GzipCompressorOutputStream.GZIP_SIGNATURE.length, input.length);
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream bzip2OutputStream = new CompressorStreamFactory().createCompressorOutputStream(BZIP2, output);
        byte[] input = new byte[5];
        bzip2OutputStream.write(input, 0, input.length);
        bzip2OutputStream.close();
        byte[] expected = new byte[BZip2CompressorOutputStream.BZIP2_HEADER_SIZE + input.length];
        System.arraycopy(BZip2CompressorOutputStream.BZIP2_SIGNATURE, 0, expected, 0, BZip2CompressorOutputStream.BZIP2_SIGNATURE.length);
        System.arraycopy(input, 0, expected, BZip2CompressorOutputStream.BZIP2_HEADER_SIZE, input.length);
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream xzOutputStream = new CompressorStreamFactory(true).createCompressorOutputStream(XZ, output);
        byte[] input = new byte[5];
        xzOutputStream.write(input, 0, input.length);
        xzOutputStream.finish();
        xzOutputStream.close();
        byte[] expected = new byte[XZCompressorOutputStream.XZ_HEADER_SIZE + input.length];
        System.arraycopy(XZCompressorOutputStream.XZ_SIGNATURE, 0, expected, 0, XZCompressorOutputStream.XZ_SIGNATURE.length);
        System.arraycopy(input, 0, expected, XZCompressorOutputStream.XZ_HEADER_SIZE, input.length);
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expected, result);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_InvalidFormat() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream("invalidFormat", output);
    }
}