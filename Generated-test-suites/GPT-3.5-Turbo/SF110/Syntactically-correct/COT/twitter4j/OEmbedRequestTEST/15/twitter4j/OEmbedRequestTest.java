
import org.junit.Assert;
import org.junit.Test;

public class OEmbedRequestTest {

    @Test
    public void testConstructor() {
        long statusId = 12345;
        String url = "https://example.com";
        OEmbedRequest request = new OEmbedRequest(statusId, url);

        Assert.assertEquals(statusId, request.getStatusId());
        Assert.assertEquals(url, request.getUrl());
    }

    @Test
    public void testSetMaxWidth() {
        int maxWidth = 500;
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(maxWidth);

        Assert.assertEquals(maxWidth, request.getMaxWidth());
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(false);

        Assert.assertFalse(request.getHideMedia());
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);

        Assert.assertFalse(request.getHideThread());
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);

        Assert.assertTrue(request.getOmitScript());
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        OEmbedRequest.Align align = OEmbedRequest.Align.CENTER;
        request.setAlign(align);

        Assert.assertEquals(align, request.getAlign());
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String[] related = {"user1", "user2"};
        request.setRelated(related);

        Assert.assertArrayEquals(related, request.getRelated());
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        String lang = "en";
        request.setLang(lang);

        Assert.assertEquals(lang, request.getLang());
    }
}
