package org.apache.commons.compress.archivers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        archiveStreamFactory = new ArchiveStreamFactory();
        inputStream = new ByteArrayInputStream("test".getBytes());
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        archiveInputStream.close();

        archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();

        // Add tests for the rest of the archive formats
        // ...

        // Test for unsupported archiver name
        boolean exceptionThrown = false;
        try {
            archiveInputStream = archiveStreamFactory.createArchiveInputStream("invalid", inputStream);
        } catch (ArchiveException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
        archiveOutputStream.close();

        archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        archiveOutputStream.close();

        // Add tests for the rest of the archive formats
        // ...

        // Test for unsupported archiver name
        boolean exceptionThrown = false;
        try {
            archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("invalid", outputStream);
        } catch (ArchiveException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect() throws Exception {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();

        // Add more tests for autodetection of archive formats
        // ...

        // Test for unsupported archive format
        boolean exceptionThrown = false;
        byte[] signature = new byte[4];
        inputStream.read(signature);
        try {
            archiveInputStream = archiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(signature));
        } catch (ArchiveException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}