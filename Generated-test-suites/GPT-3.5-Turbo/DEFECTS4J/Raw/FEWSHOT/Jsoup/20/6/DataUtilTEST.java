
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    private static final String testHtml = "<html>\n<head>\n<title>Test HTML</title>\n</head>\n<body>\n<h1>Hello World!</h1>\n<p>This is a test HTML document.</p>\n</body>\n</html>";
    private static final String testFilePath = "test.html";

    @Before
    public void setup() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(testFilePath), StandardCharsets.UTF_8))) {
            writer.write(testHtml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadFile() {
        try {
            Document doc = DataUtil.load(new File(testFilePath), null, "");
            assertNotNull(doc);
            assertEquals(testHtml, doc.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStream() {
        try (InputStream inputStream = new FileInputStream(testFilePath)) {
            Document doc = DataUtil.load(inputStream, null, "");
            assertNotNull(doc);
            assertEquals(testHtml, doc.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithCharset() {
        try (InputStream inputStream = new FileInputStream(testFilePath)) {
            Document doc = DataUtil.load(inputStream, "UTF-8", "");
            assertNotNull(doc);
            assertEquals(testHtml, doc.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadInputStreamWithCustomParser() {
        try (InputStream inputStream = new FileInputStream(testFilePath)) {
            Document doc = DataUtil.load(inputStream, null, "", Parser.xmlParser());
            assertNotNull(doc);
            assertEquals(testHtml, doc.html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
