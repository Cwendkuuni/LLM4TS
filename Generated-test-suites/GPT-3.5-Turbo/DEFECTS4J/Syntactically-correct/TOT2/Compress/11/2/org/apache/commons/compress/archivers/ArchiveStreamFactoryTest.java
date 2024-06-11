import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStreamWithValidArchiverNameAndInputStream() {
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
            assertTrue(archiveInputStream instanceof ArArchiveInputStream);
        } catch (ArchiveException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream("invalid", inputStream);
        });
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithValidArchiverNameAndOutputStream() {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
            assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        } catch (ArchiveException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream(null, outputStream);
        });
    }

    @Test
    public void testCreateArchiveInputStreamWithAutodetectFromInputStream() {
        try {
            byte[] data = {0x50, 0x4b, 0x03, 0x04}; // ZIP file signature
            InputStream inputStream = new ByteArrayInputStream(data);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
            assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        } catch (ArchiveException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream((InputStream)null);
        });
    }
    
    @Test
    public void testCreateArchiveInputStreamWithUnsupportedMark() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        };
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(inputStream);
        });
    }
}