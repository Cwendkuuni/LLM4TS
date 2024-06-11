import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JDOMAttributeIteratorTest {

    @Mock
    NodePointer parent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetNodePointer_firstPosition_success() {
        // Arrange
        Element elementMock = mock(Element.class);
        Attribute attributeMock = mock(Attribute.class);
        JDOMAttributePointer jdomAttributePointerMock = mock(JDOMAttributePointer.class);
        QName qNameMock = mock(QName.class);
        List<Attribute> attributes = Collections.singletonList(attributeMock);

        when(parent.getNode()).thenReturn(elementMock);
        when(elementMock.getAttribute(anyString(), any(Namespace.class))).thenReturn(attributeMock);

        when(jdomAttributePointerMock.getAttribute()).thenReturn(attributeMock);

        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, qNameMock);
        jdomAttributeIterator.attributes = attributes;

        // Act
        NodePointer result = jdomAttributeIterator.getNodePointer();

        // Assert
        assertNotNull(result);
        assertEquals(jdomAttributePointerMock.getClass(), result.getClass());
        assertEquals(attributeMock, ((JDOMAttributePointer) result).getAttribute());
    }

    @Test
    public void testGetNodePointer_invalidPosition_success() {
        // Arrange
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, mock(QName.class));
        jdomAttributeIterator.attributes = Collections.emptyList();
        int position = 0;

        // Act
        boolean result = jdomAttributeIterator.setPosition(position);

        // Assert
        assertFalse(result);
        assertEquals(position, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer_validPosition_success() {
        // Arrange
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, mock(QName.class));
        jdomAttributeIterator.attributes = Collections.singletonList(mock(Attribute.class));
        int position = 1;

        // Act
        boolean result = jdomAttributeIterator.setPosition(position);

        // Assert
        assertTrue(result);
        assertEquals(position, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testGetPosition_returnCurrentPosition() {
        // Arrange
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, mock(QName.class));
        int position = 3;
        jdomAttributeIterator.position = position;

        // Act
        int result = jdomAttributeIterator.getPosition();

        // Assert
        assertEquals(position, result);
    }

    @Test
    public void testSetPosition_invalidPosition_failure() {
        // Arrange
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, mock(QName.class));
        jdomAttributeIterator.attributes = Collections.emptyList();
        int position = 0;

        // Act
        boolean result = jdomAttributeIterator.setPosition(position);

        // Assert
        assertFalse(result);
        assertEquals(position, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_validPosition_success() {
        // Arrange
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parent, mock(QName.class));
        List<Attribute> attributes = Collections.singletonList(mock(Attribute.class));
        int position = 1;
        jdomAttributeIterator.attributes = attributes;

        // Act
        boolean result = jdomAttributeIterator.setPosition(position);

        // Assert
        assertTrue(result);
        assertEquals(position, jdomAttributeIterator.getPosition());
    }

}