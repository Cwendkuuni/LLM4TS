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
    public void testCreateArchiveInputStream_AR() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = ArchiveStreamFactory.AR;
        final ArchiveInputStream expected = new ArArchiveInputStream(input);
        final ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input);
        assertInstanceOf(ArArchiveInputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullInput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.AR;
        final InputStream input = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_AR_NullArchiverName() throws ArchiveException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = ArchiveStreamFactory.CPIO;
        final ArchiveInputStream expected = new CpioArchiveInputStream(input);
        final ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input);
        assertInstanceOf(CpioArchiveInputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO_NullInput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.CPIO;
        final InputStream input = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_CPIO_NullArchiverName() throws ArchiveException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_Dump() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = ArchiveStreamFactory.DUMP;
        final ArchiveInputStream expected = new DumpArchiveInputStream(input);
        final ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input);
        assertInstanceOf(DumpArchiveInputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveInputStream_Dump_NullInput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.DUMP;
        final InputStream input = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_Dump_NullArchiverName() throws ArchiveException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = ArchiveStreamFactory.JAR;
        final ArchiveInputStream expected = new JarArchiveInputStream(input);
        final ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input);
        assertInstanceOf(JarArchiveInputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveInputStream_JAR_NullInput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.JAR;
        final InputStream input = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_JAR_NullArchiverName() throws ArchiveException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = ArchiveStreamFactory.TAR;
        final ArchiveInputStream expected = new TarArchiveInputStream(input);
        final ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input);
        assertInstanceOf(TarArchiveInputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveInputStream_TAR_NullInput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.TAR;
        final InputStream input = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_TAR_NullArchiverName() throws ArchiveException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = ArchiveStreamFactory.ZIP;
        final ArchiveInputStream expected = new ZipArchiveInputStream(input);
        final ArchiveInputStream actual = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input);
        assertInstanceOf(ZipArchiveInputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP_NullInput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.ZIP;
        final InputStream input = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveInputStream_ZIP_NullArchiverName() throws ArchiveException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, input));
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = ArchiveStreamFactory.AR;
        final ArchiveOutputStream expected = new ArArchiveOutputStream(output);
        final ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output);
        assertInstanceOf(ArArchiveOutputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_NullOutput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.AR;
        final OutputStream output = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_AR_NullArchiverName() throws ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = ArchiveStreamFactory.CPIO;
        final ArchiveOutputStream expected = new CpioArchiveOutputStream(output);
        final ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output);
        assertInstanceOf(CpioArchiveOutputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO_NullOutput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.CPIO;
        final OutputStream output = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO_NullArchiverName() throws ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_Dump() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = ArchiveStreamFactory.DUMP;
        final ArchiveOutputStream expected = new DumpArchiveOutputStream(output);
        final ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output);
        assertInstanceOf(DumpArchiveOutputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveOutputStream_Dump_NullOutput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.DUMP;
        final OutputStream output = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_Dump_NullArchiverName() throws ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = ArchiveStreamFactory.JAR;
        final ArchiveOutputStream expected = new JarArchiveOutputStream(output);
        final ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output);
        assertInstanceOf(JarArchiveOutputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR_NullOutput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.JAR;
        final OutputStream output = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_JAR_NullArchiverName() throws ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = ArchiveStreamFactory.TAR;
        final ArchiveOutputStream expected = new TarArchiveOutputStream(output);
        final ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output);
        assertInstanceOf(TarArchiveOutputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveOutputStream_TAR_NullOutput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.TAR;
        final OutputStream output = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_TAR_NullArchiverName() throws ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = ArchiveStreamFactory.ZIP;
        final ArchiveOutputStream expected = new ZipArchiveOutputStream(output);
        final ArchiveOutputStream actual = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output);
        assertInstanceOf(ZipArchiveOutputStream.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP_NullOutput() throws ArchiveException {
        final String archiverName = ArchiveStreamFactory.ZIP;
        final OutputStream output = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP_NullArchiverName() throws ArchiveException {
        final OutputStream output = new ByteArrayOutputStream();
        final String archiverName = null;
        assertNull("Expected null", ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, output));
    }
}