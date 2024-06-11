package org.apache.commons.compress.compressors;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;

public class CompressorStreamFactoryTest {
    
    private CompressorStreamFactory factory;
    
    @Before
    public void setUp() {
        factory = new CompressorStreamFactory();
    }
    
    @Test
    public void testCreateCompressorInputStream_BZip2_Success() throws Exception {
        String inputFile = "test.txt";
        String compressedFile = "test.txt.bz2";
        String expectedContent = "Hello World";
        
        // Creating a test file
        FileOutputStream fileStream = new FileOutputStream(inputFile);
        fileStream.write(expectedContent.getBytes());
        fileStream.close();
        
        // Compressing the test file
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(compressedFile);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(factory.BZIP2, outputStream);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            compressorOutputStream.write(buffer, 0, length);
        }
        compressorOutputStream.close();
        inputStream.close();
        outputStream.close();
        
        // Decompressing the compressed file using the tested method
        inputStream = new FileInputStream(compressedFile);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(factory.BZIP2, inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((length = compressorInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        compressorInputStream.close();
        inputStream.close();
        
        // Asserting the decompressed content
        String actualContent = byteArrayOutputStream.toString();
        assertEquals(expectedContent, actualContent);
    }
    
    @Test
    public void testCreateCompressorInputStream_UnknownCompressor_ExceptionThrown() throws Exception {
        InputStream inputStream = new FileInputStream("test.txt");
        
        try {
            factory.createCompressorInputStream("unknown", inputStream);
            fail("Expected CompressorException to be thrown.");
        } catch (CompressorException ex) {
            assertEquals("Compressor: unknown not found.", ex.getMessage());
        }
    }
    
    @Test
    public void testCreateCompressorOutputStream_Deflate_Success() throws Exception {
        String inputFile = "test.txt";
        String compressedFile = "test.txt.deflate";
        String expectedContent = "Hello World";
        
        // Creating a test file
        FileOutputStream fileStream = new FileOutputStream(inputFile);
        fileStream.write(expectedContent.getBytes());
        fileStream.close();
        
        // Compressing the test file using the tested method
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(compressedFile);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(factory.DEFLATE, outputStream);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            compressorOutputStream.write(buffer, 0, length);
        }
        compressorOutputStream.close();
        inputStream.close();
        outputStream.close();
        
        // Decompressing the compressed file
        inputStream = new FileInputStream(compressedFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        inputStream.close();
        
        // Asserting the decompressed content
        String actualContent = byteArrayOutputStream.toString();
        assertEquals(expectedContent, actualContent);
    }
    
    @Test
    public void testCreateCompressorOutputStream_UnknownCompressor_ExceptionThrown() throws Exception {
        OutputStream outputStream = new FileOutputStream("test.txt");
        
        try {
            factory.createCompressorOutputStream("unknown", outputStream);
            fail("Expected CompressorException to be thrown.");
        } catch (CompressorException ex) {
            assertEquals("Compressor: unknown not found.", ex.getMessage());
        }
    }
}