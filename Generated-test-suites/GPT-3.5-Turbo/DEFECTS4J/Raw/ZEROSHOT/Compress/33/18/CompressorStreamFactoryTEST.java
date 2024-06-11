import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test BZIP2
        CompressorInputStream input1 = factory.createCompressorInputStream("bzip2", null);
        assertNotNull(input1);

        // Test GZIP
        CompressorInputStream input2 = factory.createCompressorInputStream("gz", null);
        assertNotNull(input2);

        // Test PACK200
        CompressorInputStream input3 = factory.createCompressorInputStream("pack200", null);
        assertNotNull(input3);

        // Test XZ
        CompressorInputStream input4 = factory.createCompressorInputStream("xz", null);
        assertNotNull(input4);

        // Test LZMA
        CompressorInputStream input5 = factory.createCompressorInputStream("lzma", null);
        assertNotNull(input5);

        // Test SNAPPY_FRAMED
        CompressorInputStream input6 = factory.createCompressorInputStream("snappy-framed", null);
        assertNotNull(input6);

        // Test SNAPPY_RAW
        CompressorInputStream input7 = factory.createCompressorInputStream("snappy-raw", null);
        assertNotNull(input7);

        // Test Z
        CompressorInputStream input8 = factory.createCompressorInputStream("z", null);
        assertNotNull(input8);

        // Test DEFLATE
        CompressorInputStream input9 = factory.createCompressorInputStream("deflate", null);
        assertNotNull(input9);
    }

    @Test
    public void testCreateCompressorOutputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test GZIP
        CompressorOutputStream output1 = factory.createCompressorOutputStream("gz", null);
        assertNotNull(output1);

        // Test BZIP2
        CompressorOutputStream output2 = factory.createCompressorOutputStream("bzip2", null);
        assertNotNull(output2);

        // Test XZ
        CompressorOutputStream output3 = factory.createCompressorOutputStream("xz", null);
        assertNotNull(output3);

        // Test PACK200
        CompressorOutputStream output4 = factory.createCompressorOutputStream("pack200", null);
        assertNotNull(output4);

        // Test DEFLATE
        CompressorOutputStream output5 = factory.createCompressorOutputStream("deflate", null);
        assertNotNull(output5);
        
    }
}