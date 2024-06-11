import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveException;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            byte[] data = new byte[]{(byte) 0x78, (byte) 0x9C, (byte) 0xDA, (byte) 0xC9, (byte) 0xC9, (byte) 0xCF, (byte) 0x57,
                    (byte) 0x08, (byte) 0x2F, (byte) 0xCA, (byte) 0x49, (byte) 0x51, (byte) 0x04, (byte) 0x00, (byte) 0x63,
                    (byte) 0x60, (byte) 0x60, (byte) 0xE0, (byte) 0x92, (byte) 0x74, (byte) 0x71, (byte) 0xAA, (byte) 0xB3};

            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("zip", inputStream);

            assertNotNull(archiveInputStream);
        } catch (ArchiveException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("zip", outputStream);

            assertNotNull(archiveOutputStream);
        } catch (ArchiveException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveInputStream(null, inputStream);
        });
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createArchiveOutputStream("zip", null);
        });
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        try {
            byte[] data = new byte[]{(byte) 0x1F, (byte) 0x8B, (byte) 0x08, (byte) 0x08, (byte) 0xF4, (byte) 0xD3,
                    (byte) 0xC8, (byte) 0xAC, (byte) 0x4B, (byte) 0x4D, (byte) 0xCB, (byte) 0xCF, (byte) 0x07, (byte)
                    0x00, (byte) 0x46, (byte) 0xC2, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);

            assertNotNull(archiveInputStream);
        } catch (ArchiveException | IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectWithInvalidData() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        assertThrows(ArchiveException.class, () -> {
            factory.createArchiveInputStream(inputStream);
        });
    }
}