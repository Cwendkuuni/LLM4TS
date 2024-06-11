package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);

        assertNotNull(ais);
        ais.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);

        assertNotNull(aos);
        aos.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException, IOException {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null, in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiverName() throws ArchiveException, IOException {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("unknown", in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(null, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiverName() throws ArchiveException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("unknown", out);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect() throws ArchiveException, IOException {
        byte[] zipData = new byte[]{80, 75, 3, 4, 10};
        ByteArrayInputStream zipIn = new ByteArrayInputStream(zipData);

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ArchiveInputStream zipAis = factory.createArchiveInputStream(zipIn);

        assertNotNull(zipAis);
        zipAis.close();

        byte[] tarData = new byte[]{0, 1, 75, 115, 0};
        ByteArrayInputStream tarIn = new ByteArrayInputStream(tarData);

        ArchiveInputStream tarAis = factory.createArchiveInputStream(tarIn);

        assertNotNull(tarAis);
        tarAis.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_AutoDetect_NoArchiveFound() throws ArchiveException, IOException {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(in);
    }
}