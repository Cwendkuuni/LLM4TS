import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.apache.commons.compress.utils.DumpArchiveUtil.convert16;
import static org.apache.commons.compress.utils.DumpArchiveUtil.convert32;
import static org.apache.commons.compress.utils.DumpArchiveUtil.decode;

public class DumpArchiveInputStreamTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private DumpArchiveInputStream inputStream;
    private ArchiveInputStream expectedArchiveInputStream;
    private ByteArrayOutputStream outputStream;
    private byte[] testData;

    @Before
    public void setUp() throws IOException {
        ZipEncoding encoding = new ZipEncoding();
        testData = TestUtils.getTestData("testdata.dump");
        outputStream = new ByteArrayOutputStream();
        expectedArchiveInputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), encoding);
        inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), encoding) {
            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(outputStream.toByteArray());
            }
        };
    }

    @Test
    public void testMatches() {
        byte[] testData1 = new byte[1024];
        byte[] testData2 = TestUtils.getTestData("testdata.dump");
        Assert.assertTrue(DumpArchiveInputStream.matches(testData2, testData2.length));
        Assert.assertFalse(DumpArchiveInputStream.matches(testData1, testData1.length));
    }

    @Test
    public void testConstructorWithDefaultEncoding() throws IOException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData));
        Assert.assertEquals("Unexpected summary.", expectedArchiveInputStream.getSummary(), inputStream.getSummary());
        Assert.assertEquals("Unexpected next entry.", expectedArchiveInputStream.getNextEntry(), inputStream.getNextEntry());
    }

    @Test
    public void testConstructorWithSpecifiedEncoding() throws IOException {
        String encoding = "UTF-8";
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), encoding);
        Assert.assertEquals("Unexpected summary.", expectedArchiveInputStream.getSummary(), inputStream.getSummary());
        Assert.assertEquals("Unexpected next entry.", expectedArchiveInputStream.getNextEntry(), inputStream.getNextEntry());
    }

    @Test
    public void testGetSummary() throws IOException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
        DumpArchiveSummary summary = inputStream.getSummary();
        Assert.assertEquals("Unexpected number of entry records.", 14, summary.getNumberOfEntryRecords());
        Assert.assertEquals("Unexpected number of address records.", 3, summary.getNumberOfAddressRecords());
        Assert.assertEquals("Unexpected number of file records.", 11, summary.getNumberOfFileRecords());
        Assert.assertEquals("Unexpected number of block size records.", 2, summary.getNumberOfBlockSizeRecords());
        Assert.assertEquals("Unexpected magic value.", DumpArchiveConstants.NFS_MAGIC, summary.getMagic());
        Assert.assertEquals("Unexpected format version.", DumpArchiveConstants.FORMAT_VERSION, summary.getFormatVersion());
        Assert.assertEquals("Unexpected number of block size.", DumpArchiveConstants.BLOCK_SIZE, summary.getBlockSize());
        Assert.assertEquals("Unexpected flags.", DumpArchiveConstants.FLAGS, summary.getFlags());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = null;
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
        while ((entry = inputStream.getNextEntry()) != null) {
            Assert.assertEquals("Unexpected name.", "file01", entry.getName());
            Assert.assertEquals("Unexpected simple name.", "file01", entry.getSimpleName());
            Assert.assertEquals("Unexpected file size.", 21, entry.getSize());
            Assert.assertEquals("Unexpected file offset.", 231, entry.getOffset());
            Assert.assertEquals("Unexpected isDirectory.", false, entry.isDirectory());
            Assert.assertEquals("Unexpected isFile.", true, entry.isFile());
            Assert.assertEquals("Unexpected length.", 21, inputStream.read(new byte[21], 0, 21));
        }
        Assert.assertNull("Unexpected non-null last entry.", inputStream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[21];
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
        int length = 0;
        int offset = 0;
        while ((length = inputStream.read(buffer, offset, buffer.length - offset)) > 0) {
            Assert.assertArrayEquals("Unexpected data.", Arrays.copyOfRange(testData, 231, 231 + length), buffer);
            offset += length;
        }
        Assert.assertEquals("Unexpected EOF.", -1, inputStream.read(new byte[1], 0, 1));
    }

    @Test
    public void testClose() throws IOException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
        inputStream.close();
        Assert.assertTrue("Unexpected open stream.", inputStream.isClosed());
    }

    @Test
    public void testGetBytesRead() throws IOException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
        long bytesRead = inputStream.getBytesRead();
        Assert.assertEquals("Unexpected number of bytes read.", testData.length, bytesRead);
    }

    @Test
    public void testGetCount() throws IOException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
        int count = inputStream.getCount();
        Assert.assertEquals("Unexpected number of bytes read.", testData.length, count);
    }

    @Test
    public void testParseSingleEntry() throws IOException {
        byte[] testData1 = new byte[32];
        System.arraycopy(testData, 0, testData1, 0, 32);
        byte[] buffer = new byte[16];
        int len = DumpArchiveEntry.parse(testData1, 0, buffer, null);
        Assert.assertEquals("Unexpected length.", 16, len);
        Assert.assertEquals("Unexpected inode.", 1013, convert32(buffer, 0));
        Assert.assertEquals("Unexpected file type.", DumpArchiveConstants.TYPE_REG, convert16(buffer, 4));
        Assert.assertEquals("Unexpected file name length.", 7, convert16(buffer, 6));
        String name = decode(null, buffer, 8, buffer[7]);
        Assert.assertEquals("Unexpected file name.", "file01", name);
        Assert.assertEquals("Unexpected file size.", 0, convert32(testData1, 12));
        Assert.assertEquals("Unexpected file offset.", 231, convert32(testData1, 16));
    }

    @Test
    public void testParseSingleEntryWithPadding() throws IOException {
        byte[] testData1 = new byte[33];
        System.arraycopy(testData, 0, testData1, 0, 32);
        testData1[32] = (byte) 0;
        byte[] buffer = new byte[16];
        int len = DumpArchiveEntry.parse(testData1, 0, buffer, null);
        Assert.assertEquals("Unexpected length.", 16, len);
        Assert.assertEquals("Unexpected inode.", 1013, convert32(buffer, 0));
        Assert.assertEquals("Unexpected file type.", DumpArchiveConstants.TYPE_REG, convert16(buffer, 4));
        Assert.assertEquals("Unexpected file name length.", 7, convert16(buffer, 6));
        String name = decode(null, buffer, 8, buffer[7]);
        Assert.assertEquals("Unexpected file name.", "file01", name);
        Assert.assertEquals("Unexpected file size.", 0, convert32(testData1, 12));
        Assert.assertEquals("Unexpected file offset.", 231, convert32(testData1, 16));
    }

    @Test
    public void testParseMultipleEntries() throws IOException {
        byte[] testData1 = new byte[32 * 3];
        System.arraycopy(testData, 0, testData1, 0, 32);
        System.arraycopy(testData, 32, testData1, 32, 32);
        System.arraycopy(testData, 64, testData1, 64, 32);
        Queue<DumpArchiveEntry> queue = new PriorityQueue<>(3, new Comparator<DumpArchiveEntry>() {
            @Override
            public int compare(DumpArchiveEntry o1, DumpArchiveEntry o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        DumpArchiveEntry entry = null;
        int len = 0;
        int offset = 0;
        while ((len = DumpArchiveEntry.parse(testData1, offset, null, null)) > 0) {
            byte[] buffer = new byte[16];
            System.arraycopy(testData1, offset, buffer, 0, 16);
            entry = new DumpArchiveEntry();
            entry.parse(buffer);
            queue.add(entry);
            offset += len;
        }
        DumpArchiveEntry expectedEntry1 = new DumpArchiveEntry();
        expectedEntry1.setName("file01");
        expectedEntry1.setSimpleName("file01");
        expectedEntry1.setParentIno(1013);
        expectedEntry1.setType(DumpArchiveConstants.TYPE_REG);
        expectedEntry1.setSize(0);
        expectedEntry1.setOffset(231);
        DumpArchiveEntry expectedEntry2 = new DumpArchiveEntry();
        expectedEntry2.setName("file02");
        expectedEntry2.setSimpleName("file02");
        expectedEntry2.setParentIno(1014);
        expectedEntry2.setType(DumpArchiveConstants.TYPE_REG);
        expectedEntry2.setSize(1024);
        expectedEntry2.setOffset(2711);
        DumpArchiveEntry expectedEntry3 = new DumpArchiveEntry();
        expectedEntry3.setName("file03");
        expectedEntry3.setSimpleName("file03");
        expectedEntry3.setParentIno(1015);
        expectedEntry3.setType(DumpArchiveConstants.TYPE_DIR);
        expectedEntry3.setSize(0);
        expectedEntry3.setOffset(3145);
        while ((entry = queue.poll()) != null) {
            Assert.assertEquals("Unexpected name.", expectedEntry1.getName(), entry.getName());
            Assert.assertEquals("Unexpected simple name.", expectedEntry1.getSimpleName(), entry.getSimpleName());
            Assert.assertEquals("Unexpected parent inode.", expectedEntry1.getParentIno(), entry.getParentIno());
            Assert.assertEquals("Unexpected file type.", expectedEntry1.getType(), entry.getType());
            Assert.assertEquals("Unexpected file size.", expectedEntry1.getSize(), entry.getSize());
            Assert.assertEquals("Unexpected file offset.", expectedEntry1.getOffset(), entry.getOffset());
            Assert.assertEquals("Unexpected next entry.", expectedEntry2, inputStream.getNextEntry());
            Assert.assertEquals("Unexpected name.", expectedEntry2.getName(), entry.getName());
            Assert.assertEquals("Unexpected simple name.", expectedEntry2.getSimpleName(), entry.getSimpleName());
            Assert.assertEquals("Unexpected parent inode.", expectedEntry2.getParentIno(), entry.getParentIno());
            Assert.assertEquals("Unexpected file type.", expectedEntry2.getType(), entry.getType());
            Assert.assertEquals("Unexpected file size.", expectedEntry2.getSize(), entry.getSize());
            Assert.assertEquals("Unexpected file offset.", expectedEntry2.getOffset(), entry.getOffset());
            Assert.assertEquals("Unexpected next entry.", expectedEntry3, inputStream.getNextEntry());
            Assert.assertEquals("Unexpected name.", expectedEntry3.getName(), entry.getName());
            Assert.assertEquals("Unexpected simple name.", expectedEntry3.getSimpleName(), entry.getSimpleName());
            Assert.assertEquals("Unexpected parent inode.", expectedEntry3.getParentIno(), entry.getParentIno());
            Assert.assertEquals("Unexpected file type.", expectedEntry3.getType(), entry.getType());
            Assert.assertEquals("Unexpected file size.", expectedEntry3.getSize(), entry.getSize());
            Assert.assertEquals("Unexpected file offset.", expectedEntry3.getOffset(), entry.getOffset());
        }
        Assert.assertNull("Unexpected non-null last entry.", inputStream.getNextEntry());
    }

    @Test
    public void testVerifyHeader() throws IOException {
        byte[] testData = new byte[DumpArchiveConstants.TP_SIZE];
        expectedEx.expect(ArchiveException.class);
        expectedEx.expectMessage("Unexpected magic value.");
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
        inputStream.getSummary();
    }

    @Test
    public void testVerifyHeaderWithInvalidFormatVersion() throws IOException {
        byte[] testData = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(testData, (byte) 0xFF);
        expectedEx.expect(ArchiveException.class);
        expectedEx.expectMessage("Unexpected format version.");
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
        inputStream.getSummary();
    }

    @Test
    public void testVerifyHeaderWithInvalidBlockSize() throws IOException {
        byte[] testData = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(testData, (byte) 0x0F);
        expectedEx.expect(ArchiveException.class);
        expectedEx.expectMessage("Unexpected number of block size records.");
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(new ByteArrayInputStream(testData), null);
        inputStream.getSummary();
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(outputStream);
    }
}