
import org.junit.Test;
import java.util.Arrays;

public class OEmbedRequestTest {

    @Test
    public void testMaxWidth() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setMaxWidth(500);
        assert(request.asHttpParameterArray()[2].getValue() == 500);
    }

    @Test
    public void testHideMedia() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideMedia(false);
        assert(request.asHttpParameterArray()[3].getValue() == false);
    }

    @Test
    public void testHideThread() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setHideThread(false);
        assert(request.asHttpParameterArray()[4].getValue() == false);
    }

    @Test
    public void testOmitScript() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setOmitScript(true);
        assert(request.asHttpParameterArray()[5].getValue() == true);
    }

    @Test
    public void testAlign() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setAlign(OEmbedRequest.Align.LEFT);
        assert(OEmbedRequest.Align.LEFT.toString().toLowerCase().equals(request.asHttpParameterArray()[6].getValue()));
    }

    @Test
    public void testRelated() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setRelated(new String[]{"user1", "user2"});
        assert(Arrays.equals(request.getRelated(), new String[]{"user1", "user2"}));
    }

    @Test
    public void testLang() {
        OEmbedRequest request = new OEmbedRequest(12345, "https://example.com");
        request.setLang("en");
        assert(request.getLang().equals("en"));
    }
}
