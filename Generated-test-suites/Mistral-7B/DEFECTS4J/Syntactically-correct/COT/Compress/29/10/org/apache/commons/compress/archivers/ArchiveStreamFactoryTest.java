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
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.apache.commons.compress.utils.bytes.BigEndianByteOrderMarkInputStream;
import org.junit.Test;

/**
 * Test class for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        InputStream input = new ByteArrayInputStream(TestUtils.getTestData("testdata/testArchiveStreamFactory/validAr.tar"));
        ArchiveInputStream stream = new ArchiveStreamFactory().createArchiveInputStream("tar", input);
        TarArchiveEntry entry = (TarArchiveEntry) stream.getNextEntry();
        assertEquals("validAr.tar", entry.getName());
        stream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiveType() throws ArchiveException, IOException {
        InputStream input = new ByteArrayInputStream(TestUtils.getTestData("testdata/testArchiveStreamFactory/validAr.tar"));
        new ArchiveStreamFactory().createArchiveInputStream("unknown", input);
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream stream = new ArchiveStreamFactory().createArchiveOutputStream("tar", output);
        stream.putNextEntry(new TarArchiveEntry("test.txt"));
        stream.write("Test ArchiveStreamFactory".getBytes());
        stream.finishStream();
        stream.close();
        String actual = new String(((ByteArrayOutputStream) output).toByteArray());
        String expected = TestUtils.getTestData("testdata/testArchiveStreamFactory/expectedOutputTar.tar");
        assertEquals(expected, actual);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream("tar", null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiveType() throws ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", output);
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/validArchiveStreamFactoryInputStreamSignatureTestData.bin";
        InputStream input = new FileInputStream(testDataFileName);
        ArchiveInputStream stream = new ArchiveStreamFactory().createArchiveInputStream(input);
        TarArchiveEntry entry = (TarArchiveEntry) stream.getNextEntry();
        assertEquals("validArchiveStreamFactoryInputStreamSignatureTestData.bin", entry.getName());
        stream.close();
        IOUtils.closeQuietly(input);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamSignatureWithInvalidSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/invalidArchiveStreamFactoryInputStreamSignatureTestData.bin";
        InputStream input = new FileInputStream(testDataFileName);
        new ArchiveStreamFactory().createArchiveInputStream(input);
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignatureWithSevenZSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/sevenzSignature.7z";
        InputStream input = new FileInputStream(testDataFileName);
        try {
            new ArchiveStreamFactory().createArchiveInputStream(input);
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignatureWithZipSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/validZipSignature.zip";
        InputStream input = new FileInputStream(testDataFileName);
        JarArchiveInputStream jarStream = (JarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(input);
        JarArchiveEntry entry = jarStream.getNextJarEntry();
        assertEquals("validZipSignature.zip!/testdata/testArchiveStreamFactory/expectedOutputZip.txt", entry.getName());
        jarStream.close();
        IOUtils.closeQuietly(input);
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignatureWithJarSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/validJarSignature.jar";
        InputStream input = new FileInputStream(testDataFileName);
        JarArchiveInputStream jarStream = (JarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(input);
        JarArchiveEntry entry = jarStream.getNextJarEntry();
        assertEquals("validJarSignature.jar!/testdata/testArchiveStreamFactory/expectedOutputJar.txt", entry.getName());
        jarStream.close();
        IOUtils.closeQuietly(input);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamSignatureWithInvalidSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/invalidZipSignature.zip";
        InputStream input = new FileInputStream(testDataFileName);
        new ArchiveStreamFactory().createArchiveInputStream(input);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamSignatureWithInvalidJarSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/invalidJarSignature.jar";
        InputStream input = new FileInputStream(testDataFileName);
        new ArchiveStreamFactory().createArchiveInputStream(input);
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignatureWithArSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/validArSignature.ar";
        InputStream input = new FileInputStream(testDataFileName);
        ArArchiveInputStream arStream = (ArArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(input);
        ArArchiveEntry entry = arStream.getNextArEntry();
        assertEquals("validArSignature.ar!testdata/testArchiveStreamFactory/expectedOutputAr.txt", entry.getName());
        arStream.close();
        IOUtils.closeQuietly(input);
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignatureWithArjSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/validArjSignature.arj";
        InputStream input = new FileInputStream(testDataFileName);
        ArjArchiveInputStream arjStream = (ArjArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(input);
        ArjArchiveEntry entry = arjStream.getNextArjEntry();
        assertEquals("validArjSignature.arj!testdata/testArchiveStreamFactory/expectedOutputArj.txt", entry.getName());
        arjStream.close();
        IOUtils.closeQuietly(input);
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignatureWithCpioSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/validCpioSignature.cpio";
        InputStream input = new FileInputStream(testDataFileName);
        CpioArchiveInputStream cpioStream = (CpioArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(input);
        CpioArchiveEntry entry = cpioStream.getNextEntry();
        assertEquals("validCpioSignature.cpio!testdata/testArchiveStreamFactory/expectedOutputCpio.txt", entry.getName());
        cpioStream.close();
        IOUtils.closeQuietly(input);
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignatureWithDumpSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/validDumpSignature.dump";
        InputStream input = new FileInputStream(testDataFileName);
        DumpArchiveInputStream dumpStream = (DumpArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream(input);
        DumpArchiveEntry entry = (DumpArchiveEntry) dumpStream.getNextEntry();
        assertEquals("validDumpSignature.dump!testdata/testArchiveStreamFactory/expectedOutputDump.txt", entry.getName());
        dumpStream.close();
        IOUtils.closeQuietly(input);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamSignatureWithInvalidArSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/invalidArSignature.ar";
        InputStream input = new FileInputStream(testDataFileName);
        new ArchiveStreamFactory().createArchiveInputStream(input);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamSignatureWithInvalidArjSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/invalidArjSignature.arj";
        InputStream input = new FileInputStream(testDataFileName);
        new ArchiveStreamFactory().createArchiveInputStream(input);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamSignatureWithInvalidCpioSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/invalidCpioSignature.cpio";
        InputStream input = new FileInputStream(testDataFileName);
        new ArchiveStreamFactory().createArchiveInputStream(input);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamSignatureWithInvalidDumpSignature() throws ArchiveException, IOException {
        final String testDataFileName = "testdata/testArchiveStreamFactory/invalidDumpSignature.dump";
        InputStream input = new FileInputStream(testDataFileName);
        new ArchiveStreamFactory().createArchiveInputStream(input);
    }

    @Test
    public void testCreateArchiveInputStreamFromStreamSignatureWithBigEndianByteOrderMark() throws ArchiveException, IOException {
        InputStream input = new ByteArrayInputStream(TestUtils.getTestData("testdata/testArchiveStreamFactory/bigEndianSignature.tar"));
        ArchiveInputStream stream = new ArchiveStreamFactory().createArchiveInputStream(new BigEndianByteOrderMarkInputStream(input, true));
        TarArchiveEntry entry = (TarArchiveEntry) stream.getNextEntry();
        assertEquals("bigEndianSignature.tar", entry.getName());
        stream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamSignatureWithInvalidBigEndianByteOrderMark() throws ArchiveException, IOException {
        InputStream input = new ByteArrayInputStream(TestUtils.getTestData("testdata/testArchiveStreamFactory/invalidBigEndianSignature.tar"));
        new ArchiveStreamFactory().createArchiveInputStream(new BigEndianByteOrderMarkInputStream(input, true));
    }
}