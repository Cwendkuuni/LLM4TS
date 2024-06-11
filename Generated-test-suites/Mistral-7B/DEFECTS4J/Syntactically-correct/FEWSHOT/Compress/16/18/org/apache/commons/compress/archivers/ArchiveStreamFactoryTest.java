package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Test AR archive
        InputStream arInputStream = new ByteArrayInputStream(new byte[]{"!"[0], '!'[0], '!'[0], 'a', 'r', 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0});
        ArchiveInputStream expectedArInputStream = new ArArchiveInputStream(arInputStream);
        ArchiveInputStream actualArInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);

        assertEquals(expectedArInputStream.getClass(), actualArInputStream.getClass());

        // Test ZIP archive
        InputStream zipInputStream = new ByteArrayInputStream(new byte[]{'P', 'P', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        ArchiveInputStream expectedZipInputStream = new ZipArchiveInputStream(zipInputStream);
        ArchiveInputStream actualZipInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);

        assertEquals(expectedZipInputStream.getClass(), actualZipInputStream.getClass());

        // Test unknown archive
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[]{'?'[0], '?'[0], '?'[0]});
        ArchiveInputStream actualUnknownInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, unknownInputStream);

        assertNull(actualUnknownInputStream);

        // Test Tar archive with a valid header
        InputStream tarInputStream = new ByteArrayInputStream(new byte[]{'!'[0], '!'[0], '!'[0], 't', 'a', 'r', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        ArchiveInputStream expectedTarInputStream = new TarArchiveInputStream(tarInputStream);
        ArchiveInputStream actualTarInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);

        assertEquals(expectedTarInputStream.getClass(), actualTarInputStream.getClass());

        TarEntry tarEntry = actualTarInputStream.getNextEntry();
        assertNotNull(tarEntry);
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test AR archive
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expectedArOutputStream = new ArArchiveOutputStream(outputStream);
        ArchiveOutputStream actualArOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);

        assertEquals(expectedArOutputStream.getClass(), actualArOutputStream.getClass());

        // Test ZIP archive
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expectedZipOutputStream = new ZipArchiveOutputStream(zipOutputStream);
        ArchiveOutputStream actualZipOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);

        assertEquals(expectedZipOutputStream.getClass(), actualZipOutputStream.getClass());

        // Test unknown archive
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream actualUnknownOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, unknownOutputStream);

        assertNull(actualUnknownOutputStream);
    }
}