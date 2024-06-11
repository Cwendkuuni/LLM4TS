package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamWithValidArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        
        ArArchiveInputStream arInputStream = (ArArchiveInputStream) factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertNotNull(arInputStream);
        
        in.reset();
        ZipArchiveInputStream zipInputStream = (ZipArchiveInputStream) factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertNotNull(zipInputStream);
        
        in.reset();
        TarArchiveInputStream tarInputStream = (TarArchiveInputStream) factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertNotNull(tarInputStream);
        
        in.reset();
        JarArchiveInputStream jarInputStream = (JarArchiveInputStream) factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertNotNull(jarInputStream);
        
        in.reset();
        CpioArchiveInputStream cpioInputStream = (CpioArchiveInputStream) factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertNotNull(cpioInputStream);
        
        in.reset();
        DumpArchiveInputStream dumpInputStream = (DumpArchiveInputStream) factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertNotNull(dumpInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        
        Exception exception = assertThrows(ArchiveException.class, () -> {
            factory.createArchiveInputStream("invalid", in);
        });
        
        String expectedMessage = "Archiver: invalid not found.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArchiverName() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertNotNull(out);
        
        out.reset();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertNotNull(out);
        
        out.reset();
        factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertNotNull(out);
        
        out.reset();
        factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertNotNull(out);
        
        out.reset();
        factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertNotNull(out);
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        Exception exception = assertThrows(ArchiveException.class, () -> {
            factory.createArchiveOutputStream("invalid", out);
        });
        
        String expectedMessage = "Archiver: invalid not found.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(null, in);
        });
        
        String expectedMessage = "Archivername must not be null.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream(null, out);
        });
        
        String expectedMessage = "Archivername must not be null.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        });
        
        String expectedMessage = "InputStream must not be null.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        });
        
        String expectedMessage = "OutputStream must not be null.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
}