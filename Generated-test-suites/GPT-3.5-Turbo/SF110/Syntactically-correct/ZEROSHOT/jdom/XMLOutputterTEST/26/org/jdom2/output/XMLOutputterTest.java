import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.output.*;
import java.io.*;

public class XMLOutputterTest {

    @Test
    public void testOutput() {
        // Arrange
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        
        // Act
        try {
            outputter.output(doc, writer);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
        
        // Assert
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>\n", writer.toString());
    }
    
    @Test
    public void testOutputElementContentString() {
        // Arrange
        Element root = new Element("root");
        Element child = new Element("child");
        child.setText("Hello, World!");
        root.addContent(child);
        XMLOutputter outputter = new XMLOutputter();
        StringWriter writer = new StringWriter();
        
        // Act
        try {
            outputter.outputElementContent(child, writer);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
        
        // Assert
        assertEquals("Hello, World!", writer.toString());
    }
    
    @Test
    public void testEscapeAttributeEntities() {
        // Arrange
        String str = "<>&\"'";
        XMLOutputter outputter = new XMLOutputter();
        
        // Act
        String result = outputter.escapeAttributeEntities(str);
        
        // Assert
        assertEquals("&lt;&gt;&amp;&quot;&apos;", result);
    }
    
    @Test
    public void testEscapeElementEntities() {
        // Arrange
        String str = "<>&\"'";
        XMLOutputter outputter = new XMLOutputter();
        
        // Act
        String result = outputter.escapeElementEntities(str);
        
        // Assert
        assertEquals("&lt;&gt;&amp;&quot;&apos;", result);
    }
}