package org.apache.commons.compress.archivers;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ArchiveStreamFactoryTest {

    private static final String AR_SIGNATURE = "!<arch>\n";
    private static final String AR_SIGNATURE_LENGTH = AR_SIGNATURE.length();
    private static final String ARJ_SIGNATURE = "ARJ\n";
    private static final String ARJ_SIGNATURE_LENGTH = ARJ_SIGNATURE.length();
    private static final String CPIO_SIGNATURE = "\001\012\001\n";
    private static final String CPIO_SIGNATURE_LENGTH = CPIO_SIGNATURE.length();
    private static final String DUMP_SIGNATURE = "\037\040\012\010\n";
    private static final String DUMP_SIGNATURE_LENGTH = DUMP_SIGNATURE.length();
    private static final String JAR_SIGNATURE = "\u0000\u000a\n";
    private static final String JAR_SIGNATURE_LENGTH = JAR_SIGNATURE.length();
    private static final String TAR_SIGNATURE = "\000\011\000\n";
    private static final String TAR_SIGNATURE_LENGTH = TAR_SIGNATURE.length();
    private static final String ZIP_SIGNATURE = "\u0003\u0004\012\000\n";
    private static final String ZIP_SIGNATURE_LENGTH = ZIP_SIGNATURE.length();

    @Test
    public void testCreateArchiveInputStreamFromArchiverNameAndInputStream() {
        testCreateArchiveInputStreamFromArchiverNameAndInputStream(ArchiveStreamFactory.AR, new ArArchiveInputStream(new ByteArrayInputStream(AR_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromArchiverNameAndInputStream(ArchiveStreamFactory.ARJ, null); // Not supported
        testCreateArchiveInputStreamFromArchiverNameAndInputStream(ArchiveStreamFactory.CPIO, new CpioArchiveInputStream(new ByteArrayInputStream(CPIO_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromArchiverNameAndInputStream(ArchiveStreamFactory.DUMP, null); // Not supported
        testCreateArchiveInputStreamFromArchiverNameAndInputStream(ArchiveStreamFactory.JAR, new JarArchiveInputStream(new ByteArrayInputStream(JAR_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromArchiverNameAndInputStream(ArchiveStreamFactory.TAR, new TarArchiveInputStream(new ByteArrayInputStream(TAR_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromArchiverNameAndInputStream(ArchiveStreamFactory.ZIP, new ZipArchiveInputStream(new ByteArrayInputStream(ZIP_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromArchiverNameAndInputStream(ArchiveStreamFactory.SEVEN_Z, null); // Not supported
    }

    private void testCreateArchiveInputStreamFromArchiverNameAndInputStream(String archiverName, ArchiveInputStream expected) {
        try {
            ArchiveInputStream actual = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, new ByteArrayInputStream(getSignature(archiverName).getBytes()));
            assertEquals("Unexpected archive input stream type", expected.getClass(), actual.getClass());
        } catch (ArchiveException e) {
            if (expected == null) {
                assertEquals("Unexpected exception", ArchiveException.class, e.getClass());
            } else {
                fail("Should not throw exception for " + archiverName);
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() {
        testCreateArchiveInputStreamFromInputStream(ArchiveStreamFactory.AR, new ArArchiveInputStream(new ByteArrayInputStream(AR_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromInputStream(ArchiveStreamFactory.ARJ, null); // Not supported
        testCreateArchiveInputStreamFromInputStream(ArchiveStreamFactory.CPIO, new CpioArchiveInputStream(new ByteArrayInputStream(CPIO_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromInputStream(ArchiveStreamFactory.DUMP, null); // Not supported
        testCreateArchiveInputStreamFromInputStream(ArchiveStreamFactory.JAR, new JarArchiveInputStream(new ByteArrayInputStream(JAR_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromInputStream(ArchiveStreamFactory.TAR, new TarArchiveInputStream(new ByteArrayInputStream(TAR_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromInputStream(ArchiveStreamFactory.ZIP, new ZipArchiveInputStream(new ByteArrayInputStream(ZIP_SIGNATURE.getBytes())));
        testCreateArchiveInputStreamFromInputStream(ArchiveStreamFactory.SEVEN_Z, null); // Not supported
    }

    private void testCreateArchiveInputStreamFromInputStream(String archiverName, ArchiveInputStream expected) {
        try {
            InputStream inputStream = new ByteArrayInputStream(getSignature(archiverName).getBytes());
            ArchiveInputStream actual = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiverName, inputStream);
            assertEquals("Unexpected archive input stream type", expected.getClass(), actual.getClass());
            IOUtils.closeQuietly(actual);
        } catch (ArchiveException | IOException e) {
            fail("Should not throw exception for " + archiverName);
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromArchiverNameAndOutputStream() {
        testCreateArchiveOutputStreamFromArchiverNameAndOutputStream(ArchiveStreamFactory.AR, new ArArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromArchiverNameAndOutputStream(ArchiveStreamFactory.ARJ, null); // Not supported
        testCreateArchiveOutputStreamFromArchiverNameAndOutputStream(ArchiveStreamFactory.CPIO, new CpioArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromArchiverNameAndOutputStream(ArchiveStreamFactory.JAR, new JarArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromArchiverNameAndOutputStream(ArchiveStreamFactory.TAR, new TarArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromArchiverNameAndOutputStream(ArchiveStreamFactory.ZIP, new ZipArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromArchiverNameAndOutputStream(ArchiveStreamFactory.SEVEN_Z, null); // Not supported
    }

    private void testCreateArchiveOutputStreamFromArchiverNameAndOutputStream(String archiverName, ArchiveOutputStream expected) {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream actual = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
            assertEquals("Unexpected archive output stream type", expected.getClass(), actual.getClass());
            IOUtils.closeQuietly(actual);
        } catch (ArchiveException e) {
            if (expected == null) {
                assertEquals("Unexpected exception", ArchiveException.class, e.getClass());
            } else {
                fail("Should not throw exception for " + archiverName);
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() {
        testCreateArchiveOutputStreamFromOutputStream(ArchiveStreamFactory.AR, new ArArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromOutputStream(ArchiveStreamFactory.ARJ, null); // Not supported
        testCreateArchiveOutputStreamFromOutputStream(ArchiveStreamFactory.CPIO, new CpioArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromOutputStream(ArchiveStreamFactory.JAR, new JarArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromOutputStream(ArchiveStreamFactory.TAR, new TarArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromOutputStream(ArchiveStreamFactory.ZIP, new ZipArchiveOutputStream(new ByteArrayOutputStream()));
        testCreateArchiveOutputStreamFromOutputStream(ArchiveStreamFactory.SEVEN_Z, null); // Not supported
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStreamWithEncoding() {
        testCreateArchiveOutputStreamFromOutputStreamWithEncoding(ArchiveStreamFactory.AR, "UTF-8", new ArArchiveOutputStream(new ByteArrayOutputStream()) {
            {
                setEncoding("UTF-8");
            }
        });
        testCreateArchiveOutputStreamFromOutputStreamWithEncoding(ArchiveStreamFactory.ARJ, null, null); // Not supported
        testCreateArchiveOutputStreamFromOutputStreamWithEncoding(ArchiveStreamFactory.CPIO, "UTF-8", new CpioArchiveOutputStream(new ByteArrayOutputStream()) {
            {
                setEncoding("UTF-8");
            }
        });
        testCreateArchiveOutputStreamFromOutputStreamWithEncoding(ArchiveStreamFactory.JAR, null, null); // Not supported
        testCreateArchiveOutputStreamFromOutputStreamWithEncoding(ArchiveStreamFactory.TAR, "UTF-8", new TarArchiveOutputStream(new ByteArrayOutputStream()) {
            {
                setEncoding("UTF-8");
            }
        });
        testCreateArchiveOutputStreamFromOutputStreamWithEncoding(ArchiveStreamFactory.ZIP, null, null); // Not supported
        testCreateArchiveOutputStreamFromOutputStreamWithEncoding(ArchiveStreamFactory.SEVEN_Z, null, null); // Not supported
    }

    private void testCreateArchiveOutputStreamFromOutputStream(String archiverName, ArchiveOutputStream expected) {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream actual = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream);
            assertEquals("Unexpected archive output stream type", expected.getClass(), actual.getClass());
            IOUtils.closeQuietly(actual);
        } catch (ArchiveException e) {
            if (expected == null) {
                assertEquals("Unexpected exception", ArchiveException.class, e.getClass());
            } else {
                fail("Should not throw exception for " + archiverName);
            }
        }
    }

    private void testCreateArchiveOutputStreamFromOutputStreamWithEncoding(String archiverName, String encoding, ArchiveOutputStream expected) {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream actual = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiverName, outputStream, encoding);
            assertEquals("Unexpected archive output stream type", expected.getClass(), actual.getClass());
            assertEquals("Unexpected archive output stream encoding", encoding, actual.getEncoding());
            IOUtils.closeQuietly(actual);
        } catch (ArchiveException e) {
            if (expected == null) {
                assertEquals("Unexpected exception", ArchiveException.class, e.getClass());
            } else {
                fail("Should not throw exception for " + archiverName);
            }
        }
    }

    private InputStream getSignature(String archiverName) {
        switch (archiverName) {
            case ArchiveStreamFactory.AR:
                return new ByteArrayInputStream(AR_SIGNATURE.getBytes());
            case ArchiveStreamFactory.ARJ:
                return new ByteArrayInputStream(ARJ_SIGNATURE.getBytes());
            case ArchiveStreamFactory.CPIO:
                return new ByteArrayInputStream(CPIO_SIGNATURE.getBytes());
            case ArchiveStreamFactory.DUMP:
                return new ByteArrayInputStream(DUMP_SIGNATURE.getBytes());
            case ArchiveStreamFactory.JAR:
                return new ByteArrayInputStream(JAR_SIGNATURE.getBytes());
            case ArchiveStreamFactory.TAR:
                return new ByteArrayInputStream(TAR_SIGNATURE.getBytes());
            case ArchiveStreamFactory.ZIP:
                return new ByteArrayInputStream(ZIP_SIGNATURE.getBytes());
            case ArchiveStreamFactory.SEVEN_Z:
                return new ByteArrayInputStream(new byte[0]);
            default:
                throw new IllegalArgumentException("Invalid archiver name: " + archiverName);
        }
    }

}