package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers. ArchiveInputStream;
import org.apache.commons.compress.archivers. ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarHeader;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAR() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamCPIO() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.CPIO;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamDump() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.DUMP;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamJar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.JAR;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamTar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.TAR;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        TarHeader header = archiveInputStream.getNextEntry();
        assertNull(header);
    }

    @Test
    public void testCreateArchiveInputStreamZip() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamUnknownFormat() throws ArchiveException, IOException {
        String archiverName = "unknownFormat";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
            assert false; // should not reach this point
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamInvalidStream() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = null;
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
            assert false; // should not reach this point
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamAR() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamCPIO() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.CPIO;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamDump() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.DUMP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamJar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamTar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.TAR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamZip() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamUnknownFormat() throws ArchiveException, IOException {
        String archiverName = "unknownFormat";
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assert false; // should not reach this point
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamInvalidStream() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = null;
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assert false; // should not reach this point
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}