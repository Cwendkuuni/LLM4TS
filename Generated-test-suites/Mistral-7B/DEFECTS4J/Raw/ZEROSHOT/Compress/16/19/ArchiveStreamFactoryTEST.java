package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

/**
 * Test for {@link ArchiveStreamFactory} class
 */
public class ArchiveStreamFactoryTest {

    @org.junit.Test
    public void testCreateArchiveInputStream() throws Exception {
        InputStream in;
        ArchiveInputStream expected;

        in = new ByteArrayInputStream("AR signature".getBytes());
        expected = new ArArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream("ar", in).getClass());

        in = new ByteArrayInputStream("ZIP signature".getBytes());
        expected = new ZipArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream("zip", in).getClass());

        in = new ByteArrayInputStream("TAR signature".getBytes());
        expected = new TarArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream("tar", in).getClass());

        in = new ByteArrayInputStream("JAR signature".getBytes());
        expected = new JarArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream("jar", in).getClass());

        in = new ByteArrayInputStream("CPIO signature".getBytes());
        expected = new CpioArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream("cpio", in).getClass());

        in = new ByteArrayInputStream("DUMP signature".getBytes());
        expected = new DumpArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream("dump", in).getClass());

        in = new ByteArrayInputStream("Unknown signature".getBytes());
        assertNull(ArchiveStreamFactory.createArchiveInputStream("unknown", in));

        in = new ByteArrayInputStream("AR signature".getBytes());
        assertFalse(ArchiveStreamFactory.createArchiveInputStream("jar", in) instanceof ArArchiveInputStream);
    }

    @org.junit.Test
    public void testCreateArchiveOutputStream() throws Exception {
        OutputStream out;
        ArchiveOutputStream expected;

        out = new ByteArrayOutputStream();
        expected = new ArArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream("ar", out).getClass());

        out = new ByteArrayOutputStream();
        expected = new ZipArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream("zip", out).getClass());

        out = new ByteArrayOutputStream();
        expected = new TarArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream("tar", out).getClass());

        out = new ByteArrayOutputStream();
        expected = new JarArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream("jar", out).getClass());

        out = new ByteArrayOutputStream();
        expected = new CpioArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream("cpio", out).getClass());

        out = new ByteArrayOutputStream();
        assertNull(ArchiveStreamFactory.createArchiveOutputStream("unknown", out));

        out = new ByteArrayOutputStream();
        assertFalse(ArchiveStreamFactory.createArchiveOutputStream("jar", out) instanceof ArArchiveOutputStream);
    }

    @org.junit.Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        InputStream in;
        ArchiveInputStream expected;

        in = new ByteArrayInputStream("AR signature".getBytes());
        expected = new ArArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream(in).getClass());

        in = new ByteArrayInputStream("ZIP signature".getBytes());
        expected = new ZipArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream(in).getClass());

        in = new ByteArrayInputStream("TAR signature".getBytes());
        expected = new TarArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream(in).getClass());

        in = new ByteArrayInputStream("JAR signature".getBytes());
        expected = new JarArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream(in).getClass());

        in = new ByteArrayInputStream("CPIO signature".getBytes());
        expected = new CpioArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream(in).getClass());

        in = new ByteArrayInputStream("DUMP signature".getBytes());
        expected = new DumpArchiveInputStream(in);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveInputStream(in).getClass());

        in = new ByteArrayInputStream("AR signature".getBytes());
        assertFalse(ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, in).isInstanceOf(ArArchiveInputStream.class));

        // Test exception in case of unknown signature
        InputStream unknown = new ByteArrayInputStream("Unknown signature".getBytes());
        try {
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, unknown);
            assertFalse(archiveInputStream instanceof ArArchiveInputStream);
        } catch (ArchiveException e) {
            // expected exception
        }
    }

    @org.junit.Test
    public void testCreateArchiveOutputStreamFromStream() throws Exception {
        OutputStream out;
        ArchiveOutputStream expected;

        out = new ByteArrayOutputStream();
        expected = new ArArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, out).getClass());

        out = new ByteArrayOutputStream();
        expected = new ZipArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out).getClass());

        out = new ByteArrayOutputStream();
        expected = new TarArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out).getClass());

        out = new ByteArrayOutputStream();
        expected = new JarArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out).getClass());

        out = new ByteArrayOutputStream();
        expected = new CpioArchiveOutputStream(out);
        assertSame(expected.getClass(), ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out).getClass());

        out = new ByteArrayOutputStream();
        assertNull(ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.UNKNOWN, out));

        out = new ByteArrayOutputStream();
        assertFalse(ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out) instanceof ArArchiveOutputStream);
    }
}