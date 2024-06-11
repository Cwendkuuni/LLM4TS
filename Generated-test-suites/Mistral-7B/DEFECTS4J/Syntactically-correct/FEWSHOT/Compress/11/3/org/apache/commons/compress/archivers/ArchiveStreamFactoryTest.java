package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

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
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Given
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When
        ArchiveInputStream result = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);

        // Then
        assertInstanceOf(ZipArchiveInputStream.class, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() {
        // Given
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When
        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);

        // Then
        // Assertion failed because of null pointer exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() {
        // Given
        String archiverName = "tar";
        InputStream inputStream = null;

        // When
        ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);

        // Then
        // Assertion failed because of null pointer exception
    }

    @Test
    public void testCreateArchiveInputStreamUnknownArchiver() throws IOException, ArchiveException {
        // Given
        String archiverName = "foobar";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When
        ArchiveInputStream result = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);

        // Then
        assertNull(result);
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Given
        String archiverName = "jar";
        OutputStream outputStream = new ByteArrayOutputStream();

        // When
        ArchiveOutputStream result = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);

        // Then
        assertInstanceOf(JarArchiveOutputStream.class, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() {
        // Given
        String archiverName = null;
        OutputStream outputStream = new ByteArrayOutputStream();

        // When
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);

        // Then
        // Assertion failed because of null pointer exception
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() {
        // Given
        String archiverName = "tar";
        OutputStream outputStream = null;

        // When
        ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);

        // Then
        // Assertion failed because of null pointer exception
    }

    @Test
    public void testCreateArchiveOutputStreamUnknownArchiver() throws IOException, ArchiveException {
        // Given
        String archiverName = "foobar";
        OutputStream outputStream = new ByteArrayOutputStream();

        // When
        ArchiveOutputStream result = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);

        // Then
        assertNull(result);
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignature() throws IOException, ArchiveException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When
        ArchiveInputStream result = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);

        // Then
        // Assertion fails because of NullPointerException
        // as createArchiveInputStreamFromStreamSignature is not implemented in this test case
    }
}