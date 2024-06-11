import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CodersTest {
    
    @Mock
    private InputStream inputStreamMock;
    
    @Mock
    private OutputStream outputStreamMock;
    
    @Test
    public void testAddDecoder_success() throws Exception {
        byte[] password = new byte[10];
        Coder coder = new Coder();
        InputStream expectedStream = mock(InputStream.class);
        
        when(inputStreamMock.read()).thenReturn(0).thenReturn(-1);
        Coders.CoderId coderId = new Coders.CoderId(SevenZMethod.LZMA, mock(Coders.CoderBase.class));
        Coders.coderTable = new Coders.CoderId[] {coderId};
        
        InputStream result = Coders.addDecoder(inputStreamMock, coder, password);
        
        Assert.assertEquals(expectedStream, result);
        verify(coderId.coder).decode(inputStreamMock, coder, password);
    }
    
    @Test(expected = IOException.class)
    public void testAddDecoder_unsupportedCompressionMethod() throws Exception {
        byte[] password = new byte[10];
        Coder coder = new Coder();
        
        when(inputStreamMock.read()).thenReturn(0).thenReturn(-1);
        Coders.CoderId coderId = new Coders.CoderId(SevenZMethod.BZIP2, mock(Coders.CoderBase.class));
        Coders.coderTable = new Coders.CoderId[] {coderId};
        
        Coders.addDecoder(inputStreamMock, coder, password);
    }
    
    @Test
    public void testAddEncoder_success() throws Exception {
        byte[] password = new byte[10];
        SevenZMethod method = SevenZMethod.LZMA;
        OutputStream expectedStream = mock(OutputStream.class);
        
        Coders.CoderId coderId = new Coders.CoderId(SevenZMethod.LZMA, mock(Coders.CoderBase.class));
        Coders.coderTable = new Coders.CoderId[] {coderId};
        
        OutputStream result = Coders.addEncoder(outputStreamMock, method, password);
        
        Assert.assertEquals(expectedStream, result);
        verify(coderId.coder).encode(outputStreamMock, password);
    }
    
    @Test(expected = IOException.class)
    public void testAddEncoder_unsupportedCompressionMethod() throws Exception {
        byte[] password = new byte[10];
        SevenZMethod method = SevenZMethod.BZIP2;
        
        Coders.CoderId coderId = new Coders.CoderId(SevenZMethod.LZMA, mock(Coders.CoderBase.class));
        Coders.coderTable = new Coders.CoderId[] {coderId};
        
        Coders.addEncoder(outputStreamMock, method, password);
    }
    
    @Test
    public void testCopyDecoder_decode() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        Coder coder = new Coder();
        
        InputStream result = new Coders.CopyDecoder().decode(inputStream, coder, null);
        
        Assert.assertEquals(inputStream, result);
    }
    
    @Test
    public void testCopyDecoder_encode() throws Exception {
        OutputStream outputStream = mock(OutputStream.class);
        byte[] password = new byte[10];
        
        OutputStream result = new Coders.CopyDecoder().encode(outputStream, password);
        
        Assert.assertEquals(outputStream, result);
    }
    
    @Test
    public void testLZMADecoder_decode() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        Coder coder = new Coder();
        coder.properties = new byte[5];
        
        InputStream result = new Coders.LZMADecoder().decode(inputStream, coder, null);
        
        //Verify that the result is an instance of LZMAInputStream
        Assert.assertTrue(result instanceof LZMAInputStream);
    }
    
    @Test(expected = IOException.class)
    public void testLZMADecoder_decode_dictSizeExceedMax() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        Coder coder = new Coder();
        coder.properties = new byte[5];
        coder.properties[0] = (byte) 0xFF;
        coder.properties[1] = (byte) 0xFF;
        coder.properties[2] = (byte) 0xFF;
        coder.properties[3] = (byte) 0xFF;
        coder.properties[4] = (byte) 0xFF;
        
        new Coders.LZMADecoder().decode(inputStream, coder, null);
    }
    
    @Test
    public void testDeflateDecoder_decode() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        Coder coder = new Coder();
        
        InputStream result = new Coders.DeflateDecoder().decode(inputStream, coder, null);
        
        //Verify that the result is an instance of InflaterInputStream
        Assert.assertTrue(result instanceof InflaterInputStream);
    }
    
    @Test
    public void testDeflateDecoder_encode() throws Exception {
        OutputStream outputStream = mock(OutputStream.class);
        byte[] password = new byte[10];
        
        OutputStream result = new Coders.DeflateDecoder().encode(outputStream, password);
        
        //Verify that the result is an instance of DeflaterOutputStream
        Assert.assertTrue(result instanceof DeflaterOutputStream);
    }
    
    @Test
    public void testBZIP2Decoder_decode() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        Coder coder = new Coder();
        
        InputStream result = new Coders.BZIP2Decoder().decode(inputStream, coder, null);
        
        //Verify that the result is an instance of BZip2CompressorInputStream
        Assert.assertTrue(result instanceof BZip2CompressorInputStream);
    }
    
    @Test
    public void testBZIP2Decoder_encode() throws Exception {
        OutputStream outputStream = mock(OutputStream.class);
        byte[] password = new byte[10];
        
        OutputStream result = new Coders.BZIP2Decoder().encode(outputStream, password);
        
        //Verify that the result is an instance of BZip2CompressorOutputStream
        Assert.assertTrue(result instanceof BZip2CompressorOutputStream);
    }
    
    @Test
    public void testAES256SHA256Decoder_decode() throws Exception {
        InputStream inputStream = mock(InputStream.class);
        Coder coder = new Coder();
        
        InputStream result = new Coders.AES256SHA256Decoder().decode(inputStream, coder, null);
        
        //Verify that the result is an instance of InputStream
        Assert.assertTrue(result instanceof InputStream);
    }
}