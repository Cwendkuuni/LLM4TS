import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorException;

public class CompressorStreamFactoryTest {

    CompressorStreamFactory factory;
    
    @Before
    public void setUp() {
        factory = new CompressorStreamFactory();
    }
    
    @Test
    public void testCreateCompressorInputStream_BZip2_Success() throws IOException, CompressorException {
        byte[] input = "This is a test string".getBytes();
        byte[] compressedResult = compressWithBZip2(input);
        
        ByteArrayInputStream in = new ByteArrayInputStream(compressedResult);
        CompressorInputStream inputStream = factory.createCompressorInputStream("bzip2", in);
        
        byte[] decompressedResult = readFully(inputStream);
        inputStream.close();
        in.close();
        
        assertArrayEquals(input, decompressedResult);
    }
    
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownCompressor_ExceptionThrown() throws IOException, CompressorException {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        
        factory.createCompressorInputStream("unknown", in);
    }
    
    private byte[] compressWithBZip2(byte[] input) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("bzip2", out);
        
        compressorOutputStream.write(input);
        compressorOutputStream.finish();
        compressorOutputStream.close();
        
        return out.toByteArray();
    }
    
    private byte[] readFully(CompressorInputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        return out.toByteArray();
    }
}