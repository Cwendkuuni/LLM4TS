import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TarArchiveOutputStreamTest {

    private static final int RECORD_SIZE = 512;
    private static final int BLOCK_SIZE = 512;

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream(1024);
        tarOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/ab/ac/ad/ae/af/ag/ah/ai/aj/ak/al/am/an/ao/ap/aq/ar/as/at/au/av/aw/ax/ay/az/ba/bb/bc/bd/be/bf/bg/bh/bi/bj/bk/bl/bm/bn/bo/bp/bq/br/bs/bt/bu/bv/bw/bx/by/bz/baa/baab/baac/bada/baae/bafa/bag/bah/bai/baj/bak/bal/bam/bana/boa/bpq/brs/bts/btu/buv/bvw/bxw/byw/bzx/baza/babz";
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), longFileName);

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.closeArchiveEntry();

        byte[] expectedEOF = new byte[TarConstants.HEADER_MAGIC.length + 2 * TarConstants.RECORD_SIZE];
        System.arraycopy(TarConstants.HEADER_MAGIC, 0, expectedEOF, 0, TarConstants.HEADER_MAGIC.length);
        tarOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        byte[] expectedContent = "Test content".getBytes();
        int position = 0;

        // Checking the header of the first record
        byte[] header = new byte[TarConstants.HEADER_LENGTH];
        System.arraycopy(output, position, header, 0, header.length);
        position += header.length;

        String name = new String(new String(new String(header, 14, TarConstants.NAME_LENGTH), Charset.forName("UTF-8"))).trim();
        Assert.assertEquals(longFileName, name);

        int size = ((int) header[25] & 0xFF) << 8 | ((int) header[26] & 0xFF);
        Assert.assertEquals(expectedContent.length, size);

        byte mode = header[27];
        byte typeflag = header[28];
        Assert.assertEquals(TarConstants.FILETYPE_REGULAR, typeflag);
        Assert.assertEquals('-', mode);

        // Checking the content of the first record
        Assert.assertArrayEquals(expectedContent, getContent(output, position, expectedContent.length));

        // Checking the EOF records
        Assert.assertArrayEquals(expectedEOF, getContent(output, position, expectedEOF.length));
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithTooLongFileName() throws IOException {
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/ab/ac/ad/ae/af/ag/ah/ai/aj/ak/al/am/an/ao/ap/aq/ar/as/at/au/av/aw/ax/ay/az/ba/bb/bc/bd/be/bf/bg/bh/bi/bj/bk/bl/bm/bn/bo/bp/bq/br/bs/bt/bu/bv/bw/bx/by/bz/baa/baab/baac/bada/baae/bafa/bag/bah/bai/baj/bak/bal/bam/bana/boa/bpq/brs/bts/btu/buv/bvw/bxw/byw/bzx/baza/babz/a";
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), longFileName);

        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithDirectoryEntry() throws IOException {
        String dirName = "test_directory";
        File dir = new File(dirName);
        TarArchiveEntry directoryEntry = new TarArchiveEntry(dir, dirName);
        directoryEntry.setDirectory(true);

        tarOutputStream.putArchiveEntry(directoryEntry);
        tarOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        int position = 0;

        // Checking the header of the first record
        byte[] header = new byte[TarConstants.HEADER_LENGTH];
        System.arraycopy(output, position, header, 0, header.length);
        position += header.length;

        String name = new String(new String(new String(header, 14, TarConstants.NAME_LENGTH), Charset.forName("UTF-8"))).trim();
        Assert.assertEquals(dirName, name);

        int size = ((int) header[25] & 0xFF) << 8 | ((int) header[26] & 0xFF);
        Assert.assertEquals(0, size);

        byte mode = header[27];
        byte typeflag = header[28];
        Assert.assertEquals(TarConstants.FILETYPE_DIRECTORY, typeflag);

        // Checking the EOF records
        byte[] expectedEOF = new byte[TarConstants.HEADER_MAGIC.length + 2 * TarConstants.RECORD_SIZE];
        System.arraycopy(TarConstants.HEADER_MAGIC, 0, expectedEOF, 0, TarConstants.HEADER_MAGIC.length);
        tarOutputStream.finish();

        Assert.assertArrayEquals(expectedEOF, getContent(output, position, expectedEOF.length));
    }

    @Test
    public void testPutArchiveEntryWithEmptyFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(new File("test.txt"), "");

        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        byte[] output = outputStream.toByteArray();

        int position = 0;

        // Checking the header of the first record
        byte[] header = new byte[TarConstants.HEADER_LENGTH];
        System.arraycopy(output, position, header, 0, header.length);
        position += header.length;

        String name = new String(new String(new String(header, 14, TarConstants.NAME_LENGTH), Charset.forName("UTF-8"))).trim();
        Assert.assertEquals("", name);

        int size = ((int) header[25] & 0xFF) << 8 | ((int) header[26] & 0xFF);
        Assert.assertEquals(expectedContent.length, size);

        byte mode = header[27];
        byte typeflag = header[28];
        Assert.assertEquals(TarConstants.FILETYPE_REGULAR, typeflag);
        Assert.assertEquals('-', mode);

        // Checking the content of the first record
        Assert.assertArrayEquals(expectedContent, getContent(output, position, expectedContent.length));

        // Checking the EOF records
        byte[] expectedEOF = new byte[TarConstants.HEADER_MAGIC.length + 2 * TarConstants.RECORD_SIZE];
        System.arraycopy(TarConstants.HEADER_MAGIC, 0, expectedEOF, 0, TarConstants.HEADER_MAGIC.length);
        tarOutputStream.finish();

        Assert.assertArrayEquals(expectedEOF, getContent(output, position, expectedEOF.length));
    }

    private byte[] getContent(byte[] output, int position, int length) {
        byte[] content = new byte[length];
        System.arraycopy(output, position, content, 0, length);
        return content;
    }
}