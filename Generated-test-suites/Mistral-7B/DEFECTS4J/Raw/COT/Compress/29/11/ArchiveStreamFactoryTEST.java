package org.apache.commons.compress.archivers.tests;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.SevenZFile;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamWithValidArchiveNameAndInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiveName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        factory.createArchiveInputStream(archiveName, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiveName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithValidArchiveNameAndByteArray() throws Exception {
        byte[] inputBytes = new byte[1024];
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(inputBytes));
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidSignature() throws Exception {
        ByteArrayInputStream invalidSignatureInput = new ByteArrayInputStream(new byte[1024]);
        factory.createArchiveInputStream(ArchiveStreamFactory.TAR, invalidSignatureInput);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArchiveNameAndOutputStream() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiveName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        factory.createArchiveOutputStream(archiveName, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiveName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", outputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArchiveNameAndByteArrayOutput() throws Exception {
        byte[] outputBytes = new byte[1024];
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream(outputBytes.length));
        assertNotNull(archiveOutputStream);
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStreamWithSevenZFormat() throws Exception {
        ByteArrayOutputStream sevenZOutputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, sevenZOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithNullArchiverNameAndInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithValidArchiveNameAndNullInputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        factory.createArchiveInputStream(archiveName, null);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(factory);
    }
}