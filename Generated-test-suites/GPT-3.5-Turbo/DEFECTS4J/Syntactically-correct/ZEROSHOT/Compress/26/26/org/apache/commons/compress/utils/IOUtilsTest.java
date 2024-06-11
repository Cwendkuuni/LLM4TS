import org.junit.Test;
import org.junit.Assert;
import java.io.*;

public class IOUtilsTest {

    private final InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
    private final InputStream testInputStream = new ByteArrayInputStream(new byte[]{1,2,3,4,5});
    private final OutputStream testOutputStream = new ByteArrayOutputStream();

    @Test
    public void testCopy() throws IOException {
        Assert.assertEquals(5, IOUtils.copy(emptyInputStream, testOutputStream));
        Assert.assertArrayEquals(new byte[0], testOutputStream.toByteArray());

        testOutputStream.reset();

        Assert.assertEquals(5, IOUtils.copy(testInputStream, testOutputStream));
        Assert.assertArrayEquals(new byte[]{1,2,3,4,5}, testOutputStream.toByteArray());

        testOutputStream.reset();

        Assert.assertEquals(0, IOUtils.copy(emptyInputStream, testOutputStream, 100));
        Assert.assertArrayEquals(new byte[0], testOutputStream.toByteArray());

        testOutputStream.reset();

        Assert.assertEquals(5, IOUtils.copy(testInputStream, testOutputStream, 100));
        Assert.assertArrayEquals(new byte[]{1,2,3,4,5}, testOutputStream.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        Assert.assertEquals(0, IOUtils.skip(emptyInputStream, 100));
        Assert.assertEquals(0, IOUtils.skip(testInputStream, 0));
        Assert.assertEquals(2, IOUtils.skip(testInputStream, 2));
        Assert.assertEquals(3, IOUtils.skip(testInputStream, 10));
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[5];

        Assert.assertEquals(0, IOUtils.readFully(emptyInputStream, buffer));
        Assert.assertArrayEquals(new byte[5], buffer);

        Assert.assertEquals(5, IOUtils.readFully(testInputStream, buffer));
        Assert.assertArrayEquals(new byte[]{1,2,3,4,5}, buffer);

        buffer = new byte[10];
        Assert.assertEquals(0, IOUtils.readFully(emptyInputStream, buffer, 0, 10));
        Assert.assertArrayEquals(new byte[10], buffer);

        buffer = new byte[10];
        Assert.assertEquals(5, IOUtils.readFully(testInputStream, buffer, 0, 10));
        Assert.assertArrayEquals(new byte[]{1,2,3,4,5,0,0,0,0,0}, buffer);

        buffer = new byte[10];
        Assert.assertEquals(3, IOUtils.readFully(testInputStream, buffer, 2, 3));
        Assert.assertArrayEquals(new byte[]{0,0,1,2,3,4,0,0,0,0}, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        Assert.assertArrayEquals(new byte[0], IOUtils.toByteArray(emptyInputStream));
        Assert.assertArrayEquals(new byte[]{1,2,3,4,5}, IOUtils.toByteArray(testInputStream));
    }

    @Test
    public void testCloseQuietly() {
        Closeable c = null;
        IOUtils.closeQuietly(c);
        c = new Closeable() {
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(c);
    }
}