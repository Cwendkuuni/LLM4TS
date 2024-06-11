
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        byteArrayOutputStream.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);

        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        String contents = "12345";
        cpioEntry.setFileSize(contents.length());

        cpioArchiveOutputStream.putNextEntry(cpioEntry);
        cpioArchiveOutputStream.write(contents.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        Mockito.verify(cpioArchiveOutputStream).write(argumentCaptor.capture(), Mockito.anyInt(), Mockito.anyInt());

        byte[] writtenBytes = argumentCaptor.getValue();
        String writtenContents = new String(writtenBytes);
        assertEquals(contents, writtenContents);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithoutSettingEntryName() throws IOException {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(cpioEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);

        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setFileSize(5);
        cpioArchiveOutputStream.putNextEntry(cpioEntry);

        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();
        assertTrue(byteArrayOutputStream.toString().endsWith("TRAILER!!!"));
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithInvalidEntrySize() throws IOException {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);

        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setFileSize(5);
        cpioArchiveOutputStream.putNextEntry(cpioEntry);

        cpioArchiveOutputStream.write("123456".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        cpioArchiveOutputStream.finish();
        assertTrue(byteArrayOutputStream.toString().endsWith("TRAILER!!!"));
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        cpioArchiveOutputStream.close();
        assertTrue(cpioArchiveOutputStream.isClosed());
        assertTrue(byteArrayOutputStream.size() == 0);
    }
}

