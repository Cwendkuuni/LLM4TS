package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream));
        } catch (ArchiveException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream));
        } catch (ArchiveException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
            fail("Expected IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            // test passed
        } catch (ArchiveException e) {
            fail("Unexpected exception type");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException for null archiver name");
        } catch (IllegalArgumentException e) {
            // test passed
        } catch (ArchiveException e) {
            fail("Unexpected exception type");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException for null stream");
        } catch (IllegalArgumentException e) {
            // test passed
        } catch (ArchiveException e) {
            fail("Unexpected exception type");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException for null stream");
        } catch (IllegalArgumentException e) {
            // test passed
        } catch (ArchiveException e) {
            fail("Unexpected exception type");
        }
    }

    @Test
    public void testCreateArchiveInputStreamInvalidArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException for invalid archiver name");
        } catch (ArchiveException e) {
            // test passed
        }
    }

    @Test
    public void testCreateArchiveOutputStreamInvalidArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream("invalid", new ByteArrayOutputStream());
            fail("Expected ArchiveException for invalid archiver name");
        } catch (ArchiveException e) {
            // test passed
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[4]);
            assertNotNull(factory.createArchiveInputStream(inputStream));
        } catch (ArchiveException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithNullStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException for null stream");
        } catch (IllegalArgumentException e) {
            // test passed
        } catch (ArchiveException e) {
            fail("Unexpected exception type");
        }
    }
}