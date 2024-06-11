package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Typical Use Case: AR
        InputStream testARInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream arInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, testARInputStream);
        assertNotNull(arInputStream);
        
        // Typical Use Case: ZIP
        InputStream testZIPInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream zipInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, testZIPInputStream);
        assertNotNull(zipInputStream);
        
        // Typical Use Case: TAR
        InputStream testTARInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream tarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testTARInputStream);
        assertNotNull(tarInputStream);
        
        // Typical Use Case: JAR
        InputStream testJARInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream jarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, testJARInputStream);
        assertNotNull(jarInputStream);
        
        // Typical Use Case: CPIO
        InputStream testCPIOInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream cpioInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, testCPIOInputStream);
        assertNotNull(cpioInputStream);
        
        // Typical Use Case: DUMP
        InputStream testDUMPInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream dumpInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, testDUMPInputStream);
        assertNotNull(dumpInputStream);
        
        // Error Scenario: Unknown Archiver Name
        try {
            InputStream testInvalidInputStream = new ByteArrayInputStream(new byte[10]);
            ArchiveInputStream invalidInputStream = new ArchiveStreamFactory().createArchiveInputStream("unknown", testInvalidInputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertTrue(e.getMessage().contains("not found"));
        }
        
        // Error Scenario: Null Archiver Name
        try {
            InputStream testNullArchiverNameInputStream = new ByteArrayInputStream(new byte[10]);
            ArchiveInputStream nullArchiverNameInputStream = new ArchiveStreamFactory().createArchiveInputStream(null, testNullArchiverNameInputStream);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("not be null"));
        }
        
        // Error Scenario: Null InputStream
        try {
            ArchiveInputStream nullInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("InputStream must not be null"));
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Typical Use Case: AR
        OutputStream testAROutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, testAROutputStream);
        assertNotNull(arOutputStream);
        
        // Typical Use Case: ZIP
        OutputStream testZIPOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, testZIPOutputStream);
        assertNotNull(zipOutputStream);
        
        // Typical Use Case: TAR
        OutputStream testTAROutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, testTAROutputStream);
        assertNotNull(tarOutputStream);
        
        // Typical Use Case: JAR
        OutputStream testJAROutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, testJAROutputStream);
        assertNotNull(jarOutputStream);
        
        // Typical Use Case: CPIO
        OutputStream testCPIOOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, testCPIOOutputStream);
        assertNotNull(cpioOutputStream);
        
        // Error Scenario: Unknown Archiver Name
        try {
            OutputStream testInvalidOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream invalidOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("unknown", testInvalidOutputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertTrue(e.getMessage().contains("not found"));
        }
        
        // Error Scenario: Null Archiver Name
        try {
            OutputStream testNullArchiverNameOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream nullArchiverNameOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(null, testNullArchiverNameOutputStream);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("not be null"));
        }
        
        // Error Scenario: Null OutputStream
        try {
            ArchiveOutputStream nullOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("OutputStream must not be null"));
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws Exception {
        // Typical Use Case: ZIP
        InputStream testZIPInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream zipInputStream = new ArchiveStreamFactory().createArchiveInputStream(testZIPInputStream);
        assertNotNull(zipInputStream);
        
        // Typical Use Case: JAR
        InputStream testJARInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream jarInputStream = new ArchiveStreamFactory().createArchiveInputStream(testJARInputStream);
        assertNotNull(jarInputStream);
        
        // Typical Use Case: AR
        InputStream testARInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream arInputStream = new ArchiveStreamFactory().createArchiveInputStream(testARInputStream);
        assertNotNull(arInputStream);
        
        // Typical Use Case: CPIO
        InputStream testCPIOInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream cpioInputStream = new ArchiveStreamFactory().createArchiveInputStream(testCPIOInputStream);
        assertNotNull(cpioInputStream);
        
        // Typical Use Case: DUMP
        InputStream testDUMPInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream dumpInputStream = new ArchiveStreamFactory().createArchiveInputStream(testDUMPInputStream);
        assertNotNull(dumpInputStream);
        
        // Typical Use Case: TAR
        InputStream testTARInputStream = new ByteArrayInputStream(new byte[10]);
        ArchiveInputStream tarInputStream = new ArchiveStreamFactory().createArchiveInputStream(testTARInputStream);
        assertNotNull(tarInputStream);
        
        // Error Scenario: No Archiver Found
        try {
            InputStream testInvalidInputStream = new ByteArrayInputStream(new byte[10]);
            ArchiveInputStream invalidInputStream = new ArchiveStreamFactory().createArchiveInputStream(testInvalidInputStream);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertTrue(e.getMessage().contains("No Archiver found for the stream signature"));
        }
        
        // Error Scenario: Null InputStream
        try {
            ArchiveInputStream nullInputStream = new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Stream must not be null"));
        }
        
        // Error Scenario: InputStream Does Not Support Mark
        try {
            InputStream testMarkUnsupportedInputStream = new FileInputStream("file.txt");
            ArchiveInputStream markUnsupportedInputStream = new ArchiveStreamFactory().createArchiveInputStream(testMarkUnsupportedInputStream);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Mark is not supported"));
        }
    }
}