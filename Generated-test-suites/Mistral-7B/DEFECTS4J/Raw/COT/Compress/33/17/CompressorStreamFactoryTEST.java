import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_BZip2() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.SIG.clone());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(BZip2CompressorInputStream.TYPE, input);
        Assert.assertTrue(compressorInputStream instanceof BZip2CompressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownType_BZip2() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream("unknownType".getBytes());
        new CompressorStreamFactory().createCompressorInputStream("unknownType", input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_NullInput() {
        new CompressorStreamFactory().createCompressorInputStream(BZip2CompressorInputStream.TYPE, null);
    }

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.SIG.clone());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(GzipCompressorInputStream.TYPE, input);
        Assert.assertTrue(compressorInputStream instanceof GzipCompressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownType_Gzip() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream("unknownType".getBytes());
        new CompressorStreamFactory().createCompressorInputStream("unknownType", input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_NullInput_Gzip() {
        new CompressorStreamFactory().createCompressorInputStream(GzipCompressorInputStream.TYPE, null);
    }

    @Test
    public void testCreateCompressorInputStream_Pack200() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream(Pack200CompressorInputStream.SIG.clone());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(Pack200CompressorInputStream.TYPE, input);
        Assert.assertTrue(compressorInputStream instanceof Pack200CompressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownType_Pack200() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream("unknownType".getBytes());
        new CompressorStreamFactory().createCompressorInputStream("unknownType", input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_NullInput_Pack200() {
        new CompressorStreamFactory().createCompressorInputStream(Pack200CompressorInputStream.TYPE, null);
    }

    @Test
    public void testCreateCompressorInputStream_XZ() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream(XZCompressorInputStream.SIG.clone());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(XZCompressorInputStream.TYPE, input);
        Assert.assertTrue(compressorInputStream instanceof XZCompressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownType_XZ() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream("unknownType".getBytes());
        new CompressorStreamFactory().createCompressorInputStream("unknownType", input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_NullInput_XZ() {
        new CompressorStreamFactory().createCompressorInputStream(XZCompressorInputStream.TYPE, null);
    }

    @Test
    public void testCreateCompressorInputStream_Deflate() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream(DeflateCompressorInputStream.SIG.clone());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(DeflateCompressorInputStream.TYPE, input);
        Assert.assertTrue(compressorInputStream instanceof DeflateCompressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownType_Deflate() throws IOException, CompressorException {
        InputStream input = new ByteArrayInputStream("unknownType".getBytes());
        new CompressorStreamFactory().createCompressorInputStream("unknownType", input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_NullInput_Deflate() {
        new CompressorStreamFactory().createCompressorInputStream(DeflateCompressorInputStream.TYPE, null);
    }

    @Test
    public void testGetDecompressConcatenated_false() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        Assert.assertFalse(factory.getDecompressConcatenated());
    }

    @Test
    public void testGetDecompressConcatenated_true() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        Assert.assertTrue(factory.getDecompressConcatenated());
    }
}