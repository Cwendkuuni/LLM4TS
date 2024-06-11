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
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        // Test AR
        ArchiveInputStream input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(ArArchiveInputStream.class, input);
        input.close();

        // Test CPIO
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(CpioArchiveInputStream.class, input);
        input.close();

        // Test Dump
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertInstanceOf(DumpArchiveInputStream.class, input);
        input.close();

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, jarInput);
        jarInput.close();
        jarInputStream.close();

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarInput);
        tarInput.close();
        tarInputStream.close();

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipInput);
        zipInput.close();
        zipInputStream.close();

        // Test unknown
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0])));
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        // Test AR
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream output = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertInstanceOf(ArArchiveOutputStream.class, output);
        output.finish();
        output.close();

        // Test CPIO
        out = new ByteArrayOutputStream();
        output = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertInstanceOf(CpioArchiveOutputStream.class, output);
        output.finish();
        output.close();

        // Test Dump
        out = new ByteArrayOutputStream();
        output = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
        assertInstanceOf(DumpArchiveOutputStream.class, output);
        output.finish();
        output.close();

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, jarOutput);
        jarOutput.putNextEntry(new JarEntry("test.txt"));
        jarOutput.write("Hello World!".getBytes());
        jarOutput.finish();
        jarOutput.close();
        assertEquals("Hello World!", new String(jarOutputStream.toByteArray()));
        jarOutputStream.close();

        // Test TAR
        out = new ByteArrayOutputStream();
        output = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertInstanceOf(TarArchiveOutputStream.class, output);
        output.finish();
        output.close();

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, zipOutput);
        zipOutput.putNextEntry(new ZipEntry("test.txt"));
        zipOutput.write("Hello World!".getBytes());
        zipOutput.finish();
        zipOutput.close();
        assertEquals("Hello World!", new String(zipOutputStream.toByteArray()));
        zipOutputStream.close();

        // Test unknown
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, new ByteArrayOutputStream()));
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws ArchiveException, IOException {
        // Test AR
        byte[] arBytes = new byte[0];
        ArchiveInputStream input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(arBytes));
        assertNull(input);

        arBytes = new byte[4096]; // Minimum size for AR signature
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(arBytes));
        assertInstanceOf(ArArchiveInputStream.class, input);
        input.close();

        // Test CPIO
        byte[] cpioBytes = new byte[0];
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(cpioBytes));
        assertNull(input);

        cpioBytes = new byte[12]; // Minimum size for CPIO signature
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(cpioBytes));
        assertInstanceOf(CpioArchiveInputStream.class, input);
        input.close();

        // Test Dump
        byte[] dumpBytes = new byte[0];
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(dumpBytes));
        assertNull(input);

        dumpBytes = new byte[32]; // Minimum size for DUMP signature
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(dumpBytes));
        assertInstanceOf(DumpArchiveInputStream.class, input);
        input.close();

        // Test JAR
        byte[] jarBytes = new byte[0];
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(jarBytes));
        assertNull(input);

        // Test TAR
        byte[] tarBytes = new byte[0];
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(tarBytes));
        assertNull(input);

        tarBytes = new byte[512]; // Minimum size for TAR header
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(tarBytes));
        assertInstanceOf(TarArchiveInputStream.class, input);
        input.close();

        // Test ZIP
        byte[] zipBytes = new byte[0];
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(zipBytes));
        assertNull(input);

        zipBytes = new byte[3 + 2 * 32]; // Minimum size for ZIP signature
        input = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(zipBytes));
        assertInstanceOf(ZipArchiveInputStream.class, input);
        input.close();
    }
}