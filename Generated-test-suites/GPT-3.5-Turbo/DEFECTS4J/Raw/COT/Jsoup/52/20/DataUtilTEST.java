
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(file, charsetName, baseUri);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testLoadInputStream() {
        InputStream is = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        try {
            is = new FileInputStream("test.html");
            Document document = DataUtil.load(is, charsetName, baseUri);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Assert.fail("IOException occurred while closing input stream");
                }
            }
        }
    }

    @Test
    public void testLoadInputStreamWithParser() {
        InputStream is = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            is = new FileInputStream("test.html");
            Document document = DataUtil.load(is, charsetName, baseUri, parser);
            Assert.assertNotNull(document);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Assert.fail("IOException occurred while closing input stream");
                }
            }
        }
    }

    @Test
    public void testCrossStreams() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = new FileInputStream("test.txt");
            DataUtil.crossStreams(is, out);
            byte[] bytes = out.toByteArray();
            Assert.assertNotNull(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Assert.fail("IOException occurred while closing input stream");
                }
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail("IOException occurred while closing output stream");
            }
        }
    }

    @Test
    public void testParseByteData() {
        ByteBuffer byteData = ByteBuffer.wrap("test".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() {
        InputStream is = null;
        try {
            is = new FileInputStream("test.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(is);
            Assert.assertNotNull(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Assert.fail("IOException occurred while closing input stream");
                }
            }
        }
    }

    @Test
    public void testReadToByteBufferWithMaxLength() {
        InputStream is = null;
        int maxSize = 1024;
        try {
            is = new FileInputStream("test.txt");
            ByteBuffer byteBuffer = DataUtil.readToByteBuffer(is, maxSize);
            Assert.assertNotNull(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Assert.fail("IOException occurred while closing input stream");
                }
            }
        }
    }

    @Test
    public void testReadFileToByteBuffer() {
        File file = new File("test.txt");
        try {
            ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
            Assert.assertNotNull(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        Assert.assertEquals(0, byteBuffer.capacity());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(DataUtil.boundaryLength, boundary.length());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 't', 'e', 's', 't'});
        String charsetName = "UTF-8";
        charsetName = DataUtil.detectCharsetFromBom(byteData, charsetName);
        Assert.assertEquals("UTF-8", charsetName);
    }
}
