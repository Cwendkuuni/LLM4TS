import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbed;

    @Before
    public void setUp() {
        oEmbed = new OEmbedRequest(123, "https://example.com");
    }

    @Test
    public void testMaxWidth() {
        oEmbed.setMaxWidth(500);
        assertEquals(500, oEmbed.maxWidth);
    }

    @Test
    public void testHideMedia() {
        oEmbed.setHideMedia(true);
        assertTrue(oEmbed.hideMedia);
    }

    @Test
    public void testHideThread() {
        oEmbed.setHideThread(true);
        assertTrue(oEmbed.hideThread);
    }

    @Test
    public void testOmitScript() {
        oEmbed.setOmitScript(true);
        assertTrue(oEmbed.omitScript);
    }

    @Test
    public void testAlign() {
        oEmbed.setAlign(OEmbedRequest.Align.CENTER);
        assertEquals(OEmbedRequest.Align.CENTER, oEmbed.align);
    }

    @Test
    public void testRelated() {
        String[] related = {"user1", "user2"};
        oEmbed.setRelated(related);
        assertArrayEquals(related, oEmbed.related);
    }

    @Test
    public void testLang() {
        oEmbed.setLang("en");
        assertEquals("en", oEmbed.lang);
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(oEmbed.equals(oEmbed));
    }

    @Test
    public void testEquals_nullObject() {
        assertFalse(oEmbed.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(oEmbed.equals("Hello"));
    }

    @Test
    public void testEquals_differentHideMedia() {
        OEmbedRequest other = new OEmbedRequest(123, "https://example.com");
        other.setHideMedia(false);
        assertFalse(oEmbed.equals(other));
    }

    @Test
    public void testEquals_differentHideThread() {
        OEmbedRequest other = new OEmbedRequest(123, "https://example.com");
        other.setHideThread(false);
        assertFalse(oEmbed.equals(other));
    }

    @Test
    public void testEquals_differentMaxWidth() {
        OEmbedRequest other = new OEmbedRequest(123, "https://example.com");
        other.setMaxWidth(600);
        assertFalse(oEmbed.equals(other));
    }

    @Test
    public void testEquals_differentOmitScript() {
        OEmbedRequest other = new OEmbedRequest(123, "https://example.com");
        other.setOmitScript(true);
        assertFalse(oEmbed.equals(other));
    }

    @Test
    public void testEquals_differentStatusId() {
        OEmbedRequest other = new OEmbedRequest(456, "https://example.com");
        assertFalse(oEmbed.equals(other));
    }

    @Test
    public void testEquals_differentAlign() {
        OEmbedRequest other = new OEmbedRequest(123, "https://example.com");
        other.setAlign(OEmbedRequest.Align.LEFT);
        assertFalse(oEmbed.equals(other));
    }

    @Test
    public void testEquals_differentLang() {
        OEmbedRequest other = new OEmbedRequest(123, "https://example.com");
        other.setLang("fr");
        assertFalse(oEmbed.equals(other));
    }

    @Test
    public void testEquals_differentRelated() {
        OEmbedRequest other = new OEmbedRequest(123, "https://example.com");
        String[] related = {"user1", "user2"};
        other.setRelated(related);
        assertFalse(oEmbed.equals(other));
    }

    @Test
    public void testEquals_differentUrl() {
        OEmbedRequest other = new OEmbedRequest(123, "https://another-url.com");
        assertFalse(oEmbed.equals(other));
    }

    @Test
    public void testHashCode_sameObject() {
        assertEquals(oEmbed.hashCode(), oEmbed.hashCode());
    }

    @Test
    public void testHashCode_equalObjects() {
        OEmbedRequest other = new OEmbedRequest(123, "https://example.com");
        assertEquals(oEmbed.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "OEmbedRequest{statusId=123, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expected, oEmbed.toString());
    }
}