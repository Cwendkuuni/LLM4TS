import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws Exception {

        // Test case 1
        byte[] input1 = "This is a test".getBytes();
        ByteArrayInputStream bais1 = new ByteArrayInputStream(input1);
        CompressorStreamFactory factory1 = new CompressorStreamFactory();
        CompressorInputStream cis1 = factory1.createCompressorInputStream(bais1);
        byte[] output1 = new byte[input1.length];
        cis1.read(output1);
        cis1.close();
        assertArrayEquals(input1, output1);

        // Test case 2
        byte[] input2 = "This is a test compressed with BZIP2".getBytes();
        ByteArrayInputStream bais2 = new ByteArrayInputStream(input2);
        CompressorStreamFactory factory2 = new CompressorStreamFactory();
        CompressorInputStream cis2 = factory2.createCompressorInputStream("bzip2", bais2);
        byte[] output2 = new byte[input2.length];
        cis2.read(output2);
        cis2.close();
        assertArrayEquals(input2, output2);
    }

    @Test
    public void testCreateCompressorOutputStream() throws Exception {

        // Test case 1
        byte[] input1 = "This is a test".getBytes();
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        CompressorStreamFactory factory1 = new CompressorStreamFactory();
        CompressorOutputStream cos1 = factory1.createCompressorOutputStream("gzip", baos1);
        cos1.write(input1);
        cos1.close();
        byte[] output1 = baos1.toByteArray();
        ByteArrayOutputStream baos1Clone = new ByteArrayOutputStream();
        CompressorOutputStream cos1Clone = factory1.createCompressorOutputStream("gzip", baos1Clone);
        cos1Clone.write(input1);
        cos1Clone.close();
        byte[] output1Clone = baos1Clone.toByteArray();
        assertArrayEquals(output1, output1Clone);

        // Test case 2
        byte[] input2 = "This is a test compressed with BZIP2".getBytes();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        CompressorStreamFactory factory2 = new CompressorStreamFactory();
        CompressorOutputStream cos2 = factory2.createCompressorOutputStream("bzip2", baos2);
        cos2.write(input2);
        cos2.close();
        byte[] output2 = baos2.toByteArray();
        ByteArrayOutputStream baos2Clone = new ByteArrayOutputStream();
        CompressorOutputStream cos2Clone = factory2.createCompressorOutputStream("bzip2", baos2Clone);
        cos2Clone.write(input2);
        cos2Clone.close();
        byte[] output2Clone = baos2Clone.toByteArray();
        assertArrayEquals(output2, output2Clone);
    }
}