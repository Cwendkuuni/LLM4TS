package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.IOUtils;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;

/**
 * Test for the ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creation of input streams for known archive types.
     * @throws IOException if creation fails
     */
    @org.junit.Test
    public void testCreateInputStreams() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test creation of known archive types
        InputStream arStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(arStream);
        ArArchiveInputStream arInput = (ArArchiveInputStream) arStream;
        assertTrue(arInput.getNextEntry() == null);
        arStream.close();

        InputStream zipStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(zipStream);
        JarArchiveInputStream zipInput = (JarArchiveInputStream) zipStream;
        assertEquals(JarArchiveInputStream.STORED, zipInput.getCompressionMethod());
        assertNull(zipInput.getNextEntry());
        zipStream.close();

        InputStream tarStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(tarStream);
        TarArchiveInputStream tarInput = (TarArchiveInputStream) tarStream;
        assertEquals(TarArchiveInputStream.GNU, tarInput.getTarVersion());
        assertNull(tarInput.getNextEntry());
        tarStream.close();

        InputStream jarStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(jarStream);
        JarArchiveInputStream jarInput = (JarArchiveInputStream) jarStream;
        assertNotNull(jarInput.getNextEntry());
        JarArchiveEntry jarEntry = jarInput.getNextEntry();
        assertNotNull(jarEntry);
        assertTrue(jarEntry.isDirectory());
        jarStream.close();

        InputStream cpioStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertNotNull(cpioStream);
        CpioArchiveInputStream cpioInput = (CpioArchiveInputStream) cpioStream;
        assertTrue(cpioInput.read() == -1);
        cpioStream.close();

        InputStream dumpStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(dumpStream);
        DumpArchiveInputStream dumpInput = (DumpArchiveInputStream) dumpStream;
        assertTrue(dumpInput.read() == -1);
        dumpStream.close();

        InputStream sevenzStream = factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayInputStream(new byte[0]));
        assertNull(sevenzStream);

        // Test creation of unknown archive type
        InputStream unknownStream = factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
        assertNull(unknownStream);
    }

    /**
     * Test creation of output streams for known archive types.
     * @throws IOException if creation fails
     */
    @org.junit.Test
    public void testCreateOutputStreams() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test creation of known archive types
        OutputStream arStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
        assertNotNull(arStream);
        ArArchiveOutputStream arOutput = (ArArchiveOutputStream) arStream;
        arStream.close();

        OutputStream zipStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertNotNull(zipStream);
        ZipArchiveOutputStream zipOutput = (ZipArchiveOutputStream) zipStream;
        zipOutput.putNextEntry(new JarArchiveEntry("test.txt"));
        zipOutput.write("test".getBytes());
        zipOutput.closeArchiveEntry();
        zipStream.close();

        OutputStream tarStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        assertNotNull(tarStream);
        TarArchiveOutputStream tarOutput = (TarArchiveOutputStream) tarStream;
        TarArchiveEntry tarEntry = new TarArchiveEntry("test.txt");
        tarEntry.setSize(5);
        tarEntry.setChecksum(new byte[0]);
        tarOutput.putNextEntry(tarEntry);
        tarOutput.write(5 * "test".getBytes());
        tarOutput.closeArchiveEntry();
        tarStream.close();

        OutputStream jarStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
        assertNotNull(jarStream);
        JarArchiveOutputStream jarOutput = (JarArchiveOutputStream) jarStream;
        JarArchiveEntry jarEntry = new JarArchiveEntry("test.txt");
        jarOutput.putNextEntry(jarEntry);
        jarOutput.write("test".getBytes());
        jarOutput.closeArchiveEntry();
        jarStream.close();

        OutputStream cpioStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
        assertNotNull(cpioStream);
        CpioArchiveOutputStream cpioOutput = (CpioArchiveOutputStream) cpioStream;
        cpioOutput.writeHeader(new String[]{"test.txt", "testdata", "testdata", null, null, null, null, "0644", null, null});
        cpioOutput.write(new byte[0]);
        cpioOutput.write(new byte[5]);
        cpioOutput.write(new byte[5]);
        cpioOutput.write("test".getBytes());
        cpioOutput.close();
        cpioStream.close();

        OutputStream dumpStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayOutputStream());
        assertNotNull(dumpStream);
        DumpArchiveOutputStream dumpOutput = (DumpArchiveOutputStream) dumpStream;
        dumpOutput.putNextEntry(new DumpArchiveEntry("test.txt"));
        dumpOutput.write("test".getBytes());
        dumpOutput.closeArchiveEntry();
        dumpStream.close();

        OutputStream sevenzStream = factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayOutputStream());
        assertNull(sevenzStream);

        // Test creation of unknown archive type
        OutputStream unknownStream = factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
        assertNull(unknownStream);
    }

    /**
     * Test creation of archive input stream using the first bytes of an input stream.
     * @throws IOException if creation fails
     */
    @org.junit.Test
    public void testCreateInputStreamFromBytes() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test known archive types
        InputStream arStream = new ByteArrayInputStream(new byte[0]);
        InputStream arInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arStream);
        assertNotNull(arInput);
        ArArchiveInputStream arArchiveInput = (ArArchiveInputStream) arInput;
        assertTrue(arArchiveInput.getNextEntry() == null);
        arInput.close();
        arStream.close();

        InputStream zipStream = new ByteArrayInputStream(new byte[0]);
        InputStream zipInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipStream);
        assertNotNull(zipInput);
        JarArchiveInputStream jarInput = (JarArchiveInputStream) zipInput;
        assertNull(jarInput.getNextEntry());
        zipInput.close();
        zipStream.close();

        InputStream tarStream = new ByteArrayInputStream(new byte[0]);
        InputStream tarInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarStream);
        assertNotNull(tarInput);
        TarArchiveInputStream tarArchiveInput = (TarArchiveInputStream) tarInput;
        assertEquals(TarArchiveInputStream.GNU, tarArchiveInput.getTarVersion());
        assertNull(tarArchiveInput.getNextEntry());
        tarInput.close();
        tarStream.close();

        InputStream jarStream = new ByteArrayInputStream(new byte[0]);
        InputStream jarInput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarStream);
        assertNotNull(jarInput);
        JarArchiveInputStream jarArchiveInput = (JarArchiveInputStream) jarInput;
        assertNotNull(jarArchiveInput.getNextEntry());
        JarArchiveEntry jarEntry = jarArchiveInput.getNextEntry();
        assertNotNull(jarEntry);
        assertTrue(jarEntry.isDirectory());
        jarInput.close();
        jarStream.close();

        InputStream cpioStream = new ByteArrayInputStream(new byte[0]);
        InputStream cpioInput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioStream);
        assertNotNull(cpioInput);
        CpioArchiveInputStream cpioArchiveInput = (CpioArchiveInputStream) cpioInput;
        assertTrue(cpioArchiveInput.read() == -1);
        cpioInput.close();
        cpioStream.close();

        InputStream dumpStream = new ByteArrayInputStream(new byte[0]);
        InputStream dumpInput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpStream);
        assertNotNull(dumpInput);
        DumpArchiveInputStream dumpArchiveInput = (DumpArchiveInputStream) dumpInput;
        assertTrue(dumpArchiveInput.read() == -1);
        dumpInput.close();
        dumpStream.close();

        InputStream sevenzStream = new ByteArrayInputStream(new byte[0]);
        InputStream sevenzInput = factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, sevenzStream);
        assertNull(sevenzInput);
        sevenzStream.close();

        // Test unknown archive type
        InputStream unknownStream = new ByteArrayInputStream(new byte[0]);
        InputStream unknownInput = factory.createArchiveInputStream("unknown", unknownStream);
        assertNull(unknownInput);
        unknownStream.close();
    }

}