package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

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
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import org.junit.Test;

/**
 * Test class for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test for createArchiveInputStream()
     */
    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        final String archiverName = "zip";

        // Test with null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(archiverName, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with valid input stream
        final InputStream in = new ByteArrayInputStream(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
        ArchiveInputStream ais = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, in);
        assertInstanceOf(ZipArchiveInputStream.class, ais);
        ais.close();

        // Test with unknown archiver name
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }

        // Test with non-supported archiver name
        try {
            new ArchiveStreamFactory().createArchiveInputStream("tar", in);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        }
    }

    /**
     * Test for createArchiveOutputStream()
     */
    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        final String archiverName = "zip";

        // Test with null output stream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(archiverName, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test with valid output stream
        final OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream oos = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, out);
        assertInstanceOf(ZipArchiveOutputStream.class, oos);
        oos.close();

        // Test with unknown archiver name
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", out);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }

        // Test with non-supported archiver name
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("tar", out);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        }
    }

    /**
     * Test for createArchiveInputStream() autodetection
     */
    @Test
    public void testCreateArchiveInputStreamAutoDetection() throws IOException, ArchiveException {
        final InputStream in = new ByteArrayInputStream(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04, (byte)0x14, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x06, (byte)0x58, (byte)0x43, (byte)0x41, (byte)0x4D, (byte)0x50, (byte)0x4C, (byte)0x45, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0B, (byte)0x05, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00});

        // Test with null encoding
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertInstanceOf(ZipArchiveInputStream.class, ais);
        ais.close();

        // Test with valid encoding
        String encoding = "UTF-8";
        ais = new ArchiveStreamFactory().createArchiveInputStream(in, encoding);
        assertInstanceOf(ZipArchiveInputStream.class, ais);
        ais.close();

        // Test with invalid encoding
        String invalidEncoding = "INVALID";
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(in, invalidEncoding);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }

        // Test with unknown archiver format
        byte[] unknownArchiveSignature = new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04, (byte)0x14, (byte)0x01, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x08, (byte)0x06, (byte)0x58, (byte)0x43, (byte)0x41, (byte)0x4D, (byte)0x50, (byte)0x4C, (byte)0x45, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x0B, (byte)0x05, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
        in.reset();
        InputStream signatureStream = new ByteArrayInputStream(unknownArchiveSignature);
        try {
            ais = new ArchiveStreamFactory().createArchiveInputStream(signatureStream, encoding);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
        in.reset();
    }
}