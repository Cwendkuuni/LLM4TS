package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructorAndGetters() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");

        assertEquals(123456789, oEmbedRequest.getStatusId());
        assertEquals("https://twitter.com/twitter/status/123456789", oEmbedRequest.getUrl());
        assertEquals(true, oEmbedRequest.getHideMedia());
        assertEquals(true, oEmbedRequest.getHideThread());
        assertEquals(false, oEmbedRequest.getOmitScript());
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.getAlign());
        assertArrayEquals(new String[0], oEmbedRequest.getRelated());
        assertNull(oEmbedRequest.getLang());
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");

        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.getMaxWidth());

        oEmbedRequest.MaxWidth(800);
        assertEquals(800, oEmbedRequest.getMaxWidth());
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");

        oEmbedRequest.setHideMedia(false);
        assertEquals(false, oEmbedRequest.getHideMedia());

        oEmbedRequest.HideMedia(true);
        assertEquals(true, oEmbedRequest.getHideMedia());
    }

    @Test
    public void testHideThread() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");

        oEmbedRequest.setHideThread(false);
        assertEquals(false, oEmbedRequest.getHideThread());

        oEmbedRequest.HideThread(true);
        assertEquals(true, oEmbedRequest.getHideThread());
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");

        oEmbedRequest.setOmitScript(true);
        assertEquals(true, oEmbedRequest.getOmitScript());

        oEmbedRequest.omitScript(false);
        assertEquals(false, oEmbedRequest.getOmitScript());
    }

    @Test
    public void testAlign() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");

        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.getAlign());

        oEmbedRequest.align(OEmbedRequest.Align.LEFT);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.getAlign());
    }

    @Test
    public void testRelated() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");

        String[] related = {"user1", "user2", "user3"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.getRelated());

        String[] newRelated = {"user4", "user5"};
        oEmbedRequest.related(newRelated);
        assertArrayEquals(newRelated, oEmbedRequest.getRelated());
    }

    @Test
    public void testLang() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");

        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.getLang());

        oEmbedRequest.lang("es");
        assertEquals("es", oEmbedRequest.getLang());
    }

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest.setRelated(new String[]{"user1", "user2"});
        oEmbedRequest.setLang("en");

        HttpParameter[] expectedParams = new HttpParameter[]{
                new HttpParameter("id", "123456789"),
                new HttpParameter("url", "https://twitter.com/twitter/status/123456789"),
                new HttpParameter("maxwidth", 500),
                new HttpParameter("hide_media", false),
                new HttpParameter("hide_thread", false),
                new HttpParameter("omit_script", true),
                new HttpParameter("align", "center"),
                new HttpParameter("related", "user1,user2"),
                new HttpParameter("lang", "en")
        };

        assertArrayEquals(expectedParams, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testEqualsAndHashcode() {
        OEmbedRequest oEmbedRequest1 = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        assertEquals(oEmbedRequest1, oEmbedRequest2);
        assertEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());

        oEmbedRequest2.setMaxWidth(500);
        assertNotEquals(oEmbedRequest1, oEmbedRequest2);
        assertNotEquals(oEmbedRequest1.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest oEmbedRequest = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(false);
        oEmbedRequest.setHideThread(false);
        oEmbedRequest.setOmitScript(true);
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        oEmbedRequest.setRelated(new String[]{"user1", "user2"});
        oEmbedRequest.setLang("en");

        String expectedString = "OEmbedRequest{statusId=123456789, url='https://twitter.com/twitter/status/123456789', maxWidth=500, hideMedia=false, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";

        assertEquals(expectedString, oEmbedRequest.toString());
    }
}