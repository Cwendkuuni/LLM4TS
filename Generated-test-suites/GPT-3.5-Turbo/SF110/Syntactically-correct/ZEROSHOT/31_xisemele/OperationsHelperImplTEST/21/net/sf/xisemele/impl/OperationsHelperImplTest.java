import static org.junit.Assert.*;
import org.junit.Test;

import org.w3c.dom.*;
import java.util.*;

public class OperationsHelperImplTest {

   @Test
   public void testChildren() {
      OperationsHelperImpl helper = new OperationsHelperImpl();

      // Test empty node
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = builder.newDocument();
      List<Node> result = helper.children(document);
      assertEquals(0, result.size());

      // Test node with children
      Element root = document.createElement("root");
      Element child1 = document.createElement("child1");
      Element child2 = document.createElement("child2");
      Text text = document.createTextNode("text node");
      root.appendChild(child1);
      root.appendChild(child2);
      root.appendChild(text);
      result = helper.children(root);
      assertEquals(2, result.size());
      assertEquals(child1, result.get(0));
      assertEquals(child2, result.get(1));
   }

   @Test
   public void testFind() {
      OperationsHelperImpl helper = new OperationsHelperImpl();

      // Test empty document
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = builder.newDocument();
      Node result = helper.find(document, "/root");
      assertNull(result);

      // Test finding root node
      Element root = document.createElement("root");
      document.appendChild(root);
      result = helper.find(document, "/root");
      assertEquals(root, result);

      // Test finding nested node
      Element child1 = document.createElement("child1");
      Element child2 = document.createElement("child2");
      Text text = document.createTextNode("text node");
      child1.appendChild(child2);
      child1.appendChild(text);
      root.appendChild(child1);
      result = helper.find(document, "/root/child1");
      assertEquals(child1, result);
      result = helper.find(document, "/root/child1/text()");
      assertEquals(text, result);
   }

   @Test
   public void testNodeWithName() {
      OperationsHelperImpl helper = new OperationsHelperImpl();

      // Test empty list
      List<Node> nodes = new ArrayList<>();
      Node result = helper.nodeWithName(nodes, "name");
      assertNull(result);

      // Test finding node
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = builder.newDocument();
      Element root = document.createElement("root");
      Element child1 = document.createElement("name");
      Element child2 = document.createElement("child2");
      Text text = document.createTextNode("text node");
      root.appendChild(child1);
      root.appendChild(child2);
      root.appendChild(text);
      nodes.add(child1);
      nodes.add(child2);
      result = helper.nodeWithName(nodes, "name");
      assertEquals(child1, result);
   }

   @Test
   public void testSublistWithName() {
      OperationsHelperImpl helper = new OperationsHelperImpl();

      // Test empty list
      List<Node> nodes = new ArrayList<>();
      List<Node> result = helper.sublistWithName(nodes, "name");
      assertEquals(0, result.size());

      // Test finding nodes
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = builder.newDocument();
      Element root = document.createElement("root");
      Element child1 = document.createElement("name");
      Element child2 = document.createElement("child2");
      Element child3 = document.createElement("name");
      Text text = document.createTextNode("text node");
      root.appendChild(child1);
      root.appendChild(child2);
      root.appendChild(child3);
      root.appendChild(text);
      nodes.add(child1);
      nodes.add(child2);
      nodes.add(child3);
      result = helper.sublistWithName(nodes, "name");
      assertEquals(2, result.size());
      assertEquals(child1, result.get(0));
      assertEquals(child3, result.get(1));
   }
}