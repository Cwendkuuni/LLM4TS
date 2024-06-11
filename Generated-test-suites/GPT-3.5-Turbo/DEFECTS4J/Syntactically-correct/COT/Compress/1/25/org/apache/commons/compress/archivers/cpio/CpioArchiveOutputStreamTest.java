
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;
    private OutputStream mockOutputStream;

    @Before
    public void setUp() {
        mockOutputStream = Mockito.mock(OutputStream.class);
        cpioOutputStream = new CpioArchiveOutputStream(mockOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        cpioOutputStream.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(cpioEntry);

        Assert.assertEquals(cpioEntry, cpioOutputStream.getCpioEntry());
        Mockito.verify(mockOutputStream, Mockito.times(1)).write(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioOutputStream.putNextEntry(cpioEntry);
        cpioOutputStream.closeArchiveEntry();

        Assert.assertNull(cpioOutputStream.getCpioEntry());
        Mockito.verify(mockOutputStream, Mockito.times(1)).write(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test(expected=IOException.class)
    public void testCloseArchiveEntryInvalidSize() throws IOException {
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setSize(10);
        cpioOutputStream.putNextEntry(cpioEntry);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[5]);
        cpioOutputStream.write(outputStream.toByteArray());
        cpioOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();

        Mockito.verify(mockOutputStream, Mockito.times(1)).write(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(mockOutputStream, Mockito.times(1)).close();
    }

    @Test
    public void testClose() throws IOException {
        cpioOutputStream.close();

        Mockito.verify(mockOutputStream, Mockito.times(1)).close();
    }
}

