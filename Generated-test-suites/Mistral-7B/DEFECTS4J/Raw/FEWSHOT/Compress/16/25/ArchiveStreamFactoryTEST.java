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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithNameAndInputStream() {
        String archiverName = "ZIP";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.AR;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.CPIO;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.DUMP;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.JAR;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.TAR;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.ZIP;
        actual = new ZipArchiveInputStream(inputStream);
        ArchiveInputStream expected = new ZipArchiveInputStream(inputStream);
        assertInstanceOf(expected.getClass(), actual.getClass());
    }

    @Test
    public void testCreateArchiveOutputStreamWithNameAndOutputStream() {
        String archiverName = "AR";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.CPIO;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.DUMP;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.JAR;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.TAR;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.ZIP;
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        archiverName = ArchiveStreamFactory.AR;
        actual = new ArArchiveOutputStream(outputStream);
        ArchiveOutputStream expected = new ArArchiveOutputStream(outputStream);
        assertInstanceOf(expected.getClass(), actual.getClass());
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        inputStream = new ByteArrayInputStream(ZipArchiveInputStream.SIGNATURE);
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        inputStream = new ByteArrayInputStream(ArArchiveInputStream.SIGNATURE);
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        inputStream = new ByteArrayInputStream(CpioArchiveInputStream.SIGNATURE);
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        inputStream = new ByteArrayInputStream(DumpArchiveInputStream.SIGNATURE);
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        inputStream = new ByteArrayInputStream(JarArchiveInputStream.SIGNATURE);
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        inputStream = new ByteArrayInputStream(TarArchiveInputStream.SIGNATURE);
        actual = null;
        try {
            actual = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            // expected
        }
        assertNull(actual);

        inputStream = new ByteArrayInputStream(ZipArchiveInputStream.SIGNATURE);
        actual = new ZipArchiveInputStream(inputStream);
        ArchiveInputStream expected = new ZipArchiveInputStream(inputStream);
        assertInstanceOf(expected.getClass(), actual.getClass());
    }
}