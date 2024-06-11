package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.util.TestInputStreams;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream testAr = TestInputStreams.getTestInputStream(ArArchiveInputStream.class, "test-ar.tar.gz");
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, testAr));

        InputStream testArj = TestInputStreams.getTestInputStream(ArjArchiveInputStream.class, "test-arj.arj");
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, testArj));

        InputStream testCpio = TestInputStreams.getTestInputStream(CpioArchiveInputStream.class, "test-cpio.cpio");
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, testCpio));

        InputStream testDump = TestInputStreams.getTestInputStream(DumpArchiveInputStream.class, "test-dump.dump");
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, testDump));

        InputStream testJar = TestInputStreams.getTestInputStream(JarArchiveInputStream.class, "test-jar.jar");
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, testJar));

        InputStream testTar = TestInputStreams.getTestInputStream(TarArchiveInputStream.class, "test-tar.tar");
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, testTar));

        InputStream testZip = TestInputStreams.getTestInputStream(ZipArchiveInputStream.class, "test-zip.zip");
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, testZip));

        InputStream testSevenZ = TestInputStreams.getTestInputStream(SevenZFile.class, "test-7z.7z");
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, testSevenZ)); // SevenZFile not supported as InputStream

        InputStream empty = new ByteArrayInputStream(new byte[0]);
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, empty));
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream testAr = new ByteArrayOutputStream();
        ArchiveOutputStream ar = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, testAr);
        ar.close();
        assertNotNull(ar);

        OutputStream testArj = new ByteArrayOutputStream();
        ArchiveOutputStream arj = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, testArj);
        arj.close();
        assertNotNull(arj);

        OutputStream testCpio = new ByteArrayOutputStream();
        ArchiveOutputStream cpio = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, testCpio);
        cpio.close();
        assertNotNull(cpio);

        OutputStream testDump = new ByteArrayOutputStream();
        ArchiveOutputStream dump = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, testDump);
        dump.close();
        assertNotNull(dump);

        OutputStream testJar = new ByteArrayOutputStream();
        JarArchiveOutputStream jar = (JarArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, testJar);
        jar.close();
        assertNotNull(jar);

        OutputStream testTar = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, testTar);
        tar.close();
        assertNotNull(tar);

        OutputStream testZip = new ByteArrayOutputStream();
        ZipArchiveOutputStream zip = (ZipArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, testZip);
        zip.close();
        assertNotNull(zip);

        OutputStream testSevenZ = new ByteArrayOutputStream();
        SevenZFile sevenz = new SevenZFile(testSevenZ);
        sevenz.createArchive();
        sevenz.finish();
        SevenZFile sevenz2 = new SevenZFile(testSevenZ, "test.7z");
        ArchiveOutputStream sevenzOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, sevenz2.getInputStream());
        sevenzOutput.close();
        assertNotNull(sevenzOutput);
    }

    @Test
    public void testCreateInputOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        InputStream testAr = TestInputStreams.getTestInputStream(ArArchiveInputStream.class, "test-ar.tar.gz");
        ArchiveInputStream ar = factory.createArchiveInputStream(ArchiveStreamFactory.AR, testAr);
        assertNotNull(ar);

        InputStream testArj = TestInputStreams.getTestInputStream(ArjArchiveInputStream.class, "test-arj.arj");
        ArchiveInputStream arj = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, testArj);
        assertNotNull(arj);

        InputStream testCpio = TestInputStreams.getTestInputStream(CpioArchiveInputStream.class, "test-cpio.cpio");
        ArchiveInputStream cpio = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, testCpio);
        assertNotNull(cpio);

        InputStream testDump = TestInputStreams.getTestInputStream(DumpArchiveInputStream.class, "test-dump.dump");
        ArchiveInputStream dump = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, testDump);
        assertNotNull(dump);

        InputStream testJar = TestInputStreams.getTestInputStream(JarArchiveInputStream.class, "test-jar.jar");
        ArchiveInputStream jar = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, testJar);
        assertNotNull(jar);

        InputStream testTar = TestInputStreams.getTestInputStream(TarArchiveInputStream.class, "test-tar.tar");
        ArchiveInputStream tar = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, testTar);
        assertNotNull(tar);

        InputStream testZip = TestInputStreams.getTestInputStream(ZipArchiveInputStream.class, "test-zip.zip");
        ArchiveInputStream zip = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, testZip);
        assertNotNull(zip);

        InputStream testSevenZ = TestInputStreams.getTestInputStream(SevenZFile.class, "test-7z.7z");
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, testSevenZ)); // SevenZFile not supported as InputStream

        InputStream empty = new ByteArrayInputStream(new byte[0]);
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, empty));
        assertNull(factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, empty));
    }

    @Test
    public void testCreateOutputOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        OutputStream testAr = new ByteArrayOutputStream();
        ArchiveOutputStream ar = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, testAr);
        ar.close();
        assertNull(ar);

        OutputStream testArj = new ByteArrayOutputStream();
        ArchiveOutputStream arj = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, testArj);
        arj.close();
        assertNull(arj);

        OutputStream testCpio = new ByteArrayOutputStream();
        ArchiveOutputStream cpio = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, testCpio);
        cpio.close();
        assertNull(cpio);

        OutputStream testDump = new ByteArrayOutputStream();
        ArchiveOutputStream dump = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, testDump);
        dump.close();
        assertNull(dump);

        OutputStream testJar = new ByteArrayOutputStream();
        JarArchiveOutputStream jar = (JarArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, testJar);
        jar.close();
        assertNull(jar);

        OutputStream testTar = new ByteArrayOutputStream();
        TarArchiveOutputStream tar = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, testTar);
        tar.close();
        assertNull(tar);

        OutputStream testZip = new ByteArrayOutputStream();
        ZipArchiveOutputStream zip = (ZipArchiveOutputStream) factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, testZip);
        zip.close();
        assertNull(zip);

        OutputStream testSevenZ = new ByteArrayOutputStream();
        SevenZFile sevenz = new SevenZFile(testSevenZ, "test.7z");
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, testSevenZ)); // SevenZFile not supported as OutputStream

        OutputStream empty = new ByteArrayOutputStream();
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, empty));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, empty));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, empty));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, empty));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, empty));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, empty));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, empty));
        assertNull(factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, empty));
    }
}