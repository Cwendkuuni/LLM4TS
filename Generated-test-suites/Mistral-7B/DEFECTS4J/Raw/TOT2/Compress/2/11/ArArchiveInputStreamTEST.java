import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ArArchiveInputStreamTest {

    private static final String AR_ARCHIVE_TEST_FILE = "testArArchive.ar";
    private static final String TAR_ARCHIVE_TEST_FILE = "testTarArchive.tar";
    private ArArchiveInputStream arInputStream;
    private TarInputStream tarInputStream;
    private ArchiveInputStream expectedInputStream;

    public ArArchiveInputStreamTest(String archiveType) throws IOException {
        if (ArchiveStreamFactory.NAME_AR.equals(archiveType)) {
            arInputStream = new ArArchiveInputStream(IOUtils.toInputStream(getClass().getResource(AR_ARCHIVE_TEST_FILE)));
        } else if (ArchiveStreamFactory.NAME_TAR.equals(archiveType)) {
            tarInputStream = new TarInputStream(IOUtils.toInputStream(getClass().getResource(TAR_ARCHIVE_TEST_FILE)));
            expectedInputStream = new TarInputStream(new ByteArrayInputStream(IOUtils.toByteArray(tarInputStream.getInputStream())));
        } else {
            throw new IllegalArgumentException("Unsupported archive type: " + archiveType);
        }
    }

    @Before
    public void setUp() throws IOException {
        ArArchiveEntry arEntry = arInputStream.getNextArEntry();
        Assert.assertNotNull(arEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry actualEntry = arInputStream.getNextEntry();
        ArchiveEntry expectedEntry = arInputStream.getNextArEntry();
        Assert.assertEquals(expectedEntry, actualEntry);
    }

    @Test
    public void testGetNextArEntry_ReturnsNullAtEndOfStream() throws IOException {
        ArArchiveEntry actualEntry = null;
        while ((actualEntry = arInputStream.getNextArEntry()) != null) {
            // Consume the entry
        }
        Assert.assertNull(actualEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_ThrowsIOExceptionOnError() throws IOException {
        arInputStream.getNextArEntry();
        arInputStream.read(); // Read a byte to simulate a successful read
        arInputStream.getNextArEntry(); // This should fail
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        Arrays.fill(signature, (byte) 0x00);
        Assert.assertFalse(ArArchiveInputStream.matches(signature, signature.length));

        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;

        Assert.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testRead() throws IOException {
        int expectedByte = tarInputStream.read();
        int actualByte = arInputStream.read();
        Assert.assertEquals(expectedByte, actualByte);
    }

    @Test
    public void testRead_ReturnsMinusOneAtEndOfStream() throws IOException {
        int actualByte = arInputStream.read();
        Assert.assertEquals(-1, actualByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expectedBytes = new byte[10];
        int read = tarInputStream.read(expectedBytes, 0, expectedBytes.length);
        byte[] actualBytes = new byte[10];
        int readActual = arInputStream.read(actualBytes, 0, actualBytes.length);
        Assert.assertEquals(read, readActual);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadByteArray_ReadsFullyAtEndOfStream() throws IOException {
        byte[] expectedBytes = new byte[1];
        int read = tarInputStream.read(expectedBytes, 0, expectedBytes.length);
        byte[] actualBytes = new byte[1];
        int readActual = arInputStream.read(actualBytes, 0, actualBytes.length);
        Assert.assertEquals(read, readActual);
        Assert.assertArrayEquals(expectedBytes, actualBytes);

        byte[] expectedFullBytes = new byte[1024];
        int readFull = tarInputStream.read(expectedFullBytes, 0, expectedFullBytes.length);
        byte[] actualFullBytes = new byte[1024];
        int readFullActual = arInputStream.read(actualFullBytes, 0, actualFullBytes.length);
        Assert.assertEquals(readFull, readFullActual);
        Assert.assertArrayEquals(expectedFullBytes, actualFullBytes);
    }

    @Parameterized.Parameters(name = "{0} archive")
    public static Collection<Object[]> getTestArchives() {
        return Arrays.asList(new Object[][]{"tar", "AR_ARCHIVE_TEST_FILE"}, new Object[]{"ar", AR_ARCHIVE_TEST_FILE});
    }
}