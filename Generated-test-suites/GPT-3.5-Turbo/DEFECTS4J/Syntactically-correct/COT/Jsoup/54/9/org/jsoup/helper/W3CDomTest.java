
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {
    
    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        W3CDom w3cDom = new W3CDom();
        
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        
        Assert.assertNotNull(w3cDocument);
    }
    
    @Test
    public void testConvert() throws ParserConfigurationException {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        Document w3cDocument = null;
        
        w3cBuilder.convert(jsoupDocument, w3cDocument);
        
        Assert.assertNotNull(w3cDocument);
    }
    
    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        W3CDom w3cDom = new W3CDom();
        
        Document w3cDocument = null;
        String result = w3cDom.asString(w3cDocument);
        
        Assert.assertNotNull(result);
    }
}
