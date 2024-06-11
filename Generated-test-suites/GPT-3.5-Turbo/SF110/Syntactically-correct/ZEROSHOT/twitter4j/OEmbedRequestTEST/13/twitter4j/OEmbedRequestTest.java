package twitter4j;

import org.junit.*;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

    @Test
    public void testAsHttpParameterArray() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(new String[]{"user1", "user2"});
        request.setLang("en");

        HttpParameter[] parameters = request.asHttpParameterArray();

        assertEquals(parameters.length, 10);

        assertEquals(parameters[0].getName(), "id");
        assertEquals(parameters[0].getValue(), "123456789");

        assertEquals(parameters[1].getName(), "url");
        assertEquals(parameters[1].getValue(), "https://example.com");

        assertEquals(parameters[2].getName(), "maxwidth");
        assertEquals(parameters[2].getValue(), "500");

        assertEquals(parameters[3].getName(), "hide_media");
        assertEquals(parameters[3].getValue(), "true");

        assertEquals(parameters[4].getName(), "hide_thread");
        assertEquals(parameters[4].getValue(), "false");

        assertEquals(parameters[5].getName(), "omit_script");
        assertEquals(parameters[5].getValue(), "true");

        assertEquals(parameters[6].getName(), "align");
        assertEquals(parameters[6].getValue(), "center");

        assertEquals(parameters[7].getName(), "related");
        assertEquals(parameters[7].getValue(), "user1,user2");

        assertEquals(parameters[8].getName(), "lang");
        assertEquals(parameters[8].getValue(), "en");
    }

    @Test
    public void testEquals() {
        OEmbedRequest request1 = new OEmbedRequest(123456789, "https://example.com");
        request1.setMaxWidth(500);
        request1.setHideMedia(true);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.CENTER);
        request1.setRelated(new String[]{"user1", "user2"});
        request1.setLang("en");

        OEmbedRequest request2 = new OEmbedRequest(123456789, "https://example.com");
        request2.setMaxWidth(500);
        request2.setHideMedia(true);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.CENTER);
        request2.setRelated(new String[]{"user1", "user2"});
        request2.setLang("en");

        assertEquals(request1, request2);
    }

    @Test
    public void testHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(123456789, "https://example.com");
        request1.setMaxWidth(500);
        request1.setHideMedia(true);
        request1.setHideThread(false);
        request1.setOmitScript(true);
        request1.setAlign(OEmbedRequest.Align.CENTER);
        request1.setRelated(new String[]{"user1", "user2"});
        request1.setLang("en");

        OEmbedRequest request2 = new OEmbedRequest(123456789, "https://example.com");
        request2.setMaxWidth(500);
        request2.setHideMedia(true);
        request2.setHideThread(false);
        request2.setOmitScript(true);
        request2.setAlign(OEmbedRequest.Align.CENTER);
        request2.setRelated(new String[]{"user1", "user2"});
        request2.setLang("en");

        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(123456789, "https://example.com");
        request.setMaxWidth(500);
        request.setHideMedia(true);
        request.setHideThread(false);
        request.setOmitScript(true);
        request.setAlign(OEmbedRequest.Align.CENTER);
        request.setRelated(new String[]{"user1", "user2"});
        request.setLang("en");

        String expected = "OEmbedRequest{statusId=123456789, url='https://example.com', maxWidth=500, hideMedia=true, hideThread=false, omitScript=true, align=CENTER, related=[user1, user2], lang='en'}";

        assertEquals(request.toString(), expected);
    }
}