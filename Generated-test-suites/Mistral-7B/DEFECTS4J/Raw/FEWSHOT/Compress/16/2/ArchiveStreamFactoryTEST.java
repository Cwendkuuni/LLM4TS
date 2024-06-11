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
import org.apache.commons.compress.archivers.test.TestArchiveInputStream;
import org.apache.commons.compress.archivers.test.TestArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String[] validArchiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};

        for (String archiverName : validArchiverNames) {
            InputStream inputStream = createMockArchiveInputStream(archiverName);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
            assertInstanceOf(archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR) ? ArArchiveInputStream.class :
                    archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO) ? CpioArchiveInputStream.class :
                            archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP) ? DumpArchiveInputStream.class :
                                    archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR) ? JarArchiveInputStream.class :
                                            archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR) ? TarArchiveInputStream.class :
                                                    archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP) ? ZipArchiveInputStream.class : null, archiveInputStream);
        }

        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream("invalid", createMockArchiveInputStream(null));
            assertNull(archiveInputStream);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String[] validArchiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.JAR,
                ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};

        for (String archiverName : validArchiverNames) {
            OutputStream outputStream = createMockArchiveOutputStream(archiverName);
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assertInstanceOf(archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR) ? ArArchiveOutputStream.class :
                    archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO) ? CpioArchiveOutputStream.class :
                            archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR) ? JarArchiveOutputStream.class :
                                    archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR) ? TarArchiveOutputStream.class :
                                            archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP) ? ZipArchiveOutputStream.class : null, archiveOutputStream);
        }

        try {
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream("invalid", createMockArchiveOutputStream(null));
            assertNull(archiveOutputStream);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromSignature() throws Exception {
        String[] validArchiverNames = {ArchiveStreamFactory.AR, ArchiveStreamFactory.CPIO, ArchiveStreamFactory.DUMP,
                ArchiveStreamFactory.JAR, ArchiveStreamFactory.TAR, ArchiveStreamFactory.ZIP};

        for (String archiverName : validArchiverNames) {
            InputStream inputStream = createMockArchiveInputStreamFromSignature(archiverName);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
            assertInstanceOf(archiverName.equalsIgnoreCase(ArchiveStreamFactory.AR) ? ArArchiveInputStream.class :
                    archiverName.equalsIgnoreCase(ArchiveStreamFactory.CPIO) ? CpioArchiveInputStream.class :
                            archiverName.equalsIgnoreCase(ArchiveStreamFactory.DUMP) ? DumpArchiveInputStream.class :
                                    archiverName.equalsIgnoreCase(ArchiveStreamFactory.JAR) ? JarArchiveInputStream.class :
                                            archiverName.equalsIgnoreCase(ArchiveStreamFactory.TAR) ? TarArchiveInputStream.class :
                                                    archiverName.equalsIgnoreCase(ArchiveStreamFactory.ZIP) ? ZipArchiveInputStream.class : null, archiveInputStream);
        }

        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(createMockArchiveInputStreamFromSignature(null));
            assertNull(archiveInputStream);
        } catch (ArchiveException e) {
            // expected
        }
    }

    private InputStream createMockArchiveInputStream(String archiverName) {
        return new TestArchiveInputStream(archiverName);
    }

    private InputStream createMockArchiveInputStreamFromSignature(String archiverName) {
        if (archiverName == null) {
            return null;
        }

        InputStream inputStream = new ByteArrayInputStream(getArchiveSignature(archiverName).getBytes());
        return inputStream;
    }

    private OutputStream createMockArchiveOutputStream(String archiverName) {
        return new TestArchiveOutputStream(archiverName);
    }

    private byte[] getArchiveSignature(String archiverName) {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return new byte[20]; // ar signature
            case ArchiveStreamFactory.CPIO:
                return new byte[16]; // cpio signature
            case ArchiveStreamFactory.DUMP:
                return new byte[19]; // dump signature
            case ArchiveStreamFactory.JAR:
                return new byte[8]; // jar signature
            case ArchiveStreamFactory.TAR:
                return new byte[8]; // tar signature
            case ArchiveStreamFactory.ZIP:
                return new byte[6]; // zip signature
            default:
                throw new IllegalStateException();
        }
    }
}