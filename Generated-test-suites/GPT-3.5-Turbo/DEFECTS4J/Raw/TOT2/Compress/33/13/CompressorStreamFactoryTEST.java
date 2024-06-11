import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException, CompressorException {
        byte[] data = "Test data for Gzip compression.".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        cos.write(data);
        cos.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CompressorInputStream cis = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
        
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = cis.read(buffer)) != -1) {
            resultStream.write(buffer, 0, length);
        }
        cis.close();
        
        assertEquals("Test data for Gzip compression.", new String(resultStream.toByteArray()));
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() throws IOException, CompressorException {
        byte[] data = "Test data for XZ compression.".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.XZ, outputStream);
        cos.write(data);
        cos.close();
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CompressorInputStream cis = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
        
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = cis.read(buffer)) != -1) {
            resultStream.write(buffer, 0, length);
        }
        cis.close();
        
        assertEquals("Test data for XZ compression.", new String(resultStream.toByteArray()));
    }

    @Test
    public void testCreateCompressorInputStream_InvalidType() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            CompressorInputStream cis = new CompressorStreamFactory().createCompressorInputStream("INVALID_TYPE", inputStream);
            fail("Expected CompressorException not thrown");
        } catch (CompressorException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateCompressorOutputStream_InvalidType() {
        ByteArrayOutputStream outputStream =  new ByteArrayOutputStream();
        try {
            CompressorOutputStream cos = new CompressorStreamFactory().createCompressorOutputStream("INVALID_TYPE", outputStream);
            fail("Expected CompressorException not thrown");
        } catch (CompressorException e) {
            // Expected exception
        }
    }

}