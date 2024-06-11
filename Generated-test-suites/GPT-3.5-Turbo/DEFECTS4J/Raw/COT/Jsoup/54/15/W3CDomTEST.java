import static org.junit.Assert.*;
import org.junit.Test;

public class W3CDomTest {
    
    @Test
    public void testFromJsoup() {
        // Test the basic functionality of the fromJsoup method
        // by converting a jsoup document to a W3C document 
        // and verifying that the conversion was successful
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        // Test the basic functionality of the convert method
        // by converting a jsoup document to a provided W3C document 
        // and verifying that the conversion was successful
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document out = w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(out);
    }
    
    @Test
    public void testAsString() {
        // Test the basic functionality of the asString method
        // by converting a W3C document to a string 
        // and verifying that the conversion was successful
        org.w3c.dom.Document w3cDoc = new org.w3c.dom.Document();
        W3CDom w3cDom = new W3CDom();
        String output = w3cDom.asString(w3cDoc);
        assertNotNull(output);
    }
    
    @Test
    public void testConvertWithLocation() {
        // Test the conversion of a jsoup document to a W3C document
        // with a specified location and verify that the documentURI
        // property of the W3C document is set correctly
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.setBaseUri("http://example.com");
        
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testFromJsoupWithNullInput() {
        // Test the fromJsoup method with a null input document
        // and expect an IllegalStateException to be thrown
        W3CDom w3cDom = new W3CDom();
        w3cDom.fromJsoup(null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConvertWithNullInput() {
        // Test the convert method with a null input document
        // and expect an IllegalStateException to be thrown
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(null, new org.w3c.dom.Document());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAsStringWithNullInput() {
        // Test the asString method with a null input document
        // and expect an IllegalStateException to be thrown
        W3CDom w3cDom = new W3CDom();
        w3cDom.asString(null);
    }
}