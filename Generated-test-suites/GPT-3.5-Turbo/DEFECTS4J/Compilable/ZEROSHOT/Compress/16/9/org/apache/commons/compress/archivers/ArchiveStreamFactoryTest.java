package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        InputStream inputStream = new FileInputStream("testFile.zip");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", inputStream);
        assertNotNull(archiveInputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws Exception {
        InputStream inputStream = new FileInputStream("testFile.zip");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(null, inputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws Exception {
        InputStream inputStream = new FileInputStream("testFile.zip");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("unknown", inputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        OutputStream outputStream = new FileOutputStream("testOutput.zip");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("zip", outputStream);
        assertNotNull(archiveOutputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws Exception {
        OutputStream outputStream = new FileOutputStream("testOutput.zip");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(null, outputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("zip", null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws Exception {
        OutputStream outputStream = new FileOutputStream("testOutput.zip");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("unknown", outputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws Exception {
        InputStream inputStream = new FileInputStream("testFile.zip");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutoDetectWithNullInputStream() throws Exception {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamAutoDetectWithUnsupportedStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectWithUnknownSignature() throws Exception {
        InputStream inputStream = new FileInputStream("testFile.invalid");
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
    }
}