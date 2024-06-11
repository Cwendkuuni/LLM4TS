package org.apache.commons.compress.archivers;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            byte[] data = new byte[100];
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertNotNull(ais);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertNotNull(aos);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            byte[] data = new byte[100];
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ArchiveInputStream ais = factory.createArchiveInputStream(null, in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = factory.createArchiveOutputStream(null, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Wrong exception thrown: " + e.getMessage());
        }
    }

    // Add more specific test cases for edge cases and exceptions here

}