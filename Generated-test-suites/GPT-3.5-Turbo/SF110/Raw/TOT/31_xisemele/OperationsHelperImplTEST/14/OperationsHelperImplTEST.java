package net.sf.xisemele.impl;

import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {
    
    private OperationsHelperImpl operationsHelper;
    private Document document;
    private Element rootElement;
    private Element child1;
    private Element child2;
    private Element grandchild;
    private Element greatgrandchild;
    
    @Before
    public void setup() {
        operationsHelper = new OperationsHelperImpl();
        document = DocumentBuilder.newDocument();
        rootElement = document.createElement("root");
        document.appendChild(rootElement);
        child1 = document.createElement("child1");
        rootElement.appendChild(child1);
        child2 = document.createElement("child2");
        rootElement.appendChild(child2);
        grandchild = document.createElement("grandchild");
        child1.appendChild(grandchild);
        greatgrandchild = document.createElement("greatgrandchild");
        grandchild.appendChild(greatgrandchild);
    }
    
    @Test
    public void testChildren() {
        List<Node> expectedResult = new ArrayList<Node>(Arrays.asList(child1, child2));
        List<Node> actualResult = operationsHelper.children(rootElement);
        assertEquals(expectedResult, actualResult);
        
        expectedResult = new ArrayList<Node>();
        actualResult = operationsHelper.children(child1);
        assertEquals(expectedResult, actualResult);
        
        expectedResult = new ArrayList<Node>(Arrays.asList(grandchild));
        actualResult = operationsHelper.children(child1);
        assertNotEquals(expectedResult, actualResult);
        
        expectedResult = new ArrayList<Node>();
        actualResult = operationsHelper.children(grandchild);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFind() {
        Node expectedResult = rootElement;
        Node actualResult = operationsHelper.find(document, "root");
        assertEquals(expectedResult, actualResult);
        
        expectedResult = null;
        actualResult = operationsHelper.find(document, "nonexistent");
        assertEquals(expectedResult, actualResult);
        
        expectedResult = grandchild;
        actualResult = operationsHelper.find(document, "root/child1/grandchild");
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testNodeWithName() {
        Node expectedResult = grandchild;
        Node actualResult = operationsHelper.nodeWithName(Arrays.asList(child1, grandchild, child2), "grandchild");
        assertEquals(expectedResult, actualResult);
        
        expectedResult = null;
        actualResult = operationsHelper.nodeWithName(Arrays.asList(child1, child2), "nonexistent");
        assertEquals(expectedResult, actualResult);
        
        expectedResult = child1;
        actualResult = operationsHelper.nodeWithName(Arrays.asList(child1, grandchild, child2), "child1");
        assertNotEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testSublistWithName() {
        List<Node> expectedResult = new ArrayList<Node>(Arrays.asList(grandchild));
        List<Node> actualResult = operationsHelper.sublistWithName(Arrays.asList(child1, grandchild, child2), "grandchild");
        assertEquals(expectedResult, actualResult);
        
        expectedResult = new ArrayList<Node>();
        actualResult = operationsHelper.sublistWithName(Arrays.asList(child1, child2), "nonexistent");
        assertEquals(expectedResult, actualResult);
        
        expectedResult = new ArrayList<Node>(Arrays.asList(child1, child2));
        actualResult = operationsHelper.sublistWithName(Arrays.asList(child1, grandchild, child2), "child1");
        assertNotEquals(expectedResult, actualResult);
    }
}