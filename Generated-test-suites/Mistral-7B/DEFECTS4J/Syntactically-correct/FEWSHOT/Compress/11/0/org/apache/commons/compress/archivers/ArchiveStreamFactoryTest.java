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
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        // Test AR
        InputStream arInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInput);
        assertInstanceOf(ArArchiveInputStream.class, arResult);

        // Test AR with signature
        byte[] arSignature = new byte[12];
        InputStream arWithSignatureInput = new ByteArrayInputStream(arSignature);
        ArchiveInputStream arWithSignatureResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arWithSignatureInput);
        assertInstanceOf(ArArchiveInputStream.class, arWithSignatureResult);

        // Test CPIO
        InputStream cpioInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInput);
        assertInstanceOf(CpioArchiveInputStream.class, cpioResult);

        // Test DUMP
        InputStream dumpInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInput);
        assertInstanceOf(DumpArchiveInputStream.class, dumpResult);

        // Test JAR
        InputStream jarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInput);
        assertInstanceOf(JarArchiveInputStream.class, jarResult);

        // Test TAR
        InputStream tarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInput);
        assertInstanceOf(TarArchiveInputStream.class, tarResult);

        // Test ZIP
        InputStream zipInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInput);
        assertInstanceOf(ZipArchiveInputStream.class, zipResult);

        // Test null archiverName
        InputStream nullArchiverNameInput = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(null, nullArchiverNameInput);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test null inputStream
        String archiverName = "AR";
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, null);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test unknown archiverName
        InputStream unknownArchiverNameInput = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream("UnknownArchiver", unknownArchiverNameInput);
            assert false;
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test AR
        OutputStream arOutput = new ByteArrayOutputStream();
        ArchiveOutputStream arResult = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutput);
        assertInstanceOf(ArArchiveOutputStream.class, arResult);

        // Test JAR
        OutputStream jarOutput = new ByteArrayOutputStream();
        ArchiveOutputStream jarResult = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutput);
        assertInstanceOf(JarArchiveOutputStream.class, jarResult);

        // Test null archiverName
        OutputStream nullArchiverNameOutput = new ByteArrayOutputStream();
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, nullArchiverNameOutput);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test null outputStream
        String archiverName = "AR";
        try {
            ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, null);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithSignatureDetection() throws ArchiveException, IOException {
        // Test AR
        ByteArrayInputStream arInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(arInput);
        assertNull(arResult);

        byte[] arSignature = ArArchiveInputStream.createMagic();
        InputStream arWithSignatureInput = new ByteArrayInputStream(arSignature);
        ArchiveInputStream arWithSignatureResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(arWithSignatureInput);
        assertInstanceOf(ArArchiveInputStream.class, arWithSignatureResult);

        // Test CPIO
        ByteArrayInputStream cpioInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(cpioInput);
        assertNull(cpioResult);

        byte[] cpioSignature = CpioArchiveInputStream.createMagic();
        InputStream cpioWithSignatureInput = new ByteArrayInputStream(cpioSignature);
        ArchiveInputStream cpioWithSignatureResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(cpioWithSignatureInput);
        assertInstanceOf(CpioArchiveInputStream.class, cpioWithSignatureResult);

        // Test DUMP
        ByteArrayInputStream dumpInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(dumpInput);
        assertNull(dumpResult);

        byte[] dumpSignature = DumpArchiveInputStream.createMagic();
        InputStream dumpWithSignatureInput = new ByteArrayInputStream(dumpSignature);
        ArchiveInputStream dumpWithSignatureResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(dumpWithSignatureInput);
        assertInstanceOf(DumpArchiveInputStream.class, dumpWithSignatureResult);

        // Test JAR
        ByteArrayInputStream jarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(jarInput);
        assertInstanceOf(JarArchiveInputStream.class, jarResult);

        // Test TAR
        ByteArrayInputStream tarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(tarInput);
        assertNull(tarResult);

        byte[] tarHeader = new byte[512];
        InputStream tarWithHeaderInput = new ByteArrayInputStream(tarHeader);
        ArchiveInputStream tarWithHeaderResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(tarWithHeaderInput);
        assertInstanceOf(TarArchiveInputStream.class, tarWithHeaderResult);

        // Test ZIP
        ByteArrayInputStream zipInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipResult = ArchiveStreamFactory.getInstance().createArchiveInputStream(zipInput);
        assertInstanceOf(ZipArchiveInputStream.class, zipResult);

        // Test unknown archive
        InputStream unknownArchiveInput = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance().createArchiveInputStream(unknownArchiveInput);
            assert false;
        } catch (ArchiveException e) {
            // expected
        }
    }
}