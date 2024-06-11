package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStream_withKnownArchiverName_shouldReturnArchiveInputStream() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream ais = factory.createArchiveInputStream("ar", in);
        assertNotNull(ais);
        assertTrue(ais instanceof ArArchiveInputStream);

        ais = factory.createArchiveInputStream("zip", in);
        assertNotNull(ais);
        assertTrue(ais instanceof ZipArchiveInputStream);

        ais = factory.createArchiveInputStream("tar", in);
        assertNotNull(ais);
        assertTrue(ais instanceof TarArchiveInputStream);

        ais = factory.createArchiveInputStream("jar", in);
        assertNotNull(ais);
        assertTrue(ais instanceof JarArchiveInputStream);

        ais = factory.createArchiveInputStream("cpio", in);
        assertNotNull(ais);
        assertTrue(ais instanceof CpioArchiveInputStream);

        ais = factory.createArchiveInputStream("dump", in);
        assertNotNull(ais);
        assertTrue(ais instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_withUnknownArchiverName_shouldThrowException() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);

        try {
            factory.createArchiveInputStream("unknown", in);
            fail("Expected ArchiveException was not thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullArchiverName_shouldThrowException() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream_shouldThrowException() throws Exception {
        factory.createArchiveInputStream("ar", null);
    }

    @Test
    public void testCreateArchiveOutputStream_withKnownArchiverName_shouldReturnArchiveOutputStream() throws Exception {
        OutputStream out = new ByteArrayOutputStream();

        ArchiveOutputStream aos = factory.createArchiveOutputStream("ar", out);
        assertNotNull(aos);
        assertTrue(aos instanceof ArArchiveOutputStream);

        aos = factory.createArchiveOutputStream("zip", out);
        assertNotNull(aos);
        assertTrue(aos instanceof ZipArchiveOutputStream);

        aos = factory.createArchiveOutputStream("tar", out);
        assertNotNull(aos);
        assertTrue(aos instanceof TarArchiveOutputStream);

        aos = factory.createArchiveOutputStream("jar", out);
        assertNotNull(aos);
        assertTrue(aos instanceof JarArchiveOutputStream);

        aos = factory.createArchiveOutputStream("cpio", out);
        assertNotNull(aos);
        assertTrue(aos instanceof CpioArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_withUnknownArchiverName_shouldThrowException() throws Exception {
        OutputStream out = new ByteArrayOutputStream();

        try {
            factory.createArchiveOutputStream("unknown", out);
            fail("Expected ArchiveException was not thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName_shouldThrowException() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream_shouldThrowException() throws Exception {
        factory.createArchiveOutputStream("ar", null);
    }

    @Test
    public void testCreateArchiveInputStream_withInputStream_shouldReturnArchiveInputStream() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream ais = factory.createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof ZipArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withInvalidInputStream_shouldThrowException() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);

        factory.createArchiveInputStream(in);
    }
}