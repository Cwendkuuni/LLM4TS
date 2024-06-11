package twitter4j;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setUp() {
        oEmbedRequest = new OEmbedRequest(12345L, "https://www.example.com");
    }

    @Test
    public void testSetMaxWidth() {
        oEmbedRequest.setMaxWidth(500);
        assertEquals(500, oEmbedRequest.maxWidth);
    }

    @Test
    public void testMaxWidth() {
        OEmbedRequest modifiedRequest = oEmbedRequest.maxWidth(800);
        assertEquals(800, modifiedRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest modifiedRequest = oEmbedRequest.HideMedia(false);
        assertFalse(modifiedRequest.hideMedia);
    }

    @Test
    public void testSetHideThread() {
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest modifiedRequest = oEmbedRequest.HideThread(false);
        assertFalse(modifiedRequest.hideThread);
    }

    @Test
    public void testSetOmitScript() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest modifiedRequest = oEmbedRequest.omitScript(false);
        assertFalse(modifiedRequest.omitScript);
    }

    @Test
    public void testSetAlign() {
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testAlign() {
        OEmbedRequest modifiedRequest = oEmbedRequest.align(OEmbedRequest.Align.RIGHT);
        assertEquals(OEmbedRequest.Align.RIGHT, modifiedRequest.align);
    }

    @Test
    public void testSetRelated() {
        String[] related = {"user1", "user2", "user3"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testRelated() {
        String[] related = {"user1", "user2", "user3"};
        OEmbedRequest modifiedRequest = oEmbedRequest.related(related);
        assertArrayEquals(related, modifiedRequest.related);
    }

    @Test
    public void testSetLang() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testLang() {
        OEmbedRequest modifiedRequest = oEmbedRequest.lang("es");
        assertEquals("es", modifiedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        oEmbedRequest.setMaxWidth(500);
        oEmbedRequest.setHideMedia(true);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(false);
        oEmbedRequest.setAlign(OEmbedRequest.Align.RIGHT);
        String[] related = {"user1", "user2", "user3"};
        oEmbedRequest.setRelated(related);
        oEmbedRequest.setLang("en");
        HttpParameter[] expectedParams = {
                new HttpParameter("id", "12345"),
                new HttpParameter("url", "https://www.example.com"),
                new HttpParameter("maxwidth", 500),
                new HttpParameter("hide_media", true),
                new HttpParameter("hide_thread", true),
                new HttpParameter("omit_script", false),
                new HttpParameter("align", "right"),
                new HttpParameter("related", "user1,user2,user3"),
                new HttpParameter("lang", "en")
        };

        assertArrayEquals(expectedParams, oEmbedRequest.asHttpParameterArray());
    }

    @Test
    public void testAppendParameterWithStringValue() {
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("testParam", "testValue", params);
        assertEquals(1, params.size());
        assertEquals("testParam", params.get(0).getName());
        assertEquals("testValue", params.get(0).getValue());
    }

    @Test
    public void testAppendParameterWithLongValue() {
        List<HttpParameter> params = new ArrayList<>();
        oEmbedRequest.appendParameter("testParam", 12345L, params);
        assertEquals(1, params.size());
        assertEquals("testParam", params.get(0).getName());
        assertEquals("12345", params.get(0).getValue());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(oEmbedRequest.equals(oEmbedRequest));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(oEmbedRequest.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(oEmbedRequest.equals("not a OEmbedRequest object"));
    }

    @Test
    public void testEqualsDifferentHideMedia() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.example.com");
        otherRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testEqualsDifferentHideThread() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.example.com");
        otherRequest.setHideThread(false);
        assertFalse(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testEqualsDifferentMaxWidth() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.example.com");
        otherRequest.setMaxWidth(500);
        assertFalse(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testEqualsDifferentOmitScript() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.example.com");
        otherRequest.setOmitScript(true);
        assertFalse(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testEqualsDifferentStatusId() {
        OEmbedRequest otherRequest = new OEmbedRequest(67890L, "https://www.example.com");
        assertFalse(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testEqualsDifferentAlign() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.example.com");
        otherRequest.setAlign(OEmbedRequest.Align.LEFT);
        assertFalse(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testEqualsDifferentLangIsNull() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.example.com");
        otherRequest.setLang("en");
        assertFalse(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testEqualsDifferentRelatedArrays() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.example.com");
        otherRequest.setRelated(new String[]{"user1"});
        assertFalse(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testEqualsDifferentUrl() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.anotherexample.com");
        assertFalse(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testEqualsEqualObject() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.example.com");
        assertTrue(oEmbedRequest.equals(otherRequest));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest otherRequest = new OEmbedRequest(12345L, "https://www.example.com");
        assertEquals(oEmbedRequest.hashCode(), otherRequest.hashCode());
    }

    @Test
    public void testToString() {
        String expectedToString = "OEmbedRequest{statusId=12345, url='https://www.example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expectedToString, oEmbedRequest.toString());
    }
}