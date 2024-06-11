import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JDOMAttributeIteratorTest {

 private NodeIterator iterator;
 @Mock
 private NodePointer parent;
 @Mock
 private QName name;
 @Mock
 private Attribute attribute1;
 @Mock
 private Attribute attribute2;
 @Mock
 private Namespace namespace;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(parent.getNode()).thenReturn(mock(Element.class));
 when(parent.getNamespaceResolver()).thenReturn(mock(NamespaceResolver.class));
 when(parent.getNamespaceResolver().getNamespaceURI("xml")).thenReturn("http://www.w3.org/XML/1998/namespace");
 when(namespace.getNamespaceURI("xml")).thenReturn("http://www.w3.org/XML/1998/namespace");
 when(namespace.getNamespacePrefix("http://www.w3.org/XML/1998/namespace")).thenReturn("xml");
 when(name.getPrefix()).thenReturn("xml");
 when(name.getName()).thenReturn("attr1");

 iterator = new JDOMAttributeIterator(parent, name);
 }

 @Test
 public void testEmptyIterator() {
 assertEquals(0, iterator.getPosition());
 assertNull(iterator.getNodePointer());
 }

 @Test
 public void testSingleAttributeIterator() {
 when(parent.getNode()).thenReturn(mock(Element.class));
 when(parent.getNode().getAttribute("attr1", namespace)).thenReturn(attribute1);

 iterator = new JDOMAttributeIterator(parent, name);

 assertEquals(1, iterator.getPosition());
 assertEquals(new JDOMAttributePointer(parent, attribute1), iterator.getNodePointer());
 }

 @Test
 public void testMultipleAttributesIterator() {
 when(parent.getNode()).thenReturn(mock(Element.class));
 when(parent.getNode().getAttributes()).thenReturn(new ArrayList<Attribute>() {{
 add(attribute1);
 add(attribute2);
 }});

 iterator = new JDOMAttributeIterator(parent, name);

 assertEquals(1, iterator.getPosition());
 assertEquals(new JDOMAttributePointer(parent, attribute1), iterator.getNodePointer());
 }

 @Test
 public void testSetPosition() {
 iterator = new JDOMAttributeIterator(parent, name);
 when(parent.getNode().getAttributes()).thenReturn(new ArrayList<Attribute>() {{
 add(attribute1);
 add(attribute2);
 }});

 iterator.setPosition(2);
 assertEquals(2, iterator.getPosition());
 assertNull(iterator.getNodePointer());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionOutOfBounds() {
 iterator = new JDOMAttributeIterator(parent, name);
 when(parent.getNode().getAttributes()).thenReturn(new ArrayList<Attribute>() {{
 add(attribute1);
 add(attribute2);
 }});

 iterator.setPosition(3);
 }
}