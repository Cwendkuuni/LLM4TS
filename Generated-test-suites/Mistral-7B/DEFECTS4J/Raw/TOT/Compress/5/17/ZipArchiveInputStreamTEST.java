package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    @Test
    public void testZipArchiveInputStream() throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get("test.zip"))) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            ArchiveEntry entry;

            // Test getting next entry
            while ((entry = zipArchiveInputStream.getNextEntry()) != null) {
                String name = entry.getName();
                System.out.println("Entry Name: " + name);

                // Test reading data
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zipArchiveInputStream.read(buffer)) > 0) {
                    // Do something with the data
                }

                zipArchiveInputStream.closeEntry();
            }

            // Test end of stream
            Assert.assertNull(zipArchiveInputStream.getNextEntry());

            // Test closing stream
            zipArchiveInputStream.close();

        } catch (ZipException e) {
            // Handle ZipException
            e.printStackTrace();
        }
    }

    @Test(expected = IOException.class)
    public void testInvalidSignature() throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04})) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            zipArchiveInputStream.getNextEntry();
        }
    }

    @Test(expected = EOFException.class)
    public void testEndOfStream() throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(new byte[0])) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            zipArchiveInputStream.getNextEntry();
            zipArchiveInputStream.read();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(new byte[0])) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            byte[] buffer = new byte[1];
            zipArchiveInputStream.read(buffer, -1, 0);
        }
    }

    @Test(expected = IOException.class)
    public void testNoEntry() throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(new byte[0])) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            zipArchiveInputStream.getNextEntry();
            zipArchiveInputStream.read();
            zipArchiveInputStream.getNextEntry();
        }
    }

    @Test
    public void testInvalidDataDescriptor() throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x04, (byte) 0x03, (byte) 0x04, 0x05, (byte) 0x01, 0x02, 0x03, 0x04, (byte) 0x01, 0x02, 0x03, 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08})) {
            ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
            zipArchiveInputStream.getNextEntry();
            zipArchiveInputStream.closeEntry();
        }
    }
}