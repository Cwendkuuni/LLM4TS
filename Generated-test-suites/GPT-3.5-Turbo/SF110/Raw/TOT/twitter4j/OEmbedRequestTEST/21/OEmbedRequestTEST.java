import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import twitter4j.OEmbedRequest;
import twitter4j.OEmbedRequest.Align;

public class OEmbedRequestTest {

    private OEmbedRequest oEmbedRequest;

    @Before
    public void setup() {
        oEmbedRequest = new OEmbedRequest(123, "https://example.com");
    }

    @Test
    public void testSetMaxWidth_validValue() {
        oEmbedRequest.setMaxWidth(100);
        assertEquals(100, oEmbedRequest.maxWidth);
    }

    @Test
    public void testSetHideMedia_true() {
        oEmbedRequest.setHideMedia(true);
        assertTrue(oEmbedRequest.hideMedia);
    }

    @Test
    public void testSetHideMedia_false() {
        oEmbedRequest.setHideMedia(false);
        assertFalse(oEmbedRequest.hideMedia);
    }

    @Test
    public void testSetHideThread_true() {
        oEmbedRequest.setHideThread(true);
        assertTrue(oEmbedRequest.hideThread);
    }

    @Test
    public void testSetHideThread_false() {
        oEmbedRequest.setHideThread(false);
        assertFalse(oEmbedRequest.hideThread);
    }

    @Test
    public void testSetOmitScript_true() {
        oEmbedRequest.setOmitScript(true);
        assertTrue(oEmbedRequest.omitScript);
    }

    @Test
    public void testSetOmitScript_false() {
        oEmbedRequest.setOmitScript(false);
        assertFalse(oEmbedRequest.omitScript);
    }

    @Test
    public void testSetAlign_validValue() {
        oEmbedRequest.setAlign(Align.CENTER);
        assertEquals(Align.CENTER, oEmbedRequest.align);
    }

    @Test
    public void testSetRelated_validValue() {
        String[] related = {"user1", "user2"};
        oEmbedRequest.setRelated(related);
        assertArrayEquals(related, oEmbedRequest.related);
    }

    @Test
    public void testSetLang_validValue() {
        oEmbedRequest.setLang("en");
        assertEquals("en", oEmbedRequest.lang);
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(oEmbedRequest.equals(oEmbedRequest));
    }

    @Test
    public void testEquals_sameValues() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        assertTrue(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(oEmbedRequest.equals("test"));
    }

    @Test
    public void testEquals_differentHideMediaValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setHideMedia(false);
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_differentHideThreadValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setHideThread(false);
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_differentMaxWidthValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setMaxWidth(200);
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_differentOmitScriptValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setOmitScript(true);
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_differentAlignValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setAlign(Align.LEFT);
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_differentLangValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setLang("es");
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_differentRelatedValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        String[] related = {"user1", "user2"};
        oEmbedRequest2.setRelated(related);
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_differentUrlValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.org");
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_nullUrlValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, null);
        assertFalse(oEmbedRequest.equals(oEmbedRequest2));
    }

    @Test
    public void testEquals_nullLangValue() {
        oEmbedRequest.setLang("en");
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        assertFalse(oEmbedRequest2.equals(oEmbedRequest));
    }

    @Test
    public void testEquals_differentClassObject() {
        assertFalse(oEmbedRequest.equals(new Object()));
    }

    @Test
    public void testHashCode_sameObject() {
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest.hashCode());
    }

    @Test
    public void testHashCode_sameValues() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        assertEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testHashCode_differentHideMediaValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setHideMedia(false);
        assertNotEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testHashCode_differentHideThreadValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setHideThread(false);
        assertNotEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testHashCode_differentMaxWidthValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setMaxWidth(200);
        assertNotEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testHashCode_differentOmitScriptValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setOmitScript(true);
        assertNotEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testHashCode_differentAlignValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setAlign(Align.LEFT);
        assertNotEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testHashCode_differentLangValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        oEmbedRequest2.setLang("es");
        assertNotEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testHashCode_differentRelatedValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.com");
        String[] related = {"user1", "user2"};
        oEmbedRequest2.setRelated(related);
        assertNotEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testHashCode_differentUrlValue() {
        OEmbedRequest oEmbedRequest2 = new OEmbedRequest(123, "https://example.org");
        assertNotEquals(oEmbedRequest.hashCode(), oEmbedRequest2.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "OEmbedRequest{statusId=123, url='https://example.com', maxWidth=0, " +
                "hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=[], lang='null'}";
        assertEquals(expectedString, oEmbedRequest.toString());
    }
}