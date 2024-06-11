import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OEmbedRequestTest {

    private OEmbedRequest request;

    @Before
    public void setup() {
        request = new OEmbedRequest(1L, "https://example.com");
    }

    // Test for setMaxWidth method
    @Test
    public void testSetMaxWidth() {
        int maxWidth = 300;
        request.setMaxWidth(maxWidth);
        assertEquals(maxWidth, request.maxWidth);
    }

    // Test for MaxWidth method
    @Test
    public void testMaxWidth() {
        int maxWidth = 500;
        OEmbedRequest newRequest = request.MaxWidth(maxWidth);
        assertEquals(maxWidth, newRequest.maxWidth);
    }

    // Test for setHideMedia method
    @Test
    public void testSetHideMedia() {
        boolean hideMedia = false;
        request.setHideMedia(hideMedia);
        assertEquals(hideMedia, request.hideMedia);
    }

    // Test for HideMedia method
    @Test
    public void testHideMedia() {
        boolean hideMedia = true;
        OEmbedRequest newRequest = request.HideMedia(hideMedia);
        assertEquals(hideMedia, newRequest.hideMedia);
    }

    // Test for setHideThread method
    @Test
    public void testSetHideThread() {
        boolean hideThread = false;
        request.setHideThread(hideThread);
        assertEquals(hideThread, request.hideThread);
    }

    // Test for HideThread method
    @Test
    public void testHideThread() {
        boolean hideThread = true;
        OEmbedRequest newRequest = request.HideThread(hideThread);
        assertEquals(hideThread, newRequest.hideThread);
    }

    // Test for setOmitScript method
    @Test
    public void testSetOmitScript() {
        boolean omitScript = true;
        request.setOmitScript(omitScript);
        assertEquals(omitScript, request.omitScript);
    }

    // Test for omitScript method
    @Test
    public void testOmitScript() {
        boolean omitScript = false;
        OEmbedRequest newRequest = request.omitScript(omitScript);
        assertEquals(omitScript, newRequest.omitScript);
    }

    // Test for setAlign method
    @Test
    public void testSetAlign() {
        OEmbedRequest.Align align = OEmbedRequest.Align.CENTER;
        request.setAlign(align);
        assertEquals(align, request.align);
    }

    // Test for align method
    @Test
    public void testAlign() {
        OEmbedRequest.Align align = OEmbedRequest.Align.RIGHT;
        OEmbedRequest newRequest = request.align(align);
        assertEquals(align, newRequest.align);
    }

    // Test for setRelated method
    @Test
    public void testSetRelated() {
        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertArrayEquals(related, request.related);
    }

    // Test for related method
    @Test
    public void testRelated() {
        String[] related = {"user3"};
        OEmbedRequest newRequest = request.related(related);
        assertArrayEquals(related, newRequest.related);
    }

    // Test for setLang method
    @Test
    public void testSetLang() {
        String lang = "en";
        request.setLang(lang);
        assertEquals(lang, request.lang);
    }

    // Test for lang method
    @Test
    public void testLang() {
        String lang = "fr";
        OEmbedRequest newRequest = request.lang(lang);
        assertEquals(lang, newRequest.lang);
    }

    // Test for asHttpParameterArray method
    @Test
    public void testAsHttpParameterArray() {
        int maxWidth = 300;
        boolean hideMedia = false;
        boolean hideThread = true;
        boolean omitScript = true;
        OEmbedRequest.Align align = OEmbedRequest.Align.CENTER;
        String[] related = {"user1", "user2"};
        String lang = "en";
        request.setMaxWidth(maxWidth);
        request.setHideMedia(hideMedia);
        request.setHideThread(hideThread);
        request.setOmitScript(omitScript);
        request.setAlign(align);
        request.setRelated(related);
        request.setLang(lang);

        HttpParameter[] expectedArray = new HttpParameter[8];
        expectedArray[0] = new HttpParameter("id", "1");
        expectedArray[1] = new HttpParameter("url", "https://example.com");
        expectedArray[2] = new HttpParameter("maxwidth", "300");
        expectedArray[3] = new HttpParameter("hide_media", false);
        expectedArray[4] = new HttpParameter("hide_thread", true);
        expectedArray[5] = new HttpParameter("omit_script", true);
        expectedArray[6] = new HttpParameter("align", "center");
        expectedArray[7] = new HttpParameter("related", "user1,user2");
        expectedArray[8] = new HttpParameter("lang", "en");

        assertArrayEquals(expectedArray, request.asHttpParameterArray());
    }

    // Test for equals method - when objects are equal
    @Test
    public void testEqualsEqualObjects() {
        OEmbedRequest sameRequest = new OEmbedRequest(1L, "https://example.com");
        assertEquals(request, sameRequest);
    }

    // Test for equals method - when objects are not equal
    @Test
    public void testEqualsNotEqualObjects() {
        OEmbedRequest differentRequest = new OEmbedRequest(2L, "https://example2.com");
        assertNotEquals(request, differentRequest);
    }

    // Test for hashCode method - when objects are equal
    @Test
    public void testHashCodeEqualObjects() {
        OEmbedRequest sameRequest = new OEmbedRequest(1L, "https://example.com");
        assertEquals(request.hashCode(), sameRequest.hashCode());
    }

    // Test for hashCode method - when objects are not equal
    @Test
    public void testHashCodeNotEqualObjects() {
        OEmbedRequest differentRequest = new OEmbedRequest(2L, "https://example2.com");
        assertNotEquals(request.hashCode(), differentRequest.hashCode());
    }

    // Test for toString method
    @Test
    public void testToString() {
        String expectedString = "OEmbedRequest{statusId=1, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expectedString, request.toString());
    }

}