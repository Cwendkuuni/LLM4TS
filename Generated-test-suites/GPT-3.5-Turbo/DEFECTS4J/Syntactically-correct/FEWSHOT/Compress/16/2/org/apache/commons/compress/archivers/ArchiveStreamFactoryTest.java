import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        InputStream in = mock(InputStream.class);

        // Test for AR archive format
        ArchiveInputStream arInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertEquals(ArArchiveInputStream.class, arInputStream.getClass());

        // Test for ZIP archive format
        ArchiveInputStream zipInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertEquals(ZipArchiveInputStream.class, zipInputStream.getClass());

        // Test for TAR archive format
        ArchiveInputStream tarInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertEquals(TarArchiveInputStream.class, tarInputStream.getClass());

        // Test for JAR archive format
        ArchiveInputStream jarInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertEquals(JarArchiveInputStream.class, jarInputStream.getClass());

        // Test for CPIO archive format
        ArchiveInputStream cpioInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertEquals(CpioArchiveInputStream.class, cpioInputStream.getClass());

        // Test for DUMP archive format
        ArchiveInputStream dumpInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertEquals(DumpArchiveInputStream.class, dumpInputStream.getClass());

        // Test for unknown archive format
        try {
            archiveStreamFactory.createArchiveInputStream("unknown", in);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        OutputStream out = mock(OutputStream.class);

        // Test for AR archive format
        ArchiveOutputStream arOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertEquals(ArArchiveOutputStream.class, arOutputStream.getClass());

        // Test for ZIP archive format
        ArchiveOutputStream zipOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertEquals(ZipArchiveOutputStream.class, zipOutputStream.getClass());

        // Test for TAR archive format
        ArchiveOutputStream tarOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertEquals(TarArchiveOutputStream.class, tarOutputStream.getClass());

        // Test for JAR archive format
        ArchiveOutputStream jarOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertEquals(JarArchiveOutputStream.class, jarOutputStream.getClass());

        // Test for CPIO archive format
        ArchiveOutputStream cpioOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertEquals(CpioArchiveOutputStream.class, cpioOutputStream.getClass());

        // Test for unknown archive format
        try {
            archiveStreamFactory.createArchiveOutputStream("unknown", out);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws ArchiveException, IOException {
        byte[] arSignature = new byte[]{0x21, (byte) 0x90, 0x4b, (byte) 0x80, 0x37, (byte) 0xbe, (byte) 0xe2, (byte) 0x88, 0x0e, (byte) 0xf6, 0x4c, 0x35};
        byte[] zipSignature = new byte[]{0x50, 0x4b, 0x03, 0x04};
        byte[] tarSignature = new byte[]{0x75, 0x73, 0x74, 0x61, 0x72};
        byte[] jarSignature = new byte[]{0x50, 0x4b, 0x03, 0x04};
        byte[] cpioSignature = new byte[]{0x30, 0x37, 0x30, 0x37};
        byte[] dumpSignature = new byte[]{(byte) 0xfd, (byte) 0xc1, (byte) 0xed, (byte) 0xfe};
        byte[] unknownSignature = new byte[]{0x00, 0x00, 0x00, 0x00};

        InputStream in = mock(InputStream.class);

        // Test for ZIP archive format
        ByteArrayInputStream zipIn = new ByteArrayInputStream(zipSignature);
        ArchiveInputStream zipInputStream = archiveStreamFactory.createArchiveInputStream(zipIn);
        assertEquals(ZipArchiveInputStream.class, zipInputStream.getClass());

        // Test for JAR archive format
        ByteArrayInputStream jarIn = new ByteArrayInputStream(jarSignature);
        ArchiveInputStream jarInputStream = archiveStreamFactory.createArchiveInputStream(jarIn);
        assertEquals(JarArchiveInputStream.class, jarInputStream.getClass());

        // Test for AR archive format
        ByteArrayInputStream arIn = new ByteArrayInputStream(arSignature);
        ArchiveInputStream arInputStream = archiveStreamFactory.createArchiveInputStream(arIn);
        assertEquals(ArArchiveInputStream.class, arInputStream.getClass());

        // Test for CPIO archive format
        ByteArrayInputStream cpioIn = new ByteArrayInputStream(cpioSignature);
        ArchiveInputStream cpioInputStream = archiveStreamFactory.createArchiveInputStream(cpioIn);
        assertEquals(CpioArchiveInputStream.class, cpioInputStream.getClass());

        // Test for DUMP archive format
        ByteArrayInputStream dumpIn = new ByteArrayInputStream(dumpSignature);
        ArchiveInputStream dumpInputStream = archiveStreamFactory.createArchiveInputStream(dumpIn);
        assertEquals(DumpArchiveInputStream.class, dumpInputStream.getClass());

        // Test for TAR archive format
        ByteArrayInputStream tarIn = new ByteArrayInputStream(tarSignature);
        ArchiveInputStream tarInputStream = archiveStreamFactory.createArchiveInputStream(tarIn);
        assertEquals(TarArchiveInputStream.class, tarInputStream.getClass());

        // Test for unknown archive format
        ByteArrayInputStream unknownIn = new ByteArrayInputStream(unknownSignature);
        try {
            archiveStreamFactory.createArchiveInputStream(unknownIn);
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
}