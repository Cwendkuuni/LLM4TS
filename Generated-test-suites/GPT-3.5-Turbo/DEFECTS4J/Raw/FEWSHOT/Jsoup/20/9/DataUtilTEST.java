package org.jsoup.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    @Test
    public void testLoad() throws Exception {
        // test when charsetName is null
        assertNull(DataUtil.load(in, null, baseUri));
        
        // test when charsetName is empty
        assertNull(DataUtil.load(in, "", baseUri));
        
        // test when charsetName is not null or empty
        assertEquals(expected, DataUtil.load(in, charsetName, baseUri));
    }

    @Test
    public void testParseByteData() throws Exception {
        // test when charsetName is null
        assertNull(DataUtil.parseByteData(byteData, null, baseUri, parser));
        
        // test when charsetName is empty
        assertNull(DataUtil.parseByteData(byteData, "", baseUri, parser));
        
        // test when charsetName is not null or empty
        assertEquals(expected, DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        assertEquals(expected, DataUtil.readToByteBuffer(inStream));
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        // test when contentType is null
        assertNull(DataUtil.getCharsetFromContentType(null));
        
        // test when contentType is empty
        assertNull(DataUtil.getCharsetFromContentType(""));
        
        // test when contentType is not null or empty
        assertEquals(expected, DataUtil.getCharsetFromContentType(contentType));
    }
}