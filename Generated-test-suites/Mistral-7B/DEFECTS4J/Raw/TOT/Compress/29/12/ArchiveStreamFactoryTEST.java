package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for org.apache.commons.compress.archivers.ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating an archive input stream from a known archive format.
     */
    @org.junit.Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        final InputStream input = new ByteArrayInputStream(
            "!<archiver type='zip'>".getBytes());
        ArchiveInputStream archive = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        assertNotNull(archive);
        assertTrue(archive instanceof ZipArchiveInputStream);
        assertFalse(archive.hasNextEntry());
        archive.close();
    }

    /**
     * Test creating an archive output stream for a known archive format.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archive = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertNotNull(archive);
        assertTrue(archive instanceof ZipArchiveOutputStream);
        archive.finish();
        archive.close();
        InputStream test = new ByteArrayInputStream(output.toByteArray());
        ArchiveInputStream in = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, test);
        assertNotNull(in);
        assertTrue(in instanceof ZipArchiveInputStream);
        in.close();
    }

    /**
     * Test creating an archive input stream from an unknown format.
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testUnknownFormat() throws IOException {
        final InputStream input = new ByteArrayInputStream("unknown".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream("unknown", input);
    }

    /**
     * Test creating an archive output stream for an unknown format.
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testCreateUnknownOutputStream() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", output);
    }

    /**
     * Test creating an archive input stream from a non-seekable input stream.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNonSeekableInputStream() throws IOException {
        final InputStream input = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }

            @Override
            public void mark(int readlimit) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void reset() throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        new ArchiveStreamFactory().createArchiveInputStream("zip", input);
    }

    /**
     * Test creating an archive input stream from a null input stream.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() {
        new ArchiveStreamFactory().createArchiveInputStream("zip", null);
    }

    /**
     * Test creating an archive output stream for a null output stream.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullOutputStream() {
        new ArchiveStreamFactory().createArchiveOutputStream("zip", null);
    }

    /**
     * Test creating an archive input stream from an invalid archive signature.
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testInvalidSignature() throws IOException {
        final InputStream input = new ByteArrayInputStream("!<archiver type='unknown'>".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream("unknown", input);
    }

    /**
     * Test creating an archive input stream from a archive signature that is too short.
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testShortSignature() throws IOException {
        final InputStream input = new ByteArrayInputStream("!<archiver type='zip'>".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream("zip", input);
    }

    /**
     * Test creating an archive input stream from a archive signature that is too long.
     */
    @org.junit.Test(expected = ArchiveException.class)
    public void testLongSignature() throws IOException {
        final InputStream input = new ByteArrayInputStream(
            "!<archiver type='zip'>abcdefghijklmnopqrstuvwxyz".getBytes());
        new ArchiveStreamFactory().createArchiveInputStream("zip", input);
    }

    /**
     * Test creating an archive output stream for a null archiver name.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullArchiverName() {
        final OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, output);
    }

    /**
     * Test creating an archive output stream for a null output stream.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullArchiveOutputStream() {
        new ArchiveStreamFactory().createArchiveOutputStream("zip", null);
    }

    /**
     * Test creating an archive output stream for an invalid archiver name.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidArchiverName() {
        new ArchiveStreamFactory().createArchiveOutputStream("invalid", new ByteArrayOutputStream());
    }
}