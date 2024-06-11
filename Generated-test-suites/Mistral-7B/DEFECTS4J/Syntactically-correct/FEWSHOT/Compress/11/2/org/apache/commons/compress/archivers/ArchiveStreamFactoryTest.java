package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.*;
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
    public void testCreateArchiveInputStream() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            InputStream input = createTestInputStream(archiverName);
            try (ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input)) {
                assertNotNull(archiveInputStream);
                assertInstanceOf(getArchiveInputStreamClass(archiverName), archiveInputStream);
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            OutputStream output = createTestOutputStream(archiverName);
            try (ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output)) {
                assertNotNull(archiveOutputStream);
                assertInstanceOf(getArchiveOutputStreamClass(archiverName), archiveOutputStream);
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            byte[] bytes = getArchiveSignatureBytes(archiverName);
            try (InputStream input = new ByteArrayInputStream(bytes)) {
                try (ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input)) {
                    assertNotNull(archiveInputStream);
                    assertInstanceOf(getArchiveInputStreamClass(archiverName), archiveInputStream);
                }
            }
        }
    }

    private InputStream createTestInputStream(String archiverName) throws Exception {
        String fileName = "test_" + archiverName + ".test";
        try (InputStream input = getClass().getResourceAsStream(fileName)) {
            return input;
        }
    }

    private OutputStream createTestOutputStream(String archiverName) throws Exception {
        String fileName = "test_" + archiverName + ".test";
        try (OutputStream output = new FileOutputStream(fileName)) {
            return output;
        }
    }

    private Class<? extends ArchiveInputStream> getArchiveInputStreamClass(String archiverName) {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return ArArchiveInputStream.class;
            case ArchiveStreamFactory.CPIO:
                return CpioArchiveInputStream.class;
            case ArchiveStreamFactory.DUMP:
                return DumpArchiveInputStream.class;
            case ArchiveStreamFactory.JAR:
                return JarArchiveInputStream.class;
            case ArchiveStreamFactory.TAR:
                return TarArchiveInputStream.class;
            case ArchiveStreamFactory.ZIP:
                return ZipArchiveInputStream.class;
            default:
                throw new IllegalStateException("Unknown archiver: " + archiverName);
        }
    }

    private Class<? extends ArchiveOutputStream> getArchiveOutputStreamClass(String archiverName) {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return ArArchiveOutputStream.class;
            case ArchiveStreamFactory.CPIO:
                return CpioArchiveOutputStream.class;
            case ArchiveStreamFactory.DUMP:
                return DumpArchiveOutputStream.class;
            case ArchiveStreamFactory.JAR:
                return JarArchiveOutputStream.class;
            case ArchiveStreamFactory.TAR:
                return TarArchiveOutputStream.class;
            case ArchiveStreamFactory.ZIP:
                return ZipArchiveOutputStream.class;
            default:
                throw new IllegalStateException("Unknown archiver: " + archiverName);
        }
    }

    private byte[] getArchiveSignatureBytes(String archiverName) {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return AR_SIGNATURE;
            case ArchiveStreamFactory.CPIO:
                return CPIO_SIGNATURE;
            case ArchiveStreamFactory.DUMP:
                return DUMP_SIGNATURE;
            case ArchiveStreamFactory.JAR:
                return JAR_SIGNATURE;
            case ArchiveStreamFactory.TAR:
                return new byte[0]; //Tar does not have a signature
            case ArchiveStreamFactory.ZIP:
                return ZIP_SIGNATURE;
            default:
                throw new IllegalStateException("Unknown archiver: " + archiverName);
        }
    }

    private static final byte[] AR_SIGNATURE = new byte[]{(byte) 0x60, (byte) 0x60, (byte) 0x60, (byte) 0x60, (byte) 0x60};
    private static final byte[] CPIO_SIGNATURE = new byte[]{'C', 'P', 'I', 'O'};
    private static final byte[] DUMP_SIGNATURE = new byte[]{'0', '7', '0', '7', 'A', 'R', 'C', 'H', 'E', 'A', 'R', ':', ' ', 'B', 'S', 'D', 'U', 'M', 'P'};
    private static final byte[] JAR_SIGNATURE = new byte[]{'0', 'x', '0', 'x', '0', 'x', '0', 'x', '0', 'x', '0', 'x', '0', 'x', '0', 'x', '0', 'x'};
    private static final byte[] ZIP_SIGNATURE = new byte[]{'P', 'P', 'M', 'D', 'R', 'M', 'S', 'T', 'S', 'E', ' ', 'C', 'R', 'C', 'K'};
}