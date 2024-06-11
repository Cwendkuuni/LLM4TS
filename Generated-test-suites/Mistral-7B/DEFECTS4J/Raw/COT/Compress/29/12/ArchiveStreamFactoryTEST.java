package org.apache.commons.compress.archivers.tests;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test instance.
     */
    private ArchiveStreamFactory factory;

    /**
     * Sets up test environment.
     * @throws IOException if initialization fails
     */
    @Before
    public void setUp() throws IOException {
        factory = new ArchiveStreamFactory();
    }

    // ============================================================================================================================
    // createArchiveInputStream
    // ============================================================================================================================

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with AR.
     * @throws ArchiveException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamAR() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.AR;
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.ar"));
        ArchiveInputStream expected = new ARArchiveInputStream(input);

        ArchiveInputStream actual = factory.createArchiveInputStream(archiverName, input);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with ARJ.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamARJ() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.ARJ;
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.arj"));
        ArchiveInputStream expected = new ArjArchiveInputStream(input);

        ArchiveInputStream actual = factory.createArchiveInputStream(archiverName, input);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with CPIO.
     * @throws ArchiveException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamCpio() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.CPIO;
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.cpio"));
        ArchiveInputStream expected = new CpioArchiveInputStream(input);

        ArchiveInputStream actual = factory.createArchiveInputStream(archiverName, input);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with DUMP.
     * @throws ArchiveException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamDump() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.DUMP;
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.dump"));
        ArchiveInputStream expected = new DumpArchiveInputStream(input);

        ArchiveInputStream actual = factory.createArchiveInputStream(archiverName, input);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with JAR.
     * @throws ArchiveException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamJar() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.JAR;
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.jar"));
        JarArchiveInputStream expected = new JarArchiveInputStream(input);

        ArchiveInputStream actual = factory.createArchiveInputStream(archiverName, input);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with TAR.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamTar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.TAR;
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.tar"));
        TarArchiveInputStream expected = new TarArchiveInputStream(input);

        ArchiveInputStream actual = factory.createArchiveInputStream(archiverName, input);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with ZIP.
     * @throws ArchiveException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamZip() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.zip"));
        ZipArchiveInputStream expected = new ZipArchiveInputStream(input);

        ArchiveInputStream actual = factory.createArchiveInputStream(archiverName, input);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with SEVEN_Z.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamSevenZ() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.SEVEN_Z;
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.7z"));
        assertThrows(StreamingNotSupportedException.class, () -> factory.createArchiveInputStream(archiverName, input));
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with unsupported archiver name.
     * @throws ArchiveException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamUnsupported() throws ArchiveException {
        String archiverName = "unsupported";
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.tar"));
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(archiverName, input));
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)} autodetection.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test
    public void testCreateArchiveInputStreamAutodetection() throws ArchiveException, IOException {
        InputStream input = new ByteArrayInputStream(TestUtils.getResourceAsBytes("testdata/test.tar"));
        ArchiveInputStream actual = factory.createArchiveInputStream(input);
        TarArchiveInputStream expected = new TarArchiveInputStream(input);

        assertSame(expected, actual);
    }

    // ============================================================================================================================
    // createArchiveOutputStream
    // ============================================================================================================================

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with AR.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test
    public void testCreateArchiveOutputStreamAR() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream expected = new ARArchiveOutputStream(output);

        ArchiveOutputStream actual = factory.createArchiveOutputStream(archiverName, output);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with ARJ.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateArchiveOutputStreamARJ() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.ARJ;
        OutputStream output = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(archiverName, output);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with CPIO.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test
    public void testCreateArchiveOutputStreamCpio() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.CPIO;
        OutputStream output = new ByteArrayOutputStream();
        CpioArchiveOutputStream expected = new CpioArchiveOutputStream(output);

        ArchiveOutputStream actual = factory.createArchiveOutputStream(archiverName, output);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with DUMP.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateArchiveOutputStreamDump() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.DUMP;
        OutputStream output = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(archiverName, output);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with JAR.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test
    public void testCreateArchiveOutputStreamJar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.JAR;
        OutputStream output = new ByteArrayOutputStream();
        JarArchiveOutputStream expected = new JarArchiveOutputStream(output);

        ArchiveOutputStream actual = factory.createArchiveOutputStream(archiverName, output);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with TAR.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test
    public void testCreateArchiveOutputStreamTar() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.TAR;
        OutputStream output = new ByteArrayOutputStream();
        TarArchiveOutputStream expected = new TarArchiveOutputStream(output);

        ArchiveOutputStream actual = factory.createArchiveOutputStream(archiverName, output);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with ZIP.
     * @throws ArchiveException if test fails
     */
    @Test
    public void testCreateArchiveOutputStreamZip() throws ArchiveException {
        String archiverName = ArchiveStreamFactory.ZIP;
        OutputStream output = new ByteArrayOutputStream();
        ZipArchiveOutputStream expected = new ZipArchiveOutputStream(output);

        ArchiveOutputStream actual = factory.createArchiveOutputStream(archiverName, output);
        assertSame(expected, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with SEVEN_Z.
     * @throws ArchiveException if test fails
     * @throws IOException if test fails
     */
    @Test
    public void testCreateArchiveOutputStreamSevenZ() throws ArchiveException, IOException {
        String archiverName = ArchiveStreamFactory.SEVEN_Z;
        OutputStream output = new ByteArrayOutputStream();
        SevenZFile sevenZFile = new SevenZFile(output);
        sevenZFile.setForceLongFileNames(true);

        ArchiveOutputStream actual = factory.createArchiveOutputStream(archiverName, output);
        assertSame(sevenZFile, actual);
    }

    /**
     * Tests {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with unsupported archiver name.
     * @throws ArchiveException if test fails
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnsupported() throws ArchiveException {
        String archiverName = "unsupported";
        OutputStream output = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(archiverName, output);
    }

    // ============================================================================================================================
    // entryEncoding
    // ============================================================================================================================

    /**
     * Tests {@link ArchiveStreamFactory#getEntryEncoding()}.
     */
    @Test
    public void testGetEntryEncoding() {
        assertNull(factory.getEntryEncoding());
    }

    /**
     * Tests {@link ArchiveStreamFactory#setEntryEncoding(String)} and {@link ArchiveStreamFactory#getEntryEncoding()}.
     */
    @Test
    public void testSetAndGetEntryEncoding() {
        String encoding = "UTF-8";
        factory.setEntryEncoding(encoding);
        assertEquals(encoding, factory.getEntryEncoding());
    }

}