import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class DOMAttributeiteratorTest {

 private NodePointer parent;
 private QName name;
 private DOMAttributeIterator iterator;

 @Before
 public void setUp() {
 // Setup the parent NodePointer and QName
 Node node = new Element("root");
 parent = new NodePointer(node);
 name = new QName("test");
 }

 @Test
 public void testConstructor() {
 // Test constructor with a valid parent and QName
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator instanceof DOMAttributeIterator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullParent() {
 // Test constructor with a null parent
 iterator = new DOMAttributeIterator(null, name);
 fail("No exception thrown for null parent");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullName() {
 // Test constructor with a null name
 iterator = new DOMAttributeIterator(parent, null);
 fail("No exception thrown for null name");
 }

 @Test
 public void testGetNodePointer() {
 // Test getting the NodePointer for the first attribute
 iterator = new DOMAttributeIterator(parent, name);
 Attr firstAttr = (Attr) iterator.getNodePointer().getNode();
 assertEquals("firstAttr", firstAttr.getNodeName(), name.getName());

 // Test getting the NodePointer for the last attribute
 iterator.setPosition(iterator.getPosition() + (iterator.getPosition() < iterator.size() ? iterator.size() - iterator.getPosition() : 0));
 Attr lastAttr = (Attr) iterator.getNodePointer().getNode();
 assertEquals("lastAttr", lastAttr.getNodeName(), name.getName());
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetNodePointerEndOfIterator() {
 // Test getting the NodePointer after the last attribute
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(iterator.getPosition() + iterator.size());
 iterator.getNodePointer();
 fail("No exception thrown for end of iterator");
 }

 @Test
 public void testGetPosition() {
 // Test getting the current position
 iterator = new DOMAttributeIterator(parent, name);
 assertEquals(0, iterator.getPosition());

 // Test setting the position to a valid index
 iterator.setPosition(1);
 assertEquals(1, iterator.getPosition());

 // Test setting the position to the last index
 iterator.setPosition(iterator.size());
 assertEquals(iterator.size(), iterator.getPosition());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionInvalidIndex() {
 // Test setting the position to an invalid index
 iterator = new DOMAttributeIterator(parent, name);
 iterator.setPosition(-1);
 fail("No exception thrown for invalid index");
 }

 @Test
 public void testSize() {
 // Test the size of the iterator
 Node node = new Element("root");
 parent = new NodePointer(node);
 iterator = new DOMAttributeIterator(parent, new QName("test"));
 Attr attr1 = node.setAttributeNode(new Attr("attr1", "value1"));
 attr1.setSpecified(true);
 iterator.add(attr1);
 Attr attr2 = node.setAttributeNode(new Attr("attr2", "value2"));
 attr2.setSpecified(true);
 iterator.add(attr2);
 int size = iterator.size();
 assertEquals(2, size);
 }
}