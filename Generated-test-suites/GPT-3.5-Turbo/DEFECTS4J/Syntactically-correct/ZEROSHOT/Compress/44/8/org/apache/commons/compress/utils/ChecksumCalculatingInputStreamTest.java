import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private Checksum checksum;
    private InputStream in;

    @Before
    public void setUp() {
        in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        checksum = new Checksum() {
            @Override
            public void update(int b) {
                // Do nothing
            }

            @Override
            public void update(byte[] b, int off, int len) {
                // Do nothing
            }

            @Override
            public long getValue() {
                return 0L;
            }

            @Override
            public void reset() {
                // Do nothing
            }
        };
        
        inputStream = new ChecksumCalculatingInputStream(checksum, in);
    }

    @Test
    public void testRead() throws IOException {
        int result = inputStream.read();
        
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] byteArray = new byte[5];
        int result = inputStream.read(byteArray);
        
        Assert.assertEquals(5, result);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] byteArray = new byte[10];
        int offset = 2;
        int length = 6;
        int result = inputStream.read(byteArray, offset, length);
        
        Assert.assertEquals(6, result);
    }

    @Test
    public void testSkip() throws IOException {
        long result = inputStream.skip(5);
        
        Assert.assertEquals(1, result);
    }

    @Test
    public void testGetValue() {
        long result = inputStream.getValue();
        
        Assert.assertEquals(0L, result);
    }

}