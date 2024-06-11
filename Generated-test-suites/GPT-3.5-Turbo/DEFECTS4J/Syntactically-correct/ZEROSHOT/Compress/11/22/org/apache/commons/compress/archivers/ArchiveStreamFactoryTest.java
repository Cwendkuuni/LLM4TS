import org.junit.Test;
import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.*;
import org.apache.commons.compress.archivers.cpio.*;
import org.apache.commons.compress.archivers.dump.*;
import org.apache.commons.compress.archivers.jar.*;
import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.zip.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        // Create an ArchiveInputStream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR archive format
        InputStream arIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ar = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arIn);
        assertTrue(ar instanceof ArArchiveInputStream);

        // Test ZIP archive format
        InputStream zipIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zip = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipIn);
        assertTrue(zip instanceof ZipArchiveInputStream);

        // Test TAR archive format
        InputStream tarIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tar = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarIn);
        assertTrue(tar instanceof TarArchiveInputStream);

        // Test JAR archive format
        InputStream jarIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jar = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarIn);
        assertTrue(jar instanceof JarArchiveInputStream);

        // Test CPIO archive format
        InputStream cpioIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpio = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioIn);
        assertTrue(cpio instanceof CpioArchiveInputStream);

        // Test DUMP archive format
        InputStream dumpIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dump = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpIn);
        assertTrue(dump instanceof DumpArchiveInputStream);

        // Test unknown archive format
        try {
            InputStream unknownIn = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream("unknown", unknownIn);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        // Create an ArchiveOutputStream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test AR archive format
        OutputStream arOut = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream ar = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOut);
        assertTrue(ar instanceof ArArchiveOutputStream);

        // Test ZIP archive format
        OutputStream zipOut = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream zip = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOut);
        assertTrue(zip instanceof ZipArchiveOutputStream);

        // Test TAR archive format
        OutputStream tarOut = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream tar = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOut);
        assertTrue(tar instanceof TarArchiveOutputStream);

        // Test JAR archive format
        OutputStream jarOut = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream jar = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOut);
        assertTrue(jar instanceof JarArchiveOutputStream);

        // Test CPIO archive format
        OutputStream cpioOut = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };
        ArchiveOutputStream cpio = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOut);
        assertTrue(cpio instanceof CpioArchiveOutputStream);

        // Test unknown archive format
        try {
            OutputStream unknownOut = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    // Do nothing
                }
            };
            factory.createArchiveOutputStream("unknown", unknownOut);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() throws ArchiveException {
        // Create an ArchiveInputStream
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test ZIP archive format
        InputStream zipIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zip = factory.createArchiveInputStream(zipIn);
        assertTrue(zip instanceof ZipArchiveInputStream);

        // Test JAR archive format
        InputStream jarIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jar = factory.createArchiveInputStream(jarIn);
        assertTrue(jar instanceof JarArchiveInputStream);

        // Test AR archive format
        InputStream arIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ar = factory.createArchiveInputStream(arIn);
        assertTrue(ar instanceof ArArchiveInputStream);

        // Test CPIO archive format
        InputStream cpioIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpio = factory.createArchiveInputStream(cpioIn);
        assertTrue(cpio instanceof CpioArchiveInputStream);

        // Test DUMP archive format
        InputStream dumpIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dump = factory.createArchiveInputStream(dumpIn);
        assertTrue(dump instanceof DumpArchiveInputStream);

        // Test TAR archive format
        InputStream tarIn = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tar = factory.createArchiveInputStream(tarIn);
        assertTrue(tar instanceof TarArchiveInputStream);
    }
}