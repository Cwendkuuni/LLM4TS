import org.junit.Test;

import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://twitter.com/twitter/status/123456789");
        assertNotNull(request);
        assertEquals(123456789, request.statusId);
        assertEquals("https://twitter.com/twitter/status/123456789", request.url);
        assertTrue(request.hideMedia);
        assertTrue(request.hideThread);
        assertFalse(request.omitScript);
        assertEquals(OEmbedRequest.Align.NONE, request.align);
        assertArrayEquals(new String[0], request.related);
        assertNull(request.lang);
    }

    @Test
    public void testSetMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(987654321, "https://twitter.com/twitter/status/987654321");
        request.setMaxWidth(500);
        assertEquals(500, request.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(111111111, "https://twitter.com/twitter/status/111111111");
        assertEquals(request, request.MaxWidth(400));
        assertEquals(400, request.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        OEmbedRequest request = new OEmbedRequest(222222222, "https://twitter.com/twitter/status/222222222");
        request.setHideMedia(false);
        assertFalse(request.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(333333333, "https://twitter.com/twitter/status/333333333");
        assertEquals(request, request.HideMedia(false));
        assertFalse(request.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        OEmbedRequest request = new OEmbedRequest(444444444, "https://twitter.com/twitter/status/444444444");
        request.setHideThread(false);
        assertFalse(request.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(555555555, "https://twitter.com/twitter/status/555555555");
        assertEquals(request, request.HideThread(false));
        assertFalse(request.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        OEmbedRequest request = new OEmbedRequest(666666666, "https://twitter.com/twitter/status/666666666");
        request.setOmitScript(true);
        assertTrue(request.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(777777777, "https://twitter.com/twitter/status/777777777");
        assertEquals(request, request.omitScript(true));
        assertTrue(request.omitScript);
    }

    @Test
    public void testSetAlign() {
        OEmbedRequest request = new OEmbedRequest(888888888, "https://twitter.com/twitter/status/888888888");
        request.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, request.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(999999999, "https://twitter.com/twitter/status/999999999");
        assertEquals(request, request.align(OEmbedRequest.Align.LEFT));
        assertEquals(OEmbedRequest.Align.LEFT, request.align);
    }

    @Test
    public void testSetRelated() {
        OEmbedRequest request = new OEmbedRequest(101010101, "https://twitter.com/twitter/status/101010101");
        String[] related = {"user1", "user2", "user3"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(121212121, "https://twitter.com/twitter/status/121212121");
        String[] related = {"user4", "user5", "user6"};
        assertEquals(request, request.related(related));
        assertArrayEquals(related, request.related);
    }

    @Test
    public void testSetLang() {
        OEmbedRequest request = new OEmbedRequest(131313131, "https://twitter.com/twitter/status/131313131");
        request.setLang("en");
        assertEquals("en", request.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(141414141, "https://twitter.com/twitter/status/141414141");
        assertEquals(request, request.lang("fr"));
        assertEquals("fr", request.lang);
    }    

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(151515151, "https://twitter.com/twitter/status/151515151");
        request.setMaxWidth(600);
        request.setHideMedia(false);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user7", "user8"};
        request.setRelated(related);
        request.setLang("de");

        HttpParameter[] expectedParams = {
                new HttpParameter("id", "151515151"),
                new HttpParameter("url", "https://twitter.com/twitter/status/151515151"),
                new HttpParameter("maxwidth", "600"),
                new HttpParameter("hide_media", false),
                new HttpParameter("hide_thread", false),
                new HttpParameter("omit_script", true),
                new HttpParameter("align", "right"),
                new HttpParameter("related", "user7,user8"),
                new HttpParameter("lang", "de")
        };

        assertArrayEquals(expectedParams, request.asHttpParameterArray());
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(161616161, "https://twitter.com/twitter/status/161616161");
        request1.setMaxWidth(700);
        request1.setHideMedia(true);
        request1.setHideThread(true);
        request1.setOmitScript(false);
        request1.setAlign(OEmbedRequest.Align.CENTER);
        String[] related1 = {"user9"};
        request1.setRelated(related1);
        request1.setLang("ja");

        OEmbedRequest request2 = new OEmbedRequest(161616161, "https://twitter.com/twitter/status/161616161");
        request2.setMaxWidth(700);
        request2.setHideMedia(true);
        request2.setHideThread(true);
        request2.setOmitScript(false);
        request2.setAlign(OEmbedRequest.Align.CENTER);
        String[] related2 = {"user9"};
        request2.setRelated(related2);
        request2.setLang("ja");

        OEmbedRequest request3 = new OEmbedRequest(171717171, "https://twitter.com/twitter/status/171717171");
        request3.setMaxWidth(800);
        request3.setHideMedia(false);
        request3.setHideThread(false);
        request3.setOmitScript(true);
        request3.setAlign(OEmbedRequest.Align.LEFT);
        String[] related3 = {"user10"};
        request3.setRelated(related3);
        request3.setLang("zh");

        assertTrue(request1.equals(request2));
        assertFalse(request1.equals(request3));
    }
    
    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(181818181, "https://twitter.com/twitter/status/181818181");
        request1.setMaxWidth(900);
        request1.setHideMedia(false);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.NONE);
        String[] related1 = {"user11"};
        request1.setRelated(related1);
        request1.setLang("es");

        OEmbedRequest request2 = new OEmbedRequest(181818181, "https://twitter.com/twitter/status/181818181");
        request2.setMaxWidth(900);
        request2.setHideMedia(false);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.NONE);
        String[] related2 = {"user11"};
        request2.setRelated(related2);
        request2.setLang("es");

        OEmbedRequest request3 = new OEmbedRequest(191919191, "https://twitter.com/twitter/status/191919191");
        request3.setMaxWidth(1000);
        request3.setHideMedia(true);
        request3.setHideThread(true);
        request3.setOmitScript(false);
        request3.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related3 = {"user12"};
        request3.setRelated(related3);
        request3.setLang("pt");

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(202020202, "https://twitter.com/twitter/status/202020202");
        request.setMaxWidth(1100);
        request.setHideMedia(true);
        request.setHideThread(true);
        request.setOmitScript(false);
        request.setAlign(OEmbedRequest.Align.LEFT);
        String[] related = {"user13"};
        request.setRelated(related);
        request.setLang("it");

        String expectedString = "OEmbedRequest{statusId=202020202, url='https://twitter.com/twitter/status/202020202', maxWidth=1100, hideMedia=true, hideThread=true, omitScript=false, align=LEFT, related=[user13], lang='it'}";
        assertEquals(expectedString, request.toString());
    }
}