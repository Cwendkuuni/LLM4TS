package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.DefaultCompressorStrategy;
import org.apache.commons.compress.compressors.DefaultStreamFactory;
import org.apache.commons.compress.compressors.StreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    ArchiveStreamFactory factory = new ArchiveStreamFactory();

    @Test
    public void testCreateArchiveInputStream_AR() throws IOException {
        InputStream input = new ByteArrayInputStream("Test data for AR archive".getBytes());
        ArchiveInputStream arInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, input);
        assertNotNull(arInput);
        arInput.close();
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws IOException {
        InputStream input = new ByteArrayInputStream("Test data for CPIO archive".getBytes());
        ArchiveInputStream cpioInput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
        assertNotNull(cpioInput);
        cpioInput.close();
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() throws IOException {
        InputStream input = new ByteArrayInputStream("Test data for DUMP archive".getBytes());
        ArchiveInputStream dumpInput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
        assertNotNull(dumpInput);
        dumpInput.close();
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws IOException {
        InputStream input = new ByteArrayInputStream("Test data for JAR archive".getBytes());
        ArchiveInputStream jarInput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        assertNotNull(jarInput);
        jarInput.close();
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws IOException {
        InputStream input = new ByteArrayInputStream("Test data for TAR archive".getBytes());
        ArchiveInputStream tarInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        assertNotNull(tarInput);
        tarInput.close();
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws IOException {
        InputStream input = new ByteArrayInputStream("Test data for ZIP archive".getBytes());
        ArchiveInputStream zipInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        assertNotNull(zipInput);
        zipInput.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownFormat() throws IOException {
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        factory.createArchiveInputStream("unknownFormat", input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() {
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(null, input);
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        });
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream arOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertNotNull(arOutput);
        arOutput.finish();
        arOutput.close();
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        assertNotNull(cpioOutput);
        cpioOutput.finish();
        cpioOutput.close();
    }

    @Test
    public void testCreateArchiveOutputStream_DUMP() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream dumpOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
        assertNotNull(dumpOutput);
        dumpOutput.finish();
        dumpOutput.close();
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        assertNotNull(jarOutput);
        jarOutput.putNextEntry(new JarArchiveEntry("test.txt"));
        jarOutput.write("Test data".getBytes());
        jarOutput.finish();
        jarOutput.close();
        assertFalse(jarOutput.isClosed());
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        assertNotNull(tarOutput);
        tarOutput.finish();
        tarOutput.close();
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertNotNull(zipOutput);
        zipOutput.putNextEntry(new ZipArchiveEntry("test.txt"));
        zipOutput.write("Test data".getBytes());
        zipOutput.finish();
        zipOutput.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownFormat() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("unknownFormat", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() {
        OutputStream output = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream(null, output);
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        });
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect() throws IOException {
        byte[] tarHeader = new byte[512];
        InputStream input = new ByteArrayInputStream(tarHeader);
        ArchiveInputStream in = factory.createArchiveInputStream(input);
        assertNotNull(in);
        assertInstanceOf(TarArchiveInputStream.class, in);
        in.close();
    }

    @Test
    public void testCreateArchiveOutputStream_AutodetectWithInvalidTar() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = factory.createArchiveOutputStream(output);
        assertNotNull(out);
        try {
            out.putNextEntry(new TarArchiveEntry("test.txt"));
            out.write("Test data".getBytes());
            out.closeArchiveEntry();
            out.finish();
            out.close();
        } finally {
            out.close();
        }
        InputStream input = new ByteArrayInputStream(tarHeader);
        ArchiveInputStream in = factory.createArchiveInputStream(input);
        assertNotNull(in);
        assertNull(in.getNextEntry());
        in.close();
    }

    @Test
    public void testCreateArchiveOutputStream_AutodetectWithZip() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = factory.createArchiveOutputStream(output);
        out.putNextEntry(new ZipArchiveEntry("test.txt"));
        out.write("Test data".getBytes());
        out.finish();
        out.close();

        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream in = factory.createArchiveInputStream(input);
        assertNull(in.getNextEntry());
        in.close();

        input = new ByteArrayInputStream(new byte[2]);
        in = factory.createArchiveInputStream(input);
        assertNull(in.getNextEntry());
        in.close();

        input = new ByteArrayInputStream(new byte[3]);
        in = factory.createArchiveInputStream(input);
        assertNull(in.getNextEntry());
        in.close();

        input = new ByteArrayInputStream(new byte[4]);
        in = factory.createArchiveInputStream(input);
        ArchiveEntry entry = in.getNextEntry();
        assertNotNull(entry);
        assertInstanceOf(ZipArchiveEntry.class, entry);
        in.close();

        input = new ByteArrayInputStream(new byte[5]);
        in = factory.createArchiveInputStream(input);
        entry = in.getNextEntry();
        assertNotNull(entry);
        assertInstanceOf(ZipArchiveEntry.class, entry);
        in.close();
    }
}