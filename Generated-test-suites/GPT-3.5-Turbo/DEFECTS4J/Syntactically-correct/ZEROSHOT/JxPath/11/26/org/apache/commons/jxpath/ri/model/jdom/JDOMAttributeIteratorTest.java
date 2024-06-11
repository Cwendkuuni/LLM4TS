import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JDOMAttributeIteratorTest {

    @Mock
    private NodePointer parentMock;

    @Mock
    private Element elementMock;

    @Mock
    private Attribute attributeMock;

    @Mock
    private Attribute attributeMock2;

    @Mock
    private QName qNameMock;

    private JDOMAttributeIterator jdomAttributeIterator;

    @Before
    public void setUp() {
        when(parentMock.getNode()).thenReturn(elementMock);
    }

    @Test
    public void testConstructor_WithNullParent_ShouldSetParent() {
        jdomAttributeIterator = new JDOMAttributeIterator(null, qNameMock);
        assertEquals(null, jdomAttributeIterator.parent);
    }

    @Test
    public void testConstructor_WithElementParentAndXmlPrefix_ShouldSetNamespace() {
        when(elementMock.getNamespace(Mockito.anyString())).thenReturn(Namespace.XML_NAMESPACE);
        when(elementMock.getAttribute(Mockito.anyString(), Mockito.eq(Namespace.XML_NAMESPACE))).thenReturn(attributeMock);

        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName("prefix", "name"));
        assertEquals(Namespace.XML_NAMESPACE, ((JDOMAttributeIterator) jdomAttributeIterator).ns);
    }

    @Test
    public void testConstructor_WithElementParentAndNonXmlPrefix_ShouldSetNamespace() {
        Namespace namespaceMock = Mockito.mock(Namespace.class);
        when(elementMock.getNamespace(Mockito.anyString())).thenReturn(namespaceMock);
        when(elementMock.getAttribute(Mockito.anyString(), Mockito.eq(namespaceMock))).thenReturn(attributeMock);

        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName("prefix", "name"));
        assertEquals(namespaceMock, ((JDOMAttributeIterator) jdomAttributeIterator).ns);
    }

    @Test
    public void testConstructor_WithElementParentAndNoPrefix_ShouldSetNamespaceToNoNamespace() {
        when(elementMock.getAttribute(Mockito.anyString(), Mockito.eq(Namespace.NO_NAMESPACE))).thenReturn(attributeMock);

        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName(null, "name"));
        assertEquals(Namespace.NO_NAMESPACE, ((JDOMAttributeIterator) jdomAttributeIterator).ns);
    }

    @Test
    public void testConstructor_WithNonElementParent_ShouldSetEmptyAttributesList() {
        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, qNameMock);
        assertEquals(Collections.EMPTY_LIST, ((JDOMAttributeIterator) jdomAttributeIterator).attributes);
    }

    @Test
    public void testConstructor_WithMatchingAttributeName_ShouldAddAttributeToList() {
        when(elementMock.getAttribute(Mockito.anyString(), Mockito.eq(Namespace.NO_NAMESPACE))).thenReturn(attributeMock);

        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName(null, "name"));
        assertEquals(1, ((JDOMAttributeIterator) jdomAttributeIterator).attributes.size());
        assertEquals(attributeMock, ((JDOMAttributeIterator) jdomAttributeIterator).attributes.get(0));
    }

    @Test
    public void testConstructor_WithNonMatchingAttributeName_ShouldNotAddAttributeToList() {
        when(elementMock.getAttribute(Mockito.anyString(), Mockito.eq(Namespace.NO_NAMESPACE))).thenReturn(null);

        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName(null, "name"));
        assertEquals(Collections.EMPTY_LIST, ((JDOMAttributeIterator) jdomAttributeIterator).attributes);
    }

    @Test
    public void testConstructor_WithWildcardAttributeName_ShouldAddAllMatchingAttributesToList() {
        ArrayList<Attribute> allAttributes = new ArrayList<>();
        allAttributes.add(attributeMock);
        allAttributes.add(attributeMock2);

        when(elementMock.getAttributes()).thenReturn(allAttributes);
        when(attributeMock.getNamespace()).thenReturn(Namespace.NO_NAMESPACE);
        when(attributeMock2.getNamespace()).thenReturn(Namespace.NO_NAMESPACE);

        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName(Namespace.NO_NAMESPACE, "*"));
        assertEquals(2, ((JDOMAttributeIterator) jdomAttributeIterator).attributes.size());
        assertEquals(attributeMock, ((JDOMAttributeIterator) jdomAttributeIterator).attributes.get(0));
        assertEquals(attributeMock2, ((JDOMAttributeIterator) jdomAttributeIterator).attributes.get(1));
    }

    @Test
    public void testGetNodePointer_WithZeroPosition_ShouldReturnNull() {
        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, qNameMock);
        jdomAttributeIterator.position = 0;
        assertEquals(null, jdomAttributeIterator.getNodePointer());
    }

    @Test
    public void testGetNodePointer_WithOnePosition_ShouldReturnAttributeNodePointer() {
        ArrayList<Attribute> allAttributes = new ArrayList<>();
        allAttributes.add(attributeMock);

        when(elementMock.getAttributes()).thenReturn(allAttributes);
        when(attributeMock.getNamespace()).thenReturn(Namespace.NO_NAMESPACE);
        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName(Namespace.NO_NAMESPACE, "name"));

        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();

        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(attributeMock, ((JDOMAttributePointer) nodePointer).attribute);
    }

    @Test
    public void testGetNodePointer_WithPositionGreaterThanOne_ShouldReturnAttributeNodePointer() {
        ArrayList<Attribute> allAttributes = new ArrayList<>();
        allAttributes.add(attributeMock);
        allAttributes.add(attributeMock2);

        when(elementMock.getAttributes()).thenReturn(allAttributes);

        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName(Namespace.NO_NAMESPACE, "*"));
        jdomAttributeIterator.position = 2;

        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();

        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(attributeMock2, ((JDOMAttributePointer) nodePointer).attribute);
    }

    @Test
    public void testGetPosition_WithNonZeroPosition_ShouldReturnPosition() {
        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, qNameMock);
        jdomAttributeIterator.position = 2;
        assertEquals(2, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testGetPosition_WithZeroPosition_ShouldReturnZero() {
        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, qNameMock);
        jdomAttributeIterator.position = 0;
        assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_WithValidPosition_ShouldSetPositionAndReturnTrue() {
        ArrayList<Attribute> allAttributes = new ArrayList<>();
        allAttributes.add(attributeMock);

        when(elementMock.getAttributes()).thenReturn(allAttributes);

        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName(Namespace.NO_NAMESPACE, "*"));
        assertTrue(jdomAttributeIterator.setPosition(1));
        assertEquals(1, jdomAttributeIterator.position);
    }

    @Test
    public void testSetPosition_WithInvalidPosition_ShouldReturnFalse() {
        ArrayList<Attribute> allAttributes = new ArrayList<>();
        allAttributes.add(attributeMock);

        when(elementMock.getAttributes()).thenReturn(allAttributes);

        jdomAttributeIterator = new JDOMAttributeIterator(parentMock, new QName(Namespace.NO_NAMESPACE, "*"));
        assertFalse(jdomAttributeIterator.setPosition(2));
        assertEquals(0, jdomAttributeIterator.position);
    }
}