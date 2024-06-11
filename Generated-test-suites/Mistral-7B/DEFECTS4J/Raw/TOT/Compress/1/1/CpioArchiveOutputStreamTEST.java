import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link CpioArchiveOutputStream}
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private CpioArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        archiveOutputStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        archiveOutputStream.close();
    }

    /**
     * Tests writing a single entry.
     *
     * @throws IOException on error
     */
    @Test
    public void testSingleEntry() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Test content";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testContent.length());

        archiveOutputStream.putNextEntry(entry);
        archiveOutputStream.write(testContent.getBytes());
        archiveOutputStream.closeArchiveEntry();

        byte[] expectedCpioData = createExpectedCpioData(testFileName, testContent.getBytes());

        Assert.assertEquals(expectedCpioData, out.toByteArray());
    }

    /**
     * Tests writing multiple entries.
     *
     * @throws IOException on error
     */
    @Test
    public void testMultipleEntries() throws IOException {
        String testFileName1 = "testFile1.txt";
        String testContent1 = "Test content 1";
        String testFileName2 = "testFile2.txt";
        String testContent2 = "Test content 2";

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName1);
        entry1.setSize(testContent1.length());

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName2);
        entry2.setSize(testContent2.length());

        archiveOutputStream.putNextEntry(entry1);
        archiveOutputStream.write(testContent1.getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.putNextEntry(entry2);
        archiveOutputStream.write(testContent2.getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();

        byte[] expectedCpioData = createExpectedCpioData(testFileName1, testContent1.getBytes(), testFileName2, testContent2.getBytes());

        Assert.assertEquals(expectedCpioData, out.toByteArray());
    }

    /**
     * Tests writing an entry with a negative format.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        archiveOutputStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        archiveOutputStream.putNextEntry(entry);
    }

    /**
     * Tests writing an entry with a negative size.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSize() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Test content";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(-testContent.length());

        archiveOutputStream.putNextEntry(entry);
    }

    /**
     * Tests writing an entry with a non-matching format and the output stream.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMismatchingFormat() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Test content";

        OutputStream out1 = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream1 = new CpioArchiveOutputStream(out1, CpioConstants.FORMAT_OLD);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testContent.length());

        archiveOutputStream1.putNextEntry(entry);
        archiveOutputStream1.write(testContent.getBytes());
        archiveOutputStream1.closeArchiveEntry();

        archiveOutputStream.putNextEntry(entry);

        archiveOutputStream.write(testContent.getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();

        byte[] expectedCpioData = createExpectedCpioData(testFileName, testContent.getBytes(), CpioConstants.FORMAT_OLD);

        Assert.assertEquals(expectedCpioData, out.toByteArray());
    }

    /**
     * Tests reading the entries from the cpio data.
     *
     * @throws IOException on error
     */
    @Test
    public void testReadEntries() throws IOException {
        String testFileName1 = "testFile1.txt";
        String testContent1 = "Test content 1";
        String testFileName2 = "testFile2.txt";
        String testContent2 = "Test content 2";

        byte[] cpioData = createExpectedCpioData(testFileName1, testContent1.getBytes(), testFileName2, testContent2.getBytes());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(cpioData);
        CpioArchiveInputStream cpioArchiveInputStream = new CpioArchiveInputStream(inputStream);
        CpioArchiveEntry entry = null;

        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        entry = new CpioArchiveEntry();
        entry.setName(testFileName1);
        expectedEntries.put(testFileName1, entry);

        entry = new CpioArchiveEntry();
        entry.setName(testFileName2);
        expectedEntries.put(testFileName2, entry);

        while ((entry = cpioArchiveInputStream.getNextEntry()) != null) {
            Assert.assertNotNull(entry);
            Assert.assertEquals(expectedEntries.get(entry.getName()), entry);
            cpioArchiveInputStream.closeArchiveEntry();
        }

        cpioArchiveInputStream.finish();
    }

    /**
     * Creates the expected cpio data for the given tests.
     *
     * @param name1 the name of the first file
     * @param content1 the content of the first file
     * @param name2 the name of the second file
     * @param content2 the content of the second file
     * @return the expected cpio data
     */
    private byte[] createExpectedCpioData(String... files) {
        StringBuilder sb = new StringBuilder();
        for (String file : files) {
            sb.append(createEntryData(file, files.length > 1 ? files[1] : null));
        }
        byte[] expectedCpioData = sb.toString().getBytes();
        return expectedCpioData;
    }

    /**
     * Creates the expected cpio data for a single entry.
     *
     * @param name the name of the file
     * @param content the content of the file
     * @return the expected cpio data
     */
    private String createEntryData(String name, String content) {
        StringBuilder sb = new StringBuilder();
        sb.append(CpioConstants.MAGIC_NEW);

        sb.append(CpioUtil.long2hex(formatLong(CpioConstants.FORMAT_NEW)));
        sb.append(CpioUtil.long2hex(formatLong(name.length() + 1)));
        sb.append(CpioUtil.long2hex(formatLong(name.length())));
        sb.append(CpioUtil.long2hex(formatLong(name.length())));

        sb.append(CpioUtil.long2hex(formatLong(CpioConstants.PERM_OWNER_READ)));
        sb.append(CpioUtil.long2hex(formatLong(CpioConstants.PERM_GROUP_READ)));
        sb.append(CpioUtil.long2hex(formatLong(CpioConstants.PERM_OTHERS_READ)));

        sb.append(CpioUtil.long2hex(formatLong(CpioConstants.UID_ZERO)));
        sb.append(CpioUtil.long2hex(formatLong(CpioConstants.GID_ZERO)));

        sb.append(CpioUtil.long2hex(formatLong(1)));
        sb.append(CpioUtil.long2hex(formatLong(System.currentTimeMillis() / 1000)));
        sb.append(CpioUtil.long2hex(formatLong(content.length())));
        sb.append(CpioUtil.long2hex(formatLong(0)));
        sb.append(CpioUtil.long2hex(formatLong(0)));
        sb.append(CpioUtil.long2hex(formatLong(0)));
        sb.append(CpioUtil.long2hex(formatLong(0)));

        sb.append(createNameData(name));

        if (content != null) {
            int chksum = 0;
            for (byte b : content.getBytes()) {
                chksum += b;
            }
            sb.append(CpioUtil.long2hex(formatLong(chksum)));
        }

        sb.append(CpioConstants.MAGIC_NEW);

        return sb.toString();
    }

    /**
     * Formats a long value as a hex value.
     *
     * @param longValue the long value
     * @return the formatted hex value
     */
    private long formatLong(long longValue) {
        longValue &= 0xFFFFFFFFL;
        long hexValue = 0;
        int bit = 0;
        for (long mask = 0x00000001L; mask != 0; mask <<= 1) {
            if ((longValue & mask) != 0) {
                hexValue |= 0x01 << bit;
            }
            bit++;
        }
        return hexValue;
    }

    /**
     * Creates the name data for the given name.
     *
     * @param name the name
     * @return the name data
     */
    private String createNameData(String name) {
        StringBuilder sb = new StringBuilder();
        for (char c : name.toCharArray()) {
            int value = 0;
            if (c >= ' ' && c <= '~') {
                value = c;
            } else {
                value = (char) ('.' + ((c >> 3) & 0xFF));
                value |= (char) ((c >> 1) & 0xFF);
                value |= (char) (c & 0x3F);
            }
            sb.append(CpioUtil.long2hex(formatLong(value)));
        }
        return sb.toString();
    }
}