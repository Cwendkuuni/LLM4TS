package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodePointer;

public class DOMAttributeIteratorTest {

 private NodePointer parent;
 private DOMAttributeIterator iterator;
 private Element element;

 @Before
 public void setUp() {
 // Create a test element
 String xml = "<root xmlns:x='http://test.com/x'><a xmlns:y='http://test.com/y' attr1='value1' attr2='value2' xmlns:z='http://test.com/z'/></root>";
 element = (Element) DOMUtils.parseString(xml).getDocumentElement();

 // Set up the iterator
 parent = new DOMPointers.DOMPointersFactory().getNodePointer(element);
 QName name = new QName("", "", "attr1");
 iterator = new DOMAttributeIterator(parent, name);
 }

 @Test
 public void testConstructor_WithName_ShouldInitializeIteratorWithOneAttribute() {
 assertNotNull(iterator);
 assertNotNull(iterator.next());
 }

 @Test
 public void testConstructor_WithWildcardName_ShouldInitializeIteratorWithAllAttributes() {
 QName name = new QName("", "", "*");
 iterator = new DOMAttributeIterator(parent, name);
 List<Attr> attributes = (List<Attr>) iterator.toList();
 int size = attributes.size();
 assertTrue(size > 0);
 }

 @Test
 public void testConstructor_WithNonExistingName_ShouldInitializeIteratorEmpty() {
 QName name = new QName("", "", "nonExistingAttr");
 iterator = new DOMAttributeIterator(parent, name);
 assertTrue(iterator.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor_WithNullParent_ShouldThrowException() {
 parent = null;
 QName name = new QName("", "", "attr1");
 iterator = new DOMAttributeIterator(parent, name);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor_WithNullName_ShouldThrowException() {
 parent = new DOMPointers.DOMPointersFactory().getNodePointer(element);
 iterator = new DOMAttributeIterator(parent, null);
 }

 @Test
 public void testNext_ShouldReturnNullIfEndOfIteratorReached() {
 Attr attr = iterator.next();
 assertTrue(attr != null);
 attr = iterator.next();
 assertTrue(attr == null);
 }

 @Test
 public void testNext_ShouldReturnNextAttributeInIterator() {
 Attr attr1 = iterator.next();
 Attr attr2 = iterator.next();
 assertFalse(attr1.isEqualNode(attr2));
 }

 @Test
 public void testGetPosition_ShouldReturnCorrectPosition() {
 iterator.next();
 iterator.next();
 int position = iterator.getPosition();
 assertTrue(position == 2);
 }

 @Test
 public void testSetPosition_ShouldReturnTrueIfPositionIsValid() {
 iterator.setPosition(1);
 boolean isValid = iterator.setPosition(2);
 assertTrue(isValid);
 }

 @Test
 public void testSetPosition_ShouldReturnFalseIfPositionIsInvalid() {
 iterator.setPosition(1);
 boolean isValid = iterator.setPosition(-1);
 assertFalse(isValid);
 }

 @Test
 public void testToList_ShouldReturnCorrectListOfAttributes() {
 List<Attr> attributes = iterator.toList();
 int size = attributes.size();
 assertTrue(size > 0);
 }

 @Test
 public void testToList_ShouldReturnEmptyListIfIteratorIsEmpty() {
 iterator = new DOMAttributeIterator(parent, new QName("", "", "nonExistingAttr"));
 List<Attr> attributes = iterator.toList();
 int size = attributes.size();
 assertTrue(size == 0);
 }

 @Test
 public void testTestAttr_ShouldReturnTrueForMatchingAttribute() {
 Attr attr = (Attr) iterator.next();
 assertTrue(iterator.testAttr(attr));
 }

 @Test
 public void testTestAttr_ShouldReturnFalseForNonMatchingAttribute() {
 Attr attr = (Attr) iterator.next();
 Attr otherAttr = element.getAttributeNodeNS("http://test.com/y", "attr1");
 assertFalse(iterator.testAttr(otherAttr));
 }

 @Test(expected = NullPointerException.class)
 public void testTestAttr_ShouldThrowExceptionIfAttributeIsNull() {
 Attr attr = null;
 assertFalse(iterator.testAttr(attr));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTestAttr_ShouldThrowExceptionIfPrefixIsEmpty() {
 iterator.testAttr(null);
 }

 @Test
 public void testEqualsStrings_ShouldReturnTrueForEqualStrings() {
 String str1 = "test";
 String str2 = "test";
 assertTrue(DOMAttributeIterator.equalStrings(str1, str2));
 }

 @Test
 public void testEqualsStrings_ShouldReturnFalseForNonEqualStrings() {
 String str1 = "test";
 String str2 = "test1";
 assertFalse(DOMAttributeIterator.equalStrings(str1, str2));
 }

 @Test
 public void testGetAttribute_ShouldReturnAttributeWithMatchingName() {
 Attr attr = iterator.next();
 QName name = new QName("", "", attr.getName());
 Attr result = DOMAttributeIterator.getAttribute(element, name);
 assertTrue(attr.isEqualNode(result));
 }

 @Test
 public void testGetAttribute_ShouldReturnNullIfNoAttributeWithNameExists() {
 Attr result = DOMAttributeIterator.getAttribute(element, new QName("", "", "nonExistingAttr"));
 assertNull(result);
 }

 @Test
 public void testGetAttribute_ShouldReturnAttributeWithNamespaceURIAndLocalName() {
 Attr attr = iterator.next();
 String prefix = attr.getNamespaceURI();
 String localName = attr.getLocalName();
 QName name = new QName(prefix, localName);
 Attr result = DOMAttributeIterator.getAttribute(element, name);
 assertTrue(attr.isEqualNode(result));
 }

 @Test
 public void testGetAttribute_ShouldReturnAttributeWithEmptyPrefixAndLocalName() {
 Attr attr = iterator.next();
 Attr result = DOMAttributeIterator.getAttribute(element, new QName("", attr.getName()));
 assertTrue(attr.isEqualNode(result));
 }

 @Test
 public void testGetNamespaceURI_ShouldReturnNullIfPrefixIsNull() {
 Attr attr = iterator.next();
 String prefix = attr.getPrefix();
 String ns = DOMAttributeIterator.getNamespaceURI(prefix);
 assertNull(ns);
 }

 @Test
 public void testGetNamespaceURI_ShouldReturnNamespaceURIIfPrefixIsNotEmpty() {
 Attr attr = iterator.next();
 String prefix = attr.getPrefix();
 String ns = DOMAttributeIterator.getNamespaceURI(prefix);
 String expectedNs = "http://test.com/x";
 assertTrue(ns.equals(expectedNs));
 }
}