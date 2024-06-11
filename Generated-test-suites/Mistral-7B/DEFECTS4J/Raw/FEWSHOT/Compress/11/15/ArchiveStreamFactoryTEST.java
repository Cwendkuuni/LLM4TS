package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);

        // Test AR
        ArchiveInputStream arInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("ar", testInputStream);
        assertInstanceOf(ArArchiveInputStream.class, arInputStream);

        // Test CPIO
        ArchiveInputStream cpioInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("cpio", testInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, cpioInputStream);

        // Test Dump
        ArchiveInputStream dumpInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("dump", testInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, dumpInputStream);

        // Test JAR
        ArchiveInputStream jarInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("jar", testInputStream);
        assertInstanceOf(JarArchiveInputStream.class, jarInputStream);

        // Test TAR
        ArchiveInputStream tarInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("tar", testInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarInputStream);

        // Test ZIP
        ArchiveInputStream zipInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("zip", testInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipInputStream);

        // Test Unknown
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", testInputStream));
    }

    @Test
    public void testCreateArchiveOutputStream() {
        OutputStream testOutputStream = new ByteArrayOutputStream();

        // Test AR
        ArchiveOutputStream arOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("ar", testOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, arOutputStream);

        // Test CPIO
        ArchiveOutputStream cpioOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("cpio", testOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, cpioOutputStream);

        // Test Dump
        ArchiveOutputStream dumpOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("dump", testOutputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, dumpOutputStream);

        // Test JAR
        ArchiveOutputStream jarOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("jar", testOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, jarOutputStream);

        // Test TAR
        ArchiveOutputStream tarOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("tar", testOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, tarOutputStream);

        // Test ZIP
        ArchiveOutputStream zipOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("zip", testOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, zipOutputStream);

        // Test Unknown
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", testOutputStream));
    }

    @Test
    public void testCreateArchiveInputStreamFromSignature() {
        byte[] arSignature = new byte[12];
        byte[] cpioSignature = new byte[32];
        byte[] dumpSignature = new byte[32];
        byte[] tarSignature = new byte[512];
        byte[] zipSignature = new byte[31];

        InputStream testInputStream = new ByteArrayInputStream(new byte[0]);

        // Test AR
        testInputStream.mark(arSignature.length);
        testInputStream.read(arSignature);
        testInputStream.reset();
        ArchiveInputStream arInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(ArArchiveInputStream.class, arInputStream);

        // Test CPIO
        testInputStream.mark(cpioSignature.length);
        testInputStream.read(cpioSignature);
        testInputStream.reset();
        ArchiveInputStream cpioInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, cpioInputStream);

        // Test Dump
        testInputStream.mark(dumpSignature.length);
        testInputStream.read(dumpSignature);
        testInputStream.reset();
        ArchiveInputStream dumpInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, dumpInputStream);

        // Test TAR
        testInputStream.mark(tarSignature.length);
        testInputStream.read(tarSignature);
        testInputStream.reset();
        ArchiveInputStream tarInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarInputStream);

        // Test ZIP
        testInputStream.mark(zipSignature.length);
        testInputStream.read(zipSignature);
        testInputStream.reset();
        ArchiveInputStream zipInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipInputStream);

        // Test Unknown
        testInputStream.mark(new byte[12].length);
        testInputStream.read(new byte[12]);
        testInputStream.reset();
        ArchiveInputStream unknownInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(testInputStream);
        assertNull(unknownInputStream);
    }
}