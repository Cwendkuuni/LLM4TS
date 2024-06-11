package org.apache.commons.compress.archivers;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}
     * with correct archive name and valid InputStream.
     */
    @Test
    public void testCreateArchiveInputStreamWithValidArchiveNameAndValidInputStream() {
        String archiveName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(archiveName, archiveInputStream.getFormat());
        IOUtils.closeQuietly(archiveInputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}
     * with incorrect archive name.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithIncorrectArchiveName() {
        String archiveName = "invalid";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)}
     * with valid InputStream.
     */
    @Test
    public void testCreateArchiveInputStreamWithValidInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        Assert.assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)}
     * with invalid InputStream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithInvalidInputStream() {
        InputStream inputStream = null;
        new ArchiveStreamFactory().createArchiveInputStream(inputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}
     * with correct archive name and valid OutputStream.
     */
    @Test
    public void testCreateArchiveOutputStreamWithValidArchiveNameAndValidOutputStream() {
        String archiveName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, outputStream);
        Assert.assertNotNull(archiveOutputStream);
        Assert.assertEquals(archiveName, archiveOutputStream.getFormat());
        archiveOutputStream.finish();
        IOUtils.closeQuietly(archiveOutputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}
     * with incorrect archive name.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithIncorrectArchiveName() {
        String archiveName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, outputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(OutputStream)}
     * with valid OutputStream.
     */
    @Test
    public void testCreateArchiveOutputStreamWithValidOutputStream() {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream("zip", outputStream);
        Assert.assertNotNull(archiveOutputStream);
        archiveOutputStream.finish();
        IOUtils.closeQuietly(archiveOutputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(OutputStream)}
     * with invalid OutputStream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithInvalidOutputStream() {
        OutputStream outputStream = null;
        new ArchiveStreamFactory().createArchiveOutputStream("zip", outputStream);
    }

}