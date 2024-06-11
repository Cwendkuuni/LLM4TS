package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidArchiverNameAndInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] {});
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] {});
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] {});
        factory.createArchiveInputStream("unknown", in);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArchiverNameAndOutputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] {});
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithInputStreamThatDoesNotSupportMark() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        };
        factory.createArchiveInputStream(in);
    }

    @Test
    public void testCreateArchiveInputStreamWithValidInputStreamAndZipSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = { 80, 75, 3, 4 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithValidInputStreamAndJarSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = { 80, 75, 3, 4, 10 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithValidInputStreamAndArSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = { 33, 60, 30, 61, 33, 60, 30, 61 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithValidInputStreamAndCpioSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = { 35, 36, 37, 38, 35, 36, 37, 38 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithValidInputStreamAndDumpSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = { 37, 36, 39, 38 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithValidInputStreamAndTarSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[512];
        for (int i = 0; i < 512; i++) {
            data[i] = 0;
        }
        data[257] = 1;
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(in);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[512];
        InputStream in = new ByteArrayInputStream(data);
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignatureAndInvalidTarHeaderSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[512];
        InputStream in = new ByteArrayInputStream(data);
        factory.createArchiveInputStream(in);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignatureAndInvalidTarSignature() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[1024];
        InputStream in = new ByteArrayInputStream(data);
        factory.createArchiveInputStream(in);
    }
}