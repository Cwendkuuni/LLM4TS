package org.apache.commons.compress.archivers;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ARArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
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
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR
        byte[] arBytes = getTestFileBytes("ar-test.tar.gz");
        InputStream arInputStream = new ByteArrayInputStream(arBytes);
        ArchiveInputStream arInputStreamFromFactory = factory.createArchiveInputStream("AR", arInputStream);
        assertInstanceOf(ARArchiveInputStream.class, arInputStreamFromFactory);

        // Test ARJ
        byte[] arjBytes = getTestFileBytes("arj-test.tar.gz");
        InputStream arjInputStream = new ByteArrayInputStream(arjBytes);
        ArchiveInputStream arjInputStreamFromFactory = factory.createArchiveInputStream("ARJ", arjInputStream);
        assertInstanceOf(ArjArchiveInputStream.class, arjInputStreamFromFactory);

        // Test CPIO
        byte[] cpioBytes = getTestFileBytes("cpio-test.tar.gz");
        InputStream cpioInputStream = new ByteArrayInputStream(cpioBytes);
        ArchiveInputStream cpioInputStreamFromFactory = factory.createArchiveInputStream("CPIO", cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, cpioInputStreamFromFactory);

        // Test Dump
        byte[] dumpBytes = getTestFileBytes("dump-test.tar.gz");
        InputStream dumpInputStream = new ByteArrayInputStream(dumpBytes);
        ArchiveInputStream dumpInputStreamFromFactory = factory.createArchiveInputStream("DUMP", dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, dumpInputStreamFromFactory);

        // Test JAR
        byte[] jarBytes = getTestFileBytes("jar-test.jar");
        InputStream jarInputStream = new ByteArrayInputStream(jarBytes);
        ArchiveInputStream jarInputStreamFromFactory = factory.createArchiveInputStream("JAR", jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, jarInputStreamFromFactory);

        // Test TAR
        byte[] tarBytes = getTestFileBytes("tar-test.tar.gz");
        InputStream tarInputStream = new ByteArrayInputStream(tarBytes);
        ArchiveInputStream tarInputStreamFromFactory = factory.createArchiveInputStream("TAR", tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarInputStreamFromFactory);

        // Test ZIP
        byte[] zipBytes = getTestFileBytes("zip-test.zip");
        InputStream zipInputStream = new ByteArrayInputStream(zipBytes);
        ArchiveInputStream zipInputStreamFromFactory = factory.createArchiveInputStream("ZIP", zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipInputStreamFromFactory);

        // Test SEVEN_Z
        byte[] sevenzBytes = getTestFileBytes("sevenz-test.7z");
        InputStream sevenzInputStream = new ByteArrayInputStream(sevenzBytes);
        try {
            factory.createArchiveInputStream("SEVEN_Z", sevenzInputStream);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Test null input stream
        assertNull(factory.createArchiveInputStream(null));
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStreamFromFactory = factory.createArchiveOutputStream("AR", outputStream);
        assertInstanceOf(ARArchiveOutputStream.class, arOutputStreamFromFactory);

        // Test ARJ
        OutputStream arjOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjOutputStreamFromFactory = factory.createArchiveOutputStream("ARJ", arjOutputStream);
        assertInstanceOf(ARjArchiveOutputStream.class, arjOutputStreamFromFactory);

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStreamFromFactory = factory.createArchiveOutputStream("CPIO", cpioOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, cpioOutputStreamFromFactory);

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStreamFromFactory = factory.createArchiveOutputStream("JAR", jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, jarOutputStreamFromFactory);

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStreamFromFactory = factory.createArchiveOutputStream("TAR", tarOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, tarOutputStreamFromFactory);

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStreamFromFactory = factory.createArchiveOutputStream("ZIP", zipOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, zipOutputStreamFromFactory);

        // Test SEVEN_Z
        OutputStream sevenzOutputStream = new ByteArrayOutputStream();
        try {
            factory.createArchiveOutputStream("SEVEN_Z", sevenzOutputStream);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Test null output stream
        assertNull(factory.createArchiveOutputStream(null));
    }

    @Test
    public void testCreateArchiveInputStreamFromSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(getTestArSignature());
        ArchiveInputStream arInputStreamFromFactory = factory.createArchiveInputStream("", arInputStream);
        assertInstanceOf(ARArchiveInputStream.class, arInputStreamFromFactory);

        // Test ARJ
        InputStream arjInputStream = new ByteArrayInputStream(getTestArjSignature());
        ArchiveInputStream arjInputStreamFromFactory = factory.createArchiveInputStream("", arjInputStream);
        assertInstanceOf(ArjArchiveInputStream.class, arjInputStreamFromFactory);

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(getTestCpioSignature());
        ArchiveInputStream cpioInputStreamFromFactory = factory.createArchiveInputStream("", cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, cpioInputStreamFromFactory);

        // Test DUMP
        InputStream dumpInputStream = new ByteArrayInputStream(getTestDumpSignature());
        ArchiveInputStream dumpInputStreamFromFactory = factory.createArchiveInputStream("", dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, dumpInputStreamFromFactory);

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(getTestJarSignature());
        ArchiveInputStream jarInputStreamFromFactory = factory.createArchiveInputStream("", jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, jarInputStreamFromFactory);

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(getTestTarSignature());
        ArchiveInputStream tarInputStreamFromFactory = factory.createArchiveInputStream("", tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarInputStreamFromFactory);

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(getTestZipSignature());
        ArchiveInputStream zipInputStreamFromFactory = factory.createArchiveInputStream("", zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipInputStreamFromFactory);

        // Test SEVEN_Z
        InputStream sevenzInputStream = new ByteArrayInputStream(getTestSevenzSignature());
        try {
            factory.createArchiveInputStream("", sevenzInputStream);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Test null input stream
        assertNull(factory.createArchiveInputStream("", null));
    }

    @Test
    public void testCreateArchiveOutputStreamFromSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStreamFromFactory = factory.createArchiveOutputStream("", arOutputStream);
        assertInstanceOf(ARArchiveOutputStream.class, arOutputStreamFromFactory);

        // Test ARJ
        OutputStream arjOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjOutputStreamFromFactory = factory.createArchiveOutputStream("", arjOutputStream);
        assertInstanceOf(ARjArchiveOutputStream.class, arjOutputStreamFromFactory);

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStreamFromFactory = factory.createArchiveOutputStream("", cpioOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, cpioOutputStreamFromFactory);

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStreamFromFactory = factory.createArchiveOutputStream("", jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, jarOutputStreamFromFactory);

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStreamFromFactory = factory.createArchiveOutputStream("", tarOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, tarOutputStreamFromFactory);

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStreamFromFactory = factory.createArchiveOutputStream("", zipOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, zipOutputStreamFromFactory);

        // Test SEVEN_Z
        OutputStream sevenzOutputStream = new ByteArrayOutputStream();
        try {
            factory.createArchiveOutputStream("", sevenzOutputStream);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Test null output stream
        assertNull(factory.createArchiveOutputStream("", null));
    }

    private byte[] getTestArSignature() {
        return new byte[0]; // AR signature is not provided in the code
    }

    private byte[] getTestArjSignature() {
        return new byte[0]; // ARJ signature is not provided in the code
    }

    private byte[] getTestCpioSignature() {
        return IOUtils.toByteArray(getClass().getResourceAsStream("/cpio-signature.txt"));
    }

    private byte[] getTestDumpSignature() {
        return new byte[0]; // DUMP signature is not provided in the code
    }

    private byte[] getTestJarSignature() {
        return new byte[0]; // JAR signature is not provided in the code
    }

    private byte[] getTestTarSignature() {
        return new byte[0]; // TAR signature is not provided in the code
    }

    private byte[] getTestZipSignature() {
        return new byte[0]; // ZIP signature is not provided in the code
    }

    private byte[] getTestFileBytes(String fileName) {
        return IOUtils.toByteArray(getClass().getResource(fileName));
    }
}