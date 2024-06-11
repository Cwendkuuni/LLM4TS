import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

  private NodePointer parent;

  @Before
  public void setUp() {
    Element element = new MockElement();
    this.parent = new DOMNodePointer(element);
  }

  @Test
  public void testConstructor() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attribute"));

    assertNotNull(iterator);
    assertSame(parent, iterator.parent);
    assertEquals(new QName("attribute"), iterator.name);
    assertNotNull(iterator.attributes);
    assertEquals(0, iterator.position);
  }

  @Test
  public void testConstructorWithWildcardName() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*"));

    assertNotNull(iterator);
    assertSame(parent, iterator.parent);
    assertEquals(new QName("*"), iterator.name);
    assertNotNull(iterator.attributes);
    assertEquals(0, iterator.position);
  }

  @Test
  public void testConstructorWithElementNode() {
    Element element = new MockElement();
    DOMNodePointer parentPointer = new DOMNodePointer(element);

    DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("attribute"));

    assertNotNull(iterator);
    assertSame(parentPointer, iterator.parent);
    assertEquals(new QName("attribute"), iterator.name);
    assertNotNull(iterator.attributes);
    assertEquals(0, iterator.position);
  }

  @Test
  public void testConstructorWithElementNodeAndWildcardName() {
    Element element = new MockElement();
    DOMNodePointer parentPointer = new DOMNodePointer(element);

    DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("*"));

    assertNotNull(iterator);
    assertSame(parentPointer, iterator.parent);
    assertEquals(new QName("*"), iterator.name);
    assertNotNull(iterator.attributes);
    assertEquals(0, iterator.position);
  }

  @Test
  public void testGetNodePointer() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attribute"));

    NodePointer nodePointer = iterator.getNodePointer();

    assertNotNull(nodePointer);
    assertTrue(nodePointer instanceof DOMAttributePointer);
    assertSame(parent, nodePointer.getParent());
  }

  @Test
  public void testGetNodePointerWithWildcardName() {
    Element element = new MockElement();
    element.setAttribute("attr1", "value1");
    element.setAttribute("attr2", "value2");

    DOMNodePointer parentPointer = new DOMNodePointer(element);
    DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, new QName("*"));

    NodePointer nodePointer1 = iterator.getNodePointer();
    assertNotNull(nodePointer1);
    assertTrue(nodePointer1 instanceof DOMAttributePointer);
    assertSame(parentPointer, nodePointer1.getParent());

    iterator.setPosition(2);
    NodePointer nodePointer2 = iterator.getNodePointer();
    assertNotNull(nodePointer2);
    assertTrue(nodePointer2 instanceof DOMAttributePointer);
    assertSame(parentPointer, nodePointer2.getParent());

    assertNotSame(nodePointer1, nodePointer2);
  }

  @Test
  public void testGetPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attribute"));

    int position = iterator.getPosition();

    assertEquals(0, position);
  }

  @Test
  public void testSetPosition() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attribute"));

    boolean result = iterator.setPosition(1);

    assertTrue(result);
    assertEquals(1, iterator.getPosition());
  }

  @Test
  public void testSetPositionOutOfBounds() {
    DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("attribute"));

    boolean result = iterator.setPosition(0);

    assertFalse(result);
    assertEquals(0, iterator.getPosition());

    result = iterator.setPosition(2);

    assertFalse(result);
    assertEquals(0, iterator.getPosition());
  }


  private class MockElement implements Element {
    private NamedNodeMap attributes;

    public MockElement() {
      this.attributes = new MockNamedNodeMap();
    }

    @Override
    public String getNodeName() {
      return null;
    }

    @Override
    public String getNodeValue() {
      return null;
    }

    @Override
    public void setNodeValue(String s) {

    }

    @Override
    public short getNodeType() {
      return 0;
    }

    @Override
    public Node getParentNode() {
      return null;
    }

    @Override
    public NodeList getChildNodes() {
      return null;
    }

    @Override
    public Node getFirstChild() {
      return null;
    }

    @Override
    public Node getLastChild() {
      return null;
    }

    @Override
    public Node getPreviousSibling() {
      return null;
    }

    @Override
    public Node getNextSibling() {
      return null;
    }

    @Override
    public NamedNodeMap getAttributes() {
      return attributes;
    }

    @Override
    public Document getOwnerDocument() {
      return null;
    }

    @Override
    public Node insertBefore(Node node, Node node1) throws DOMException {
      return null;
    }

    @Override
    public Node replaceChild(Node node, Node node1) throws DOMException {
      return null;
    }

    @Override
    public Node removeChild(Node node) throws DOMException {
      return null;
    }

    @Override
    public Node appendChild(Node node) throws DOMException {
      return null;
    }

    @Override
    public boolean hasChildNodes() {
      return false;
    }

    @Override
    public Node cloneNode(boolean b) {
      return null;
    }

    @Override
    public void normalize() {

    }

    @Override
    public boolean isSupported(String s, String s1) {
      return false;
    }

    @Override
    public String getNamespaceURI() {
      return null;
    }

    @Override
    public String getPrefix() {
      return null;
    }

    @Override
    public void setPrefix(String s) throws DOMException {

    }

    @Override
    public String getLocalName() {
      return null;
    }

    @Override
    public boolean hasAttributes() {
      return false;
    }

    @Override
    public String getBaseURI() {
      return null;
    }

    @Override
    public short compareDocumentPosition(Node node) throws DOMException {
      return 0;
    }

    @Override
    public String getTextContent() throws DOMException {
      return null;
    }

    @Override
    public void setTextContent(String s) throws DOMException {

    }

    @Override
    public boolean isSameNode(Node node) {
      return false;
    }

    @Override
    public String lookupPrefix(String s) {
      return null;
    }

    @Override
    public boolean isDefaultNamespace(String s) {
      return false;
    }

    @Override
    public String lookupNamespaceURI(String s) {
      return null;
    }

    @Override
    public boolean isEqualNode(Node node) {
      return false;
    }

    @Override
    public Object getFeature(String s, String s1) {
      return null;
    }

    @Override
    public Object setUserData(String s, Object o, UserDataHandler userDataHandler) {
      return null;
    }

    @Override
    public Object getUserData(String s) {
      return null;
    }

    @Override
    public short getDocumentType() {
      return 0;
    }

    @Override
    public DomConfiguration getDomConfig() {
      return null;
    }

    @Override
    public TypeInfo getSchemaTypeInfo() {
      return null;
    }

    @Override
    public void setIdAttribute(String s, boolean b) throws DOMException {

    }

    @Override
    public void setIdAttributeNS(String s, String s1, boolean b) throws DOMException {

    }

    @Override
    public void setIdAttributeNode(Attr attr, boolean b) throws DOMException {

    }
  }

  private class MockNamedNodeMap implements NamedNodeMap {

    @Override
    public Node getNamedItem(String s) {
      return null;
    }

    @Override
    public Node setNamedItem(Node node) throws DOMException {
      return null;
    }

    @Override
    public Node removeNamedItem(String s) throws DOMException {
      return null;
    }

    @Override
    public Node item(int i) {
      return null;
    }

    @Override
    public int getLength() {
      return 0;
    }

    @Override
    public Node getNamedItemNS(String s, String s1) throws DOMException {
      return null;
    }

    @Override
    public Node setNamedItemNS(Node node) throws DOMException {
      return null;
    }

    @Override
    public Node removeNamedItemNS(String s, String s1) throws DOMException {
      return null;
    }
  }
}