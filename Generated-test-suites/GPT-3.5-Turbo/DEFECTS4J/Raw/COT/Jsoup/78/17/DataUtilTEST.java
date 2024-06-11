
package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Document doc = DataUtil.load(in, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadCustomParser() throws IOException {
        InputStream in = new FileInputStream("test.html");
        Parser parser = Parser.xmlParser();
        Document doc = DataUtil.load(in, "UTF-8", "", parser);
        assertNotNull(doc);
    }
}
