package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Test with a known archive type
        InputStream input = new ByteArrayInputStream(new byte[]{"P PK\003\020\012"});
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        // Test with an unknown archive type
        input = new ByteArrayInputStream(new byte[]{"!<arch>\000\002 0 type tar\000"});
        ArchiveInputStream archiveInputStream2 = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        assertNull(archiveInputStream2);

        // Test with null input stream
        InputStream nullInputStream = null;
        assertNull(new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, nullInputStream));
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test with a known archive type
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

        // Test with an unknown archive type
        OutputStream output2 = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream2 = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, output2);
        assertNull(archiveOutputStream2);

        // Test with null output stream
        OutputStream nullOutputStream = null;
        assertNull(new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, nullOutputStream));
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknownType() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"P PK\003\020\012"});
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(null, input);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
    }
}