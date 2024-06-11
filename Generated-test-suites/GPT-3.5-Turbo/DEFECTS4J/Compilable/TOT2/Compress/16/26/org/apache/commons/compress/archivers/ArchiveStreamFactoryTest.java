package org.apache.commons.compress.archivers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream));
    }

    @Test
    public void testCreateArchiveOutputStream_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        factory.createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Success() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{80, 75, 3, 4});
        assertNotNull(factory.createArchiveInputStream(inputStream));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_NullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_NoMarkSupport() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        factory.createArchiveInputStream(inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_NoArchiverFound() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        factory.createArchiveInputStream(inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream));
    }
    
    @Test
    public void testCreateArchiveOutputStream_JAR_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream));
    }

    @Test
    public void testCreateArchiveOutputStream_TAR_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream));
    }

    @Test
    public void testCreateArchiveInputStream_AR_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream));
    }
    
    @Test
    public void testCreateArchiveInputStream_JAR_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_TAR_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_CPIO_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_Dump_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_ZIP_Success() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream));
    }

}