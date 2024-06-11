package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void loadFile_validFile_success() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "");
            Assert.assertNotNull(doc);
            // Add more assertions here
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void loadFile_nullFile_exceptionThrown() {
        try {
            Document doc = DataUtil.load(null, "UTF-8", "");
            Assert.fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void loadFile_nonexistentFile_exceptionThrown() {
        File file = new File("nonexistent.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "");
            Assert.fail("FileNotFoundException not thrown");
        } catch (FileNotFoundException e) {
            // Expected exception
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void loadFile_invalidCharset_exceptionThrown() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "InvalidCharset", "");
            Assert.fail("UnsupportedEncodingException not thrown");
        } catch (UnsupportedEncodingException e) {
            // Expected exception
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void loadFile_withBaseUri_success() {
        File file = new File("test.html");
        try {
            Document doc = DataUtil.load(file, "UTF-8", "https://example.com");
            Assert.assertNotNull(doc);
            // Add more assertions here
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void loadInputStream_validInputStream_success() {
        InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "");
            Assert.assertNotNull(doc);
            // Add more assertions here
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Assert.fail("IOException occurred");
            }
        }
    }

    @Test
    public void loadInputStream_nullInputStream_exceptionThrown() {
        try {
            Document doc = DataUtil.load((InputStream) null, "UTF-8", "");
            Assert.fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        }
    }

    @Test
    public void loadInputStream_invalidCharset_exceptionThrown() {
        InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, "InvalidCharset", "");
            Assert.fail("UnsupportedEncodingException not thrown");
        } catch (UnsupportedEncodingException e) {
            // Expected exception
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Assert.fail("IOException occurred");
            }
        }
    }

    @Test
    public void loadInputStream_withBaseUri_success() {
        InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "https://example.com");
            Assert.assertNotNull(doc);
            // Add more assertions here
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Assert.fail("IOException occurred");
            }
        }
    }

    @Test
    public void loadInputStream_withParser_success() {
        InputStream inputStream = new ByteArrayInputStream("<html><body></body></html>".getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, "UTF-8", "", Parser.xmlParser());
            Assert.assertNotNull(doc);
            // Add more assertions here
        } catch (IOException e) {
            Assert.fail("IOException occurred");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Assert.fail("IOException occurred");
            }
        }
    }

    @Test
    public void parseByteData_withCharset_success() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><body></body></html>".getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(doc);
        // Add more assertions here
    }

    @Test
    public void parseByteData_nullByteBuffer_exceptionThrown() {
        try {
            Document doc = DataUtil.parseByteData(null, "UTF-8", "", Parser.htmlParser());
            Assert.fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void parseByteData_invalidCharset_exceptionThrown() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("<html><body></body></html>".getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.parseByteData(byteBuffer, "InvalidCharset", "", Parser.htmlParser());
            Assert.fail("UnsupportedEncodingException not thrown");
        } catch (UnsupportedEncodingException e) {
            // Expected exception
        }
    }

    @Test
    public void getCharsetFromContentType_validCharset_success() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNotNull(charset);
        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void getCharsetFromContentType_nullCharset_nullReturned() {
        String charset = DataUtil.getCharsetFromContentType(null);
        Assert.assertNull(charset);
    }

    @Test
    public void getCharsetFromContentType_invalidCharset_nullReturned() {
        String contentType = "text/html; charset=";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

}