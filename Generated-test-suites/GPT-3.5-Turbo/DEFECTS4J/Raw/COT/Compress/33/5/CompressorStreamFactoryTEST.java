
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_BZip2() throws IOException {
        // Arrange
        CompressorStreamFactory factory = new CompressorStreamFactory();
        String inputString = "Hello, world!";
        InputStream input = new ByteArrayInputStream(inputString.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutput = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        IOUtils.copy(input, compressorOutput);
        compressorOutput.close();
        byte[] compressedData = output.toByteArray();

        // Act
        CompressorInputStream compressorInput = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, new ByteArrayInputStream(compressedData));
        ByteArrayOutputStream decompressedOutput = new ByteArrayOutputStream();
        IOUtils.copy(compressorInput, decompressedOutput);
        compressorInput.close();

        // Assert
        String decompressedString = new String(decompressedOutput.toByteArray());
        Assert.assertEquals(inputString, decompressedString);
    }

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException {
        // Arrange
        CompressorStreamFactory factory = new CompressorStreamFactory();
        String inputString = "Hello, world!";
        InputStream input = new ByteArrayInputStream(inputString.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutput = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        IOUtils.copy(input, compressorOutput);
        compressorOutput.close();
        byte[] compressedData = output.toByteArray();

        // Act
        CompressorInputStream compressorInput = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, new ByteArrayInputStream(compressedData));
        ByteArrayOutputStream decompressedOutput = new ByteArrayOutputStream();
        IOUtils.copy(compressorInput, decompressedOutput);
        compressorInput.close();

        // Assert
        String decompressedString = new String(decompressedOutput.toByteArray());
        Assert.assertEquals(inputString, decompressedString);
    }

    // Test cases for other methods...

}

