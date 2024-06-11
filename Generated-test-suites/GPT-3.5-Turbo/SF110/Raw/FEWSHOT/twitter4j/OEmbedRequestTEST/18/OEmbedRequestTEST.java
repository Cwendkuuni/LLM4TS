
package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        assertEquals(123L, oembed.getStatusId());
        assertEquals("https://example.com", oembed.getUrl());
        assertTrue(oembed.getHideMedia());
        assertTrue(oembed.getHideThread());
        assertFalse(oembed.getOmitScript());
        assertEquals(OEmbedRequest.Align.NONE, oembed.getAlign());
        assertEquals(0, oembed.getRelated().length);
        assertEquals(null, oembed.getLang());
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        oembed.setMaxWidth(500);
        assertEquals(500, oembed.getMaxWidth());
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        oembed.setHideMedia(false);
        assertFalse(oembed.getHideMedia());
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        oembed.setHideThread(false);
        assertFalse(oembed.getHideThread());
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        oembed.setOmitScript(true);
        assertTrue(oembed.getOmitScript());
    }

    @Test
    public void testAlign() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        oembed.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oembed.getAlign());
    }

    @Test
    public void testRelated() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        String[] related = {"related1", "related2"};
        oembed.setRelated(related);
        assertEquals(related, oembed.getRelated());
    }

    @Test
    public void testLang() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        oembed.setLang("en");
        assertEquals("en", oembed.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        oembed.setMaxWidth(500);
        oembed.setHideMedia(false);
        oembed.setHideThread(false);
        oembed.setOmitScript(true);
        oembed.setAlign(OEmbedRequest.Align.CENTER);
        String[] related = {"related1", "related2"};
        oembed.setRelated(related);
        oembed.setLang("en");

        HttpParameter[] expectedArray = new HttpParameter[12];
        expectedArray[0] = new HttpParameter("id", "123");
        expectedArray[1] = new HttpParameter("url", "https://example.com");
        expectedArray[2] = new HttpParameter("maxwidth", "500");
        expectedArray[3] = new HttpParameter("hide_media", false);
        expectedArray[4] = new HttpParameter("hide_thread", false);
        expectedArray[5] = new HttpParameter("omit_script", true);
        expectedArray[6] = new HttpParameter("align", "center");
        expectedArray[7] = new HttpParameter("related", StringUtil.join(related));
        expectedArray[8] = new HttpParameter("lang", "en");

        HttpParameter[] actualArray = oembed.asHttpParameterArray();

        assertEquals(expectedArray.length, actualArray.length);
        for (int i = 0; i < expectedArray.length; i++) {
            assertEquals(expectedArray[i].getName(), actualArray[i].getName());
            assertEquals(expectedArray[i].getValue(), actualArray[i].getValue());
        }
    }

    @Test
    public void testEquals() {
        OEmbedRequest oembed1 = new OEmbedRequest(123L, "https://example.com");
        OEmbedRequest oembed2 = new OEmbedRequest(123L, "https://example.com");
        assertTrue(oembed1.equals(oembed2));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest oembed1 = new OEmbedRequest(123L, "https://example.com");
        OEmbedRequest oembed2 = new OEmbedRequest(123L, "https://example.com");
        assertEquals(oembed1.hashCode(), oembed2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oembed = new OEmbedRequest(123L, "https://example.com");
        assertEquals("OEmbedRequest{statusId=123, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}", oembed.toString());
    }
}
