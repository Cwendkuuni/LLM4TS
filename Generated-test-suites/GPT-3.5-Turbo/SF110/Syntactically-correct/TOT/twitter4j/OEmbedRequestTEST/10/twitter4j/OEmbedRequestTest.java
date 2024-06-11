package twitter4j;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setup() {
        oEmbedRequest = new OEmbedRequest(1234, "https://example.com");
        oEmbedRequest.setMaxWidth(800);
        oEmbedRequest.setHideMedia(true);
        oEmbedRequest.setHideThread(true);
        oEmbedRequest.setOmitScript(false);
        oEmbedRequest.setAlign(OEmbedRequest.Align.LEFT);
        oEmbedRequest.setRelated(new String[]{"user1", "user2"});
        oEmbedRequest.setLang("en");
    }

    @Test
    public void testOEmbedRequestCreation() {
        assertEquals(1234, oEmbedRequest.statusId);
        assertEquals("https://example.com", oEmbedRequest.url);
        assertEquals(800, oEmbedRequest.maxWidth);
        assertTrue(oEmbedRequest.hideMedia);
        assertTrue(oEmbedRequest.hideThread);
        assertFalse(oEmbedRequest.omitScript);
        assertEquals(OEmbedRequest.Align.LEFT, oEmbedRequest.align);
        assertArrayEquals(new String[]{"user1", "user2"}, oEmbedRequest.related);
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testMaxWidth() {
        // Typical use case
        oEmbedRequest.setMaxWidth(600);
        assertEquals(600, oEmbedRequest.maxWidth);

        // Edge case: Set maximum width to the largest possible int value
        oEmbedRequest.setMaxWidth(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, oEmbedRequest.maxWidth);

        // Error scenario: Set maximum width to a negative value
        oEmbedRequest.setMaxWidth(-100);
        assertEquals(-100, oEmbedRequest.maxWidth);
    }

    @Test
    public void testHideMedia() {
        // Typical use case
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);

        // Edge case: Set hide media to true
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testHideThread() {
        // Typical use case
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);

        // Edge case: Set hide thread to true
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testOmitScript() {
        // Typical use case
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);

        // Error scenario: Set omit script to false
        oEmbedRequest.setOmitScript(false);
        assertFalse(oEmbedRequest.omitScript);
    }

    @Test
    public void testAlign() {
        // Typical use case
        oEmbedRequest.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbedRequest.align);

        // Edge case: Set align to NONE
        oEmbedRequest.setAlign(OEmbedRequest.Align.NONE);
        assertEquals(OEmbedRequest.Align.NONE, oEmbedRequest.align);
    }

    @Test
    public void testRelated() {
        // Typical use case
        String[] related = new String[]{"user3", "user4"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);

        // Error scenario: Set related to an empty array
        String[] emptyRelated = new String[0];
        oEmbedRequest.setRelated(emptyRelated);
        assertArrayEquals(emptyRelated, oEmbedRequest.related);
    }

    @Test
    public void testLang() {
        // Typical use case
        oEmbedRequest.setLang("fr");
        assertEquals("fr", oEmbedRequest.lang);

        // Edge case: Set lang to an empty string
        oEmbedRequest.setLang("");
        assertEquals("", oEmbedRequest.lang);

        // Error scenario: Set lang to null
        oEmbedRequest.setLang(null);
        assertNull(oEmbedRequest.lang);
    }

    @Test
    public void testAsHttpParameterArray() {
        HttpParameter[] expectedParams = new HttpParameter[]{
            new HttpParameter("id", "1234"),
            new HttpParameter("url", "https://example.com"),
            new HttpParameter("maxwidth", "800"),
            new HttpParameter("hide_media", true),
            new HttpParameter("hide_thread", true),
            new HttpParameter("omit_script", false),
            new HttpParameter("align", "left"),
            new HttpParameter("related", "user1,user2"),
            new HttpParameter("lang", "en")
        };

        HttpParameter[] actualParams = oEmbedRequest.asHttpParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testEquals() {
        OEmbedRequest other = new OEmbedRequest(1234, "https://example.com");
        other.setMaxWidth(800);
        other.setHideMedia(true);
        other.setHideThread(true);
        other.setOmitScript(false);
        other.setAlign(OEmbedRequest.Align.LEFT);
        other.setRelated(new String[]{"user1", "user2"});
        other.setLang("en");

        assertTrue(oEmbedRequest.equals(other));
    }

    @Test
    public void testHashCode() {
        OEmbedRequest other = new OEmbedRequest(1234, "https://example.com");
        other.setMaxWidth(800);
        other.setHideMedia(true);
        other.setHideThread(true);
        other.setOmitScript(false);
        other.setAlign(OEmbedRequest.Align.LEFT);
        other.setRelated(new String[]{"user1", "user2"});
        other.setLang("en");

        assertEquals(oEmbedRequest.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String expectedToString = "OEmbedRequest{statusId=1234, url='https://example.com', maxWidth=800, hideMedia=true, hideThread=true, omitScript=false, align=LEFT, related=[user1, user2], lang='en'}";
        assertEquals(expectedToString, oEmbedRequest.toString());
    }
}