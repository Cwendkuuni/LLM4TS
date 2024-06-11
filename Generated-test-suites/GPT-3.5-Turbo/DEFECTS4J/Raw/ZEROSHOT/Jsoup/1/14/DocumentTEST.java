import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("http://www.example.com");
    }
    
    @Test
    public void testHead() {
        assertNotNull(document.head());
    }
    
    @Test
    public void testBody() {
        assertNotNull(document.body());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());
    }
    
    @Test
    public void testSetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }
    
    @Test
    public void testNormalise() {
        document.normalise();
        assertNotNull(document.head());
        assertNotNull(document.body());
    }
    
    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }
    
    @Test
    public void testText() {
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}