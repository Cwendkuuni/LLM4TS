package org.apache.commons.compress.archivers;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR
        InputStream arInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInput);
        assertTrue(arInputStream instanceof ArArchiveInputStream);

        // Test ZIP
        InputStream zipInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInput);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);

        // Test TAR
        InputStream tarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInput);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);

        // Test JAR
        InputStream jarInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInput);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);

        // Test CPIO
        InputStream cpioInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInput);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);

        // Test DUMP
        InputStream dumpInput = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInput);
        assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null, new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR
        OutputStream arOutput = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOutput);
        assertTrue(arOutputStream instanceof ArArchiveOutputStream);

        // Test ZIP
        OutputStream zipOutput = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutput);
        assertTrue(zipOutputStream instanceof ZipArchiveOutputStream);

        // Test TAR
        OutputStream tarOutput = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutput);
        assertTrue(tarOutputStream instanceof TarArchiveOutputStream);

        // Test JAR
        OutputStream jarOutput = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutput);
        assertTrue(jarOutputStream instanceof JarArchiveOutputStream);

        // Test CPIO
        OutputStream cpioOutput = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutput);
        assertTrue(cpioOutputStream instanceof CpioArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
    }

    @Test
    public void testCreateArchiveInputStreamDetect() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test ZIP
        InputStream zipInput = new ByteArrayInputStream(new byte[]{80, 75, 3, 4});
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(zipInput);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);

        // Test JAR
        InputStream jarInput = new ByteArrayInputStream(new byte[]{80, 75, 3, 4});
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(jarInput);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);

        // Test AR
        InputStream arInput = new ByteArrayInputStream(new byte[]{33, 60, 48, 72, 86, 14});
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(arInput);
        assertTrue(arInputStream instanceof ArArchiveInputStream);

        // Test CPIO
        InputStream cpioInput = new ByteArrayInputStream(new byte[]{71, 60, 35, 2});
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(cpioInput);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);

        // Test DUMP
        InputStream dumpInput = new ByteArrayInputStream(new byte[]{128, 18, 60, 31, 113, 97});
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(dumpInput);
        assertTrue(dumpInputStream instanceof DumpArchiveInputStream);

        // Test TAR
        InputStream tarInput = new ByteArrayInputStream(new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0});
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(tarInput);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamDetectNullInputStream() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamDetectMarkNotSupported() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamDetectNoArchiverFound() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(inputStream);
    }
    
}