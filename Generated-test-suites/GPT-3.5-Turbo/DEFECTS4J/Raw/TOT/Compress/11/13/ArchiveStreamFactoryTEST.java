import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RunWith(MockitoJUnitRunner.class)
public class ArchiveStreamFactoryTest {

    @Mock
    InputStream inputMock;
    @Mock
    OutputStream outputMock;

    ArchiveStreamFactory archiveStreamFactory;
    byte[] byteArray;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        archiveStreamFactory = new ArchiveStreamFactory();
        byteArray = new byte[]{1, 2, 3, 4, 5};
    }

    @Test
    public void testCreateArchiveInputStream_AR_Format() throws Exception {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputMock);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP_Format() throws Exception {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputMock);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_TAR_Format() throws Exception {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputMock);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_JAR_Format() throws Exception {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputMock);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO_Format() throws Exception {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputMock);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_DUMP_Format() throws Exception {
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputMock);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_Invalid_Format() throws Exception {
        archiveStreamFactory.createArchiveInputStream("invalid", inputMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_Null_Format() throws Exception {
        archiveStreamFactory.createArchiveInputStream(null, inputMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_Null_InputStream() throws Exception {
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_ZIP_Format() throws Exception {
        when(inputMock.markSupported()).thenReturn(true);
        when(inputMock.read(any(byte[].class))).thenReturn(5);

        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputMock);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_JAR_Format() throws Exception {
        when(inputMock.markSupported()).thenReturn(true);
        when(inputMock.read(any(byte[].class))).thenReturn(5);

        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputMock);
        assertTrue(archiveInputStream instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_AR_Format() throws Exception {
        when(inputMock.markSupported()).thenReturn(true);
        when(inputMock.read(any(byte[].class))).thenReturn(5);

        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputMock);
        assertTrue(archiveInputStream instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_CPIO_Format() throws Exception {
        when(inputMock.markSupported()).thenReturn(true);
        when(inputMock.read(any(byte[].class))).thenReturn(5);

        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputMock);
        assertTrue(archiveInputStream instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_Dump_Format() throws Exception {
        when(inputMock.markSupported()).thenReturn(true);
        when(inputMock.read(any(byte[].class))).thenReturn(5);

        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputMock);
        assertTrue(archiveInputStream instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_TAR_Format() throws Exception {
        when(inputMock.markSupported()).thenReturn(true);
        when(inputMock.read(any(byte[].class))).thenReturn(5);

        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputMock);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_Null_Stream() throws Exception {
        archiveStreamFactory.createArchiveInputStream(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_AutoDetect_Stream_Not_Support_Mark() throws Exception {
        when(inputMock.markSupported()).thenReturn(false);
        archiveStreamFactory.createArchiveInputStream(inputMock);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_Format() throws Exception {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputMock);
        assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP_Format() throws Exception {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputMock);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_TAR_Format() throws Exception {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputMock);
        assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR_Format() throws Exception {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputMock);
        assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO_Format() throws Exception {
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputMock);
        assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_Invalid_Format() throws Exception {
        archiveStreamFactory.createArchiveOutputStream("invalid", outputMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_Null_Format() throws Exception {
        archiveStreamFactory.createArchiveOutputStream(null, outputMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_Null_OutputStream() throws Exception {
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStream_AutoDetect_No_Format_Detected() throws Exception {
        when(inputMock.markSupported()).thenReturn(true);
        when(inputMock.read(any(byte[].class))).thenReturn(5, 5, 5);

        try {
            archiveStreamFactory.createArchiveInputStream(inputMock);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
}