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
import org.apache.commons.compress.test.utils.TestUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        final String[] validArchivers = {AR, CPIO, DUMP, JAR, TAR, ZIP};
        for (String archiver : validArchivers) {
            InputStream testInputStream = null;
            try {
                testInputStream = new ByteArrayInputStream(TestUtils.getTestFileBytes(archiver + ".bin"));
                ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiver, testInputStream);
                assertInstanceOf(archiver.charAt(0) == 'z' ? ZipArchiveInputStream.class : ArchiveInputStream.class, inputStream.getClass());
            } finally {
                TestUtils.cleanUp(testInputStream);
            }
        }

        InputStream testInputStream = new ByteArrayInputStream("invalid_stream".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, testInputStream);
            assert false; // should not reach this point
        } finally {
            TestUtils.cleanUp(testInputStream);
        }

        InputStream testInputStream = new ByteArrayInputStream("invalid_stream".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("invalid_archiver", testInputStream);
            assert false; // should not reach this point
        } finally {
            TestUtils.cleanUp(testInputStream);
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        final String[] validArchivers = {AR, CPIO, DUMP, JAR, TAR, ZIP};
        for (String archiver : validArchivers) {
            ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
            ArchiveOutputStream outputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiver, testOutputStream);
            assertInstanceOf(archiver.charAt(0) == 'z' ? JarArchiveOutputStream.class : ArchiveOutputStream.class, outputStream.getClass());
            outputStream.finish();
            testOutputStream.close();
        }

        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(null, testOutputStream);
            assert false; // should not reach this point
        } finally {
            testOutputStream.close();
        }

        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("invalid_archiver", testOutputStream);
            assert false; // should not reach this point
        } finally {
            testOutputStream.close();
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromFirstBytes() throws IOException {
        final String[] validArchivers = {AR, CPIO, DUMP, JAR, TAR, ZIP};
        for (String archiver : validArchivers) {
            InputStream testInputStream = new ByteArrayInputStream(TestUtils.getTestFileBytes(archiver + ".bin").subArray(0, 12));
            ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(testInputStream);
            assertInstanceOf(archiver.charAt(0) == 'z' ? ZipArchiveInputStream.class : ArchiveInputStream.class, inputStream.getClass());
            inputStream.close();
        }

        InputStream testInputStream = new ByteArrayInputStream("invalid_bytes".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream(testInputStream);
            assert false; // should not reach this point
        } finally {
            TestUtils.cleanUp(testInputStream);
        }

        InputStream testInputStream = new ByteArrayInputStream("invalid_bytes".getBytes());
        try {
            new ArchiveStreamFactory().createArchiveInputStream("invalid_archiver", testInputStream);
            assert false; // should not reach this point
        } finally {
            TestUtils.cleanUp(testInputStream);
        }
    }

    @Test
    public void testUnknownArchiver() {
        InputStream testInputStream = new ByteArrayInputStream(TestUtils.getTestFileBytes("ar.bin"));
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown_archiver", testInputStream);
            assert false; // should not reach this point
        } finally {
            TestUtils.cleanUp(testInputStream);
        }
    }

    @Test
    public void testNullArguments() {
        assertNull(new ArchiveStreamFactory().createArchiveOutputStream(null));
        assertNull(new ArchiveStreamFactory().createArchiveInputStream(null));
    }
}