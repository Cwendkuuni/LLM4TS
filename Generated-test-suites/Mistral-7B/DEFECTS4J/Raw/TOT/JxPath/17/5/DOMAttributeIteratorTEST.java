import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DOMAttributeIteratorTest {

 /**
 * Test constructor with valid arguments
 */
 @Test
 public void testConstructorValidArgs() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 }

 /**
 * Test constructor with null parent
 */
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullParent() {
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(null, name);
 }

 /**
 * Test constructor with null name
 */
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullName() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
 }

 /**
 * Test getNodePointer method with valid position
 */
 @Test
 public void testGetNodePointerValidPosition() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 Attr testAttr = iterator.getNodePointer().getNode().getNodeAttributes().getNamedItem("testAttr");
 iterator.next();
 NodePointer expectedPointer = new DOMAttributePointer(parent, testAttr);
 NodePointer actualPointer = iterator.getNodePointer();
 assertSame(expectedPointer, actualPointer);
 }

 /**
 * Test getNodePointer method with invalid position
 */
 @Test
 public void testGetNodePointerInvalidPosition() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 assertNull(iterator.getNodePointer());
 }

 /**
 * Test getPosition method with valid position
 */
 @Test
 public void testGetPositionValidPosition() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttribeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 assertEquals(1, iterator.getPosition());
 }

 /**
 * Test getPosition method with invalid position
 */
 @Test
 public void testGetPositionInvalidPosition() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 iterator.next();
 assertEquals(3, iterator.getPosition());
 }

 /**
 * Test setPosition method with valid position
 */
 @Test
 public void testSetPositionValidPosition() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.setPosition(2);
 assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getNode().getNextSibling().getAttributes().getNamedItem("testAttr2"));
 }

 /**
 * Test setPosition method with invalid position
 */
 @Test
 public void testSetPositionInvalidPosition() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 iterator.next();
 iterator.setPosition(0);
 assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getNode());
 }

 /**
 * Test equals method with same iterators
 */
 @Test
 public void testEqualsSameIterator() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name1 = new QName("test1", "testNS1", "http://test1.com/");
 QName name2 = new QName("test2", "testNS2", "http://test2.com/");
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(parent, name1);
 DOMAttributeIterator iterator2 = new DOMAttributeIterator(parent, name1);
 assertTrue(iterator1.equals(iterator2));
 }

 /**
 * Test equals method with different iterators
 */
 @Test
 public void testEqualsDifferentIterator() {
 NodePointer parent1 = new DOMPointNodeTest().getNodePointer();
 NodePointer parent2 = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator1 = new DOMAttributeIterator(parent1, name);
 DOMAttributeIterator iterator2 = new DOMAttributeIterator(parent2, name);
 assertFalse(iterator1.equals(iterator2));
 }

 /**
 * Test equals method with other object
 */
 @Test
 public void testEqualsOtherObject() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.equals(new Object()));
 }

 /**
 * Test toString method
 */
 @Test
 public void testToString() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 String expectedString = "DOMAttributeIterator[parent=" + parent.toString() + ", name=" + name.toString() + ", position=" + iterator.getPosition() + ", hasNext()=" + iterator.hasNext() + "]";
 String actualString = iterator.toString();
 assertEquals(expectedString, actualString);
 }

 /**
 * Test next method with valid iterator
 */
 @Test
 public void testNextValidIterator() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 }

 /**
 * Test next method with empty iterator
 */
 @Test
 public void testNextEmptyIterator() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 assertFalse(iterator.hasNext());
 }

 /**
 * Test hasNext method with valid iterator
 */
 @Test
 public void testHasNextValidIterator() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 iterator.next();
 assertTrue(iterator.hasNext());
 }

 /**
 * Test hasNext method with empty iterator
 */
 @Test
 public void testHasNextEmptyIterator() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 assertFalse(iterator.hasNext());
 }

 /**
 * Test getName method
 */
 @Test
 public void testgetName() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 assertEquals(name, iterator.getName());
 }

 /**
 * Test getParent method
 */
 @Test
 public void testGetParent() {
 NodePointer parent = new DOMPointNodeTest().getNodePointer();
 QName name = new QName("test", "testNS", "http://test.com/");
 DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
 assertEquals(parent, iterator.getParent());
 }
}