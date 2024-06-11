package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Tests for the ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating an archive input stream from an archiver name and an input stream.
     * @throws IOException if the test fails
     */
    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertEquals("AR Archive InputStream", arArchiveInputStream.getClass().getName());
        assertNull("No next entry", arArchiveInputStream.getNextEntry());
        arArchiveInputStream.close();

        // Test ARJ
        InputStream arjInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arjArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, arjInputStream);
        assertEquals("ARJ Archive Input Stream", arjArchiveInputStream.getClass().getName());
        assertNull("No next entry", arjArchiveInputStream.getNextEntry());
        arjArchiveInputStream.close();

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertEquals("CPIO Archive Input Stream", cpioArchiveInputStream.getClass().getName());
        assertNull("No next entry", cpioArchiveInputStream.getNextEntry());
        cpioArchiveInputStream.close();

        // Test DUMP
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertEquals("Dump Archive Input Stream", dumpArchiveInputStream.getClass().getName());
        assertNull("No next entry", dumpArchiveInputStream.getNextEntry());
        dumpArchiveInputStream.close();

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertEquals("JAR Archive Input Stream", jarArchiveInputStream.getClass().getName());
        assertNull("No next entry", jarArchiveInputStream.getNextEntry());
        jarArchiveInputStream.close();

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertEquals("TAR Archive Input Stream", tarArchiveInputStream.getClass().getName());
        assertNull("No next entry", tarArchiveInputStream.getNextEntry());
        tarArchiveInputStream.close();

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertEquals("ZIP Archive Input Stream", zipArchiveInputStream.getClass().getName());
        assertNull("No next entry", zipArchiveInputStream.getNextEntry());
        zipArchiveInputStream.close();

        // Test ZIP with entry encoding
        InputStream zipInputStreamEnc = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipArchiveInputStreamEnc = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStreamEnc, "UTF-8");
        assertEquals("ZIP Archive Input Stream with UTF-8 encoding", zipArchiveInputStreamEnc.getClass().getName());
        assertNull("No next entry", zipArchiveInputStreamEnc.getNextEntry());
        zipArchiveInputStreamEnc.close();

        // Test SEVEN_Z
        InputStream sevenZInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, sevenZInputStream);
            fail("Expected ArchiveException for SEVEN_Z");
        } catch (ArchiveException e) {
            // expected
        }

        // Test invalid archiver name
        InputStream invalidArchiveInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("INVALID", invalidArchiveInputStream);
            fail("Expected ArchiveException for invalid archiver name");
        } catch (ArchiveException e) {
            // expected
        }
    }

    /**
     * Test creating an archive output stream from an archiver name and an output stream.
     * @throws IOException if the test fails
     */
    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test AR
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNull("No next entry", arArchiveOutputStream.getNextArchiveEntry());
        arArchiveOutputStream.finish();
        assertEquals("AR Archive Output Stream", arArchiveOutputStream.getClass().getName());
        arArchiveOutputStream.close();

        // Test ARJ
        OutputStream arjOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, arjOutputStream);
        arjArchiveOutputStream.putNextEntry(new ArjArchiveEntry("test.txt", "test content".getBytes()));
        arjArchiveOutputStream.finish();
        assertEquals("ARJ Archive Output Stream", arjArchiveOutputStream.getClass().getName());
        arjArchiveOutputStream.close();

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry("test.txt", "test content".getBytes()));
        cpioArchiveOutputStream.finish();
        assertEquals("CPIO Archive Output Stream", cpioArchiveOutputStream.getClass().getName());
        cpioArchiveOutputStream.close();

        // Test DUMP
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("test.txt", "test content".getBytes()));
        dumpArchiveOutputStream.finish();
        assertEquals("Dump Archive Output Stream", dumpArchiveOutputStream.getClass().getName());
        dumpArchiveOutputStream.close();

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        jarArchiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt", "test content".getBytes()));
        jarArchiveOutputStream.finish();
        assertEquals("JAR Archive Output Stream", jarArchiveOutputStream.getClass().getName());
        jarArchiveOutputStream.close();

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt", "test content".getBytes()));
        tarArchiveOutputStream.finish();
        assertEquals("TAR Archive Output Stream", tarArchiveOutputStream.getClass().getName());
        tarArchiveOutputStream.close();

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("test.txt", "test content".getBytes()));
        zipArchiveOutputStream.finish();
        assertEquals("ZIP Archive Output Stream", zipArchiveOutputStream.getClass().getName());
        zipArchiveOutputStream.close();

        // Test ZIP with entry encoding
        OutputStream zipOutputStreamEnc = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStreamEnc = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStreamEnc, "UTF-8");
        zipArchiveOutputStreamEnc.putNextEntry(new ZipArchiveEntry("test.txt", "test content".getBytes("UTF-8")));
        zipArchiveOutputStreamEnc.finish();
        assertEquals("ZIP Archive Output Stream with UTF-8 encoding", zipArchiveOutputStreamEnc.getClass().getName());
        zipArchiveOutputStreamEnc.close();

        // Test SEVEN_Z
        OutputStream sevenZOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, sevenZOutputStream);
            fail("Expected StreamingNotSupportedException for SEVEN_Z");
        } catch (StreamingNotSupportedException e) {
            // expected
        }

        // Test invalid archiver name
        OutputStream invalidArchiveOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("INVALID", invalidArchiveOutputStream);
            fail("Expected IllegalArgumentException for invalid archiver name");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test creating an archive input stream from an input stream, autodetecting the archive type.
     * @throws IOException if the test fails
     */
    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws IOException {
        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(ZipArchiveInputStream.TEST_DATA);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(zipInputStream);
        assertEquals("ZIP Archive Input Stream", archiveInputStream.getClass().getName());
        assertNotNull("Next entry should not be null", archiveInputStream.getNextEntry());
        archiveInputStream.close();

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(JarArchiveInputStream.TEST_DATA);
        ArchiveInputStream archiveInputStreamJar = new ArchiveStreamFactory().createArchiveInputStream(jarInputStream);
        assertEquals("JAR Archive Input Stream", archiveInputStreamJar.getClass().getName());
        assertNotNull("Next entry should not be null", archiveInputStreamJar.getNextEntry());
        archiveInputStreamJar.close();

        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(ArArchiveInputStream.TEST_DATA);
        ArchiveInputStream archiveInputStreamAr = new ArchiveStreamFactory().createArchiveInputStream(arInputStream);
        assertEquals("AR Archive Input Stream", archiveInputStreamAr.getClass().getName());
        assertNotNull("Next entry should not be null", archiveInputStreamAr.getNextEntry());
        archiveInputStreamAr.close();

        // Test ARJ
        InputStream arjInputStream = new ByteArrayInputStream(ArjArchiveInputStream.TEST_DATA);
        ArchiveInputStream archiveInputStreamArj = new ArchiveStreamFactory().createArchiveInputStream(arjInputStream);
        assertEquals("ARJ Archive Input Stream", archiveInputStreamArj.getClass().getName());
        assertNotNull("Next entry should not be null", archiveInputStreamArj.getNextEntry());
        archiveInputStreamArj.close();

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(CpioArchiveInputStream.TEST_DATA);
        ArchiveInputStream archiveInputStreamCpio = new ArchiveStreamFactory().createArchiveInputStream(cpioInputStream);
        assertEquals("CPIO Archive Input Stream", archiveInputStreamCpio.getClass().getName());
        assertNotNull("Next entry should not be null", archiveInputStreamCpio.getNextEntry());
        archiveInputStreamCpio.close();

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(TarArchiveInputStream.TEST_DATA);
        ArchiveInputStream archiveInputStreamTar = new ArchiveStreamFactory().createArchiveInputStream(tarInputStream);
        assertEquals("TAR Archive Input Stream", archiveInputStreamTar.getClass().getName());
        assertNotNull("Next entry should not be null", archiveInputStreamTar.getNextEntry());
        archiveInputStreamTar.close();

        // Test DUMP
        InputStream dumpInputStream = new ByteArrayInputStream(DumpArchiveInputStream.TEST_DATA);
        ArchiveInputStream archiveInputStreamDump = new ArchiveStreamFactory().createArchiveInputStream(dumpInputStream);
        assertEquals("Dump Archive Input Stream", archiveInputStreamDump.getClass().getName());
        assertNotNull("Next entry should not be null", archiveInputStreamDump.getNextEntry());
        archiveInputStreamDump.close();

        // Test SEVEN_Z
        InputStream sevenZInputStream = new ByteArrayInputStream(SevenZFile.TEST_DATA);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(sevenZInputStream);
            fail("Expected StreamingNotSupportedException for SEVEN_Z");
        } catch (StreamingNotSupportedException e) {
            // expected
        }
    }
}