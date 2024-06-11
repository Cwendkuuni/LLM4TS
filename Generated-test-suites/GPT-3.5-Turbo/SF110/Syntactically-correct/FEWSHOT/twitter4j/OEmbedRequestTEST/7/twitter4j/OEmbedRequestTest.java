package twitter4j;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OEmbedRequestTest {

    @Test
    public void testConstructorAndGetters() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");

        assertEquals(12345L, request.getStatusId());
        assertEquals("https://example.com", request.getUrl());
        assertEquals(true, request.getHideMedia());
        assertEquals(true, request.getHideThread());
        assertEquals(false, request.getOmitScript());
        assertEquals(Align.NONE, request.getAlign());
        assertEquals(0, request.getRelated().length);
        assertEquals(null, request.getLang());
    }

    @Test
    public void testSettersAndGetters() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");

        request.setMaxWidth(500);
        assertEquals(500, request.getMaxWidth());

        request.setHideMedia(false);
        assertEquals(false, request.getHideMedia());

        request.setHideThread(false);
        assertEquals(false, request.getHideThread());

        request.setOmitScript(true);
        assertEquals(true, request.getOmitScript());

        request.setAlign(Align.LEFT);
        assertEquals(Align.LEFT, request.getAlign());

        String[] related = {"user1", "user2"};
        request.setRelated(related);
        assertEquals(related, request.getRelated());

        request.setLang("en");
        assertEquals("en", request.getLang());
    }

    @Test
    public void testEqualsAndHashCode() {
        OEmbedRequest request1 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest request2 = new OEmbedRequest(12345L, "https://example.com");
        OEmbedRequest request3 = new OEmbedRequest(123456L, "https://example.com");

        assertEquals(true, request1.equals(request2));
        assertEquals(true, request1.hashCode() == request2.hashCode());

        assertEquals(false, request1.equals(request3));
        assertEquals(false, request1.hashCode() == request3.hashCode());
    }

    @Test
    public void testToString() {
        OEmbedRequest request = new OEmbedRequest(12345L, "https://example.com");
        String expected = "OEmbedRequest{statusId=12345, url='https://example.com', maxWidth=0, hideMedia=true, hideThread=true, omitScript=false, align=NONE, related=null, lang='null'}";
        assertEquals(expected, request.toString());
    }
}