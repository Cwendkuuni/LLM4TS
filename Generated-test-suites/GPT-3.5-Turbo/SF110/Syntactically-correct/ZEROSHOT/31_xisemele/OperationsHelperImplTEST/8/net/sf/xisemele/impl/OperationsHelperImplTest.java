import org.junit.Test;
import org.junit.Assert;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

    @Test
    public void testChildren() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createTestDocument();
        
        List<Node> expectedResult = new ArrayList<>();
        Element element1 = document.createElement("element1");
        Element element2 = document.createElement("element2");
        expectedResult.add(element1);
        expectedResult.add(element2);
        
        List<Node> result = helper.children(document.getDocumentElement());
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testFind() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createTestDocument();
        
        String path = "root/element1/element3";
        
        Node expectedResult = document.getElementsByTagName("element3").item(0);
        
        Node result = helper.find(document, path);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testNodeWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createTestDocument();
        
        List<Node> nodes = helper.children(document.getDocumentElement());
        
        String name = "element1";
        
        Node expectedResult = document.getElementsByTagName(name).item(0);
        
        Node result = helper.nodeWithName(nodes, name);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    @Test
    public void testSublistWithName() {
        OperationsHelperImpl helper = new OperationsHelperImpl();
        Document document = createTestDocument();
        
        List<Node> nodes = helper.children(document.getDocumentElement());
        
        String name = "element1";
        
        List<Node> expectedResult = new ArrayList<>();
        expectedResult.add(document.getElementsByTagName(name).item(0));
        
        List<Node> result = helper.sublistWithName(nodes, name);
        
        Assert.assertEquals(expectedResult, result);
    }
    
    private Document createTestDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        
        Element root = document.createElement("root");
        Element element1 = document.createElement("element1");
        Element element2 = document.createElement("element2");
        Element element3 = document.createElement("element3");
        
        root.appendChild(element1);
        root.appendChild(element2);
        element1.appendChild(element3);
        
        document.appendChild(root);
        
        return document;
    }
}