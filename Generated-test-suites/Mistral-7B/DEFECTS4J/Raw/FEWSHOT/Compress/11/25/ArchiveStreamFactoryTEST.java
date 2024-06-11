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
        // Test with known archive types
        InputStream testInputStream = new ByteArrayInputStream("ZIP signature".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream("JAR signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, testInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream("AR signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, testInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream("CPIO signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, testInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream("DUMP signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, testInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream("TAR signature".getBytes());
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        testInputStream = new ByteArrayInputStream("Unknown signature".getBytes());
        assertNull(new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, testInputStream));

        // Test with null or empty input stream
        assertNull(new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null));
        assertNull(new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0][])));
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test with known archive types
        OutputStream testOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, testOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

        testOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, testOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);

        testOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, testOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);

        testOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, testOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);

        testOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, testOutputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);

        testOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, testOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);

        testOutputStream = new ByteArrayOutputStream();
        assertNull(new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, testOutputStream));

        // Test with null or empty output stream
        assertNull(new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null));
        assertNull(new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream()));
    }
}