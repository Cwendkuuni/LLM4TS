import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TarArchiveOutputStream.class})
public class TarArchiveOutputStreamTest {

    private File tmpFile;
    private File testFile;
    private ByteArrayOutputStream bos;

    @Before
    public void setUp() throws Exception {
        tmpFile = File.createTempFile("junit-", ".tar");
        testFile = new File(tmpFile.getAbsolutePath() + ".test");
        bos = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        if (tmpFile != null) {
            tmpFile.delete();
        }
        if (testFile != null) {
            testFile.delete();
        }
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(bos);
        assertNotNull(tarOut);
    }

    @Test
    public void testPutArchiveEntryWithNullOutputStream() {
        assertThrows(NullPointerException.class, () -> new TarArchiveOutputStream(null));
    }

    @Test
    public void testPutArchiveEntryWithFileName() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));
        String fileName = "test.txt";
        File fileToAdd = new File("src/test/resources/testFile.txt");

        TarArchiveEntry entry = tarOut.createArchiveEntry(fileToAdd, fileName);
        tarOut.putArchiveEntry(entry);

        assertNotNull(entry);
        assertEquals(fileName, entry.getName());
        assertEquals(fileToAdd.length(), entry.getSize());

        tarOut.close();

        byte[] bytes = FileUtils.readAllBytes(testFile);
        byte[] expected = FileUtils.readAllBytes(fileToAdd);

        assertEquals(expected.length + 512, bytes.length);
        assertEquals(expected, bytes, 0, expected.length);

        testFile.delete();
    }

    @Test
    public void testPutArchiveEntryWithInputStream() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(bos);
        String fileName = "test.txt";
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream input = new ByteArrayInputStream(data);

        TarArchiveEntry entry = tarOut.createArchiveEntry("test.txt", fileName);
        tarOut.putArchiveEntry(entry, input);

        assertNotNull(entry);
        assertEquals(fileName, entry.getName());
        assertEquals(data.length, entry.getSize());

        byte[] expected = new byte[data.length + 512];
        System.arraycopy(data, 0, expected, 0, data.length);
        assertEquals(expected, bos.toByteArray());

        tarOut.close();
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        PowerMockito.mockStatic(TarConstants.class);
        when(TarConstants.class.getNameConstant("NAMELEN")).thenReturn((short) 100);

        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));
        String fileName = "testLongFileName.txt";
        File fileToAdd = new File("src/test/resources/testFile.txt");

        TarArchiveEntry entry = tarOut.createArchiveEntry(fileToAdd, fileName);
        tarOut.putArchiveEntry(entry);

        try {
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tarOut.putArchiveEntry(entry);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOut.putArchiveEntry(entry);

        assertNotNull(entry);
        assertEquals(fileName, entry.getName());
        assertEquals(fileToAdd.length(), entry.getSize());

        tarOut.close();

        byte[] bytes = FileUtils.readAllBytes(testFile);
        byte[] expected = FileUtils.readAllBytes(fileToAdd);

        assertEquals(expected.length + 1024, bytes.length);
        assertEquals(expected, bytes, 0, expected.length);

        testFile.delete();
    }

    @Test
    public void testCloseArchiveEntryWithoutPuttingOne() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));

        assertThrows(IllegalStateException.class, tarOut::closeArchiveEntry);

        tarOut.close();
        testFile.delete();
    }

    @Test
    public void testCloseArchiveEntryWithPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));
        String fileName = "test.txt";
        File fileToAdd = new File("src/test/resources/testFile.txt");

        TarArchiveEntry entry = tarOut.createArchiveEntry(fileToAdd, fileName);
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();

        assertNull(entry.getHeaderRecord());

        tarOut.close();
        testFile.delete();
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));

        assertThrows(NullPointerException.class, () -> tarOut.putArchiveEntry(null));

        tarOut.close();
        testFile.delete();
    }

    @Test
    public void testWriteWithValidData() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));
        String fileName = "test.txt";
        File fileToAdd = new File("src/test/resources/testFile.txt");

        TarArchiveEntry entry = tarOut.createArchiveEntry(fileToAdd, fileName);
        tarOut.putArchiveEntry(entry);

        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);

        tarOut.write(data, 0, data.length);
        byte[] expected = new byte[data.length + 512];
        System.arraycopy(data, 0, expected, 0, data.length);

        tarOut.closeArchiveEntry();
        byte[] bytes = FileUtils.readAllBytes(testFile);

        assertEquals(expected, bytes);
        testFile.delete();
    }

    @Test
    public void testWriteWithInvalidDataSize() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));
        String fileName = "test.txt";
        File fileToAdd = new File("src/test/resources/testFile.txt");

        TarArchiveEntry entry = tarOut.createArchiveEntry(fileToAdd, fileName);
        tarOut.putArchiveEntry(entry);

        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);

        tarOut.write(data, data.length, 1);

        tarOut.closeArchiveEntry();

        byte[] bytes = FileUtils.readAllBytes(testFile);
        assertEquals(data.length + 512, bytes.length);
        assertEquals(data, bytes, 0, data.length);

        testFile.delete();
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));

        tarOut.finish();
        byte[] bytes = FileUtils.readAllBytes(testFile);

        assertEquals(1024, bytes.length);
        testFile.delete();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile));

        tarOut.close();
        byte[] bytes = FileUtils.readAllBytes(testFile);

        assertEquals(1024, bytes.length);
        testFile.delete();
    }
}