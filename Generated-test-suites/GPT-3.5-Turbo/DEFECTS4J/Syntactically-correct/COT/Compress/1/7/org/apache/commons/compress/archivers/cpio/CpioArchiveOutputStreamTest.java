
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "testfile";
    private static final String TEST_FILE_CONTENTS = "12345";

    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_FILE_NAME);
        entry.setFileSize(TEST_FILE_CONTENTS.length());

        archiveOutputStream.putNextEntry(entry);

        archiveOutputStream.write(TEST_FILE_CONTENTS.getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();

        byte[] compressedData = outputStream.toByteArray();
        assertNotNull(compressedData);
        assertTrue(compressedData.length > 0);
    }
}
