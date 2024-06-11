import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {
    
    private String inputString;
    private byte[] inputData;
    private ByteArrayInputStream inputStream;
    private DeflateCompressorInputStream deflateCompressorInputStream;
    
    @Before
    public void setUp() {
        inputString = "This is a sample input string";
        inputData = inputString.getBytes();
        inputStream = new ByteArrayInputStream(inputData);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        deflateCompressorInputStream.close();
    }
    
    @Test
    public void testRead() throws IOException {
        int character;
        StringBuilder outputString = new StringBuilder();
        
        while ((character = deflateCompressorInputStream.read()) != -1) {
            outputString.append((char) character);
        }
        
        assertEquals(inputString, outputString.toString());
    }
    
    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] outputData = new byte[inputData.length];
        
        int bytesRead = deflateCompressorInputStream.read(outputData, 0, outputData.length);
        assertEquals(inputData.length, bytesRead);
        
        assertArrayEquals(inputData, outputData);
    }
    
    @Test
    public void testSkip() throws IOException {
        long totalSkipped = deflateCompressorInputStream.skip(5);
        
        assertEquals(5, totalSkipped);
    }
    
    @Test
    public void testAvailable() throws IOException {
        int available = deflateCompressorInputStream.available();
        
        assertTrue(available > 0);
    }
    
    @Test
    public void testClose() throws IOException {
        deflateCompressorInputStream.close();
        
        assertTrue(inputStream.read() == -1);
    }
    
    @Test
    public void testDeflateParameters() throws IOException {
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStreamWithParameters = 
            new DeflateCompressorInputStream(inputStream, parameters);
        
        assertNotNull(deflateCompressorInputStreamWithParameters);
        
        deflateCompressorInputStreamWithParameters.close();
    }
    
    @Test
    public void testSignatureMatching() {
        byte[] signature = new byte[] { (byte) 0x78, (byte) 0x9c };
        
        boolean isZlibHeader = deflateCompressorInputStream.checkSignature(signature, 2);
        
        assertTrue(isZlibHeader);
    }
}