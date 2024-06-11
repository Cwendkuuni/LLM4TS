package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test creating archive input streams from different archiver names
        for (String archiverName : new String[]{"AR", "ARJ", "CPIO", "DUMP", "JAR", "TAR", "ZIP", "SEVEN_Z"}) {
            InputStream in = new ByteArrayInputStream(getTestFileData(archiverName));
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, in);
            ArchiveEntry entry = ais.getNextEntry();
            Assert.assertNotNull(entry);
            ais.close();
        }

        // Test creating archive input stream with the wrong format
        InputStream in = new ByteArrayInputStream(getTestFileData("WRONG"));
        try {
            new ArchiveStreamFactory().createArchiveInputStream("WRONG", in);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test creating archive output streams from different archiver names
        for (String archiverName : new String[]{"AR", "ZIP", "TAR", "JAR", "CPIO"}) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, out);
            aos.putArchiveEntry(new JarEntry("test.txt"));
            aos.write(new byte[]{'T', 'e', 's', 't', '\n'});
            aos.closeArchiveEntry();
            aos.finish();
            aos.close();

            InputStream is = new ByteArrayInputStream(out.toByteArray());
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(archiverName, is);
            ArchiveEntry entry = ais.getNextEntry();
            Assert.assertNotNull(entry);
            Assert.assertEquals("test.txt", entry.getName());
            ais.close();
        }

        // Test creating archive output stream with the wrong format
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("WRONG", out);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        // Test creating archive input streams from different archiver types using stream autodetection
        for (String archiverName : new String[]{"AR", "JAR", "ZIP", "TAR"}) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] testData = getTestFileData(archiverName);
            out.write(testData, 0, testData.length);
            InputStream in = new ByteArrayInputStream(testData);
            ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
            ArchiveEntry entry = ais.getNextEntry();
            Assert.assertNotNull(entry);
            ais.close();
        }

        // Test creating archive input stream from a stream with an unsupported format
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write("Unsupported format".getBytes());
        InputStream in = new ByteArrayInputStream(out.toByteArray());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(in);
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }
    }

    private byte[] getTestFileData(String archiverName) {
        switch (archiverName) {
            case "AR":
                return new ArArchiveFactory().createArchive("test.ar", new String[]{"test.txt"}, new String[]{"text/plain"}).getBytes();
            case "ARJ":
                return new ArjArchiveFactory().createArchive("test.arj", new String[]{"test.txt"}, new String[]{"text/plain"}).getBytes();
            case "CPIO":
                return new CpioArchiveFactory().createArchive("test.cpio", new String[]{"test.txt"}, new String[]{"text/plain"}).getBytes();
            case "DUMP":
                return new DumpArchiveFactory().createArchive("test.dmp", new String[]{"test.txt"}, new String[]{"text/plain"}).getBytes();
            case "JAR":
                return new JarFactory().createArchive("test.jar", new String[]{"test.txt"}, new String[]{"text/plain"}).getBytes();
            case "TAR":
                return new TarFactory().createArchive("test.tar", new String[]{"test.txt"}, new String[]{"text/plain"}).getBytes();
            case "ZIP":
                return new ZipFactory().createArchive("test.zip", new String[]{"test.txt"}, new String[]{"text/plain"}).getBytes();
            case "SEVEN_Z":
                SevenZFile sevenZFile = new SevenZFile("test.7z");
                byte[] bytes = new byte[sevenZFile.size()];
                sevenZFile.read(bytes, 0, sevenZFile.size());
                return bytes;
            default:
                throw new IllegalArgumentException("Unknown archiver name: " + archiverName);
        }
    }

    private static class JarEntry implements ArchiveEntry {
        private final String name;

        public JarEntry(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getSize() {
            return 4;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }
    }
}