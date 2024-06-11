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
    public void testCreateArchiveInputStreamAR() {
        ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(ArArchiveInputStream.class, in);
    }

    @Test
    public void testCreateArchiveInputStreamARNull() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(ArchiveStreamFactory.AR, null);
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamARUnknown() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamCpio() {
        ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(CpioArchiveInputStream.class, in);
    }

    @Test
    public void testCreateArchiveInputStreamCpioNull() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamCpioUnknown() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamDump() {
        ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(DumpArchiveInputStream.class, in);
    }

    @Test
    public void testCreateArchiveInputStreamDumpNull() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamDumpUnknown() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamJar() {
        ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(JarArchiveInputStream.class, in);
    }

    @Test
    public void testCreateArchiveInputStreamJarNull() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(ArchiveStreamFactory.JAR, null);
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamJarUnknown() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamTar() {
        ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(TarArchiveInputStream.class, in);
    }

    @Test
    public void testCreateArchiveInputStreamTarNull() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamTarUnknown() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamZip() {
        ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(ZipArchiveInputStream.class, in);
    }

    @Test
    public void testCreateArchiveInputStreamZipNull() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamZipUnknown() {
        try {
            ArchiveInputStream in = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
            assertNull(in);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamAR() {
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
        assertInstanceOf(ArArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamARNull() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamARUnknown() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamCpio() {
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
        assertInstanceOf(CpioArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamCpioNull() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamCpioUnknown() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamDump() {
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayOutputStream());
        assertInstanceOf(DumpArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamDumpNull() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream(ArchiveStreamFactory.DUMP, null);
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamDumpUnknown() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamJar() {
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
        assertInstanceOf(JarArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamJarNull() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamJarUnknown() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamTar() {
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        assertInstanceOf(TarArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamTarNull() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamTarUnknown() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamZip() {
        ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertInstanceOf(ZipArchiveOutputStream.class, out);
    }

    @Test
    public void testCreateArchiveOutputStreamZipNull() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamZipUnknown() {
        try {
            ArchiveOutputStream out = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream("unknown", new ByteArrayOutputStream());
            assertNull(out);
        } catch (ArchiveException e) {
            // expected
        }
    }
}