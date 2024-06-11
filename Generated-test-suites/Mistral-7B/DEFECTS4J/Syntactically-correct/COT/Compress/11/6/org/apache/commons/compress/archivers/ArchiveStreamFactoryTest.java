package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import org.apache.commons.compress.test.utils.TestUtils;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidArguments() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream(TestUtils.loadResource("archives/test.ar"));
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws Exception {
        String archiverName = "unkown";
        InputStream inputStream = new ByteArrayInputStream(TestUtils.loadResource("archives/test.ar"));
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream));
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = null;
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws Exception {
        String archiverName = "unkown";
        OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = null;
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream));
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamWithSignature() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(TestUtils.loadResource("archives/test.ar".getBytes()));
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        String detectedArchiverName = archiveInputStream.getFormat().getName();
        if (ArchiveStreamFactory.AR.equalsIgnoreCase(detectedArchiverName)) {
            archiveInputStream.close();
            return;
        }
        assertThrows(ArchiveException.class, () -> {
            archiveInputStream.getNextEntry();
            ArchiveInputStream arInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
            arInputStream.close();
        });
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidSignature() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(TestUtils.loadResource("archives/test.gz".getBytes()));
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream));
    }

    @Test
    public void testCreateArchiveInputStreamWithNullSignature() throws Exception {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(TestUtils.loadResource("archives/test.ar".getBytes()));
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream));
    }

}