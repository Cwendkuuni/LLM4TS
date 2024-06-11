import static org.junit.Assert.assertEquals;
import org.jsoup.nodes.XmlDeclaration;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("#declaration", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
}